package com.sostrovsky.travelup.ui.ticket.search_result

import androidx.lifecycle.*
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class TicketSearchResultViewModel() : ViewModel() {
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
    private val ticketRepository = Repository.getTickets()

    private val _ticketSearchResult = MutableLiveData<List<TicketDomainModel>>()
    val ticketSearchResult: LiveData<List<TicketDomainModel>>
        get() = _ticketSearchResult

    init {
        viewModelScope.launch {
//            _ticketSearchResult.value = ticketRepository.getTickets()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
