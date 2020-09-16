package com.sostrovsky.travelup.database.dao.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.settings.Country

/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface CountryDao {
    @Query("DELETE FROM country")
    fun clear()

    @Query("SELECT * FROM country")
    fun getAll(): List<Country>

    @Query("SELECT code FROM country WHERE id=:id")
    fun getCodeById(id: Int): String

    @Query("SELECT id FROM country WHERE code=:code")
    fun getIdByCode(code: String): Int

    @Query("SELECT COUNT(*) FROM country")
    fun getRowsCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countries: List<Country>)
}