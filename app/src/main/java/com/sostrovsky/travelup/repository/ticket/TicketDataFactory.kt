package com.sostrovsky.travelup.repository.ticket

import com.sostrovsky.travelup.database.entities.ticket.TicketDBModel
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.util.network.NetworkHelper
import com.sostrovsky.travelup.repository.DataFetcher
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object TicketDataFactory {
    suspend fun fetch(param: TicketSearchParams): List<TicketDBModel> {
        Timber.e("TicketDataFactory: fetch()")
        val ticketFetcher: DataFetcher<TicketSearchParams, List<TicketDBModel>> =
            if (NetworkHelper.isAvailable) {
                TicketFetcherOnline
            } else {
                TicketFetcherOffline
            }
        return ticketFetcher.fetch(param)
    }
}