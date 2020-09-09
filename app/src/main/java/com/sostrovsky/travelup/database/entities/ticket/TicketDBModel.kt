package com.sostrovsky.travelup.database.entities.ticket

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
    val id: Int
)

/**
 * Map TicketDBModel to domain entity TicketDomainModel
 */
fun List<TicketDBModel>.asDomainModel(): List<TicketDomainModel> {
    return map {
        TicketDomainModel(
            id = it.id
        )
    }
}