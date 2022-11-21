package com.excentriq.stocks.dividends.yahoo

import com.excentriq.stocks.dividends.yahoo.ChartResponse.*
import com.excentriq.stocks.domain.*
import zio.json.*

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
  opaque type Error <: String = String

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
    def toError: Error = value

  extension (value: BigDecimal)
    def toPrice: Price = value
    def scaled(scale: Int) = value.setScale(scale, RoundingMode.HALF_UP)

  extension (value: Int)
    def toPrecision: PricePrecision = value
    def toZoneOffset: ZoneOffset = value

  extension (value: Instant) def toTimestamp: Timestamp = value

  case class DecodeException(body: String, error: Error) extends RuntimeException(error)

  case class Chart(result: List[Result], error: Option[Error])
  case class Result(meta: MetaInfo, timestamp: List[Timestamp] /*, events */)

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
      currentTradingPeriod: CurrentTradingPeriod,
      dataGranularity: YahooInterval,
      range: YahooRange,
      validRanges: List[YahooRange],
  ) {
    def price: Price = regularMarketPrice.scaled(priceHint)
    def previousClosePrice: Price = chartPreviousClose.scaled(priceHint)
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

  implicit val timestampDecoder: JsonDecoder[Timestamp] = JsonDecoder.long.map(Instant.ofEpochSecond)
  implicit val intervalDecoder: JsonDecoder[YahooInterval] = JsonDecoder.string.mapOrFail(YahooInterval.byCode)
  implicit val rangeDecoder: JsonDecoder[YahooRange] = JsonDecoder.string.mapOrFail(YahooRange.byCode)
  implicit val tradingPeriodDecoder: JsonDecoder[TradingPeriod] = DeriveJsonDecoder.gen
  implicit val currentTradingPeriodDecoder: JsonDecoder[CurrentTradingPeriod] = DeriveJsonDecoder.gen
  implicit val metaInfoDecoder: JsonDecoder[MetaInfo] = DeriveJsonDecoder.gen
  implicit val resultDecoder: JsonDecoder[Result] = DeriveJsonDecoder.gen
  implicit val chartDecoder: JsonDecoder[Chart] = DeriveJsonDecoder.gen
  implicit val chartResponseDecoder: JsonDecoder[ChartResponse] = DeriveJsonDecoder.gen
