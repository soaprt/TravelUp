package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.network.dto.place.asDatabaseModel
import com.sostrovsky.travelup.network.dto.ticket.TicketsResponse
import com.sostrovsky.travelup.network.dto.ticket.asDomainModel
import com.sostrovsky.travelup.repository.DataFetcher
import com.sostrovsky.travelup.util.mockedTicketsResponse
import com.sostrovsky.travelup.util.network.safeApiCall
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketWebServiceFetcher : DataFetcher<TicketSearchParams, List<TicketDomainModel>> {
    override suspend fun fetch(params: TicketSearchParams): List<TicketDomainModel> {
        Timber.e("TicketWebServiceFetcher: fetch()")
        val tickets = mutableListOf<TicketDomainModel>()

        val debug = false

        if (debug) {
            fetchMockedTickets()?.let {
                if (it.Quotes.isNotEmpty()) {
                    tickets.addAll(it.asDomainModel())
                }
            }
        } else {
            val originPlace = getPlaceId(params, params.placeFrom)
            var destinationPlace = ""
            var continueSearch = true

            if (originPlace.isEmpty()) {
                continueSearch = false
            }

            if (continueSearch) {
                destinationPlace = getPlaceId(params, params.placeTo)
                if (destinationPlace.isEmpty()) {
                    continueSearch = false
                }
            }

            if (continueSearch) {
                fetchTickets(originPlace, destinationPlace, params)?.let {
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

    private suspend fun fetchTickets(originPlace: String, destinationPlace: String,
        params: TicketSearchParams
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

    private suspend fun getPlaceId(params: TicketSearchParams, query: String): String {
        var result = ""

        val response = safeApiCall(
            call = {
                WebService.placeService.fetchPlacesAsync(
                    BuildConfig.API_VERSION,
                    params.countryCode,
                    params.currencyCode,
                    params.localeCode,
                    query,
                    BuildConfig.API_KEY
                ).await()
            },
            error = "Error fetching place"
        )

        if (response?.Places?.isNotEmpty() == true) {
            result = response.asDatabaseModel()[0].placeCode
        }

        return result
    }
}