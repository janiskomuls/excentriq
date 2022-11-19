package com.excentriq.stocks

import java.time.Instant

package object domain:
  opaque type StockTicker <: String = String
//  opaque type Currency <: String = String
//  opaque type ExchangeName <: String = String
//  opaque type InstrumentType <: String = String
//  opaque type TimeZoneName <: String = String
//  opaque type Price <: BigDecimal = BigDecimal
//  opaque type PricePrecision <: Int = Int
//  opaque type ZoneOffset <: Int = Int
//  opaque type Timestamp <: Instant = Instant

  extension (value: String)
    def toTicker: StockTicker = value
//    def toCurrency: Currency = value
//    def toExchangeName: ExchangeName = value
//    def toInstrumentType: InstrumentType = value
//    def toTimeZoneName: TimeZoneName = value
//
//  extension (value: BigDecimal) def toPrice: Price = value
//
//  extension (value: Int)
//    def toPrecision: PricePrecision = value
//    def toZoneOffset: ZoneOffset = value
//
//  extension (value: Instant) def toTimestamp: Timestamp = value
