package com.sostrovsky.travelup.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
fun isNotPastDate(date: Calendar): Boolean {
    val currentDate = Calendar.getInstance()
    val isNotPastYear = (date.get(Calendar.YEAR) >= currentDate.get(Calendar.YEAR))
    val isNotPastMonth = (date.get(Calendar.MONTH) >= currentDate.get(Calendar.MONTH))
    val isNotPastDate = (date.get(Calendar.DATE) >= currentDate.get(Calendar.DATE))
    return (isNotPastYear && isNotPastMonth && isNotPastDate)
}

fun getFormattedDate(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US) // Locale.getDefault())
    return sdf.format(date).split("T")[0]
}