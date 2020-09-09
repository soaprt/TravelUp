package com.sostrovsky.travelup.domain.preferences

/**
 * Author: Sergey Ostrovsky
 * Date: 20.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
data class UserLanguageDomainModel(val localeCode: String, val languageName: String) {
    override fun toString(): String {
        return languageName
    }
}