package com.sostrovsky.travelup.database.entities.preferences

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.preferences.UserCountryDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "user_country")
class UserCountryDBModel constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0L,

    @ColumnInfo(name = "country_code")
    val countryCode: String,

    @ColumnInfo(name = "country_name")
    val countryName: String
)

/**
 * Map UserCountryDBModel to domain entity UserCountryDomainModel
 */
fun List<UserCountryDBModel>.asDomainModel(): List<UserCountryDomainModel> {
    return map {
        UserCountryDomainModel(
            countryCode = it.countryCode,
            countryName = it.countryName
        )
    }
}