package com.sostrovsky.travelup.repository.ticket.search_result

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.ticket.TicketSearchResult
import com.sostrovsky.travelup.domain.ticket.TicketDomain
import com.sostrovsky.travelup.domain.ticket.TicketSearchParamsDomain
import com.sostrovsky.travelup.repository.settings.SettingsRepository
import com.sostrovsky.travelup.repository.settings.SettingsRepository.fetchSettingsIdFromDB
import com.sostrovsky.travelup.repository.settings.SettingsRepository.generateSettingsDomain
import com.sostrovsky.travelup.repository.ticket.carrier.CarrierRepository
import com.sostrovsky.travelup.repository.ticket.market_place.MarketPlaceRepository
import com.sostrovsky.travelup.repository.ticket.ticket_search_params.TicketSearchParamsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*


/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketSearchResultRepo {
    val database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

    suspend fun fetchTickets(params: TicketSearchParamsDomain): List<TicketSearchResult> {
        val result = mutableListOf<TicketSearchResult>()

        withContext(Dispatchers.IO) {
            var canContinue = true

            val settingsId = SettingsRepository.fetchSettings()?.id
            var ticketSearchParamsId = 0

            if (settingsId != null && settingsId <= 0) {
                canContinue = false
            }

            if (canContinue) {
                ticketSearchParamsId = TicketSearchParamsRepo.getTicketSearchParams(
                    params.placeFrom, params.placeTo, params.departureDate
                )

                if (ticketSearchParamsId <= 0) {
                    canContinue = false
                }
            }

            if (canContinue) {
                result.addAll(
                    database.ticketSearchResultDao.getTickets(
                        settingsId!!,
                        ticketSearchParamsId, getTimestampValid()
                    )
                )
            }
        }

        return result
    }

    private fun getTimestampValid(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 1)
        return calendar.timeInMillis
    }

    suspend fun saveTickets(params: TicketSearchParamsDomain, tickets: List<TicketDomain>): Int {
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

                if (settingsId == 0) {
                    canContinue = false
                }

                if (canContinue) {
                    marketPlaceIdFrom = MarketPlaceRepository.getIdByName(params.placeFrom)
                    marketPlaceIdTo = MarketPlaceRepository.getIdByName(params.placeTo)

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

                    if (ticketSearchParamsId == 0) {
                        canContinue = false
                    }
                }

                if (canContinue) {
                    carrierId = CarrierRepository.getIdByName(it.carrierName)

                    if (carrierId == 0) {
                        canContinue = false
                    }
                }

                if (canContinue) {
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
                }
            }

            ticketsDB.let {
                savedRowsAmount =
                    database.ticketSearchResultDao.insertAll(ticketsDB as List<TicketSearchResult>).size
            }
        }

        return savedRowsAmount
    }
}