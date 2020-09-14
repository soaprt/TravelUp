package com.sostrovsky.travelup.repository.place

import com.sostrovsky.travelup.domain.place.PlaceDomainModel
import com.sostrovsky.travelup.domain.place.PlaceSearchParamsDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 24.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface PlaceContract {
    suspend fun init()
    suspend fun getPlaces(param: PlaceSearchParamsDomainModel): List<PlaceDomainModel>
}