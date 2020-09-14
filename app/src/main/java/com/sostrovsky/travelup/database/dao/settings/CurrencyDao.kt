package com.sostrovsky.travelup.database.dao.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.settings.Currency

/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface CurrencyDao {
    @Query("DELETE FROM currency")
    fun clear()

    @Query("SELECT * FROM currency")
    fun getAll(): List<Currency>

    @Query("SELECT code FROM currency WHERE id=:id")
    fun getCodeById(id: Long): String

    @Query("SELECT id FROM currency WHERE code=:code")
    fun getIdByCode(code: String): Long

    @Query("SELECT COUNT(*) FROM currency")
    fun getRowsCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(currencies: List<Currency>)
}