package com.sostrovsky.travelup.database.entities.ticket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Sergey Ostrovsky
 * Date: 15.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity
class Carrier constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0L,

    @ColumnInfo
    val code: String,

    @ColumnInfo
    val name: String
)