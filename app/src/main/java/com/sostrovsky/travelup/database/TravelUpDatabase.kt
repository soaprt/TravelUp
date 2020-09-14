package com.sostrovsky.travelup.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sostrovsky.travelup.database.dao.settings.CountryDao
import com.sostrovsky.travelup.database.dao.settings.CurrencyDao
import com.sostrovsky.travelup.database.dao.settings.LanguageDao
import com.sostrovsky.travelup.database.dao.settings.SettingsDao
import com.sostrovsky.travelup.database.entities.settings.Country
import com.sostrovsky.travelup.database.entities.settings.Currency
import com.sostrovsky.travelup.database.entities.settings.Language
import com.sostrovsky.travelup.database.entities.settings.Settings

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Database(
//    entities = [PlaceDBModel::class, TicketDBModel::class, UserLanguageDBModel::class,
//        UserCurrencyDBModel::class, UserCountryDBModel::class, UserSettingsDBModel::class],

    entities = [Country::class, Currency::class, Language::class, Settings::class],
    version = 1, exportSchema = false
)
abstract class TravelUpDatabase : RoomDatabase() {
    abstract val countryDao: CountryDao
    abstract val currencyDao: CurrencyDao
    abstract val languageDao: LanguageDao
    abstract val settingsDao: SettingsDao

//    abstract val countryDBDao: CountryDBDao
//    abstract val currencyDBDao: CurrencyDBDao
//    abstract val languageDBDao: LanguageDBDao
//    abstract val placeDBDao: PlaceDBDao
//    abstract val ticketDBDao: TicketDBDao
//    abstract val userSettingsDao: UserSettingsDBDao

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