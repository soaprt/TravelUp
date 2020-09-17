package com.sostrovsky.travelup.repository.settings.country

import com.sostrovsky.travelup.database.entities.settings.asDomain
import com.sostrovsky.travelup.domain.settings.CountryDomain
import com.sostrovsky.travelup.repository.settings.SettingsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CountryRepository : SettingsData<List<CountryDomain>, MutableList<Int>>() {
    override suspend fun populate() {
        if (hasCountries()) database.countryDao.clear()

        val countries = CountryFactory.generate()
        if (countries.isNotEmpty()) {
            database.countryDao.insertAll(countries)
        }
    }

    private fun hasCountries() = (database.countryDao.getRowsCount() > 0L)

    override suspend fun fetchSpinnerData(id: Int): Pair<List<CountryDomain>, MutableList<Int>> {
        val result = Pair(mutableListOf<CountryDomain>(), mutableListOf<Int>())

        withContext(Dispatchers.IO) {
            result.first.addAll(database.countryDao.getAll().asDomain())
            result.second.add(fetchSpinnerCountryPosition(result.first, id))
        }

        return result
    }

    private suspend fun fetchSpinnerCountryPosition(list: List<CountryDomain>, countryId: Int):
            Int {
        return list.indexOfFirst { it.code == fetchSettingsCountryCode(countryId) }
    }

    private suspend fun fetchSettingsCountryCode(countryId: Int): String? {
        var result: String? = null

        withContext(Dispatchers.IO) {
            result = database.countryDao.getCodeById(countryId)
        }

        return result
    }
}