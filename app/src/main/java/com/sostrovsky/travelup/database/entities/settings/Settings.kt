package com.sostrovsky.travelup.database.entities.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity
data class Settings(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "language_id")
    val languageId: Int,

    @ColumnInfo(name = "currency_id")
    val currencyId: Int,

    @ColumnInfo(name = "country_id")
    val countryId: Int,

    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean = false
) {
    override fun toString(): String {
        return "Settings(id=$id, languageId=$languageId, currencyId=$currencyId, countryId=$countryId, isSelected=$isSelected)"
    }
}