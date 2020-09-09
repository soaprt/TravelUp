package com.sostrovsky.travelup.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.network.dao.PlaceNetworkDao
import com.sostrovsky.travelup.network.dao.PreferencesNetworkDao
import com.sostrovsky.travelup.network.dao.TicketNetworkDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 *
 * Main entry point for network access.
 */

object Network {
    private const val READ_TIMEOUT: Long = 20
    private const val WRITE_TIMEOUT: Long = 20

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()

    val placeNetworkDao: PlaceNetworkDao = retrofit.create(PlaceNetworkDao::class.java)
    val preferencesNetworkDao: PreferencesNetworkDao =
        retrofit.create(PreferencesNetworkDao::class.java)
    val ticketNetworkDao: TicketNetworkDao = retrofit.create(TicketNetworkDao::class.java)
}