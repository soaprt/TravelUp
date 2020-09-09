package com.sostrovsky.travelup.util

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
fun placeCodeFromPlaceId(placeId: String): String {
    var result = placeId

    val separator = "-"
    if (placeId.isNotEmpty() && placeId.contains(separator)) {
        result = placeId.split(separator)[0]
    }

    return result
}