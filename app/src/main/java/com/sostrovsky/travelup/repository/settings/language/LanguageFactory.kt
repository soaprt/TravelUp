package com.sostrovsky.travelup.repository.settings.language

import com.sostrovsky.travelup.database.entities.settings.Language
import com.sostrovsky.travelup.repository.settings.DataGenerator
import com.sostrovsky.travelup.util.network.NetworkHelper

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object LanguageFactory {
    suspend fun generate(): List<Language> {
        val dataGenerator: DataGenerator<List<Language>> =
            if (NetworkHelper.isAvailable) {
                LanguageGeneratorOnline
            } else {
                LanguageGeneratorOffline
            }
        return (dataGenerator.generate()).sortedBy { it.code }
    }
}
