package com.sostrovsky.travelup.database.dao.preferences

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.preferences.UserCountryDBModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface CountryDBDao {
    @Query("select * from user_country")
    fun getCountries(): List<UserCountryDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countries: List<UserCountryDBModel>)

    @Query("DELETE FROM user_country")
    fun clear()
}