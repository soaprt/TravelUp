package com.sostrovsky.travelup.repository.settings

import com.sostrovsky.travelup.database.entities.settings.Settings
import com.sostrovsky.travelup.repository.settings.*
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object SettingsGenerator : RowsGenerator<Settings>() {
    override suspend fun execute(): Settings {
        Timber.e("SettingsGenerator(): no data in db")
        return Settings(
            id = generateRowId(),
            languageId = 1,
            countryId = 2,
            currencyId = 3
//            localeCode = getFormattedLocale(),
//            currencyCode = getCurrencyCodeFromLocale(),
//            countryCode = getCountryCodeFromLocale()
        )
    }
}