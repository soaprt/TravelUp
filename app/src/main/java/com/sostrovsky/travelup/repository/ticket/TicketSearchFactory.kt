package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.repository.DataFetcher
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketSearchFactory {
    suspend fun fetch(fromCache: Boolean, params: TicketSearchParams): List<TicketDomainModel> {
        Timber.e("TicketSearchFactory: fetch()")
        val ticketFetcher: DataFetcher<TicketSearchParams, List<TicketDomainModel>> =
            if (fromCache) {
                TicketCacheFetcher
            } else {
                TicketWebServiceFetcher
            }
        return ticketFetcher.fetch(params)
    }
}