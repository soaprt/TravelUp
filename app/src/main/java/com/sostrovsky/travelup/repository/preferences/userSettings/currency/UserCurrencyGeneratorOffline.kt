package com.sostrovsky.travelup.repository.preferences.userSettings.currency

import com.sostrovsky.travelup.database.entities.preferences.UserCurrencyDBModel
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import com.sostrovsky.travelup.util.getCurrencyFromLocale
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserCurrencyGeneratorOffline : UserDataGenerator<List<UserCurrencyDBModel>>() {
    override suspend fun execute(): List<UserCurrencyDBModel> {
        return fetchCurrencies().map {
            UserCurrencyDBModel(
                id = generateRowId(),
                currencyCode = it!!.currencyCode,
                currencyName = generateLabel(
                    it
                )
            )
        }
    }

    private fun fetchCurrencies(): SortedSet<Currency?> {
        val currencies = mutableSetOf<Currency?>()
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

    private fun removeNullableCurrencies(currencies: MutableSet<Currency?>): MutableSet<Currency?> {
        val nonNullableCurrencies = mutableSetOf<Currency?>()

        with(currencies) {
            filterTo(nonNullableCurrencies, { it != null })
        }

        return nonNullableCurrencies
    }

    private fun generateLabel(currency: Currency): String {
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