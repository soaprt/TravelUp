package com.sostrovsky.travelup.repository.settings.country

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.settings.Country
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.network.dto.settings.CountryFromJSON
import com.sostrovsky.travelup.repository.settings.DataGenerator
import com.sostrovsky.travelup.util.getFormattedLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CountryGeneratorOnline :
    DataGenerator<List<Country>> {
    override suspend fun generate(): List<Country> {
        return fetchCountries().map {
            Country(
                id = 0,
                code = it.Code.toLowerCase(),
                name = it.Name.capitalize()
            )
        }
    }

    private suspend fun fetchCountries(): SortedSet<CountryFromJSON> {
        val countries = mutableSetOf<CountryFromJSON>()

        withContext(Dispatchers.IO) {
            val response = WebService.settingsService.getCountries(
                BuildConfig.API_VERSION, getFormattedLocale(), BuildConfig.API_KEY
            ).await()
            countries.addAll(response.Countries)
        }

        return countries.toSortedSet(compareBy { it.Name })
    }
}