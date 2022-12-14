package com.excentriq.stocks.yahoo

import com.excentriq.stocks.yahoo.HistoryResponse.*
import com.excentriq.stocks.yahoo.YahooHttpClient.DecodeException
import sttp.client3.*
import sun.nio.cs.{StandardCharsets, UTF_8}
import zio.*
import zio.json.*

import java.net.URLEncoder
import java.nio.charset.Charset

class YahooHttpClient(
    sttp: SttpBackend[Task, Any],
    semaphore: Semaphore
):
  def historyRequest(
    symbol: StockSymbol,
    from: Timestamp,
    to: Timestamp,
    interval: Interval = Interval.Week,
  ): Task[HistoryResponse] =
    for
      symbol <- ZIO.succeed(
        URLEncoder.encode(symbol, Charset.defaultCharset())
      )
      params = Map(
        "interval" -> interval.code,
        "period1" -> from.getEpochSecond,
        "period2" -> to.getEpochSecond,
        "events" -> "div%7Csplit"
      ).map((k, v) => s"$k=$v").mkString("&")
      url = s"${YahooHttpClient.Url}/$symbol?$params"
      request = basicRequest.response(asStringAlways).get(uri"$url")
      response <- semaphore.withPermit(sttp.send(request))
      _ <- ZIO.debug("Response json: " + response.body)
      chart <- ZIO
        .fromEither(response.body.fromJson[HistoryResponse])
        .mapError(DecodeException(response.body, _))
      _ <- ZIO.debug("Response object: " + chart)
    yield chart

object YahooHttpClient:
  private val Url = "https://query1.finance.yahoo.com/v8/finance/chart"

  case class DecodeException(body: String, error: String) extends RuntimeException(error)

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
