package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.domain.ticket.TicketDomain
import com.sostrovsky.travelup.domain.ticket.TicketSearchParamsDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketRepository : TicketContract {
    val database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

    override suspend fun fetchTicket(placeFrom: String, placeTo: String, departureDate: String):
            List<TicketDomain> {
        val result = mutableListOf<TicketDomain>()

        withContext(Dispatchers.IO) {
            val params = generateTicketSearchParams(placeFrom, placeTo, departureDate)

            if (params != null) {
                result.addAll(TicketCacheFetcher.fetch(params))
            }
        }

        return result
    }

    private suspend fun generateTicketSearchParams(
        placeFrom: String, placeTo: String,
        departureDate: String
    ): TicketSearchParamsDomain? {
        var result: TicketSearchParamsDomain? = null

        withContext(Dispatchers.IO) {
            val settings = database.settingsDao.getSelected()
            val countryCode = database.countryDao.getCodeById(settings.countryId)
            val currencyCode = database.currencyDao.getCodeById(settings.currencyId)
            val localeCode = database.languageDao.getCodeById(settings.languageId)

            result = TicketSearchParamsDomain(
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