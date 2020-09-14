package com.sostrovsky.travelup.database.dao.trash

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.travelup.database.entities.trash.UserCurrencyDBModel

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
//@Dao
//interface CurrencyDBDao {
//    @Query("select * from user_currency")
//    fun getCurrencies(): List<UserCurrencyDBModel>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertCurrencies(currencies: List<UserCurrencyDBModel>)
//
//    @Query("DELETE FROM user_currency")
//    fun clear()
//}