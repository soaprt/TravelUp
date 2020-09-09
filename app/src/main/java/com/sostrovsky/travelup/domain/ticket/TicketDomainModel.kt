package com.sostrovsky.travelup.domain.ticket

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class TicketDomainModel(val id: Int, val departureDate: String, val departureTime: String,
                             val departureFrom: String, val departureTo: String,
                             val carrierName: String, val flightPrice: String,
                             val flightPriceCurrency: String)