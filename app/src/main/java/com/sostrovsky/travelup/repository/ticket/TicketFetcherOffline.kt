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
object TicketFetcherOffline : DataFetcher<TicketSearchParams, List<TicketDomainModel>> {
    override suspend fun fetch(param: TicketSearchParams): List<TicketDomainModel> {
        Timber.e("TicketFetcherOffline: fetch()")

        return emptyList()
    }
}