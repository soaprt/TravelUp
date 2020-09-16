package com.sostrovsky.travelup.repository.settings.country

import com.sostrovsky.travelup.database.entities.settings.Country
import com.sostrovsky.travelup.repository.settings.DataGenerator
import com.sostrovsky.travelup.util.network.NetworkHelper
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CountryFactory {
    suspend fun generate(): List<Country> {
        Timber.e("CountryFactory: generate(): no data in db")
        val dataGenerator: DataGenerator<List<Country>> =
            if (NetworkHelper.isAvailable) {
                CountryGeneratorOnline
            } else {
                CountryGeneratorOffline
            }
        return (dataGenerator.generate()).sortedBy { it.code }
    }
}