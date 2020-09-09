package com.sostrovsky.travelup.network.dto.ticket

import com.sostrovsky.travelup.database.entities.ticket.TicketDBModel
import com.sostrovsky.travelup.network.dto.preferences.CurrencyFromJSON
import com.sostrovsky.travelup.util.getFormattedUTCDate
import com.sostrovsky.travelup.util.getFormattedUTCTime
import com.squareup.moshi.JsonClass

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@JsonClass(generateAdapter = true)
data class TicketsResponse(val Quotes: List<QuoteFromJSON>, val Places: List<TicketPlaceFromJSON>,
                           val Carriers: List<CarrierFromJSON>, val Currencies: List<CurrencyFromJSON>)

@JsonClass(generateAdapter = true)
data class QuoteFromJSON(val QuoteId: Int, val MinPrice: Int, val Direct: Boolean,
                         val OutboundLeg: BoundLegFromJSON, val InboundLeg: BoundLegFromJSON?,
                         val QuoteDateTime: String)

@JsonClass(generateAdapter = true)
data class BoundLegFromJSON(val CarrierIds: List<Int>, val OriginId: Int, val DestinationId: Int,
                            val DepartureDate: String)

@JsonClass(generateAdapter = true)
data class TicketPlaceFromJSON(val PlaceId: Int, val IataCode: String?, val Name: String,
                               val Type: String, val SkyscannerCode: String, val CityName: String?,
                               val CityId: String?, val CountryName: String?)

@JsonClass(generateAdapter = true)
data class CarrierFromJSON(val CarrierId: Int, val Name: String)

/**
 * Convert Network results to database objects
 */
fun TicketsResponse.asDatabaseModel(): List<TicketDBModel> {
    val tickets = mutableListOf<TicketDBModel>()

    Quotes.forEach {
        tickets.add(TicketDBModel(
            id = it.QuoteId,
            departureDate = getFormattedUTCDate(it.OutboundLeg.DepartureDate),
            departureTime = getFormattedUTCTime(it.QuoteDateTime),
            departureFrom = placeIdToPlaceName(Places, it.OutboundLeg.OriginId),
            departureTo = placeIdToPlaceName(Places, it.OutboundLeg.DestinationId),
            carrierName = carrierIdToCarrierName(Carriers, it.OutboundLeg.CarrierIds[0]),
            flightPrice = it.MinPrice.toString(),
            flightPriceCurrency = Currencies[0].Code
        ))
    }

    return tickets
}

fun placeIdToPlaceName(places: List<TicketPlaceFromJSON>, placeId: Int): String {
    var result = ""

    places.forEach {
        if (it.PlaceId == placeId) {
            result = it.Name
            return@forEach
        }
    }

    return result
}

fun carrierIdToCarrierName(carriers: List<CarrierFromJSON>, carrierId: Int): String {
    var result = ""

    carriers.forEach {
        if (it.CarrierId == carrierId) {
            result = it.Name
            return@forEach
        }
    }

    return result
}