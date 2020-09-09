package com.sostrovsky.travelup.repository.preferences.userSettings.country

import com.sostrovsky.travelup.database.entities.preferences.UserCountryDBModel
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import com.sostrovsky.travelup.util.getCountryCodeFromLocale
import com.sostrovsky.travelup.util.getCountryLabelFromLocale
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserCountryGeneratorOffline : UserDataGenerator<List<UserCountryDBModel>>() {
    override suspend fun execute(): List<UserCountryDBModel> {
        return fetchLocales().map {
            UserCountryDBModel(
                id = generateRowId(),
                countryCode = getCountryCodeFromLocale(it),
                countryName = getCountryLabelFromLocale(it)
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