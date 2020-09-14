package com.sostrovsky.travelup.repository.settings

import androidx.room.Transaction
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.settings.Settings
import com.sostrovsky.travelup.database.entities.settings.asDomain
import com.sostrovsky.travelup.domain.settings.CountryDomain
import com.sostrovsky.travelup.domain.settings.CurrencyDomain
import com.sostrovsky.travelup.domain.settings.LanguageDomain
import com.sostrovsky.travelup.domain.settings.SettingsDomain
import com.sostrovsky.travelup.repository.settings.country.CountryFactory
import com.sostrovsky.travelup.repository.settings.currency.CurrencyFactory
import com.sostrovsky.travelup.repository.settings.language.LanguageFactory
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
            populateLanguages()
            populateCurrencies()
            populateCountries()

            addDefaultSettings()
        }
    }

    // Settings
    private suspend fun addDefaultSettings() {
        val settingsDomain = generateSettingsDomain(getFormattedLocale(),
            getCurrencyCodeFromLocale(), getCountryCodeFromLocale())
        val settingsTable = newSettingsTable(settingsDomain)
        addNewSettings(settingsTable)
    }

    private suspend fun addNewSettings(settings: Settings) : Long {
        val addedRowId = mutableListOf<Long>()

        withContext(Dispatchers.IO) {
            addedRowId.add(database.settingsDao.insertAndSetSelected(settings))
        }

        return addedRowId[0]
    }

    private suspend fun fetchSettings(): Settings? {
        var result: Settings? = null

        withContext(Dispatchers.IO) {
            result = database.settingsDao.getSelected()
        }

        return result
    }

    private suspend fun fetchSettingsCountryCode(): String? {
        var result: String? = null

        withContext(Dispatchers.IO) {
            result = database.countryDao.getCodeById(fetchSettings()?.countryId ?: -1)
        }

        return result
    }

    private suspend fun fetchSettingsCurrencyCode(): String? {
        var result: String? = null

        withContext(Dispatchers.IO) {
            result = database.currencyDao.getCodeById(fetchSettings()?.currencyId ?: -1)
        }

        return result
    }

    private suspend fun fetchSettingsIdFromDB(settings: SettingsDomain) : Long {
        val result = mutableListOf(0L)

        withContext(Dispatchers.IO) {
            result[0] = database.settingsDao.getId(settings.languageId, settings.currencyId,
                settings.countryId)
        }

        return result[0]
    }

    private suspend fun fetchSettingsLanguageCode(): String? {
        var result: String? = null

        withContext(Dispatchers.IO) {
            result = database.languageDao.getCodeById(fetchSettings()?.languageId ?: -1)
        }

        return result
    }

    private suspend fun hasNoSettings(): Boolean {
        val result = mutableListOf<Boolean>()

        withContext(Dispatchers.IO) {
            result.add(database.settingsDao.checkIfEmpty() == 0L)
        }

        return result[0]
    }

    private suspend fun isSettingsSaved(settingsId: Long) : Boolean {
        val result = mutableListOf<Boolean>()

        withContext(Dispatchers.IO) {
            result.add(database.settingsDao.checkIfSelected(settingsId) != 0L)
        }

        return result[0]
    }

    private suspend fun newSettingsTable(settingsDomain: SettingsDomain): Settings {
        val settingsTable = domainToSettingsTable(settingsDomain)

        withContext (Dispatchers.IO) {
            settingsTable.id = database.settingsDao.checkIfEmpty().plus(1)
        }

        return settingsTable
    }

    override suspend fun saveUserSettings(languageCode: String, currencyCode: String,
                                          countryCode: String): Boolean {
        var isSaved = false

        withContext(Dispatchers.IO) {
            val settingsDomain = generateSettingsDomain(languageCode, currencyCode, countryCode)
            var settingsId = fetchSettingsIdFromDB(settingsDomain)

            if (settingsId != 0L) {
                setSelectedSettings(settingsId)
            } else {
                val settingsTable = newSettingsTable(settingsDomain)
                settingsId = addNewSettings(settingsTable)
            }

            isSaved = isSettingsSaved(settingsId)
        }

        return isSaved
    }

    private suspend fun setSelectedSettings(settingsId: Long) {
        withContext(Dispatchers.IO) {
            database.settingsDao.setSelected(settingsId)
        }
    }

    // Settings Domain
    private fun domainToSettingsTable(settingsDomain: SettingsDomain): Settings {
        return Settings(
            languageId = settingsDomain.languageId,
            currencyId = settingsDomain.currencyId,
            countryId = settingsDomain.countryId
        )
    }

    private suspend fun generateSettingsDomain(languageCode: String, currencyCode: String,
                                               countryCode: String) : SettingsDomain {
        val result = mutableListOf<SettingsDomain>()
        withContext(Dispatchers.IO) {
            result.add(SettingsDomain(
                languageId = database.languageDao.getIdByCode(languageCode),
                currencyId = database.currencyDao.getIdByCode(currencyCode),
                countryId = database.countryDao.getIdByCode(countryCode)
            ))
        }
        return result[0]
    }

    // Languages
    private fun hasLanguages() = (database.languageDao.getRowsCount() > 0L)

    private suspend fun populateLanguages() {
        if (hasLanguages()) database.languageDao.clear()

        val languages = LanguageFactory.generate()
        if (languages.isNotEmpty()) {
            database.languageDao.insertAll(languages)
        }
    }

    override suspend fun fetchSpinnerLanguage(): Pair<List<LanguageDomain>, MutableList<Int>> {
        val result = Pair(mutableListOf<LanguageDomain>(), mutableListOf<Int>() )

        withContext(Dispatchers.IO) {
            result.first.addAll(database.languageDao.getAll().asDomain())
            result.second.add(fetchSpinnerLangPosition(result.first))
        }

        return result
    }

    private suspend fun fetchSpinnerLangPosition(list: List<LanguageDomain>): Int {
        return list.indexOfFirst { it.code == fetchSettingsLanguageCode() }
    }

    // Currencies
    private fun hasCurrencies() = (database.currencyDao.getRowsCount() > 0L)

    private suspend fun populateCurrencies() {
        if (hasCurrencies()) database.currencyDao.clear()

        val currencies = CurrencyFactory.generate()
        if (currencies.isNotEmpty()) {
            database.currencyDao.insertAll(currencies)
        }
    }

    override suspend fun fetchSpinnerCurrency(): Pair<List<CurrencyDomain>, MutableList<Int>> {
        val result = Pair(mutableListOf<CurrencyDomain>(), mutableListOf<Int>() )

        withContext(Dispatchers.IO) {
            result.first.addAll(database.currencyDao.getAll().asDomain())
            result.second.add(fetchSpinnerCurrencyPosition(result.first))
        }

        return result
    }

    private suspend fun fetchSpinnerCurrencyPosition(list: List<CurrencyDomain>): Int {
        return list.indexOfFirst { it.code == fetchSettingsCurrencyCode() }
    }

    // Countries
    private fun hasCountries() = (database.countryDao.getRowsCount() > 0L)

    private suspend fun populateCountries() {
        if (hasCountries()) database.countryDao.clear()

        val countries = CountryFactory.generate()
        if (countries.isNotEmpty()) {
            database.countryDao.insertAll(countries)
        }
    }

    override suspend fun fetchSpinnerCountry(): Pair<List<CountryDomain>, MutableList<Int>> {
        val result = Pair(mutableListOf<CountryDomain>(), mutableListOf<Int>() )

        withContext(Dispatchers.IO) {
            result.first.addAll(database.countryDao.getAll().asDomain())
            result.second.add(fetchSpinnerCountryPosition(result.first))
        }

        return result
    }

    private suspend fun fetchSpinnerCountryPosition(list: List<CountryDomain>): Int {
        return list.indexOfFirst { it.code == fetchSettingsCountryCode() }
    }

