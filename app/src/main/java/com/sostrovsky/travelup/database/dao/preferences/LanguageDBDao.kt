package com.sostrovsky.travelup.database.dao.preferences

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.preferences.UserLanguageDBModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface LanguageDBDao {
    @Query("select * from user_language")
    fun getLanguages(): List<UserLanguageDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLanguages(languages: List<UserLanguageDBModel>)

    @Query("DELETE FROM user_language")
    fun clear()
}