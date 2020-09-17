package com.sostrovsky.travelup.database.entities.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.settings.CurrencyDomain


/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity
data class Currency(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo
    val code: String,

    @ColumnInfo
    val name: String
)

fun List<Currency>.asDomain(): List<CurrencyDomain> {
    return map {
        CurrencyDomain(
            code = it.code,
            name = it.name
        )
    }
}