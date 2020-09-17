package com.sostrovsky.travelup.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sostrovsky.travelup.BuildConfig
import com.sostrovsky.travelup.network.dao.PlaceService
import com.sostrovsky.travelup.network.dao.SettingsService
import com.sostrovsky.travelup.network.dao.TicketService
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

object WebService {
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

    val placeService: PlaceService = retrofit.create(PlaceService::class.java)
    val settingsService: SettingsService = retrofit.create(SettingsService::class.java)
    val ticketService: TicketService = retrofit.create(TicketService::class.java)
}