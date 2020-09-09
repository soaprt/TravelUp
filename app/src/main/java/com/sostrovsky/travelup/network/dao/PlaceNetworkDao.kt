package com.sostrovsky.travelup.network.dao

import com.sostrovsky.travelup.network.dto.place.PlaceDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface PlaceNetworkDao {
    @GET("autosuggest/{version}/{country}/{currency}/{locale}")
    fun getPlaces(
        @Path("version") version: String,
        @Path("country") country: String,
        @Path("currency") currency: String,
        @Path("locale") locale: String,
        @Query("query") query: String,
        @Query("apikey") apiKey: String
    ): Deferred<PlaceDTO>
}