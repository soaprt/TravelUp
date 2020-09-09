package com.sostrovsky.travelup.repository.place

import com.sostrovsky.travelup.domain.place.PlaceDomainModel
import com.sostrovsky.travelup.domain.place.PlaceSearchParamsDomainModel
import com.sostrovsky.travelup.repository.DataFetcher
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 24.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object PlaceFetcherOffline: DataFetcher<PlaceSearchParamsDomainModel, List<PlaceDomainModel>> {
    override suspend fun fetch(param: PlaceSearchParamsDomainModel): List<PlaceDomainModel> {
        Timber.e("PlaceFetcherOffline: fetch()")
        return emptyList()
    }
}