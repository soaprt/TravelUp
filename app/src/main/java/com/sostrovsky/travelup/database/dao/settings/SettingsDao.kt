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
    fun checkIfEmpty(): Int

    @Query("SELECT COUNT(*) FROM settings WHERE id=:settingsId AND is_selected=1")
    fun checkIfSelected(settingsId: Int): Int

    @Query("SELECT id FROM settings WHERE language_id=:languageId AND currency_id=:currencyId AND country_id=:countryId")
    fun getId(languageId: Int, currencyId: Int, countryId: Int): Int

    @Query("SELECT * FROM settings WHERE is_selected=1")
    fun getSelected(): Settings

    @Query("SELECT id FROM settings WHERE is_selected=1")
    fun getSelectedSettingsId(): Int

    @Insert
    fun insert(settings: Settings): Long

    @Query("UPDATE settings SET is_selected=0")
    fun unSelectAll()

    @Query("UPDATE settings SET is_selected=1 WHERE id=:settingsId")
    fun selectOne(settingsId: Int)

    @Transaction
    fun insertAndSetSelected(settings: Settings): Long {
        val rowId = insert(settings)
        setSelected(settings.id)

        return rowId
    }

    @Transaction
    fun setSelected(settingsId: Int) {
        unSelectAll()
        selectOne(settingsId)
    }
}