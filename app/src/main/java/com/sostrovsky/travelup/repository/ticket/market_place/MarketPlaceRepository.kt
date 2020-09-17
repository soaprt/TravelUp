package com.sostrovsky.travelup.repository.ticket.market_place

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.ticket.MarketPlace
import com.sostrovsky.travelup.domain.ticket.TicketSearchParams
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.network.dto.place.asDatabaseModel
import com.sostrovsky.travelup.util.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object MarketPlaceRepository {
    val database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())

    private suspend fun fetchFromWebService(params: TicketSearchParams, query: String):
            List<MarketPlace> {
        val result = mutableListOf<MarketPlace>()

        withContext(Dispatchers.IO) {
            val response = safeApiCall(
                call = {
                    WebService.placeService.fetchPlacesAsync(
                        BuildConfig.API_VERSION,
                        params.countryCode,
                        params.currencyCode,
                        params.localeCode,
                        query,
                        BuildConfig.API_KEY
                    ).await()
                },
                error = "Error fetching place"
            )

            if (response?.Places?.isNotEmpty() == true) {
                result.addAll(response.asDatabaseModel())
            }
        }

        return result
    }

    suspend fun fetchCodeFromWebService(params: TicketSearchParams, query: String): String {
        var result = ""

        val marketPlaces = fetchFromWebService(params, query)

        if (marketPlaces.isNotEmpty()) {
            marketPlaces.forEach {
                addMarketPlaceToDB(it.code, it.name)
            }

            result = marketPlaces[0].code
        }

        return result
    }

    suspend fun getIdByName(name: String): Int {
        val result = mutableListOf(0)

        withContext(Dispatchers.IO) {
            result[0] = database.marketPlaceDao.getIdByName(normalizedValue(name))
        }

        return result[0]
    }

    suspend fun hasNoItems(): Boolean {
        var result = true

        withContext(Dispatchers.IO) {
            result = database.marketPlaceDao.checkIfEmpty() == 0
        }

        return result
    }

    private suspend fun isInDB(name: String): Boolean {
        return getIdByName(name) != 0
    }

    private suspend fun addMarketPlaceToDB(code: String, name: String): Int {
        var result = 0

        withContext(Dispatchers.IO) {
            val normalizedName = normalizedValue(name)
            if (!isInDB(normalizedName)) {
                result = database.marketPlaceDao.insert(generateModel(code, normalizedName)).toInt()
            }
        }

        return result
    }

    private fun normalizedValue(name: String): String {
        return name.replace("ё", "е")
    }

    private fun generateModel(code: String, name: String): MarketPlace {
        return MarketPlace(
            id = 0,
            code = code,
            name = name
        )
    }
}