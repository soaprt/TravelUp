package com.sostrovsky.travelup.repository.preferences.userSettings

import com.sostrovsky.travelup.database.entities.preferences.UserSettingsDBModel
import com.sostrovsky.travelup.repository.preferences.*
import com.sostrovsky.travelup.util.getCountryCodeFromLocale
import com.sostrovsky.travelup.util.getCurrencyCodeFromLocale
import com.sostrovsky.travelup.util.getFormattedLocale
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object UserSettingsGenerator : UserDataGenerator<UserSettingsDBModel>() {
    override suspend fun execute(): UserSettingsDBModel {
        Timber.e("UserSettingsGenerator(): no data in db")
        return UserSettingsDBModel(
            id = generateRowId(),
            localeCode = getFormattedLocale(),
            currencyCode = getCurrencyCodeFromLocale(),
            countryCode = getCountryCodeFromLocale()
        )
    }
}