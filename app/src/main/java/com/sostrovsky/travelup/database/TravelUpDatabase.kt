package com.sostrovsky.travelup.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sostrovsky.travelup.database.dao.settings.CountryDao
import com.sostrovsky.travelup.database.dao.settings.CurrencyDao
import com.sostrovsky.travelup.database.dao.settings.LanguageDao
import com.sostrovsky.travelup.database.dao.settings.SettingsDao
import com.sostrovsky.travelup.database.dao.ticket.CarrierDao
import com.sostrovsky.travelup.database.dao.ticket.MarketPlaceDao
import com.sostrovsky.travelup.database.dao.ticket.TicketSearchParamsDao
import com.sostrovsky.travelup.database.dao.ticket.TicketSearchResultDao
import com.sostrovsky.travelup.database.entities.settings.Country
import com.sostrovsky.travelup.database.entities.settings.Currency
import com.sostrovsky.travelup.database.entities.settings.Language
import com.sostrovsky.travelup.database.entities.settings.Settings
import com.sostrovsky.travelup.database.entities.ticket.Carrier
import com.sostrovsky.travelup.database.entities.ticket.MarketPlace
import com.sostrovsky.travelup.database.entities.ticket.TicketSearchParams
import com.sostrovsky.travelup.database.entities.ticket.TicketSearchResult

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@Database(
    entities = [Carrier::class, Country::class, Currency::class, Language::class,
        MarketPlace::class, Settings::class, TicketSearchParams::class, TicketSearchResult::class],
    version = 1, exportSchema = false
)
abstract class TravelUpDatabase : RoomDatabase() {
    abstract val carrierDao: CarrierDao
    abstract val countryDao: CountryDao
    abstract val currencyDao: CurrencyDao
    abstract val languageDao: LanguageDao
    abstract val marketPlaceDao: MarketPlaceDao
    abstract val settingsDao: SettingsDao
    abstract val ticketSearchResultDao: TicketSearchResultDao
    abstract val ticketSearchParamsDao: TicketSearchParamsDao

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