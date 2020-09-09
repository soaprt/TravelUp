package com.sostrovsky.travelup.database.entities.preferences

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.preferences.UserLanguageDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "user_language")
class UserLanguageDBModel constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0L,

    @ColumnInfo(name = "locale_code")
    val localeCode: String,

    @ColumnInfo(name = "language_name")
    val languageName: String
)

/**
 * Map UserLanguageDBModel to domain entity UserLanguageDomainModel
 */
fun List<UserLanguageDBModel>.asDomainModel(): List<UserLanguageDomainModel> {
    return map {
        UserLanguageDomainModel(
            localeCode = it.localeCode,
            languageName = it.languageName
        )
    }
}