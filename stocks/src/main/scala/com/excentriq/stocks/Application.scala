package com.excentriq.stocks

import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.domain.*
import zio.*

import java.time.temporal.TemporalAmount
import java.time.{Instant, ZoneOffset}

class Application(fetchDividendsIncrease: FetchDividendsIncrease):
  def run(): Task[Unit] = ???

object Application:
  val live: ZLayer[FetchDividendsIncrease, Nothing, Application] =
    ZLayer.fromFunction(Application(_))

  def testRun(
    ticker: String,
    years: Int
  ): ZLayer[FetchDividendsIncrease, Nothing, Application] =
    ZLayer {
      for
        to0 <- Clock.localDateTime
        from0 = to0.minusYears(years)
        fetchDividendsHistory <- ZIO.service[FetchDividendsIncrease]
        (from, to) = (
          from0.toInstant(ZoneOffset.UTC),
          to0.toInstant(ZoneOffset.UTC)
        )
      yield new Application(fetchDividendsHistory) {
        override def run(): Task[Unit] =
          fetchDividendsHistory(ticker.toSymbol)(from.toTimestamp, to.toTimestamp).flatMap(v => ZIO.debug(v))
      }
    }
