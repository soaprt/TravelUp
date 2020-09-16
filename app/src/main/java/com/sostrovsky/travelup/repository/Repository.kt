package com.sostrovsky.travelup.repository

import com.sostrovsky.travelup.repository.settings.SettingsContract
import com.sostrovsky.travelup.repository.settings.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 19.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object Repository {
    suspend fun init(): Boolean {
        var isInitComplete = false

        withContext(Dispatchers.IO) {
            getSettingsRepo().init()
            isInitComplete = true
        }
        return isInitComplete
    }

    private fun getSettingsRepo(): SettingsContract {
        return SettingsRepository
    }
}