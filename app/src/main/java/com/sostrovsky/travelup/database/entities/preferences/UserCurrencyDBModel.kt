package com.sostrovsky.travelup.database.entities.preferences

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sostrovsky.travelup.domain.preferences.UserCurrencyDomainModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Entity(tableName = "user_currency")
class UserCurrencyDBModel constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0L,

    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "currency_name")
    val currencyName: String
)

/**
 * Map UserCurrencyDBModel to domain entity UserCurrencyDomainModel
 */
fun List<UserCurrencyDBModel>.asDomainModel(): List<UserCurrencyDomainModel> {
    return map {
        UserCurrencyDomainModel(
            currencyCode = it.currencyCode,
            currencyName = it.currencyName
        )
    }
}