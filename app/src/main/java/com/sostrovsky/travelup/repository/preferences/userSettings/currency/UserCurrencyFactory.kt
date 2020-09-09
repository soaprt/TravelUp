package com.sostrovsky.travelup.repository.preferences.userSettings.currency

import com.sostrovsky.travelup.database.entities.preferences.UserCurrencyDBModel
import com.sostrovsky.travelup.util.network.NetworkHelper
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserCurrencyFactory {
    suspend fun generate(): List<UserCurrencyDBModel> {
        Timber.e("UserCurrencyFactory: generate(): no data in db")
        val userCurrencyGenerator: UserDataGenerator<List<UserCurrencyDBModel>> =
            if (NetworkHelper.isAvailable) {
                UserCurrencyGeneratorOnline
            } else {
                UserCurrencyGeneratorOffline
            }
        return (userCurrencyGenerator.execute()).sortedBy { it.currencyCode }
    }
}