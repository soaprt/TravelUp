package com.sostrovsky.travelup.database.entities.ticket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Sergey Ostrovsky
 * Date: 15.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "ticket_search_result")
data class TicketSearchResult (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "settings_id")
    val settingsId: Int,

    @ColumnInfo(name = "ticket_search_params_id")
    val ticketSearchParamsId: Int,

    @ColumnInfo(name = "carrier_id")
    val carrierId: Int,

    @ColumnInfo(name = "departure_time")
    val departureTime: String,

    @ColumnInfo(name = "flight_price")
    val flightPrice: Long,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)