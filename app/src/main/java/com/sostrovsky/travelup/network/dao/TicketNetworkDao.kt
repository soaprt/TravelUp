package com.sostrovsky.travelup.network.dao

import com.sostrovsky.travelup.network.dto.ticket.TicketsResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface TicketNetworkDao {
    @GET("browsequotes/{version}/{country}/{currency}/{locale}/{originPlace}/{destinationPlace}/{outboundPartialDate}/{inboundPartialDate}")
    fun getTickets(
        @Path("version") version: String,
        @Path("country") country: String,
        @Path("currency") currency: String,
        @Path("locale") locale: String,
        @Path("originPlace") originPlace: String,
        @Path("destinationPlace") destinationPlace: String,
        @Path("outboundPartialDate") outboundPartialDate: String,
        @Path("inboundPartialDate") inboundPartialDate: String,
        @Query("apikey") apiKey: String
    ): Deferred<TicketsResponse>
}