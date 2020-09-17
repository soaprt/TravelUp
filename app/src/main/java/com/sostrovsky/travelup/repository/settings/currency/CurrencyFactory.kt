package com.sostrovsky.travelup.repository.settings.currency

import com.sostrovsky.travelup.database.entities.settings.Currency
import com.sostrovsky.travelup.repository.settings.DataGenerator
import com.sostrovsky.travelup.util.network.NetworkHelper

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CurrencyFactory {
    suspend fun generate(): List<Currency> {
        val dataGenerator: DataGenerator<List<Currency>> =
            if (NetworkHelper.isAvailable) {
                CurrencyGeneratorOnline
            } else {
                CurrencyGeneratorOffline
            }
        return (dataGenerator.generate()).sortedBy { it.code }
    }
}