package com.sostrovsky.travelup.network

import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Author: Sergey Ostrovsky
 * Date: 26.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, error: String): T? {
    val result = networkResponseOutput(call, error)
    var output: T? = null
    when (result) {
        is NetworkResponse.Success ->
            output = result.output
        is NetworkResponse.Error -> Timber.e("${result.exception}")
    }
    return output

}

private suspend fun <T : Any> networkResponseOutput(call: suspend () -> Response<T>, error: String):
        NetworkResponse<T> {
    val response = call.invoke()
    return if (response.isSuccessful)
        NetworkResponse.Success(response.body()!!)
    else
        NetworkResponse.Error(IOException(error))
}