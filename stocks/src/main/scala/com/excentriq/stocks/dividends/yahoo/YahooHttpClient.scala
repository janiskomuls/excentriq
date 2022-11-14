package com.excentriq.stocks.dividends.yahoo

import com.excentriq.stocks.StockTicker
import com.excentriq.stocks.dividends.DividendsHistory
import sttp.client3.*
import sttp.client3.httpclient.zio.*
import sun.nio.cs.{StandardCharsets, UTF_8}
import zio.*

import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.Instant

class YahooHttpClient(
    sttp: SttpBackend[Task, Any],
    semaphore: YahooHttpClientSemaphore
):
  def request(
      ticker: StockTicker,
      from: Instant,
      to: Instant,
      interval: YahooInterval
  ): Task[List[DividendsHistory]] =
    for
      ticker <- ZIO.succeed(
        URLEncoder.encode(ticker.value, Charset.defaultCharset())
      )
      params = Map(
        "interval" -> interval.value,
        "period1" -> from.toEpochMilli,
        "period2" -> to.toEpochMilli,
        "events" -> "div%7Csplit"
      ).map((k, v) => s"$k=$v").mkString("&")
      url = s"${YahooHttpClient.Url}/$ticker?$params"
      request = basicRequest.response(asStringAlways).get(uri"$url")
      res <- semaphore.withPermit(sttp.send(request))
    yield
      println(res.body)
      List.empty

object YahooHttpClient:

  private[yahoo] val Url = "https://query1.finance.yahoo.com/v8/finance/chart"

  val live: URLayer[
    SttpBackend[Task, Any] & YahooHttpClientSemaphore,
    YahooHttpClient
  ] = ZLayer.fromFunction(YahooHttpClient(_, _))

class YahooHttpClientSemaphore(private val underlying: Semaphore):
  def withPermit[R, E, A](zio: ZIO[R, E, A])(implicit
      trace: Trace
  ): ZIO[R, E, A] = underlying.withPermit(zio)

object YahooHttpClientSemaphore:
  val default: ULayer[YahooHttpClientSemaphore] =
    ZLayer(
      ZIO
        .succeed(java.lang.Runtime.getRuntime.availableProcessors.toLong)
        .flatMap(Semaphore.make(_))
        .map(YahooHttpClientSemaphore(_))
    )
