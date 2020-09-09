package com.sostrovsky.travelup.database.entities.preferences

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.preferences.UserSettingsDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "user_settings")
class UserSettingsDBModel constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo(name = "locale_code")
    val localeCode: String,

    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "country_code")
    val countryCode: String
)

/**
 * Map UserSettingsDBModel to domain entity UserSettingsDomainModel
 */
fun UserSettingsDBModel.asDomainModel(): UserSettingsDomainModel {
    return UserSettingsDomainModel(
        isSet = true,
        localeCode = this.localeCode,
        currencyCode = this.currencyCode,
        countryCode = this.countryCode
    )
}