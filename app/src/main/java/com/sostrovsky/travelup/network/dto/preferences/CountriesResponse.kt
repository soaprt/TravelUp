package com.sostrovsky.travelup.network.dto.preferences

import com.squareup.moshi.JsonClass

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@JsonClass(generateAdapter = true)
data class CountriesResponse(val Countries: List<CountryFromJSON>)

@JsonClass(generateAdapter = true)
data class CountryFromJSON(val Code: String, val Name: String)