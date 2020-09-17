package com.sostrovsky.travelup.domain.ticket

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class TicketSearchParamsDomain(
    val placeFrom: String, val placeTo: String,
    val departureDate: String, val countryCode: String,
    val currencyCode: String, val localeCode: String
)