package com.sostrovsky.travelup.domain.place

import com.sostrovsky.travelup.domain.preferences.UserSettingsDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class PlaceSearchParamsDomainModel(var query: String = "any",
    var userSettings: UserSettingsDomainModel = UserSettingsDomainModel()) {
    override fun toString(): String {
        return "PlaceSearchParams(query='$query', userSettings=$userSettings)"
    }
}