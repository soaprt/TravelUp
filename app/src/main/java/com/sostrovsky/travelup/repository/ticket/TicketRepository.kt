package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.util.network.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketRepository : TicketContract {
    lateinit var database: TravelUpDatabase

    override suspend fun init() {
        database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())
    }

    override suspend fun fetchTicket(placeFrom: String, placeTo: String, departureDate: String):
            List<TicketDomainModel> {
        Timber.e("1_TicketRepository: fetchTicket():" +
                "\nplaceFrom: $placeFrom" +
                "\nplaceTo: $placeTo" +
                "\ndepartureDate: $departureDate ")

        val result = mutableListOf<TicketDomainModel>()

        withContext(Dispatchers.IO) {
            val params = generateTicketSearchParams(placeFrom, placeTo, departureDate)

            if (params != null) {
                val cachedData = TicketSearchFactory.fetch(true, params)

                if (cachedData.isEmpty()) {
                    if (NetworkHelper.isAvailable) {
                        val webServiceData = TicketSearchFactory.fetch(false, params)
                        result.addAll(webServiceData)
                    } else {
                    }
                } else {
                    result.addAll(cachedData)
                }
            }
        }

        Timber.e("2_TicketRepository: fetchTicket():" +
                "\nresult size = ${result.size}")

        return result
    }

    private suspend fun generateTicketSearchParams(placeFrom: String, placeTo: String,
                                           departureDate: String): TicketSearchParams? {
        var result: TicketSearchParams? = null

        withContext(Dispatchers.IO) {
            val settings = database.settingsDao.getSelected()
            val countryCode = database.countryDao.getCodeById(settings.countryId)
            val currencyCode = database.currencyDao.getCodeById(settings.currencyId)
            val localeCode = database.languageDao.getCodeById(settings.languageId)

            result = TicketSearchParams(
                placeFrom = placeFrom, placeTo = placeTo,
                departureDate = departureDate,
                countryCode = countryCode,
                currencyCode = currencyCode,
                localeCode = localeCode
            )
        }

        return result
    }
}