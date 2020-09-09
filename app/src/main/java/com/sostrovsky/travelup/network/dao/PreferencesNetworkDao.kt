package com.sostrovsky.travelup.network.dao

import com.sostrovsky.travelup.network.dto.preferences.CountriesResponse
import com.sostrovsky.travelup.network.dto.preferences.CurrenciesResponse
import com.sostrovsky.travelup.network.dto.preferences.LocalesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface PreferencesNetworkDao {

    @GET("reference/{version}/countries/{locale}")
    fun getCountries(@Path("version") version: String, @Path("locale") locale: String,
                     @Query("apikey") apiKey: String): Deferred<CountriesResponse>

    @GET("reference/{version}/currencies")
    fun getCurrencies(@Path("version") version: String, @Query("apikey") apiKey: String):
            Deferred<CurrenciesResponse>

    @GET("reference/{version}/locales")
    fun getLocales(@Path("version") version: String, @Query("apikey") apiKey: String):
            Deferred<LocalesResponse>
}