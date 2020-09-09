package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface TicketContract {
    suspend fun init()
    suspend fun getTickets(params: TicketSearchParams): List<TicketDomainModel>
}