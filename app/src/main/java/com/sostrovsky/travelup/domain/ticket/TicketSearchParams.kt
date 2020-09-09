package com.sostrovsky.travelup.domain.ticket

import com.sostrovsky.travelup.domain.preferences.UserSettingsDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class TicketSearchParams(var destinationFrom: String = "", var flyingTo: String = "",
                              var departureDate: String = "",
                              var userSettings: UserSettingsDomainModel = UserSettingsDomainModel()) {
    override fun toString(): String {
        return "TicketSearchParams(destinationFrom='$destinationFrom', flyingTo='$flyingTo', " +
                "departureDate='$departureDate', userSettings=$userSettings)"
    }
}