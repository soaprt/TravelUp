package com.sostrovsky.travelup.ui.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.domain.preferences.UserCountryDomainModel
import com.sostrovsky.travelup.domain.preferences.UserCurrencyDomainModel
import com.sostrovsky.travelup.domain.preferences.UserSettingsDomainModel
import com.sostrovsky.travelup.domain.preferences.UserLanguageDomainModel
import com.sostrovsky.travelup.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class PreferencesViewModel : ViewModel() {
    /**
     * The job for all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     */
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val preferencesRepo = Repository.getPreferences()

    // Live Data
    val userSettingsLiveData = MutableLiveData<UserSettingsDomainModel>()
    val userLanguagesLiveData = MutableLiveData<List<UserLanguageDomainModel>>()
    val userCurrenciesLiveData = MutableLiveData<List<UserCurrencyDomainModel>>()
    val userCountriesLiveData = MutableLiveData<List<UserCountryDomainModel>>()

    val languagePositionLiveData = MutableLiveData<Int>()
    val currencyPositionLiveData = MutableLiveData<Int>()
    val countryPositionLiveData = MutableLiveData<Int>()

    private var _initComplete = MutableLiveData<Boolean>()
    val initComplete: LiveData<Boolean>
        get() = _initComplete

    private var _saveButtonVisible = MutableLiveData<Boolean>()
    val saveButtonVisible: LiveData<Boolean>
        get() = _saveButtonVisible

    private var _showSnackBarEvent = MutableLiveData<Pair<Boolean, String>>()
    val showSnackBarEvent: LiveData<Pair<Boolean, String>>
        get() = _showSnackBarEvent

    init {
        viewModelScope.launch {
            _initComplete.value = false
            _saveButtonVisible.value = false

            userSettingsLiveData.value = preferencesRepo.getUserSettings()
            userLanguagesLiveData.value = preferencesRepo.getLanguages()
            userCurrenciesLiveData.value = preferencesRepo.getCurrencies()
            userCountriesLiveData.value = preferencesRepo.getCountries()

            setLanguagePosition()
            setCurrencyPosition()
            setCountryPosition()

            _initComplete.value = true
        }
    }

    private fun setLanguagePosition() {
        userLanguages.forEachIndexed { index, element ->
            if (element.localeCode.equals(userSettings.localeCode, true)) {
                languagePositionLiveData.value = index
                return@forEachIndexed
            }
        }
    }

    private fun setCurrencyPosition() {
        userCurrencies.forEachIndexed { index, element ->
            if (element.currencyCode.equals(userSettings.currencyCode, true)) {
                currencyPositionLiveData.value = index
                return@forEachIndexed
            }
        }
    }

    private fun setCountryPosition() {
        userCountries.forEachIndexed { index, element ->
            if (element.countryCode.equals(userSettings.countryCode, true)) {
                countryPositionLiveData.value = index
                return@forEachIndexed
            }
        }
    }

    // Real Time Data
    var userSettings = UserSettingsDomainModel()

    lateinit var userLanguages: List<UserLanguageDomainModel>
    lateinit var userCurrencies: List<UserCurrencyDomainModel>
    lateinit var userCountries: List<UserCountryDomainModel>

    var oldLanguagePosition = -1
    var newLanguagePosition = -1
    var languageChanged = false

    var oldCurrencyPosition = -1
    var newCurrencyPosition = -1
    var currencyChanged = false

    var oldCountryPosition = -1
    var newCountryPosition = -1
    var countryChanged = false

    val context = TravelUpApp.applicationContext()
    private val saveDataSuccess = context.getString(R.string.msg_save_data_success)
    private val saveDataFailure = context.getString(R.string.msg_save_data_failure)

    fun setNewLanguage(position: Int) {
        if (oldLanguagePosition == -1 && newLanguagePosition == -1) {
            oldLanguagePosition = position
        }

        newLanguagePosition = position

        languageChanged = (newLanguagePosition != oldLanguagePosition)
        languagePositionLiveData.postValue(newLanguagePosition)

        if (languageChanged) {
            userSettings.localeCode = userLanguages[newLanguagePosition].localeCode
        }

        checkSaveButton()
    }

    fun setNewCurrency(position: Int) {
        if (oldCurrencyPosition == -1 && newCurrencyPosition == -1) {
            oldCurrencyPosition = position
        }

        newCurrencyPosition = position

        currencyChanged = (newCurrencyPosition != oldCurrencyPosition)
        currencyPositionLiveData.postValue(newCurrencyPosition)

        if (currencyChanged) {
            userSettings.currencyCode = userCurrencies[newCurrencyPosition].currencyCode
        }

        checkSaveButton()
    }

    fun setNewCountry(position: Int) {
        if (oldCountryPosition == -1 && newCountryPosition == -1) {
            oldCountryPosition = position
        }

        newCountryPosition = position

        countryChanged = (newCountryPosition != oldCountryPosition)
        countryPositionLiveData.postValue(newCountryPosition)

        if (countryChanged) {
            userSettings.countryCode = userCountries[newCountryPosition].countryCode
        }

        checkSaveButton()
    }

    private fun checkSaveButton() {
        val result = (languageChanged || currencyChanged || countryChanged)
        _saveButtonVisible.postValue(result)
    }

    fun saveUserSettings() {
        viewModelScope.launch {
            _saveButtonVisible.postValue(false)

            if (preferencesRepo.saveUserSettings(userSettings) > 0) {
                showSaveSuccess()
            } else {
                showSaveFailure()
            }
        }
    }

    private fun showSaveSuccess() {
        _showSnackBarEvent.value = Pair(true, saveDataSuccess)

    }

    private fun showSaveFailure() {
        _saveButtonVisible.postValue(true)
        _showSnackBarEvent.value = Pair(true, saveDataFailure)
    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = Pair(false, "")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
