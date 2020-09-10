package com.sostrovsky.travelup.ui.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.domain.place.PlaceSearchParamsDomainModel
import com.sostrovsky.travelup.domain.preferences.UserSettingsDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.repository.Repository
import com.sostrovsky.travelup.util.calendarDateToLocalDate
import com.sostrovsky.travelup.util.isNotPastDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class TicketViewModel : ViewModel() {
    /**
     * The job for all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val preferencesRepository = Repository.getPreferences()
    private val ticketRepository = Repository.getTickets()

    // Live Data
    private var _searchButtonVisible = MutableLiveData<Boolean>()
    val searchButtonVisible: LiveData<Boolean>
        get() = _searchButtonVisible

    private val _ticketSearchResult = MutableLiveData<Pair<List<TicketDomainModel>, String>>()
    val ticketSearchResult: LiveData<Pair<List<TicketDomainModel>, String>>
        get() = _ticketSearchResult

    private var _userSettings = MutableLiveData<UserSettingsDomainModel>()
    val userSettings: LiveData<UserSettingsDomainModel>
        get() = _userSettings

    val departureDate = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            _searchButtonVisible.value = false
            _userSettings.value = preferencesRepository.getUserSettings()
        }
    }

    // Real Time Data
    val placeSearchParams = PlaceSearchParamsDomainModel()
    val ticketSearchParams = TicketSearchParams()

    private val inputRequireError = TravelUpApp.applicationContext()
        .getText(R.string.error_input_data_require)

    private val inputDateError = TravelUpApp.applicationContext()
        .getText(R.string.error_input_date_incorrect)

    private var destinationFromComplete = false
    private var flyingToComplete = false
    private var departureDateComplete = false

    val context = TravelUpApp.applicationContext()
    private val ticketNotFoundMessage = context.getString(R.string.msg_ticket_not_found)

    fun setDestinationFrom(destinationFrom: CharSequence?): CharSequence? {
        var setResult: CharSequence? = inputRequireError

        destinationFromComplete = destinationFrom?.isNotEmpty() ?: false
        checkSearchButton()

        if (destinationFromComplete) {
            ticketSearchParams.destinationFrom = destinationFrom!!.toString()
            setResult = null
        }

        return setResult
    }

    fun setFlyingTo(flyingTo: CharSequence?): CharSequence? {
        var setResult: CharSequence? = inputRequireError

        flyingToComplete = flyingTo?.isNotEmpty() ?: false
        checkSearchButton()

        if (flyingToComplete) {
            ticketSearchParams.flyingTo = flyingTo!!.toString()
            setResult = null
        }

        return setResult
    }

    fun setDepartureDate(selectedDate: Calendar): CharSequence? {
        var setResult: CharSequence? = inputDateError

        departureDateComplete = isNotPastDate(selectedDate)
        checkSearchButton()

        departureDate.value = calendarDateToLocalDate(selectedDate.time).let {
            if (departureDateComplete) {
                ticketSearchParams.departureDate = it
                setResult = null
            }
            it
        }

        return setResult
    }

    private fun checkSearchButton() {
        val result = (destinationFromComplete && flyingToComplete && departureDateComplete)
        _searchButtonVisible.postValue(result)
    }

    fun searchTicket() {
        viewModelScope.launch {
            disableSearchButton()
            _ticketSearchResult.value = Pair(
                ticketRepository.getTickets(ticketSearchParams),
                ticketNotFoundMessage
            )
            enableSearchButton()
        }
    }

    private fun enableSearchButton() {
        _searchButtonVisible.postValue(true)
    }

    private fun disableSearchButton() {
        _searchButtonVisible.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}