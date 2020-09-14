package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.ticket.TicketDBModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.network.dto.place.asDatabaseModel
import com.sostrovsky.travelup.network.dto.ticket.TicketsResponse
import com.sostrovsky.travelup.network.dto.ticket.asDatabaseModel
import com.sostrovsky.travelup.util.network.safeApiCall
import com.sostrovsky.travelup.repository.DataFetcher
import com.sostrovsky.travelup.util.mockedTicketsResponse
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketFetcherOnline : DataFetcher<TicketSearchParams, List<TicketDBModel>> {
    override suspend fun fetch(param: TicketSearchParams): List<TicketDBModel> {
        Timber.e("TicketFetcherOnline: fetch()")
        val tickets = mutableListOf<TicketDBModel>()

        val debug = false

        if (debug) {
            fetchMockedTickets()?.let {
                if (it.Quotes.isNotEmpty()) {
                    tickets.addAll(it.asDatabaseModel())
                }
            }
        } else {
            val originPlace = getPlaceId(param, param.destinationFrom)
            var destinationPlace = ""
            var continueSearch = true

            if (originPlace.isEmpty()) {
                continueSearch = false
            }

            if (continueSearch) {
                destinationPlace = getPlaceId(param, param.flyingTo)
                if (destinationPlace.isEmpty()) {
                    continueSearch = false
                }
            }

            if (continueSearch) {
                fetchTickets(originPlace, destinationPlace, param)?.let {
                    if (it.Quotes.isNotEmpty()) {
                        tickets.addAll(it.asDatabaseModel())
                    }
                }
            }
        }

        return tickets
    }

    private fun fetchMockedTickets(): TicketsResponse? {
        return mockedTicketsResponse()
    }

    private suspend fun fetchTickets(
        originPlace: String, destinationPlace: String,
        param: TicketSearchParams
    ): TicketsResponse? {
        return safeApiCall(
            call = {
                WebService.ticketService.fetchTicketsAsync(
                    BuildConfig.API_VERSION,
                    param.userSettings.countryCode,
                    param.userSettings.currencyCode,
                    param.userSettings.localeCode,
                    originPlace,
                    destinationPlace,
                    param.departureDate,
                    "",
                    BuildConfig.API_KEY
                ).await()
            },
            error = "Error fetching tickets"
        )
    }

    private suspend fun getPlaceId(param: TicketSearchParams, query: String): String {
        var result = ""

        val response = safeApiCall(
            call = {
                WebService.placeService.fetchPlacesAsync(
                    BuildConfig.API_VERSION,
                    param.userSettings.countryCode,
                    param.userSettings.currencyCode,
                    param.userSettings.localeCode,
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