package com.sostrovsky.travelup.repository.preferences.userSettings.language

import com.sostrovsky.travelup.database.entities.preferences.UserLanguageDBModel
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import com.sostrovsky.travelup.util.getFormattedLocale
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserLangGeneratorOffline : UserDataGenerator<List<UserLanguageDBModel>>() {
    override suspend fun execute(): List<UserLanguageDBModel> {
        return fetchLocales().map {
            UserLanguageDBModel(
                id = generateRowId(),
                languageName = generateLabel(
                    it
                ),
                localeCode = getFormattedLocale(it)
            )
        }
    }

    private fun fetchLocales(): SortedSet<Locale> {
        val locales = mutableSetOf<Locale>()
        locales.addAll(Locale.getAvailableLocales())

        return locales.toSortedSet(compareBy { it.displayName })
    }

    private fun generateLabel(locale: Locale): String {
        val sb = StringBuilder(locale.displayName.capitalize())

        if (locale.language.isNotEmpty()) {
            with(sb) {
                append(" (")
                append(getFormattedLocale(locale))
                append(")")
            }
        }

        return sb.toString()
    }
}