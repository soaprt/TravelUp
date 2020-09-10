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

fun calendarDateToLocalDate(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(date)
}

fun isoDateToLocalDate(date: String): String {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val localFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val result = isoFormat.parse(date)
    return if (result != null) {
        localFormat.format(result)
    } else ""
}

fun isoTimeToLocalTime(date: String): String {
    val fromIsoToLocal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val shortDate = SimpleDateFormat("HH:mm", Locale.getDefault())

    val localDate = fromIsoToLocal.parse(date)
    return if (localDate != null) {
        shortDate.format(localDate)
    } else ""
}

