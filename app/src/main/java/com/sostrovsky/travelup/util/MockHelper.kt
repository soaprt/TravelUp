package com.sostrovsky.travelup.util

import com.sostrovsky.travelup.network.dto.settings.CurrencyFromJSON
import com.sostrovsky.travelup.network.dto.ticket.*

/**
 * Author: Sergey Ostrovsky
 * Date: 03.09.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
fun mockedTicketsResponse() : TicketsResponse {
    val Quotes = mutableListOf<QuoteFromJSON>()
    Quotes.add(QuoteFromJSON(
        QuoteId = 1,
        MinPrice = 23702,
        Direct = false,
        OutboundLeg = BoundLegFromJSON (
            CarrierIds = listOf(469),
            OriginId = 63446,
            DestinationId = 88879,
            DepartureDate = "2020-09-11T00:00:00"
        ),
        InboundLeg = null,
        QuoteDateTime = "2020-09-09T14:14:00"
    ))

    val Places = mutableListOf<TicketPlaceFromJSON>()
    Places.add(TicketPlaceFromJSON(
        PlaceId = 47493,
        IataCode = "DME",
        Name = "Москва Домодедово",
        Type = "Station",
        SkyscannerCode = "DME",
        CityName = "Москва",
        CityId = "MOSC",
        CountryName = "Россия"
    ))
    Places.add(TicketPlaceFromJSON(
        PlaceId = 63446,
        IataCode = "KIV",
        Name = "Кишинёв",
        Type = "Station",
        SkyscannerCode = "KIV",
        CityName = "Кишинёв",
        CityId = "KIVA",
        CountryName = "Молдавия"
    ))
    Places.add(TicketPlaceFromJSON(
        PlaceId = 82495,
        IataCode = "SVO",
        Name = "Москва Шереметьево",
        Type = "Station",
        SkyscannerCode = "SVO",
        CityName = "Москва",
        CityId = "MOSC",
        CountryName = "Россия"
    ))
    Places.add(TicketPlaceFromJSON(
        PlaceId = 88879,
        IataCode = "VKO",
        Name = "Москва Внуково",
        Type = "Station",
        SkyscannerCode = "VKO",
        CityName = "Москва",
        CityId = "MOSC",
        CountryName = "Россия"
    ))
    Places.add(TicketPlaceFromJSON(
        PlaceId = 97985,
        IataCode = "ZIA",
        Name = "Москва Жуковский",
        Type = "Station",
        SkyscannerCode = "ZIA",
        CityName = "Москва",
        CityId = "MOSC",
        CountryName = "Россия"
    ))

    val Carriers = mutableListOf<CarrierFromJSON>()
    Carriers.add(CarrierFromJSON(
        CarrierId = 469,
        Name = "Air Moldova"
    ))
    Carriers.add(CarrierFromJSON(
        CarrierId = 1523,
        Name = "Austrian Airlines"
    ))
    Carriers.add(CarrierFromJSON(
        CarrierId = 1755,
        Name = "Turkish Airlines"
    ))

    val Currencies = mutableListOf<CurrencyFromJSON>()
    Currencies.add(CurrencyFromJSON(
        Code = "RUB",
        Symbol = "₽",
        ThousandsSeparator = " ",
        DecimalSeparator = ",",
        SymbolOnLeft = false,
        SpaceBetweenAmountAndSymbol = true,
        RoundingCoefficient = Integer(0),
        DecimalDigits = Integer(2)
    ))

    return TicketsResponse(Quotes, Places, Carriers, Currencies)
}
