package com.sostrovsky.travelup.database.dao.place

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.trash.place.PlaceDBModel

/**
 * Author: Sergey Ostrovsky
 * Date: 28.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
//@Dao
//interface PlaceDBDao {
//    @Query("select * from place")
//    fun getPlaces(): LiveData<List<PlaceDBModel>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertPlaces(places: List<PlaceDBModel>)
//
//    @Query("DELETE FROM place")
//    fun clear()
//}