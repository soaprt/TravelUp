package com.sostrovsky.travelup.util.network

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.sostrovsky.travelup.TravelUpApp
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */

object NetworkHelper :
    ConnectivityReceiver.ConnectivityReceiverListener {
    var isAvailable = false
    val stateObservable = PublishSubject.create<Boolean>()

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isAvailable != isConnected) {
            isAvailable = isConnected
            stateObservable.onNext(
                isAvailable
            )
        }
    }

    fun create() {
        isAvailable = isNetworkAvailable()

        TravelUpApp.applicationContext().registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = TravelUpApp.applicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            Timber.e("${Build.VERSION.SDK_INT < Build.VERSION_CODES.M}")
            return false
        }

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Timber.i("NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Timber.i("NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Timber.i("NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}
