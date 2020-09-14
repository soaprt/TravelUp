package com.sostrovsky.travelup.domain.preferences

import com.sostrovsky.travelup.database.entities.trash.UserSettingsDBModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class UserSettingsDomainModel(
    var id: Long = 1, var isSet: Boolean = false, var localeCode: String = "",
    var currencyCode: String = "", var countryCode: String = ""
)

fun UserSettingsDomainModel.asDBModel(): UserSettingsDBModel {
    return UserSettingsDBModel(
        id = this.id,
        localeCode = this.localeCode,
        currencyCode = this.currencyCode,
        countryCode = this.countryCode
    )
}
