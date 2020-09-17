package com.sostrovsky.travelup.repository.settings.currency

import com.sostrovsky.travelup.database.entities.settings.asDomain
import com.sostrovsky.travelup.domain.settings.CurrencyDomain
import com.sostrovsky.travelup.repository.settings.SettingsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CurrencyRepository : SettingsData<List<CurrencyDomain>, MutableList<Int>>() {

    override suspend fun populate() {
        if (hasCurrencies()) database.currencyDao.clear()

        val currencies = CurrencyFactory.generate()
        if (currencies.isNotEmpty()) {
            database.currencyDao.insertAll(currencies)
        }
    }

    private fun hasCurrencies() = (database.currencyDao.getRowsCount() > 0L)

    override suspend fun fetchSpinnerData(id: Int): Pair<List<CurrencyDomain>, MutableList<Int>> {
        val result = Pair(mutableListOf<CurrencyDomain>(), mutableListOf<Int>())

        withContext(Dispatchers.IO) {
            result.first.addAll(database.currencyDao.getAll().asDomain())
            result.second.add(fetchSpinnerCurrencyPosition(result.first, id))
        }

        return result
    }

    private suspend fun fetchSpinnerCurrencyPosition(list: List<CurrencyDomain>, currencyId: Int):
            Int {
        return list.indexOfFirst { it.code == fetchSettingsCurrencyCode(currencyId) }
    }

    private suspend fun fetchSettingsCurrencyCode(currencyId: Int): String? {
        var result: String? = null

        withContext(Dispatchers.IO) {
            result = database.currencyDao.getCodeById(currencyId)
        }

        return result
    }
}