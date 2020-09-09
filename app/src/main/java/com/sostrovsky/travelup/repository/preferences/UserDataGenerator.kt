package com.sostrovsky.travelup.repository.preferences

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
abstract class UserDataGenerator<T> {
    var rowId: Long = 0L

    abstract suspend fun execute(): T

    fun generateRowId(): Long {
        rowId = rowId.inc()
        return rowId
    }
}