package com.sostrovsky.travelup.repository

import com.sostrovsky.travelup.repository.place.PlaceContract
import com.sostrovsky.travelup.repository.place.PlaceRepository
import com.sostrovsky.travelup.repository.preferences.PreferencesContract
import com.sostrovsky.travelup.repository.preferences.PreferencesRepository
import com.sostrovsky.travelup.repository.ticket.TicketContract
import com.sostrovsky.travelup.repository.ticket.TicketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 19.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object Repository {
    var isInitComplete = false;

    suspend fun init(): Boolean {
        withContext(Dispatchers.IO) {
            getPlaces().init()
            getPreferences().init()
            getTickets().init()

            isInitComplete = true
        }
        return isInitComplete
    }

    fun getPlaces(): PlaceContract {
        return PlaceRepository
    }

    fun getPreferences(): PreferencesContract {
        return PreferencesRepository
    }

    fun getTickets(): TicketContract {
        return TicketRepository
    }
}