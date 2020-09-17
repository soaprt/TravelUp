package com.sostrovsky.travelup.database.entities.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.settings.CountryDomain

/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity
data class Country(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo
    val code: String,

    @ColumnInfo
    val name: String
)

fun List<Country>.asDomain(): List<CountryDomain> {
    return map {
        CountryDomain(
            code = it.code,
            name = it.name
        )
    }
}