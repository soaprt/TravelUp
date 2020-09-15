package com.sostrovsky.travelup.domain.ticket

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class TicketSearchParams(
    val placeFrom: String, val placeTo: String,
    val departureDate: String, val countryCode: String,
    val currencyCode: String, val localeCode: String
) {

    override fun toString(): String {
        return "TicketSearchParams(placeFrom='$placeFrom', placeTo='$placeTo', " +
                "departureDate='$departureDate', countryCode='$countryCode', " +
                "currencyCode='$currencyCode', localeCode='$localeCode')"
    }
}