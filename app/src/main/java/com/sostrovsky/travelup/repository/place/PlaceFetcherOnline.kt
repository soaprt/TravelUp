package com.sostrovsky.travelup.repository.place

import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.database.entities.trash.place.PlaceDBModel
import com.sostrovsky.travelup.domain.place.PlaceDomainModel
import com.sostrovsky.travelup.domain.place.PlaceSearchParamsDomainModel
import com.sostrovsky.travelup.network.WebService
import com.sostrovsky.travelup.repository.DataFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 24.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object PlaceFetcherOnline : DataFetcher<PlaceSearchParamsDomainModel, List<PlaceDomainModel>> {
    override suspend fun fetch(param: PlaceSearchParamsDomainModel): List<PlaceDomainModel> {
        Timber.e("PlaceFetcherOnline: fetch()")

        return fetchPlaces(param).map {
            PlaceDomainModel(
                placeCode = it.placeCode,
                placeName = it.placeName
            )
        }
    }

    private suspend fun fetchPlaces(param: PlaceSearchParamsDomainModel): List<PlaceDBModel> {
        val places = mutableListOf<PlaceDBModel>()

        withContext(Dispatchers.IO) {
            val response = WebService.placeService.fetchPlacesAsync(
                BuildConfig.API_VERSION,
                param.userSettings.countryCode,
                param.userSettings.currencyCode,
                param.userSettings.localeCode,
                param.query,
                BuildConfig.API_KEY).await()

//            response.body()?.asDatabaseModel()?.let {
//                TravelUpDatabase.getInstance(TravelUpApp.applicationContext())
//                    .placeDBDao.insertPlaces(it)
//            }
        }

        return places
    }
}