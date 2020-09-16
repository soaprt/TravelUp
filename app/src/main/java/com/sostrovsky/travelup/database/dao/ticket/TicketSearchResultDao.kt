package com.sostrovsky.travelup.database.dao.ticket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.ticket.TicketSearchResult

/**
 * Author: Sergey Ostrovsky
 * Date: 15.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface TicketSearchResultDao {
    @Query("SELECT * FROM ticket_search_result WHERE settings_id=:settingsId AND ticket_search_params_id=:ticketSearchParamsId AND timestamp<=:timestampValid")
    fun getTickets(settingsId: Int, ticketSearchParamsId: Int, timestampValid: Long):
            List<TicketSearchResult>

    @Insert
    fun insertAll(result: List<TicketSearchResult>): List<Long>

    @Delete
    fun delete(result: TicketSearchResult)
}