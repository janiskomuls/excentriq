package com.excentriq.stocks.yahoo

import com.excentriq.stocks.dividends.DividendsHistory
import com.excentriq.stocks.yahoo.ChartResponse.*
import sttp.client3.*
import sttp.client3.ShowError.*
import sttp.client3.httpclient.zio.*
import sun.nio.cs.{StandardCharsets, UTF_8}
import zio.*
import zio.json.*

import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.{Instant, OffsetTime, ZoneOffset}
import java.util.{SimpleTimeZone, TimeZone}
import scala.math.BigDecimal.RoundingMode

class YahooHttpClient(
    sttp: SttpBackend[Task, Any],
    semaphore: Semaphore
):
  def request(
      ticker: StockTicker,
      from: Instant,
      to: Instant,
      interval: Interval
  ): Task[List[DividendsHistory]] =
    for
      ticker <- ZIO.succeed(
        URLEncoder.encode(ticker, Charset.defaultCharset())
      )
      params = Map(
        "interval" -> interval.code,
        "period1" -> from.getEpochSecond,
        "period2" -> to.getEpochSecond,
        "events" -> "div%7Csplit"
      ).map((k, v) => s"$k=$v").mkString("&")
      url = s"${YahooHttpClient.Url}/$ticker?$params"
      request = basicRequest.response(asStringAlways).get(uri"$url")
      response <- semaphore.withPermit(sttp.send(request))
      _ <- ZIO.debug(response.body)
      result <- ZIO
        .fromEither(response.body.fromJson[ChartResponse])
        .mapError(error => DecodeException(response.body, error.toError))
    yield
      println(result)
      List.empty

object YahooHttpClient:

  private val Url = "https://query1.finance.yahoo.com/v8/finance/chart"

  val live: URLayer[
    SttpBackend[Task, Any] & YahooConfig,
    YahooHttpClient
  ] = ZLayer {
    for
      sttp <- ZIO.service[SttpBackend[Task, Any]]
      config <- ZIO.service[YahooConfig]
      semaphore <- Semaphore.make(config.desiredParallelism)
    yield YahooHttpClient(sttp, semaphore)
  }
