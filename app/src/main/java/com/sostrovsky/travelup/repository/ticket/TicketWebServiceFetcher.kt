package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.domain.ticket.TicketDomain
import com.sostrovsky.travelup.domain.ticket.TicketSearchParamsDomain
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.network.dto.ticket.TicketsResponse
import com.sostrovsky.travelup.network.dto.ticket.asDomainModel
import com.sostrovsky.travelup.repository.DataFetcher
import com.sostrovsky.travelup.repository.ticket.market_place.MarketPlaceRepository
import com.sostrovsky.travelup.util.mockedTicketsResponse
import com.sostrovsky.travelup.util.network.safeApiCall

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketWebServiceFetcher : DataFetcher<TicketSearchParamsDomain, List<TicketDomain>> {
    override suspend fun fetch(params: TicketSearchParamsDomain): List<TicketDomain> {
        val tickets = mutableListOf<TicketDomain>()

        val debug = false

        if (debug) {
            fetchMockedTickets()?.let {
                if (it.Quotes.isNotEmpty()) {
                    tickets.addAll(it.asDomainModel())
                }
            }
        } else {
            val originPlace = MarketPlaceRepository.fetchCodeFromWebService(
                params,
                params.placeFrom
            )
            var destinationPlace = ""
            var continueSearch = true

            if (originPlace.isEmpty()) {
                continueSearch = false
            }

            if (continueSearch) {
                destinationPlace = MarketPlaceRepository.fetchCodeFromWebService(
                    params,
                    params.placeTo
                )
                if (destinationPlace.isEmpty()) {
                    continueSearch = false
                }
            }

            if (continueSearch) {
                fetchFromWebService(originPlace, destinationPlace, params)?.let {
                    if (it.Quotes.isNotEmpty()) {
                        tickets.addAll(it.asDomainModel())
                    }
                }
            }
        }

        return tickets
    }

    private fun fetchMockedTickets(): TicketsResponse? {
        return mockedTicketsResponse()
    }

    private suspend fun fetchFromWebService(
        originPlace: String, destinationPlace: String,
        params: TicketSearchParamsDomain
    ): TicketsResponse? {
        return safeApiCall(
            call = {
                WebService.ticketService.fetchTicketsAsync(
                    BuildConfig.API_VERSION,
                    params.countryCode,
                    params.currencyCode,
                    params.localeCode,
                    originPlace,
                    destinationPlace,
                    params.departureDate,
                    "",
                    BuildConfig.API_KEY
                ).await()
            },
            error = "Error fetching tickets"
        )
    }
}