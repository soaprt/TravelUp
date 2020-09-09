package com.sostrovsky.travelup.repository.preferences.userSettings.language

import com.sostrovsky.travelup.database.entities.preferences.UserLanguageDBModel
import com.sostrovsky.travelup.util.network.NetworkHelper
import com.sostrovsky.travelup.repository.preferences.UserDataGenerator
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserLanguageFactory {
    suspend fun generate(): List<UserLanguageDBModel> {
        Timber.e("UserLanguageFactory: generate(): no data in db")
        val userLangGenerator: UserDataGenerator<List<UserLanguageDBModel>> =
            if (NetworkHelper.isAvailable) {
                UserLangGeneratorOnline
            } else {
                UserLangGeneratorOffline
            }
        return (userLangGenerator.execute()).sortedBy { it.localeCode }
    }
}
