package com.sostrovsky.travelup.database.entities.trash.place

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.place.PlaceDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 28.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
//@Entity(tableName = "place")
class PlaceDBModel constructor(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "place_code")
    val placeCode: String,

    @ColumnInfo(name = "place_name")
    val placeName: String
)

/**
 * Map PlaceDBModel to domain entity PlaceDomainModel
 */
fun List<PlaceDBModel>.asDomainModel(): List<PlaceDomainModel> {
    return map {
        PlaceDomainModel(
            placeCode = it.placeCode,
            placeName = it.placeName
        )
    }
}