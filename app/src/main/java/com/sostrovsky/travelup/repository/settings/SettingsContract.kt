package com.sostrovsky.travelup.repository.settings

import com.sostrovsky.travelup.domain.settings.CountryDomain
import com.sostrovsky.travelup.domain.settings.CurrencyDomain
import com.sostrovsky.travelup.domain.settings.LanguageDomain

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface SettingsContract {
    suspend fun init()

    suspend fun fetchSpinnerLanguage(): Pair<List<LanguageDomain>, MutableList<Int>>
    suspend fun fetchSpinnerCurrency(): Pair<List<CurrencyDomain>, MutableList<Int>>
    suspend fun fetchSpinnerCountry(): Pair<List<CountryDomain>, MutableList<Int>>

    suspend fun saveUserSettings(
        languageCode: String,
        currencyCode: String,
        countryCode: String
    ): Boolean
}