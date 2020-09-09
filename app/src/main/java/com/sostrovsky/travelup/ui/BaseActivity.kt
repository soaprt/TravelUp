package com.sostrovsky.travelup.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.util.network.NetworkHelper
import io.reactivex.disposables.Disposable

open class BaseActivity(private val rootLayoutId: Int) : AppCompatActivity() {
    private var snackBar: Snackbar? = null
    private var networkDisposable: Disposable? = null

    override fun onResume() {
        super.onResume()
        networkStateSubscribe()
        checkOffline(NetworkHelper.isAvailable)
    }

    override fun onPause() {
        super.onPause()
        networkStateUnsubscribe()
    }

    @SuppressLint("CheckResult")
    private fun networkStateSubscribe () {
        networkDisposable = NetworkHelper.stateObservable.subscribe { it ->
            checkOffline(it)
        }
    }

    private fun checkOffline(isOffline: Boolean) {
        when(isOffline) {
            true -> hideOfflineMessage()
            false -> showOfflineMessage()
        }
    }

    private fun networkStateUnsubscribe() {
        networkDisposable?.dispose()
    }

    private fun showOfflineMessage() {
        val messageToUser = getString(R.string.you_are_offline_msg)

        snackBar = Snackbar.make(findViewById(rootLayoutId), messageToUser, Snackbar.LENGTH_LONG)
        snackBar?.duration = Snackbar.LENGTH_INDEFINITE
        snackBar?.show()
    }

    private fun hideOfflineMessage() {
        snackBar?.dismiss()
    }
}
