package com.sostrovsky.travelup.repository.ticket.search_result

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.ticket.TicketSearchResult
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.repository.settings.SettingsRepository
import com.sostrovsky.travelup.repository.settings.SettingsRepository.fetchSettingsIdFromDB
import com.sostrovsky.travelup.repository.settings.SettingsRepository.generateSettingsDomain
import com.sostrovsky.travelup.repository.ticket.carrier.CarrierRepository
import com.sostrovsky.travelup.repository.ticket.market_place.MarketPlaceRepository
import com.sostrovsky.travelup.repository.ticket.ticket_search_params.TicketSearchParamsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*


/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketSearchResultRepo {
    val database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

    suspend fun fetchTickets(params: TicketSearchParams): List<TicketSearchResult> {
//        Timber.e("1_TicketSearchResultRepo: fetchTickets():" +
//                "\nplaceFrom: ${params.placeFrom}" +
//                "\nplaceTo: ${params.placeTo}" +
//                "\ndepartureDate: ${params.departureDate}")

        val result = mutableListOf<TicketSearchResult>()

        withContext(Dispatchers.IO) {
            var canContinue = true

            val settingsId = SettingsRepository.fetchSettings()?.id
            var ticketSearchParamsId = 0

//            Timber.e("2_TicketSearchResultRepo: fetchTickets():" +
//                    "\nsettingsId: $settingsId")

            if (settingsId != null && settingsId <= 0) {
                canContinue = false
            }

//            Timber.e("3_TicketSearchResultRepo: fetchTickets():" +
//                    "\ncanContinue: $canContinue")

            if (canContinue) {
                ticketSearchParamsId = TicketSearchParamsRepo.getTicketSearchParams(
                    params.placeFrom, params.placeTo, params.departureDate
                )

//                Timber.e("3_1_TicketSearchResultRepo: fetchTickets():" +
//                        "\nticketSearchParamsId: $ticketSearchParamsId")

                if (ticketSearchParamsId <= 0) {
                    canContinue = false
                }
            }

//            Timber.e("4_TicketSearchResultRepo: fetchTickets():" +
//                    "\ncanContinue: $canContinue")

            if (canContinue) {
                result.addAll(
                    database.ticketSearchResultDao.getTickets(
                        settingsId!!,
                        ticketSearchParamsId, getTimestampValid()
                    )
                )
            }

//            Timber.e("5_TicketSearchResultRepo: fetchTickets():" +
//                    "\nresult size : ${result.size}")
        }

        return result
    }

    private fun getTimestampValid(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 1)
        return calendar.timeInMillis
    }

    suspend fun saveTickets(params: TicketSearchParams, tickets: List<TicketDomainModel>): Int {
        Timber.e(
            "1_TicketSearchResultRepo: saveTickets():" +
                    "\nparams: $params" +
                    "\ntickets size: ${tickets.size}"
        )

        var savedRowsAmount = 0

        withContext(Dispatchers.IO) {
            val ticketsDB = mutableListOf<TicketSearchResult>()

            tickets.forEach {
                var canContinue = true

                val settingsDomain = generateSettingsDomain(
                    params.localeCode, params.currencyCode,
                    params.countryCode
                )
                val settingsId = fetchSettingsIdFromDB(settingsDomain)
                var ticketSearchParamsId = 0
                var marketPlaceIdFrom = 0
                var marketPlaceIdTo = 0
                var carrierId = 0

//                Timber.e("2_1_TicketSearchResultRepo: saveTickets():" +
//                        "\nsettingsDomain: $settingsDomain" +
//                        "\nsettingsId: $settingsId")

                if (settingsId == 0) {
                    canContinue = false
                }

                if (canContinue) {
                    marketPlaceIdFrom = MarketPlaceRepository.getIdByName(params.placeFrom)
                    marketPlaceIdTo = MarketPlaceRepository.getIdByName(params.placeTo)

//                    Timber.e("2_2_TicketSearchResultRepo: saveTickets():" +
//                            "\nmarketPlaceIdFrom: $marketPlaceIdFrom" +
//                            "\nmarketPlaceIdTo: $marketPlaceIdTo")

                    if (marketPlaceIdFrom == 0 || marketPlaceIdTo == 0) {
                        canContinue = false
                    }
                }

                if (canContinue) {
                    ticketSearchParamsId = TicketSearchParamsRepo
                        .getTicketSearchParamsId(
                            marketPlaceIdFrom, marketPlaceIdTo,
                            params.departureDate
                        )

//                    Timber.e("2_3_TicketSearchResultRepo: saveTickets():" +
//                            "\nticketSearchParamsId: $ticketSearchParamsId")

                    if (ticketSearchParamsId == 0) {
                        canContinue = false
                    }
                }

                if (canContinue) {
                    carrierId = CarrierRepository.getIdByName(it.carrierName)

//                    Timber.e("2_4_TicketSearchResultRepo: saveTickets():" +
//                            "\ncarrierId: $carrierId")

                    if (carrierId == 0) {
                        canContinue = false
                    }
                }

//                Timber.e("2_5_TicketSearchResultRepo: saveTickets():" +
//                        "\ncanContinue: $canContinue")

                if (canContinue) {
                    Timber.e(
                        "2_5_1_TicketSearchResultRepo: saveTickets():" +
                                "\nsave row"
                    )
                    ticketsDB.add(
                        TicketSearchResult(
                            id = 0,
                            settingsId = settingsId,
                            ticketSearchParamsId = ticketSearchParamsId,
                            carrierId = carrierId,
                            departureTime = it.departureTime,
                            flightPrice = it._flightPriceAsLong,
                            timestamp = Date().time
                        )
                    )
                } else {
                    Timber.e(
                        "2_5_2_TicketSearchResultRepo: saveTickets():" +
                                "\nrefuse row"
                    )
                }
            }

            ticketsDB.let {
                savedRowsAmount =
                    database.ticketSearchResultDao.insertAll(ticketsDB as List<TicketSearchResult>).size
            }
        }

        Timber.e(
            "3_TicketSearchResultRepo: saveTickets():" +
                    "\nsavedRowsAmount: $savedRowsAmount"
        )

        return savedRowsAmount
    }
}