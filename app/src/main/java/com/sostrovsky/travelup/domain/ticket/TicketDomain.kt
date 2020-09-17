package com.sostrovsky.travelup.domain.ticket

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class TicketDomain(
    val departureDate: String, val departureTime: String,
    private val departureFrom: String, private val departureTo: String,
    val carrierName: String, val _flightPriceAsLong: Long,
    private val flightPriceCurrency: String
) {
    val flightPrice = "$_flightPriceAsLong $flightPriceCurrency"
    val departureFromTo = "$departureFrom - $departureTo"
}