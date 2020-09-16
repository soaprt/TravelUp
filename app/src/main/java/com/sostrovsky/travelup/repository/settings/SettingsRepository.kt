package com.sostrovsky.travelup.repository.settings

import androidx.room.Transaction
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.settings.Settings
import com.sostrovsky.travelup.domain.settings.CountryDomain
import com.sostrovsky.travelup.domain.settings.CurrencyDomain
import com.sostrovsky.travelup.domain.settings.LanguageDomain
import com.sostrovsky.travelup.domain.settings.SettingsDomain
import com.sostrovsky.travelup.repository.settings.country.CountryRepository
import com.sostrovsky.travelup.repository.settings.currency.CurrencyRepository
import com.sostrovsky.travelup.repository.settings.language.LanguageRepository
import com.sostrovsky.travelup.util.getCountryCodeFromLocale
import com.sostrovsky.travelup.util.getCurrencyCodeFromLocale
import com.sostrovsky.travelup.util.getFormattedLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object SettingsRepository : SettingsContract {
    lateinit var database: TravelUpDatabase

    @Transaction
    override suspend fun init() {
        this.database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

        if (hasNoSettings()) {
            CountryRepository.populate()
            CurrencyRepository.populate()
            LanguageRepository.populate()

            addDefaultSettings()
        }
    }

    private suspend fun addDefaultSettings() {
        val settingsDomain = generateSettingsDomain(
            getFormattedLocale(),
            getCurrencyCodeFromLocale(), getCountryCodeFromLocale()
        )
        val settingsTable = newSettingsTable(settingsDomain)
        addNewSettings(settingsTable)
    }

    private suspend fun addNewSettings(settings: Settings): Int {
        val addedRowId = mutableListOf<Int>()

        withContext(Dispatchers.IO) {
            addedRowId.add(database.settingsDao.insertAndSetSelected(settings).toInt())
        }

        return addedRowId[0]
    }

    private suspend fun fetchSettings(): Settings? {
        var result = mutableListOf<Settings>()

        withContext(Dispatchers.IO) {
            result.add(database.settingsDao.getSelected())
        }

        return result[0]
    }

    private suspend fun fetchSettingsIdFromDB(settings: SettingsDomain): Int {
        val result = mutableListOf(0)

        withContext(Dispatchers.IO) {
            result[0] = database.settingsDao.getId(
                settings.languageId, settings.currencyId,
                settings.countryId
            )
        }

        return result[0]
    }

    private suspend fun hasNoSettings(): Boolean {
        val result = mutableListOf<Boolean>()

        withContext(Dispatchers.IO) {
            result.add(database.settingsDao.checkIfEmpty() == 0)
        }

        return result[0]
    }

    private suspend fun isSettingsSaved(settingsId: Int): Boolean {
        val result = mutableListOf<Boolean>()

        withContext(Dispatchers.IO) {
            result.add(database.settingsDao.checkIfSelected(settingsId) != 0)
        }

        return result[0]
    }

    private suspend fun newSettingsTable(settingsDomain: SettingsDomain): Settings {
        val settingsTable = domainToSettingsTable(settingsDomain)

        withContext(Dispatchers.IO) {
            settingsTable.id = database.settingsDao.checkIfEmpty().plus(1)
        }

        return settingsTable
    }

    override suspend fun saveUserSettings(
        languageCode: String, currencyCode: String,
        countryCode: String
    ): Boolean {
        var isSaved = false

        withContext(Dispatchers.IO) {
            val settingsDomain = generateSettingsDomain(languageCode, currencyCode, countryCode)
            var settingsId = fetchSettingsIdFromDB(settingsDomain)

            if (settingsId != 0) {
                setSelectedSettings(settingsId)
            } else {
                val settingsTable = newSettingsTable(settingsDomain)
                settingsId = addNewSettings(settingsTable)
            }

            isSaved = isSettingsSaved(settingsId)
        }

        return isSaved
    }

    private suspend fun setSelectedSettings(settingsId: Int) {
        withContext(Dispatchers.IO) {
            database.settingsDao.setSelected(settingsId)
        }
    }

    private fun domainToSettingsTable(settingsDomain: SettingsDomain): Settings {
        return Settings(
            id = 0,
            languageId = settingsDomain.languageId,
            currencyId = settingsDomain.currencyId,
            countryId = settingsDomain.countryId
        )
    }

    private suspend fun generateSettingsDomain(
        languageCode: String, currencyCode: String,
        countryCode: String
    ): SettingsDomain {
        val result = mutableListOf<SettingsDomain>()
        withContext(Dispatchers.IO) {
            result.add(
                SettingsDomain(
                    languageId = database.languageDao.getIdByCode(languageCode),
                    currencyId = database.currencyDao.getIdByCode(currencyCode),
                    countryId = database.countryDao.getIdByCode(countryCode)
                )
            )
        }
        return result[0]
    }

    override suspend fun fetchSpinnerLanguage(): Pair<List<LanguageDomain>, MutableList<Int>> {
        return LanguageRepository.fetchSpinnerData(fetchSettings()?.languageId ?: -1)
    }

    override suspend fun fetchSpinnerCurrency(): Pair<List<CurrencyDomain>, MutableList<Int>> {
        return CurrencyRepository.fetchSpinnerData(fetchSettings()?.currencyId ?: -1)
    }

    override suspend fun fetchSpinnerCountry(): Pair<List<CountryDomain>, MutableList<Int>> {
        return CountryRepository.fetchSpinnerData(fetchSettings()?.countryId ?: -1)
    }
}