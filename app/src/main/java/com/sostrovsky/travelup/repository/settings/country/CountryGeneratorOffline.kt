package com.sostrovsky.travelup.repository.settings.country

import com.sostrovsky.travelup.database.entities.settings.Country
import com.sostrovsky.travelup.repository.settings.DataGenerator
import com.sostrovsky.travelup.util.getCountryCodeFromLocale
import com.sostrovsky.travelup.util.getCountryNameFromLocale
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CountryGeneratorOffline :
    DataGenerator<List<Country>> {
    override suspend fun generate(): List<Country> {
        return fetchLocales().map {
            Country(
                id = 0,
                code = getCountryCodeFromLocale(it),
                name = getCountryNameFromLocale(it)
            )
        }
    }

    private fun fetchLocales(): SortedSet<Locale> {
        val localesWithCountry = mutableSetOf<Locale>()
        with(Locale.getAvailableLocales()) {
            filterTo(localesWithCountry,
                { it.country.isNotEmpty() && it.displayCountry.isNotEmpty() })
        }

        return localesWithCountry.toSortedSet(compareBy { it.displayCountry })
    }
}