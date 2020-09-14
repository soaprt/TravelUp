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

//    suspend fun fetchSettings(): Settings?
//    suspend fun fetchSettingsLanguageCode(): String?
//    suspend fun fetchSettingsCurrencyCode(): String?
//    suspend fun fetchSettingsCountryCode(): String?

    //    suspend fun fetchSettings(): SettingsDomain
    suspend fun fetchSpinnerLanguage(): Pair<List<LanguageDomain>, MutableList<Int>>
    suspend fun fetchSpinnerCurrency(): Pair<List<CurrencyDomain>, MutableList<Int>>
    suspend fun fetchSpinnerCountry(): Pair<List<CountryDomain>, MutableList<Int>>

    suspend fun saveUserSettings(
        languageCode: String,
        currencyCode: String,
        countryCode: String
    ): Boolean

//    suspend fun fetchLanguages(): List<LanguageDomain>
//    suspend fun fetchCurrencies(): List<CurrencyDomain>
//    suspend fun fetchCountries(): List<CountryDomain>

//    suspend fun getUserSettings(): UserSettingsDomainModel
//    suspend fun saveUserSettings(userSettings: UserSettingsDomainModel): Int


//    fun getSettings(): LiveData<SettingsDomain>
//    fun getLanguages(): LiveData<List<LanguageDomain>>
//    fun getCurrencies(): LiveData<List<CurrencyDomain>>
//    fun getCountries(): LiveData<List<CountryDomain>>

//    suspend fun getLanguages(): List<UserLanguageDomainModel>
//    suspend fun getCurrencies(): List<UserCurrencyDomainModel>
//    suspend fun getCountries(): List<UserCountryDomainModel>
}