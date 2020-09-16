package com.sostrovsky.travelup.database.entities.ticket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Sergey Ostrovsky
 * Date: 15.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "ticket_search_params")
class TicketSearchParams (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "market_place_id_from")
    val marketPlaceIdFrom: Int,

    @ColumnInfo(name = "market_place_id_to")
    val marketPlaceIdTo: Int,

    @ColumnInfo(name = "departure_date")
    val departureDate: String
)