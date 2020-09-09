package com.sostrovsky.travelup.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sostrovsky.travelup.database.dao.place.PlaceDBDao
import com.sostrovsky.travelup.database.dao.preferences.CountryDBDao
import com.sostrovsky.travelup.database.dao.preferences.CurrencyDBDao
import com.sostrovsky.travelup.database.dao.preferences.LanguageDBDao
import com.sostrovsky.travelup.database.dao.preferences.UserSettingsDBDao
import com.sostrovsky.travelup.database.dao.ticket.TicketDBDao
import com.sostrovsky.travelup.database.entities.place.PlaceDBModel
import com.sostrovsky.travelup.database.entities.preferences.UserCountryDBModel
import com.sostrovsky.travelup.database.entities.preferences.UserCurrencyDBModel
import com.sostrovsky.travelup.database.entities.preferences.UserLanguageDBModel
import com.sostrovsky.travelup.database.entities.preferences.UserSettingsDBModel
import com.sostrovsky.travelup.database.entities.ticket.TicketDBModel

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Database(
    entities = [PlaceDBModel::class, TicketDBModel::class, UserLanguageDBModel::class,
        UserCurrencyDBModel::class, UserCountryDBModel::class, UserSettingsDBModel::class],
    version = 1, exportSchema = false
)
abstract class TravelUpDatabase : RoomDatabase() {
    abstract val countryDBDao: CountryDBDao
    abstract val currencyDBDao: CurrencyDBDao
    abstract val languageDBDao: LanguageDBDao
    abstract val placeDBDao: PlaceDBDao
    abstract val ticketDBDao: TicketDBDao
    abstract val userSettingsDao: UserSettingsDBDao

    companion object {
        @Volatile
        private var INSTANCE: TravelUpDatabase? = null

        fun getInstance(context: Context): TravelUpDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TravelUpDatabase::class.java, "travel_up_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}