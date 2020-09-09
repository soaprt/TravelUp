package com.sostrovsky.travelup.repository.preferences

import com.sostrovsky.travelup.domain.preferences.UserCountryDomainModel
import com.sostrovsky.travelup.domain.preferences.UserCurrencyDomainModel
import com.sostrovsky.travelup.domain.preferences.UserSettingsDomainModel
import com.sostrovsky.travelup.domain.preferences.UserLanguageDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface PreferencesContract {
    suspend fun init()

    suspend fun getUserSettings(): UserSettingsDomainModel
    suspend fun saveUserSettings(userSettings: UserSettingsDomainModel): Int

    suspend fun getLanguages(): List<UserLanguageDomainModel>
    suspend fun getCurrencies(): List<UserCurrencyDomainModel>
    suspend fun getCountries(): List<UserCountryDomainModel>
}