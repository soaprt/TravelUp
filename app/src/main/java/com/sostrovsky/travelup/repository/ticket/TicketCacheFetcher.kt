package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.database.entities.ticket.TicketSearchResult
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.repository.DataFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketCacheFetcher : DataFetcher<TicketSearchParams, List<TicketDomainModel>> {
    override suspend fun fetch(params: TicketSearchParams): List<TicketDomainModel> {
        Timber.e("TicketCacheFetcher: fetch()")

        return fetchTickets(params).map {
            val carrierName = TicketRepository.database.carrierDao.getNameById(it.carrierId)

            TicketDomainModel(
                departureDate = params.departureDate,
                departureTime = it.departureTime,
                departureFrom = params.placeFrom,
                departureTo = params.placeTo,
                carrierName = carrierName,
                _flightPrice = it.flightPrice,
                flightPriceCurrency = params.currencyCode
            )
        }
    }

    private suspend fun fetchTickets(params: TicketSearchParams) : List<TicketSearchResult>{
        Timber.e("1_TicketCacheFetcher: fetchTickets():" +
                "\nplaceFrom: ${params.placeFrom}" +
                "\nplaceTo: ${params.placeTo}" +
                "\ndepartureDate: ${params.departureDate}")

        val result = mutableListOf<TicketSearchResult>()

        withContext(Dispatchers.IO) {
            var canContinue = true

            val settingsId = getSelectedSettingsId()
            var ticketSearchParamsId = 0L
            val timestampValid = getTimestampValid()

            Timber.e("2_TicketCacheFetcher: fetchTickets():" +
                    "\nsettingsId: $settingsId" +
                    "\ntimestampValid: $timestampValid")

            if (settingsId <= 0) {
                canContinue = false
            }

            Timber.e("3_TicketCacheFetcher: fetchTickets():" +
                    "\ncanContinue: $canContinue")

            if (canContinue) {
                ticketSearchParamsId = getTicketSearchParams(params.placeFrom,
                    params.placeTo, params.departureDate)

                Timber.e("3_1_TicketCacheFetcher: fetchTickets():" +
                        "\nticketSearchParamsId: $ticketSearchParamsId")

                if (ticketSearchParamsId <= 0L) {
                    canContinue = false
                }
            }

            Timber.e("4_TicketCacheFetcher: fetchTickets():" +
                    "\ncanContinue: $canContinue")

            if (canContinue) {
                result.addAll(
                    TicketRepository.database.ticketSearchResultDao.getTickets(
                        settingsId,
                        ticketSearchParamsId, timestampValid
                    )
                )
            }

            Timber.e("5_TicketCacheFetcher: fetchTickets():" +
                    "\nresult size : ${result.size}")
        }

        return result
    }

    private suspend fun getTicketSearchParams(
        placeFrom: String, placeTo: String,
        departureDate: String
    ): Long {
        Timber.e("1_TicketCacheFetcher: getTicketSearchParams():" +
                "\nplaceFrom: $placeFrom" +
                "\nplaceTo: $placeTo" +
                "\ndepartureDate: $departureDate ")

        val result = mutableListOf(0L)

        withContext(Dispatchers.IO) {
            val isMarketPlaceDBEmpty = TicketRepository.database.marketPlaceDao.checkIfEmpty() == 0L
            Timber.e("2_TicketCacheFetcher: getTicketSearchParams():" +
                    "\nmarketPlace is empty: $isMarketPlaceDBEmpty")

            if (!isMarketPlaceDBEmpty) {
                var canContinue = true

                val marketPlaceIdFrom = getMarketPlaceId(placeFrom)
                var marketPlaceIdTo = 0L

                Timber.e("2_1_TicketCacheFetcher: getTicketSearchParams():" +
                        "\nmarketPlaceIdFrom: $marketPlaceIdFrom")

                if (marketPlaceIdFrom <= 0) {
                    canContinue = false
                }

                Timber.e("2_2_TicketCacheFetcher: getTicketSearchParams():" +
                        "\ncanContinue: $canContinue")

                if (canContinue) {
                    marketPlaceIdTo = getMarketPlaceId(placeTo)

                    Timber.e("2_2_1_TicketCacheFetcher: getTicketSearchParams():" +
                            "\nmarketPlaceIdTo: $marketPlaceIdTo")

                    if (marketPlaceIdTo <= 0) {
                        canContinue = false
                    }
                }

                Timber.e("2_3_TicketCacheFetcher: getTicketSearchParams():" +
                        "\ncanContinue: $canContinue")

                if (canContinue) {
                    result[0] = TicketRepository.database.ticketSearchParamsDao.getId(
                        marketPlaceIdFrom, marketPlaceIdTo, departureDate
                    )
                }
            }

            Timber.e("3_TicketCacheFetcher: getTicketSearchParams():" +
                    "\nticketSearchParamsId: ${result[0]}")
        }

        return result[0]
    }

    private suspend fun getSelectedSettingsId(): Long {
        val result = mutableListOf(0L)

        withContext(Dispatchers.IO) {
            result[0] = TicketRepository.database.settingsDao.getSelectedSettingsId()
        }

        Timber.e("TicketCacheFetcher: getSelectedSettingsId():" +
                "\nresult : ${result[0]}")

        return result[0]
    }

    private fun getTimestampValid(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 1)
        return calendar.timeInMillis
    }

    private suspend fun getMarketPlaceId(name: String): Long {
        Timber.e("1_TicketCacheFetcher: getMarketPlaceId():" +
                "\nname: $name")

        val result = mutableListOf(0L)

        withContext(Dispatchers.IO) {
            result[0] = TicketRepository.database.marketPlaceDao.getMarketPlaceIdByName(name)
        }

        Timber.e("2_TicketCacheFetcher: getMarketPlaceId():" +
                "\nmarketPlaceIdByName: ${result[0]}")

        return result[0]
    }
}