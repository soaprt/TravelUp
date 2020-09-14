package com.sostrovsky.travelup.repository.settings.language

import com.sostrovsky.travelup.database.entities.settings.Language
import com.sostrovsky.travelup.repository.settings.RowsGenerator
import com.sostrovsky.travelup.util.getFormattedLocale
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object LanguageGeneratorOffline : RowsGenerator<List<Language>>() {
    override suspend fun execute(): List<Language> {
        return fetchLocales().map {
            Language(
                id = generateRowId(),
                name = generateName(it),
                code = getFormattedLocale(it)
            )
        }
    }

    private fun fetchLocales(): SortedSet<Locale> {
        val locales = mutableSetOf<Locale>()
        locales.addAll(Locale.getAvailableLocales())

        return locales.toSortedSet(compareBy { it.displayName })
    }

    private fun generateName(locale: Locale): String {
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