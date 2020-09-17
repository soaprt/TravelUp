package com.sostrovsky.travelup.repository.settings.currency

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.settings.Currency
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.network.dto.settings.CurrencyFromJSON
import com.sostrovsky.travelup.repository.settings.DataGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CurrencyGeneratorOnline : DataGenerator<List<Currency>> {
    override suspend fun generate(): List<Currency> {
        return fetchCurrencies().map {
            Currency(
                id = 0,
                code = it.Code,
                name = generateName(it)
            )
        }
    }

    private suspend fun fetchCurrencies(): SortedSet<CurrencyFromJSON> {
        val currencies = mutableSetOf<CurrencyFromJSON>()

        withContext(Dispatchers.IO) {
            val response = WebService.settingsService.getCurrencies(
                BuildConfig.API_VERSION,
                BuildConfig.API_KEY
            ).await()
            currencies.addAll(response.Currencies)
        }

        return currencies.toSortedSet(compareBy { it.Code })
    }

    private fun generateName(currency: CurrencyFromJSON): String {
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