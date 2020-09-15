package com.sostrovsky.travelup.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.domain.settings.CountryDomain
import com.sostrovsky.travelup.domain.settings.CurrencyDomain
import com.sostrovsky.travelup.domain.settings.LanguageDomain
import com.sostrovsky.travelup.repository.settings.SettingsRepository.fetchSpinnerCountry
import com.sostrovsky.travelup.repository.settings.SettingsRepository.fetchSpinnerCurrency
import com.sostrovsky.travelup.repository.settings.SettingsRepository.fetchSpinnerLanguage
import com.sostrovsky.travelup.repository.settings.SettingsRepository.saveUserSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class SettingsViewModel : ViewModel() {
    /**
     * The job for all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     */
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var languages = MutableLiveData<Pair<List<LanguageDomain>, MutableList<Int>>>()
    private var languagesLoaded = false

    private var currencies = MutableLiveData<Pair<List<CurrencyDomain>, MutableList<Int>>>()
    private var currenciesLoaded = false

    private var countries = MutableLiveData<Pair<List<CountryDomain>, MutableList<Int>>>()
    private var countriesLoaded = false

    val context = TravelUpApp.applicationContext()
    private val saveDataSuccess = context.getString(R.string.msg_save_data_success)
    private val saveDataFailure = context.getString(R.string.msg_save_data_failure)

    private var _saveButtonVisible = MutableLiveData<Boolean>()
    val saveButtonVisible: LiveData<Boolean>
        get() = _saveButtonVisible

    private var _showSnackBarEvent = MutableLiveData<Pair<Boolean, String>>()
    val showSnackBarEvent: LiveData<Pair<Boolean, String>>
        get() = _showSnackBarEvent

    fun fetchLanguages(): LiveData<Pair<List<LanguageDomain>, MutableList<Int>>> {
        if (!languagesLoaded) {
            viewModelScope.launch {
                languages.value = fetchSpinnerLanguage()
                languagesLoaded = true
            }
        }

        return languages
    }

    fun fetchCurrencies(): LiveData<Pair<List<CurrencyDomain>, MutableList<Int>>> {
        if (!currenciesLoaded) {
            viewModelScope.launch {
                currencies.value = fetchSpinnerCurrency()
                currenciesLoaded = true
            }
        }

        return currencies
    }

    fun fetchCountries(): LiveData<Pair<List<CountryDomain>, MutableList<Int>>> {
        if (!countriesLoaded) {
            viewModelScope.launch {
                countries.value = fetchSpinnerCountry()
                countriesLoaded = true
            }
        }

        return countries
    }

    fun setNewLanguage(position: Int) {
        setNewPosition(languages.value?.second!!, position)
        checkSaveButton()
    }

    fun setNewCurrency(position: Int) {
        setNewPosition(currencies.value?.second!!, position)
        checkSaveButton()
    }

    fun setNewCountry(position: Int) {
        setNewPosition(countries.value?.second!!, position)
        checkSaveButton()
    }

    private fun setNewPosition(list: MutableList<Int>, position: Int) {
        if (position == getLastListItem(list)) return

        if (list.size > 1) {
            if (position != list[0]) {
                list[1] = position
            } else {
                list.removeAt(1)
            }
        } else {
            if (position != list[0]) {
                list.add(position)
            }
        }
    }

    private fun checkSaveButton() {
        val result = (valueChanged(languages) || valueChanged(currencies) ||
                valueChanged(countries))
        _saveButtonVisible.postValue(result)
    }

    fun saveSettings() {
        viewModelScope.launch {
            _saveButtonVisible.postValue(false)

            val languagePosition = getLastListItem(languages.value?.second!!)
            val languageCode = languages.value?.first?.get(languagePosition)?.code!!

            val currencyPosition = getLastListItem(currencies.value?.second!!)
            val currencyCode = currencies.value?.first?.get(currencyPosition)?.code!!

            val countryPosition = getLastListItem(countries.value?.second!!)
            val countryCode = countries.value?.first?.get(countryPosition)?.code!!

            if (saveUserSettings(languageCode, currencyCode, countryCode)) {
                resetPositions()
                showSaveSuccess()
            } else {
                showSaveFailure()
            }
        }
    }

    fun getLastListItem(list: MutableList<Int>): Int {
        return list.takeLast(1)[0]
    }

    private fun resetPositions() {
        if (valueChanged(languages)) {
            resetPosition(languages)
        }

        if (valueChanged(currencies)) {
            resetPosition(currencies)
        }

        if (valueChanged(countries)) {
            resetPosition(countries)
        }
    }

    private fun <T> valueChanged(list: MutableLiveData<Pair<List<T>, MutableList<Int>>>): Boolean {
        return list.value?.second?.size?.let { it > 1 } ?: false
    }

    private fun <T> resetPosition(list: MutableLiveData<Pair<List<T>, MutableList<Int>>>) {
        list.value?.second?.let {
            it[0] = it[1]
            it.removeAt(1)
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
