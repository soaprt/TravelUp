package com.sostrovsky.travelup.database.entities.ticket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Author: Sergey Ostrovsky
 * Date: 15.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "market_place")
data class MarketPlace constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo
    val code: String,

    @ColumnInfo
    val name: String
) {
    override fun toString(): String {
        return "MarketPlace(id=$id, code='$code', name='$name')"
    }
}