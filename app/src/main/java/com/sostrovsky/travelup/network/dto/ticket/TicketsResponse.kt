package com.sostrovsky.travelup.network.dto.ticket

import com.sostrovsky.travelup.database.entities.ticket.TicketDBModel
import com.sostrovsky.travelup.network.dto.preferences.CurrencyFromJSON
import com.squareup.moshi.JsonClass

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@JsonClass(generateAdapter = true)
data class TicketsResponse(val Quotes: List<QuoteFromJSON>, val Places: List<TicketPlaceFromJSON>,
                           val Carriers: List<Carrier>, val Currencies: List<CurrencyFromJSON>)

@JsonClass(generateAdapter = true)
data class QuoteFromJSON(val QuoteId: Int, val MinPrice: Int, val Direct: Boolean,
                         val OutboundLeg: BoundLegFromJSON, val InboundLeg: BoundLegFromJSON,
                         val QuoteDateTime: String)

@JsonClass(generateAdapter = true)
data class BoundLegFromJSON(val CarrierIds: List<Int>, val OriginId: Int, val DestinationId: Int,
                            val DepartureDate: String)

@JsonClass(generateAdapter = true)
data class TicketPlaceFromJSON(val PlaceId: Int, val IataCode: String?, val Name: String,
                               val Type: String, val SkyscannerCode: String, val CityName: String?,
                               val CityId: String?, val CountryName: String?)

@JsonClass(generateAdapter = true)
data class Carrier(val CarrierId: Int,val Name: String)


/**
 * Convert Network results to database objects
 */
fun TicketsResponse.asDatabaseModel(): List<TicketDBModel> {
    return Quotes.map {
        TicketDBModel(
            id = it.QuoteId)
    }
}

/**
 * Convert Network results to database objects
 */
/*fun NetworkBrowseQuotes.asDomainModel(): List<DomainTicketSearchResult> {
    return tickets.map {
        DomainTicketSearchResult(
            id = it.Quotes.get(0).QuoteId)
    }
}*/