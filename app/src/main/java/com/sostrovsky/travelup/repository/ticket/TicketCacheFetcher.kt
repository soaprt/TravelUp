package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.domain.ticket.TicketDomain
import com.sostrovsky.travelup.domain.ticket.TicketSearchParamsDomain
import com.sostrovsky.travelup.repository.DataFetcher
import com.sostrovsky.travelup.repository.ticket.carrier.CarrierRepository
import com.sostrovsky.travelup.repository.ticket.search_result.TicketSearchResultRepo
import com.sostrovsky.travelup.util.network.NetworkHelper

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketCacheFetcher : DataFetcher<TicketSearchParamsDomain, List<TicketDomain>> {
    override suspend fun fetch(params: TicketSearchParamsDomain): List<TicketDomain> {
        val tickets = mutableListOf<TicketDomain>()
        tickets.addAll(fetchTicketsFromDB(params))

        if (tickets.isEmpty() && NetworkHelper.isAvailable) {
            val ticketsWebService = TicketWebServiceFetcher.fetch(params)
            if (ticketsWebService.isNotEmpty()) {
                tickets.addAll(savedTicketsToDB(params, ticketsWebService))
            }
        }

        return tickets
    }

    private suspend fun fetchTicketsFromDB(params: TicketSearchParamsDomain): List<TicketDomain> {
        return TicketSearchResultRepo.fetchTickets(params).map {
            TicketDomain(
                departureDate = params.departureDate,
                departureTime = it.departureTime,
                departureFrom = params.placeFrom,
                departureTo = params.placeTo,
                carrierName = CarrierRepository.getNameById(it.carrierId),
                _flightPriceAsLong = it.flightPrice,
                flightPriceCurrency = params.currencyCode
            )
        }
    }

    private suspend fun savedTicketsToDB(
        params: TicketSearchParamsDomain,
        tickets: List<TicketDomain>
    ): List<TicketDomain> {
        TicketSearchResultRepo.saveTickets(params, tickets)
        return tickets
    }
}