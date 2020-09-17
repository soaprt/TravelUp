package com.sostrovsky.travelup.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.util.network.NetworkHelper
import io.reactivex.disposables.Disposable

open class BaseActivity(private val rootLayoutId: Int) : AppCompatActivity() {
    lateinit var navController: NavController
    private var snackBarOffline: Snackbar? = null
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
            true -> hideShackBarOffline()
            false -> showShackBarOffline()
        }
    }

    private fun networkStateUnsubscribe() {
        networkDisposable?.dispose()
    }

    private fun showShackBarOffline() {
        val message = getString(R.string.msg_you_are_offline)

        snackBarOffline = generateSnackBar(findViewById(rootLayoutId), message,
            Snackbar.LENGTH_INDEFINITE)
        snackBarOffline?.let {
            it.duration = Snackbar.LENGTH_INDEFINITE
            it.show()
        }
    }

    private fun generateSnackBar(view: View, message: String, duration: Int) : Snackbar {
        val result = Snackbar.make(view, message, duration)
        val snackBarView: View = result.view
        snackBarView.setBackgroundColor(applicationContext.resources.getColor(R.color.colorPrimary))

        return result
    }

    private fun hideShackBarOffline() {
        snackBarOffline?.dismiss()
    }

    fun showSnackBarEvent(message: String) {
        generateSnackBar(findViewById(rootLayoutId), message, Snackbar.LENGTH_SHORT).show()
    }

    fun hideSoftKeyboard(activity: Activity) {
        activity.currentFocus?.let {
            val inputMethodManager: InputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    fun moveTo(action: NavDirections) {
        navController.navigate(action)
    }
}
