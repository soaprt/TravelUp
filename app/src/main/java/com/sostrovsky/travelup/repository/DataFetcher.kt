package com.sostrovsky.travelup.repository

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface DataFetcher<V, R> {
    suspend fun fetch(param: V): R
}