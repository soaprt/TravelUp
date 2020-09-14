package com.sostrovsky.travelup.domain.settings


/**
 * Author: Sergey Ostrovsky
 * Date: 12.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class CurrencyDomain(val code: String, val name: String) {
    override fun toString(): String {
        return name
    }
}