package com.sostrovsky.travelup.repository.preferences.userSettings.currency

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.preferences.UserCurrencyDBModel
import com.sostrovsky.travelup.network.Network
import com.sostrovsky.travelup.network.dto.preferences.CurrencyFromJSON
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserCurrencyGeneratorOnline : UserDataGenerator<List<UserCurrencyDBModel>>() {
    override suspend fun execute(): List<UserCurrencyDBModel> {
        return fetchCurrencies().map {
            UserCurrencyDBModel(
                id = generateRowId(),
                currencyCode = it.Code,
                currencyName = generateLabel(
                    it
                )
            )
        }
    }

    private suspend fun fetchCurrencies(): SortedSet<CurrencyFromJSON> {
        val currencies = mutableSetOf<CurrencyFromJSON>()

        withContext(Dispatchers.IO) {
            val response = Network.preferencesNetworkDao.getCurrencies(
                BuildConfig.API_VERSION,
                BuildConfig.API_KEY
            ).await()
            currencies.addAll(response.Currencies)
        }

        return currencies.toSortedSet(compareBy { it.Code })
    }

    private fun generateLabel(currency: CurrencyFromJSON): String {
        val sb = StringBuilder(currency.Code)

        if (currency.Symbol.isNotEmpty()) {
            with(sb) {
                append(" (")
                append(currency.Symbol)
                append(")")
            }
        }
        return sb.toString()
    }
}