//    override suspend fun fetchSettings(): SettingsDomain {
//        var languageCode = ""
//        var currencyCode = ""
//        var countryCode = ""
//
//        withContext(Dispatchers.IO) {
//            val settings = database.settingsDao.getLastRow()
//            languageCode = database.languageDao.getCodeById(settings.languageId)
//            currencyCode = database.currencyDao.getCodeById(settings.currencyId)
//            countryCode = database.countryDao.getCodeById(settings.countryId)
//        }
//
//        return SettingsDomain(languageCode, currencyCode, countryCode)
//    }

//    override suspend fun fetchLanguages(): List<LanguageDomain> {
//        Timber.e("SettingsRepository: fetchLanguages()")
//
//        val list = mutableListOf<LanguageDomain>()
//
//        withContext(Dispatchers.IO) {
//            list.addAll(database.languageDao.getAll().asDomain())
//        }
//
//        return list
//    }
//
//    override suspend fun fetchCurrencies(): List<CurrencyDomain> {
//        val list = mutableListOf<CurrencyDomain>()
//
//        withContext(Dispatchers.IO) {
//            list.addAll(database.currencyDao.getAll().asDomain())
//        }
//
//        return list
//    }
//
//    override suspend fun fetchCountries(): List<CountryDomain> {
//        val list = mutableListOf<CountryDomain>()
//
//        withContext(Dispatchers.IO) {
//            list.addAll(database.countryDao.getAll().asDomain())
//        }
//
//        return list
//    }


