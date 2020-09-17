package com.sostrovsky.travelup.ui.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.domain.ticket.MarketPlaceDomain
import com.sostrovsky.travelup.domain.ticket.TicketDomain
import com.sostrovsky.travelup.repository.ticket.TicketRepository.fetchTicket
import com.sostrovsky.travelup.repository.ticket.market_place.MarketPlaceRepository
import com.sostrovsky.travelup.util.calendarDateToLocalDate
import com.sostrovsky.travelup.util.isNotPastDate
import kotlinx.coroutines.*
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

    private var placeFrom = ""
    private var placeFromComplete = false

    private var placeTo = ""
    private var placeToComplete = false

    private val departureDate = MutableLiveData<String>()
    private var departureDateComplete = false

    private val marketPlaces = MutableLiveData<List<MarketPlaceDomain>>()
    private var needToLoadMarketPlaces = true

    private val searchButtonVisible = MutableLiveData<Boolean>()

    private val ticketSearchResult = MutableLiveData<Triple<List<TicketDomain>, NavDirections,
            String>>()

    fun fetchPlaceFrom(): String {
        return placeFrom
    }

    fun fetchPlaceTo(): String {
        return placeTo
    }

    fun fetchDepartureDate(): LiveData<String> {
        return departureDate
    }

    fun fetchMarketPlaces(): LiveData<List<MarketPlaceDomain>> {
        if (needToLoadMarketPlaces) {
            viewModelScope.launch {
                marketPlaces.value = MarketPlaceRepository.fetchAll()
                needToLoadMarketPlaces = false
            }
        }
        return marketPlaces
    }

    private fun reloadMarketPlaces() {
        marketPlaces.value = emptyList()
        needToLoadMarketPlaces = true
        fetchMarketPlaces()
    }

    fun fetchSearchButtonVisible(): LiveData<Boolean> {
        return searchButtonVisible
    }

    fun fetchTicketSearchResult(): LiveData<Triple<List<TicketDomain>, NavDirections, String>> {
        return ticketSearchResult
    }

    fun setPlaceFrom(text: CharSequence?): CharSequence? {
        var setResult: CharSequence? = inputRequireError

        placeFromComplete = text?.isNotEmpty() ?: false
        checkSearchButton()

        if (placeFromComplete) {
            this.placeFrom = text!!.toString()
            setResult = null
        }

        return setResult
    }

    fun setPlaceTo(text: CharSequence?): CharSequence? {
        var setResult: CharSequence? = inputRequireError

        placeToComplete = text?.isNotEmpty() ?: false
        checkSearchButton()

        if (placeToComplete) {
            this.placeTo = text!!.toString()
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
                setResult = null
            }
            it
        }

        return setResult
    }

    private fun checkSearchButton() {
        val result = (placeFromComplete && placeToComplete && departureDateComplete)
        searchButtonVisible.postValue(result)
    }

    private val inputRequireError = TravelUpApp.applicationContext()
        .getText(R.string.error_input_data_require)

    private val inputDateError = TravelUpApp.applicationContext()
        .getText(R.string.error_input_date_incorrect)

    var canMoveToResults: Boolean = false

    val context = TravelUpApp.applicationContext()
    private val ticketNotFoundMessage = context.getString(R.string.msg_ticket_not_found)

    fun searchTicket() {
        viewModelScope.launch {
            disableSearchButton()
            canMoveToResults = true

            ticketSearchResult.value = Triple(
                fetchTicket(placeFrom, placeTo, departureDate.value!!),
                TicketSearchFragmentDirections.actionTicketSearchFragmentToTicketSearchResultFragment(),
                ticketNotFoundMessage
            )

            enableSearchButton()
            reloadMarketPlaces()
        }
    }

    private fun enableSearchButton() {
        searchButtonVisible.postValue(true)
    }

    private fun disableSearchButton() {
        searchButtonVisible.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}