package com.sostrovsky.travelup.repository.settings.language

import com.sostrovsky.travelup.database.entities.settings.Language
import com.sostrovsky.travelup.util.network.NetworkHelper
import com.sostrovsky.travelup.repository.settings.RowsGenerator
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object LanguageFactory {
    suspend fun generate(): List<Language> {
        Timber.e("LanguageFactory: generate(): no data in db")
        val languageGenerator: RowsGenerator<List<Language>> =
            if (NetworkHelper.isAvailable) {
                LanguageGeneratorOnline
            } else {
                LanguageGeneratorOffline
            }
        return (languageGenerator.execute()).sortedBy { it.code }
    }
}
