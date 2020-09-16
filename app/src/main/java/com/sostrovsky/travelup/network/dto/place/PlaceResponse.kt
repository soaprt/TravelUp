package com.sostrovsky.travelup.network.dto.place

import com.sostrovsky.travelup.database.entities.ticket.MarketPlace
import com.sostrovsky.travelup.util.placeCodeFromPlaceId
import com.squareup.moshi.JsonClass

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@JsonClass(generateAdapter = true)
data class PlaceDTO(val Places: List<PlaceFromJSON>)

@JsonClass(generateAdapter = true)
data class PlaceFromJSON(val PlaceId: String, val PlaceName: String, val CountryId: String,
                         val RegionId: String, val CityId: String, val CountryName: String)

/**
 * Convert PlaceResponse to database objects PlaceDBModel
 */
fun PlaceDTO.asDatabaseModel(): List<MarketPlace> {
    return Places.map {
        MarketPlace(
            id = 0,
            code = placeCodeFromPlaceId(it.PlaceId),
            name = it.PlaceName
        )
    }
}