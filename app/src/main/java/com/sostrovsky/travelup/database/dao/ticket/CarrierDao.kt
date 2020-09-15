package com.sostrovsky.travelup.database.dao.ticket

import androidx.room.Dao
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.ticket.Carrier


/**
 * Author: Sergey Ostrovsky
 * Date: 15.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface CarrierDao {
    @Query("SELECT * FROM carrier WHERE id=:id")
    fun getById(id: Long): Carrier

    @Query("SELECT name FROM carrier WHERE id=:id")
    fun getNameById(id: Long): String
}