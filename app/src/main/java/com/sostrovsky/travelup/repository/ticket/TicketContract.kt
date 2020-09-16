package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.domain.ticket.TicketDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface TicketContract {
    suspend fun fetchTicket(placeFrom: String, placeTo: String, departureDate: String):
            List<TicketDomainModel>
}