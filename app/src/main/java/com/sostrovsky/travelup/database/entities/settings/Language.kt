package com.sostrovsky.travelup.database.entities.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.settings.LanguageDomain

/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity
class Language (
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0L,

    @ColumnInfo
    val code: String,

    @ColumnInfo
    val name: String
)

fun List<Language>.asDomain(): List<LanguageDomain> {
    return map {
        LanguageDomain(
            code = it.code,
            name = it.name
        )
    }
}