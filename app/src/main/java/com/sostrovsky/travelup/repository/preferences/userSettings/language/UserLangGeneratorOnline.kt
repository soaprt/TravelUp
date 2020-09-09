package com.sostrovsky.travelup.repository.preferences.userSettings.language

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.preferences.UserLanguageDBModel
import com.sostrovsky.travelup.network.Network
import com.sostrovsky.travelup.network.dto.preferences.LocaleFromJSON
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserLangGeneratorOnline : UserDataGenerator<List<UserLanguageDBModel>>() {
    override suspend fun execute(): List<UserLanguageDBModel> {
        return fetchLocales().map {
            UserLanguageDBModel(
                id = generateRowId(),
                languageName = generateLabel(
                    it
                ),
                localeCode = it.Code
            )
        }
    }

    private suspend fun fetchLocales(): SortedSet<LocaleFromJSON> {
        val locales = mutableSetOf<LocaleFromJSON>()

        withContext(Dispatchers.IO) {
            val response = Network.preferencesNetworkDao.getLocales(
                BuildConfig.API_VERSION,
                BuildConfig.API_KEY
            ).await()
            locales.addAll(response.Locales)
        }
        return locales.toSortedSet(compareBy { it.Code })
    }

    private fun generateLabel(locale: LocaleFromJSON): String {
        val sb = StringBuilder(locale.Name.capitalize())

        if (locale.Code.isNotEmpty()) {
            with(sb) {
                append(" (")
                append(locale.Code)
                append(")")
            }
        }
        return sb.toString()
    }
}