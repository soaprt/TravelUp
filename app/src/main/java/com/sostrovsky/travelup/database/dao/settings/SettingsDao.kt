package com.sostrovsky.travelup.database.dao.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.sostrovsky.travelup.database.entities.settings.Settings

/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Dao
interface SettingsDao {
    @Query("SELECT COUNT(*) FROM settings")
    fun checkIfEmpty(): Long

    @Query("SELECT COUNT(*) FROM settings WHERE id=:settingsId AND is_selected=1")
    fun checkIfSelected(settingsId: Long): Long

    @Query("SELECT id FROM settings WHERE language_id=:languageId AND currency_id=:currencyId AND country_id=:countryId")
    fun getId(languageId: Long, currencyId: Long, countryId: Long): Long

    @Query("SELECT * FROM settings WHERE is_selected=1")
    fun getSelected(): Settings

    @Query("SELECT id FROM settings WHERE is_selected=1")
    fun getSelectedSettingsId(): Long

    @Insert
    fun insert(settings: Settings): Long

    @Query("UPDATE settings SET is_selected=0")
    fun unSelectAll()

    @Query("UPDATE settings SET is_selected=1 WHERE id=:settingsId")
    fun selectOne(settingsId: Long)

    @Transaction
    fun insertAndSetSelected(settings: Settings): Long {
        val rowId = insert(settings)
        setSelected(settings.id)

        return rowId
    }

    @Transaction
    fun setSelected(settingsId: Long) {
        unSelectAll()
        selectOne(settingsId)
    }
}