package com.sostrovsky.travelup.repository.settings

/**
 * Author: Sergey Ostrovsky
 * Date: 23.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
abstract class RowsGenerator<T> {
    var rowId: Long = 0L

    abstract suspend fun execute(): T

    fun generateRowId(): Long {
        rowId = rowId.inc()
        return rowId
    }
}