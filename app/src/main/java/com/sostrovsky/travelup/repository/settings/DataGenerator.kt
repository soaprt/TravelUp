package com.sostrovsky.travelup.repository.settings

/**
 * Author: Sergey Ostrovsky
 * Date: 16.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
interface DataGenerator<T> {
    suspend fun generate(): T
}