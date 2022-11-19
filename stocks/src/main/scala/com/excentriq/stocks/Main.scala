package com.excentriq.stocks

import com.excentriq.stocks.dividends.FetchDividendsHistory
import com.excentriq.stocks.dividends.yahoo.*
import sttp.client3.*
import sttp.client3.httpclient.zio.*
import zio.*

import java.time.temporal.TemporalAmount
import java.time.{Instant, ZoneOffset}

object Main extends ZIOAppDefault:

  private val app = testApp

  val run: ZIO[Any, Throwable, Unit] =
    app.build(Scope.global).flatMap(_.get.run())

  private def testApp: RLayer[Any, Application] =
    ZLayer.make[Application](
      Application.testRun("googl", years = 1),
      FetchDividendsHistory.live,
      YahooModule.live,
      HttpClientZioBackend.layer()
    )
