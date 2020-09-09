package com.sostrovsky.travelup.repository.preferences.userSettings.country

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.preferences.UserCountryDBModel
import com.sostrovsky.travelup.network.Network
import com.sostrovsky.travelup.network.dto.preferences.CountryFromJSON
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import com.sostrovsky.travelup.util.getFormattedLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserCountryGeneratorOnline : UserDataGenerator<List<UserCountryDBModel>>() {
    override suspend fun execute(): List<UserCountryDBModel> {
        return fetchCountries().map {
            UserCountryDBModel(
                id = generateRowId(),
                countryCode = it.Code.toLowerCase(),
                countryName = it.Name.capitalize()
            )
        }
    }

    private suspend fun fetchCountries(): SortedSet<CountryFromJSON> {
        val countries = mutableSetOf<CountryFromJSON>()

        withContext(Dispatchers.IO) {
            val response = Network.preferencesNetworkDao.getCountries(
                BuildConfig.API_VERSION, getFormattedLocale(), BuildConfig.API_KEY
            ).await()
            countries.addAll(response.Countries)
        }

        return countries.toSortedSet(compareBy { it.Name })
    }
}