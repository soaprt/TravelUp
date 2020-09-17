package com.sostrovsky.travelup.repository.settings.currency

import com.sostrovsky.travelup.database.entities.settings.Currency
import com.sostrovsky.travelup.repository.settings.DataGenerator
import com.sostrovsky.travelup.util.getCurrencyFromLocale
import java.util.*
import java.util.Currency as SystemCurrency


/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CurrencyGeneratorOffline : DataGenerator<List<Currency>> {
    override suspend fun generate(): List<Currency> {
        return fetchCurrencies().map {
            Currency(
                id = 0,
                code = it!!.currencyCode,
                name = generateName(it!!)
            )
        }
    }

    private fun fetchCurrencies(): SortedSet<SystemCurrency?> {
        val currencies = mutableSetOf<SystemCurrency?>()
        fetchLocalesWithCountry().forEach { currencies.add(getCurrencyFromLocale(it)) }

        return removeNullableCurrencies(currencies).toSortedSet(compareBy { it?.currencyCode })
    }

    private fun fetchLocalesWithCountry(): Set<Locale> {
        val localesWithCountry = mutableSetOf<Locale>()

        with(Locale.getAvailableLocales()) {
            filterTo(localesWithCountry, { it.country.isNotEmpty() })
        }

        return localesWithCountry
    }

    private fun removeNullableCurrencies(currencies: MutableSet<SystemCurrency?>): MutableSet<SystemCurrency?> {
        val nonNullableCurrencies = mutableSetOf<SystemCurrency?>()

        with(currencies) {
            filterTo(nonNullableCurrencies, { it != null })
        }

        return nonNullableCurrencies
    }

    private fun generateName(currency: SystemCurrency): String {
        val result = StringBuilder()

        result.append(currency.currencyCode)
        if (currency.symbol.isNotEmpty()) {
            with(result) {
                append(" (")
                append(currency.symbol)
                append(")")
            }
        }

        return result.toString()
    }
}