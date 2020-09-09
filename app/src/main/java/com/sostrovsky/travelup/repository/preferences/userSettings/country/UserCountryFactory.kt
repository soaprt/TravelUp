package com.sostrovsky.travelup.repository.preferences.userSettings.country

import com.sostrovsky.travelup.database.entities.preferences.UserCountryDBModel
import com.sostrovsky.travelup.util.network.NetworkHelper
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserCountryFactory {
    suspend fun generate(): List<UserCountryDBModel> {
        Timber.e("UserCountryFactory: generate(): no data in db")
        val userCountryGenerator: UserDataGenerator<List<UserCountryDBModel>> =
            if (NetworkHelper.isAvailable) {
                UserCountryGeneratorOnline
            } else {
                UserCountryGeneratorOffline
            }
        return (userCountryGenerator.execute()).sortedBy { it.countryCode }
    }
}