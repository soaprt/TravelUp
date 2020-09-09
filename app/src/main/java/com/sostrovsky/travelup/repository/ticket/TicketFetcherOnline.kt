package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.ticket.TicketDBModel
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.network.Network
import com.sostrovsky.travelup.network.dto.place.asDatabaseModel
import com.sostrovsky.travelup.network.dto.ticket.asDatabaseModel
import com.sostrovsky.travelup.repository.DataFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketFetcherOnline : DataFetcher<TicketSearchParams, List<TicketDomainModel>> {
    override suspend fun fetch(param: TicketSearchParams): List<TicketDomainModel> {
        Timber.e("TicketFetcherOnline: fetch()")
        return fetchTickets(
            param
        ).map {
            TicketDomainModel(
                id = it.id
            )
        }
    }

    private suspend fun fetchTickets(param: TicketSearchParams): List<TicketDBModel> {
        val tickets = mutableListOf<TicketDBModel>()

        withContext(Dispatchers.IO) {
            try {
                val response = Network.ticketNetworkDao.getTickets(
                    BuildConfig.API_VERSION,
                    param.userSettings.countryCode,
                    param.userSettings.currencyCode,
                    param.userSettings.localeCode,
                    getPlaceId(
                        param,
                        param.destinationFrom
                    ),
                    getPlaceId(
                        param,
                        param.flyingTo
                    ),
                    param.departureDate,
                    "",
                    BuildConfig.API_KEY
                ).await()

                tickets.addAll(response.asDatabaseModel())
            } catch (e: Exception) {
                Timber.e("fetchTickets error")
            }
        }

        return tickets
    }

    private suspend fun getPlaceId(param: TicketSearchParams, query: String): String {
        var result = ""

        val response = Network.placeNetworkDao.getPlaces(
            BuildConfig.API_VERSION,
            param.userSettings.countryCode,
            param.userSettings.currencyCode,
            param.userSettings.localeCode,
            query,
            BuildConfig.API_KEY
        ).await()

        if (response.Places.isNotEmpty()) {
            result = response.asDatabaseModel()[0].placeCode
        }

        return result
    }

//    withContext(Dispatchers.IO) {
//        result = database.ticketDao.getAll().asDomainModel()
//    }
}