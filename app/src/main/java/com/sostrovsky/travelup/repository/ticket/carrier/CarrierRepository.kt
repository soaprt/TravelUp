package com.sostrovsky.travelup.repository.ticket.carrier

import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.ticket.Carrier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object CarrierRepository {
    val database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

    suspend fun getNameById(id: Int): String {
        var result = ""

        withContext(Dispatchers.IO) {
            result = database.carrierDao.getNameById(id)
        }

        return result
    }

    suspend fun getIdByName(name: String): Int {
        val result = mutableListOf(0)

        withContext(Dispatchers.IO) {
            result[0] = database.carrierDao.getIdByName(name)
        }

        return result[0]
    }

    private suspend fun isInDB(name: String): Boolean {
        return getIdByName(name) != 0
    }

    suspend fun addCarrierToDB(code: Int, name: String): Int {
        var result = 0

        withContext(Dispatchers.IO) {
            if (!isInDB(name)) {
                result = database.carrierDao.insert(generateModel(code, name)).toInt()
            }
        }

        return result
    }

    private fun generateModel(code: Int, name: String): Carrier {
        return Carrier(
            id = 0,
            code = code,
            name = name
        )
    }
}