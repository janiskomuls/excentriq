package com.excentriq.stocks

import com.excentriq.stocks.dividends.FetchDividendsIncrease
import com.excentriq.stocks.yahoo.YahooModule
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
      Application.testRun(List("AAPL", "SWK"), years = 1),
      FetchDividendsIncrease.live,
      YahooModule.live,
      HttpClientZioBackend.layer()
    )
