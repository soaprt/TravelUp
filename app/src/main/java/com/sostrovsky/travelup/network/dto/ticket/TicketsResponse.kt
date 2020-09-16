package com.sostrovsky.travelup.network.dto.ticket

import com.sostrovsky.travelup.domain.ticket.TicketDomainModel
import com.sostrovsky.travelup.network.dto.settings.CurrencyFromJSON
import com.sostrovsky.travelup.repository.ticket.carrier.CarrierRepository
import com.sostrovsky.travelup.util.isoTimeToLocalTime
import com.sostrovsky.travelup.util.isoDateToLocalDate
import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@JsonClass(generateAdapter = true)
data class TicketsResponse(
    val Quotes: List<QuoteFromJSON>, val Places: List<MarketPlaceFromJSON>,
    val Carriers: List<CarrierFromJSON>, val Currencies: List<CurrencyFromJSON>
)

@JsonClass(generateAdapter = true)
data class QuoteFromJSON(
    val QuoteId: Int, val MinPrice: Int, val Direct: Boolean,
    val OutboundLeg: BoundLegFromJSON, val InboundLeg: BoundLegFromJSON?,
    val QuoteDateTime: String
)

@JsonClass(generateAdapter = true)
data class BoundLegFromJSON(
    val CarrierIds: List<Int>, val OriginId: Int, val DestinationId: Int,
    val DepartureDate: String
)

@JsonClass(generateAdapter = true)
data class MarketPlaceFromJSON(
    val PlaceId: Int, val IataCode: String?, val Name: String,
    val Type: String, val SkyscannerCode: String, val CityName: String?,
    val CityId: String?, val CountryName: String?
)

@JsonClass(generateAdapter = true)
data class CarrierFromJSON(val CarrierId: Int, val Name: String)

/**
 * Convert Network results to domain objects
 */
suspend fun TicketsResponse.asDomainModel(): List<TicketDomainModel> {
    val tickets = mutableListOf<TicketDomainModel>()

    Quotes.forEach {
        Carriers.forEach {
            val insertedRowId = CarrierRepository.addCarrierToDB(it.CarrierId, it.Name)
            Timber.e(
                "TicketsResponse: TicketsResponse.asDomainModel():" +
                        "\ninsertedRowId: $insertedRowId"
            )
        }

        tickets.add(
            TicketDomainModel(
                departureDate = isoDateToLocalDate(it.OutboundLeg.DepartureDate),
                departureTime = isoTimeToLocalTime(it.QuoteDateTime),
                departureFrom = placeIdToPlaceName(Places, it.OutboundLeg.OriginId),
                departureTo = placeIdToPlaceName(Places, it.OutboundLeg.DestinationId),
                carrierName = carrierIdToCarrierName(Carriers, it.OutboundLeg.CarrierIds[0]),
                _flightPriceAsLong = it.MinPrice.toLong(),
                flightPriceCurrency = Currencies[0].Code
            )
        )
    }

    return tickets
}

fun placeIdToPlaceName(places: List<MarketPlaceFromJSON>, placeId: Int): String {
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