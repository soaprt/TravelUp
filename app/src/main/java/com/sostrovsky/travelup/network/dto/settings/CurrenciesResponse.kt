package com.sostrovsky.travelup.network.dto.settings

import com.squareup.moshi.JsonClass

/**
 * Author: Sergey Ostrovsky
 * Date: 22.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
@JsonClass(generateAdapter = true)
data class CurrenciesResponse(val Currencies: List<CurrencyFromJSON>)

@JsonClass(generateAdapter = true)
data class CurrencyFromJSON(val Code: String, val Symbol: String, val ThousandsSeparator: String,
                    val DecimalSeparator: String, val SymbolOnLeft: Boolean,
                    val SpaceBetweenAmountAndSymbol: Boolean, val RoundingCoefficient: Integer,
                    val DecimalDigits: Integer)