package com.sostrovsky.travelup.domain.preferences

/**
 * Author: Sergey Ostrovsky
 * Date: 2.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class UserCountryDomainModel(val countryCode: String, val countryName: String) {
    override fun toString(): String {
        return countryName
    }
}