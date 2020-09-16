package com.sostrovsky.travelup.domain.ticket

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class TicketDomainModel(
    val departureDate: String, val departureTime: String,
    private val departureFrom: String, private val departureTo: String,
    val carrierName: String, val _flightPriceAsLong: Long,
    private val flightPriceCurrency: String
) {

    val flightPrice = "$_flightPriceAsLong $flightPriceCurrency"

    val departureFromTo = "$departureFrom - $departureTo"

    override fun toString(): String {
        return "TicketDomainModel(departureDate='$departureDate', departureTime='$departureTime', " +
                "departureFrom='$departureFrom', departureTo='$departureTo', " +
                "carrierName='$carrierName', _flightPrice=$_flightPriceAsLong, " +
                "flightPriceCurrency='$flightPriceCurrency', flightPrice='$flightPrice', " +
                "departureFromTo='$departureFromTo')"
    }
}