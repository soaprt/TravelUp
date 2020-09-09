package com.sostrovsky.travelup

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.sostrovsky.travelup.util.network.NetworkHelper
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 19.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class TravelUpApp : MultiDexApplication() {
    init {
        instance = this
    }

    companion object {
        private var instance: TravelUpApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        NetworkHelper.create()
    }
}