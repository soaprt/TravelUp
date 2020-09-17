package com.sostrovsky.travelup.repository.settings

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase

/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
abstract class SettingsData<T, V> {
    val database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

    abstract suspend fun populate()
    abstract suspend fun fetchSpinnerData(id: Int): Pair<T, V>
}