package com.sostrovsky.travelup.database.dao.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.settings.Language

/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface LanguageDao {
    @Query("DELETE FROM language")
    fun clear()

    @Query("SELECT * FROM language")
    fun getAll(): List<Language>

    @Query("SELECT code FROM language WHERE id=:id")
    fun getCodeById(id: Long): String

    @Query("SELECT id FROM language WHERE code=:code")
    fun getIdByCode(code: String): Long

    @Query("SELECT COUNT(*) FROM language")
    fun getRowsCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(languages: List<Language>)
}