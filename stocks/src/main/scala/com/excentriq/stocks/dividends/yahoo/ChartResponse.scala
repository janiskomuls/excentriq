package com.excentriq.stocks.dividends.yahoo

import com.excentriq.stocks.dividends.yahoo.ChartResponse.*
import com.excentriq.stocks.domain.*

import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.{Instant, OffsetTime, ZoneOffset}
import java.util.{SimpleTimeZone, TimeZone}
import scala.math.BigDecimal.RoundingMode

case class ChartResponse(chart: Chart)

object ChartResponse:
  opaque type StockTicker <: String = String
  opaque type Currency <: String = String
  opaque type ExchangeName <: String = String
  opaque type InstrumentType <: String = String
  opaque type TimeZoneName <: String = String
  opaque type Price <: BigDecimal = BigDecimal
  opaque type PricePrecision <: Int = Int
  opaque type ZoneOffset <: Int = Int
  opaque type Timestamp <: Instant = Instant

  extension (value: String)
    def toTicker: StockTicker = value
    def toCurrency: Currency = value
    def toExchangeName: ExchangeName = value
    def toInstrumentType: InstrumentType = value
    def toTimeZoneName: TimeZoneName = value

  extension (value: BigDecimal) def toPrice: Price = value

  extension (value: Int)
    def toPrecision: PricePrecision = value
    def toZoneOffset: ZoneOffset = value

  extension (value: Instant) def toTimestamp: Timestamp = value

  case class Chart(results: List[Result], error: Option[String])
  case class Result(metaInfo: MetaInfo)

  case class MetaInfo(
      symbol: StockTicker,
      currency: Currency,
      exchangeName: ExchangeName,
      instrumentType: InstrumentType,
      firstTradeDate: Timestamp,
      regularMarketTime: Timestamp,
      gmtoffset: ZoneOffset,
      timezone: TimeZoneName,
      exchangeTimezoneName: TimeZoneName,
      regularMarketPrice: Price,
      chartPreviousClose: Price,
      priceHint: PricePrecision,
      dataGranularity: YahooInterval,
      range: YahooRange,
      validRanges: List[YahooRange],
      timestamp: List[Instant],
      currentTradingPeriod: CurrentTradingPeriod
  ) {
    def price: Price =
      regularMarketPrice.setScale(priceHint, RoundingMode.HALF_UP)
    def previousClosePrice: Price =
      chartPreviousClose.setScale(priceHint, RoundingMode.HALF_UP)
  }

  case class TradingPeriod(
      timezone: TimeZoneName,
      start: Timestamp,
      end: Timestamp,
      gmtoffset: ZoneOffset
  )

  case class CurrentTradingPeriod(
      pre: TradingPeriod,
      regular: TradingPeriod,
      post: TradingPeriod
  )
