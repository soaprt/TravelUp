package com.sostrovsky.travelup.repository.settings.language

import com.sostrovsky.travelup.database.entities.settings.asDomain
import com.sostrovsky.travelup.domain.settings.LanguageDomain
import com.sostrovsky.travelup.repository.settings.SettingsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object LanguageRepository : SettingsData<List<LanguageDomain>, MutableList<Int>>() {
    override suspend fun populate() {
        if (hasLanguages()) database.languageDao.clear()

        val languages = LanguageFactory.generate()
        if (languages.isNotEmpty()) {
            database.languageDao.insertAll(languages)
        }
    }

    private fun hasLanguages() = (database.languageDao.getRowsCount() > 0L)

    override suspend fun fetchSpinnerData(id: Int): Pair<List<LanguageDomain>, MutableList<Int>> {
        val result = Pair(mutableListOf<LanguageDomain>(), mutableListOf<Int>())

        withContext(Dispatchers.IO) {
            result.first.addAll(database.languageDao.getAll().asDomain())
            result.second.add(fetchSpinnerLangPosition(result.first, id))
        }

        return result
    }

    private suspend fun fetchSpinnerLangPosition(list: List<LanguageDomain>, languageId: Int):
            Int {
        return list.indexOfFirst { it.code == fetchSettingsLanguageCode(languageId) }
    }

    private suspend fun fetchSettingsLanguageCode(languageId: Int): String? {
        var result: String? = null

        withContext(Dispatchers.IO) {
            result = database.languageDao.getCodeById(languageId)
        }

        return result
    }
}