package com.sostrovsky.travelup.repository.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.sostrovsky.travelup.TravelUpApp
import com.sostrovsky.travelup.database.TravelUpDatabase
import com.sostrovsky.travelup.database.entities.trash.place.asDomainModel
import com.sostrovsky.travelup.domain.place.PlaceDomainModel
import com.sostrovsky.travelup.domain.place.PlaceSearchParamsDomainModel
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 24.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object PlaceRepository : PlaceContract {
    lateinit var database: TravelUpDatabase

    override suspend fun init() {
        this.database = TravelUpDatabase.getInstance(TravelUpApp.applicationContext())
    }

    override suspend fun getPlaces(param: PlaceSearchParamsDomainModel): List<PlaceDomainModel> {
        Timber.e("PlacesRepository: getPlaces(): params: $param")
        return PlaceDataFactory.fetch(param)
    }

//    override fun getPlacesLiveData(): LiveData<List<PlaceDomainModel>> {
//        return Transformations.map(database.placeDBDao.getPlaces()) {
//            it.asDomainModel()
//        }
//    }
}