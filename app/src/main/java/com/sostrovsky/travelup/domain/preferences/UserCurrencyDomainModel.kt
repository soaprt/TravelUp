package com.sostrovsky.travelup.domain.preferences

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class UserCurrencyDomainModel(val currencyCode: String, val currencyName: String) {
    override fun toString(): String {
        return currencyName
    }
}