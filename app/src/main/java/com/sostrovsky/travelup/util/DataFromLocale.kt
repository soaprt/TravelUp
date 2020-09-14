package com.sostrovsky.travelup.util

import java.util.*

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
fun getFormattedLocale(locale: Locale = Locale.getDefault()): String {
    return locale.toString().replace("_", "-")
}

fun getCurrencyFromLocale(locale: Locale = Locale.getDefault()): Currency? {
    return try {
        Currency.getInstance(locale)
    } catch (e: Exception) {
        null
    }
}

fun getCurrencyCodeFromLocale(locale: Locale = Locale.getDefault()): String {
    return getCurrencyFromLocale(locale)?.currencyCode ?: ""
}

fun getCountryCodeFromLocale(locale: Locale = Locale.getDefault()): String {
    return locale.country.toLowerCase()
}

fun getCountryNameFromLocale(locale: Locale = Locale.getDefault()): String {
    return locale.displayCountry.capitalize()
}
