package com.sostrovsky.travelup.database.entities.ticket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.ticket.TicketDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 28.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "ticket")
class TicketDBModel constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "flight_date")
    val departureDate: String,

    @ColumnInfo(name = "departure_time")
    val departureTime: String,

    @ColumnInfo(name = "destination_from")
    val departureFrom: String,

    @ColumnInfo(name = "flying_to")
    val departureTo: String,

    @ColumnInfo(name = "carrier_name")
    val carrierName: String,

    @ColumnInfo(name = "flight_price")
    val flightPrice: String,

    @ColumnInfo(name = "flight_price_currency")
    val flightPriceCurrency: String
)

/**
 * Map TicketDBModel to domain entity TicketDomainModel
 */
fun List<TicketDBModel>.asDomainModel(): List<TicketDomainModel> {
    return map {
        TicketDomainModel(
            id = it.id,
            departureDate = it.departureDate,
            departureTime = it.departureTime,
            departureFrom = it.departureFrom,
            departureTo = it.departureTo,
            carrierName = it.carrierName,
            flightPrice = it.flightPrice,
            flightPriceCurrency = it.flightPriceCurrency
        )
    }
}