package com.sostrovsky.travelup.repository.preferences

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.preferences.asDomainModel
import com.sostrovsky.travelup.domain.preferences.UserCurrencyDomainModel
import com.sostrovsky.travelup.domain.preferences.UserSettingsDomainModel
import com.sostrovsky.travelup.domain.preferences.asDBModel
import com.sostrovsky.travelup.domain.preferences.UserCountryDomainModel
import com.sostrovsky.travelup.domain.preferences.UserLanguageDomainModel
import com.sostrovsky.travelup.repository.preferences.userSettings.UserSettingsGenerator
import com.sostrovsky.travelup.repository.preferences.userSettings.country.UserCountryFactory
import com.sostrovsky.travelup.repository.preferences.userSettings.currency.UserCurrencyFactory
import com.sostrovsky.travelup.repository.preferences.userSettings.language.UserLanguageFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object PreferencesRepository : PreferencesContract {
    lateinit var database: TravelUpDatabase

    override suspend fun init() {
        this.database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

        initLanguages()
        initCurrencies()
        initCountries()
        initUserSettings()
    }

    private suspend fun initLanguages() {
        if (getLanguages().isEmpty()) {
            val userLanguages = UserLanguageFactory.generate()
            if (userLanguages.isNotEmpty()) {
                database.languageDBDao.insertLanguages(userLanguages)
            }
        }
    }

    private suspend fun initCurrencies() {
        if (getCurrencies().isEmpty()) {
            val userCurrencies = UserCurrencyFactory.generate()
            if (userCurrencies.isNotEmpty()) {
                database.currencyDBDao.insertCurrencies(userCurrencies)
            }
        }
    }

    private suspend fun initCountries() {
        if (getCountries().isEmpty()) {
            val userCountries = UserCountryFactory.generate()
            if (userCountries.isNotEmpty()) {
                database.countryDBDao.insertCountries(userCountries)
            }
        }
    }

    private suspend fun initUserSettings() {
        val userSettings = getUserSettings()
        if (!userSettings.isSet) {
            database.userSettingsDao.insertSettings(UserSettingsGenerator.execute())
        }
    }

    override suspend fun getUserSettings(): UserSettingsDomainModel {
        lateinit var result: UserSettingsDomainModel

        withContext(Dispatchers.IO) {
            val userSettingsTable = database.userSettingsDao.getSettings()
            result = userSettingsTable?.asDomainModel() ?: UserSettingsDomainModel()
        }

        return result
    }

    override suspend fun saveUserSettings(userSettings: UserSettingsDomainModel): Int {
        var updatedRowsAmount = 0

        withContext(Dispatchers.IO) {
            val newUserSettings = userSettings.asDBModel()
            updatedRowsAmount = database.userSettingsDao.updateSettings(newUserSettings)
        }

        return updatedRowsAmount
    }

    override suspend fun getLanguages(): List<UserLanguageDomainModel> {
        lateinit var result: List<UserLanguageDomainModel>

        withContext(Dispatchers.IO) {
            result = database.languageDBDao.getLanguages().asDomainModel()
        }

        return result
    }

    override suspend fun getCurrencies(): List<UserCurrencyDomainModel> {
        lateinit var result: List<UserCurrencyDomainModel>

        withContext(Dispatchers.IO) {
            result = database.currencyDBDao.getCurrencies().asDomainModel()
        }

        return result
    }

    override suspend fun getCountries(): List<UserCountryDomainModel> {
        lateinit var result: List<UserCountryDomainModel>

        withContext(Dispatchers.IO) {
            result = database.countryDBDao.getCountries().asDomainModel()
        }

        return result
    }
}