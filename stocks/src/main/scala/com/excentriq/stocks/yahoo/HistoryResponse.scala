package com.excentriq.stocks.yahoo

import com.excentriq.stocks.yahoo.*
import com.excentriq.stocks.yahoo.HistoryResponse.*
import com.excentriq.stocks.yahoo.json.*
import zio.json.*

import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.{Instant, OffsetTime, ZoneOffset}
import java.util.{SimpleTimeZone, TimeZone}
import scala.math.BigDecimal.RoundingMode

case class HistoryResponse(chart: HistoryChart)

object HistoryResponse:
  opaque type StockSymbol <: String = String
  opaque type Currency <: String = String
  opaque type ExchangeName <: String = String
  opaque type InstrumentType <: String = String
  opaque type TimeZoneName <: String = String
  opaque type Error <: String = String
  opaque type SplitRatioStr <: String = String

  opaque type Price <: BigDecimal = BigDecimal
  opaque type PricePrecision <: Int = Int
  opaque type Amount <: BigDecimal = BigDecimal
  opaque type ZoneOffset <: Int = Int
  opaque type Numerator <: Int = Int
  opaque type Denominator <: Int = Int

  opaque type Timestamp <: Instant = Instant

  extension (value: String)
    def yhSymbol: StockSymbol = value
    def yhCurrency: Currency = value
    def yhExchangeName: ExchangeName = value
    def yhInstrumentType: InstrumentType = value
    def yhTimeZoneName: TimeZoneName = value
    def yhError: Error = value
    def yhSplitRatio: SplitRatioStr = value

  extension (value: BigDecimal)
    def yhPrice: Price = value
    def yhAmount: Amount = value
    def scaled(scale: Int) = value.setScale(scale, RoundingMode.HALF_UP)

  extension (value: Int)
    def yhPrecision: PricePrecision = value
    def yhZoneOffset: ZoneOffset = value

  extension (value: Instant) def yhTimestamp: Timestamp = value

  case class HistoryChart(result: List[Result], error: Option[Error])

  case class Result(meta: MetaInfo, timestamp: List[Timestamp] = List.empty, events: Events)

  case class MetaInfo(
    symbol: StockSymbol,
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
    dataGranularity: Interval,
    range: Range,
    validRanges: List[Range]
  ):
    def price: Price = regularMarketPrice.scaled(priceHint)
    def previousClosePrice: Price = chartPreviousClose.scaled(priceHint)

  case class Events(splits: Map[Timestamp, Split], dividends: Map[Timestamp, Dividend]):
    def dividendsList: List[Dividend] = dividends.values.toList

  case class Split(
    date: Timestamp,
    numerator: Numerator, // 20
    denominator: Denominator, // 1
    splitRatio: SplitRatioStr // "20:1"
  )

  case class Dividend(amount: Amount, date: Timestamp):
    def exDividendDate: Timestamp = date

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

  implicit val timestampDecoder: JsonDecoder[Timestamp] = InstantDecoder
  implicit val timestampFieldDecoder: JsonFieldDecoder[Timestamp] = InstantFieldDecoder
  implicit val tradingPeriodDecoder: JsonDecoder[TradingPeriod] = DeriveJsonDecoder.gen
  implicit val currentTradingPeriodDecoder: JsonDecoder[CurrentTradingPeriod] = DeriveJsonDecoder.gen
  implicit val metaInfoDecoder: JsonDecoder[MetaInfo] = DeriveJsonDecoder.gen
  implicit val splitDecoder: JsonDecoder[Split] = DeriveJsonDecoder.gen
  implicit val dateToSplitMapDecoder: JsonDecoder[Map[Timestamp, Split]] = MapWithDefaultJsonEncoder.gen
  implicit val dividendDecoder: JsonDecoder[Dividend] = DeriveJsonDecoder.gen
  implicit val dividendMapDecoder: JsonDecoder[Map[Timestamp, Dividend]] = MapWithDefaultJsonEncoder.gen
  implicit val eventsDecoder: JsonDecoder[Events] = DeriveJsonDecoder.gen
  implicit val resultDecoder: JsonDecoder[Result] = DeriveJsonDecoder.gen
  implicit val historyDecoder: JsonDecoder[HistoryChart] = DeriveJsonDecoder.gen
  implicit val historyResponseDecoder: JsonDecoder[HistoryResponse] = DeriveJsonDecoder.gen
