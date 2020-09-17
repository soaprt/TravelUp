package com.sostrovsky.travelup.network.dto.settings

import com.squareup.moshi.JsonClass

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@JsonClass(generateAdapter = true)
data class LocalesResponse(val Locales: List<LocaleFromJSON>)

@JsonClass(generateAdapter = true)
data class LocaleFromJSON(val Code: String, val Name: String)