//    override suspend fun getUserSettings(): UserSettingsDomainModel {
//        lateinit var result: UserSettingsDomainModel
//
//        withContext(Dispatchers.IO) {
//            val userSettingsTable = database.userSettingsDao.getSettings()
//            result = userSettingsTable?.asDomainModel() ?: UserSettingsDomainModel()
//        }
//
//        return result
//    }

/*    override suspend fun saveUserSettings(userSettings: UserSettingsDomainModel): Int {
        var updatedRowsAmount = 0

        withContext(Dispatchers.IO) {
            val newUserSettings = userSettings.asDBModel()
//            updatedRowsAmount = database.userSettingsDao.updateSettings(newUserSettings)
        }

        return updatedRowsAmount
    }*/

//    private val settings: LiveData<SettingsDomain> = Transformations.map(database.settingsDao.getLastRow()) {
//        SettingsDomain(
//            languageCode = database.languageDao.getCodeById(it.languageId),
//            currencyCode = database.currencyDao.getCodeById(it.currencyId),
//            countryCode = database.countryDao.getCodeById(it.countryId)
//        )
//    }

//    private val languages: LiveData<List<LanguageDomain>> =
//        Transformations.map(database.languageDao.getAll()) {
//            it.asDomain()
//        }

//    private val currencies: LiveData<List<CurrencyDomain>> =
//        Transformations.map(database.currencyDao.getAll()) {
//            it.asDomain()
//        }

//    private val countries: LiveData<List<CountryDomain>> =
//        Transformations.map(database.countryDao.getAll()) {
//            it.asDomain()
//        }

//    override fun getSettings(): LiveData<SettingsDomain> {
//        return Transformations.map(database.settingsDao.getLastRow()) {
//            SettingsDomain(
//                languageCode = database.languageDao.getCodeById(it.languageId),
//                currencyCode = database.currencyDao.getCodeById(it.currencyId),
//                countryCode = database.countryDao.getCodeById(it.countryId)
//            )
//        }
//    }

//    override fun getLanguages(): LiveData<List<LanguageDomain>> {
//        return Transformations.map(database.languageDao.getAll()) {
//            it.asDomain()
//        }
//    }

//    override fun getCurrencies(): LiveData<List<CurrencyDomain>> {
//        return Transformations.map(database.currencyDao.getAll()) {
//            it.asDomain()
//        }
//    }
//
//    override fun getCountries(): LiveData<List<CountryDomain>> {
//        return Transformations.map(database.countryDao.getAll()) {
//            it.asDomain()
//        }
//    }

//    override fun getSelectedLangPos(): Int {
//        return database.languageDao.getIdByCode()
//    }

//    override suspend fun getLanguages(): List<UserLanguageDomainModel> {
//        lateinit var result: List<UserLanguageDomainModel>
//
//        withContext(Dispatchers.IO) {
//            result = database.languageDao.getLanguages().asDomainModel()
////            result = database.languageDBDao.getLanguages().asDomainModel()
//        }
//
//        return result
//    }

//    override suspend fun getCurrencies(): List<UserCurrencyDomainModel> {
//        lateinit var result: List<UserCurrencyDomainModel>
//
//        withContext(Dispatchers.IO) {
//            result = database.currencyDBDao.getCurrencies().asDomainModel()
//        }
//
//        return result
//    }
//
//    override suspend fun getCountries(): List<UserCountryDomainModel> {
//        lateinit var result: List<UserCountryDomainModel>
//
//        withContext(Dispatchers.IO) {
//            result = database.countryDBDao.getCountries().asDomainModel()
//        }
//
//        return result
//    }
}