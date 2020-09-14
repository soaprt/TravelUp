package com.sostrovsky.travelup.repository.settings.language

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.settings.Language
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.network.dto.settings.LocaleFromJSON
import com.sostrovsky.travelup.repository.settings.RowsGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object LanguageGeneratorOnline : RowsGenerator<List<Language>>() {
    override suspend fun execute(): List<Language> {
        return fetchLocales().map {
            Language(
                id = generateRowId(),
                name = generateName(it),
                code = it.Code
            )
        }
    }

    private suspend fun fetchLocales(): SortedSet<LocaleFromJSON> {
        val locales = mutableSetOf<LocaleFromJSON>()

        withContext(Dispatchers.IO) {
            val response = WebService.settingsService.getLocales(
                BuildConfig.API_VERSION,
                BuildConfig.API_KEY
            ).await()
            locales.addAll(response.Locales)
        }
        return locales.toSortedSet(compareBy { it.Code })
    }

    private fun generateName(locale: LocaleFromJSON): String {
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