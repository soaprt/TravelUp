package com.sostrovsky.travelup.util.network

import com.sostrovsky.travelup.network.WebServiceResponse
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
        is WebServiceResponse.Success -> output = result.output
        is WebServiceResponse.Error -> Timber.e("${result.exception}")
    }
    return output

}

private suspend fun <T : Any> networkResponseOutput(call: suspend () -> Response<T>, error: String):
        WebServiceResponse<T> {
    val response = call.invoke()
    return if (response.isSuccessful)
        WebServiceResponse.Success(response.body()!!)
    else
        WebServiceResponse.Error(
            IOException(
                error
            )
        )
}