package com.sostrovsky.travelup.repository.place

import com.sostrovsky.travelup.domain.place.PlaceDomainModel
import com.sostrovsky.travelup.domain.place.PlaceSearchParamsDomainModel
import com.sostrovsky.travelup.util.network.NetworkHelper
import com.sostrovsky.travelup.repository.DataFetcher
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 24.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
object PlaceDataFactory {
    suspend fun fetch(param: PlaceSearchParamsDomainModel): List<PlaceDomainModel> {
        Timber.e("PlaceDataFactory: fetch()")
        val placeFetcher: DataFetcher<PlaceSearchParamsDomainModel, List<PlaceDomainModel>> =
            if (NetworkHelper.isAvailable) {
                PlaceFetcherOnline
            } else {
                PlaceFetcherOffline
            }
        return (placeFetcher.fetch(param))
    }
}