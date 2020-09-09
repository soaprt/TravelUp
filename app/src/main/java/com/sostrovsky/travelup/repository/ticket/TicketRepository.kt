package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams

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

    override suspend fun getTickets(params: TicketSearchParams): List<TicketDomainModel> {
        return TicketDataFactory.fetch(params)
    }
}