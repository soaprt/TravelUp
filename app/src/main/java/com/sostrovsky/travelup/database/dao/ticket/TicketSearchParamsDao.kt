package com.sostrovsky.travelup.database.dao.ticket

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.ticket.TicketSearchParams

/**
 * Author: Sergey Ostrovsky
 * Date: 15.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface TicketSearchParamsDao {
    @Query("SELECT * FROM ticket_search_params WHERE id=:id")
    fun getById(id: Int): TicketSearchParams

    @Query("SELECT Id FROM ticket_search_params WHERE market_place_id_from=:marketPlaceIdFrom AND market_place_id_to=:marketPlaceIdTo AND departure_date=:departureDate")
    fun getId(
        marketPlaceIdFrom: Int, marketPlaceIdTo: Int,
        departureDate: String
    ): Int

    @Insert
    fun insert(searchParams: TicketSearchParams): Long
}