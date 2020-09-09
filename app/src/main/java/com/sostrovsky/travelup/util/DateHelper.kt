package com.sostrovsky.travelup.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

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

fun getFormattedUTCDate(date: String): String {
//    val timeInMillis: Long = Date(date).time
//    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    return dateFormat.format(Date(getTimestampWithOffset(timeInMillis)))
    // TODO: probably need to user dateFormat.parse(String)
    return "2020-09-11"
}

fun getTimestampWithOffset(timestamp: Long): Long {
    var result = timestamp
    val offset = TimeZone.getDefault().getOffset(timestamp)
    if (offset < 0) {
        result += abs(offset).toLong()
    } else {
        result -= abs(offset).toLong()
    }
    return result
}

fun getFormattedUTCTime(date: String): String {
//    val timeFormat: DateFormat = SimpleDateFormat("HH:mm")
//
//    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
//    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
//    return simpleDateFormat.parse(date.toString())!!.toString()
    // TODO: probably need to finish
    return "14:14:00"
}

