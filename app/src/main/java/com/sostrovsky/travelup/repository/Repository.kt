package com.sostrovsky.travelup.repository

import com.sostrovsky.travelup.repository.settings.SettingsContract
import com.sostrovsky.travelup.repository.settings.SettingsRepository
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
    suspend fun init(): Boolean {
        var isInitComplete = false

        withContext(Dispatchers.IO) {
//            getPlaces().init()
            getSettingsRepo().init()
//            getTickets().init()

            isInitComplete = true
        }
        return isInitComplete
    }

//    fun getPlaces(): PlaceContract {
//        return PlaceRepository
//    }

    fun getSettingsRepo(): SettingsContract {
        return SettingsRepository
    }

    fun getTickets(): TicketContract {
        return TicketRepository
    }
}