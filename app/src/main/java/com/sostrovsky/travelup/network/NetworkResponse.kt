package com.sostrovsky.travelup.network

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
sealed class NetworkResponse <out T : Any> {
    data class Success<out T : Any>(val output : T) : NetworkResponse<T>()
    data class Error(val exception: Exception)  : NetworkResponse<Nothing>()
}