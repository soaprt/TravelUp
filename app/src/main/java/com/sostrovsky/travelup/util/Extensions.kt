package com.sostrovsky.travelup.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Author: Sergey Ostrovsky
 * Date: 02.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}