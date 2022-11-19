package com.excentriq.stocks

import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.domain.*
import zio.*

import java.time.temporal.TemporalAmount
import java.time.{Instant, ZoneOffset}

class Application(fetchDividendsHistory: FetchDividendsHistory):
  def run(): Task[Unit] = ???

object Application:
  val live: ZLayer[FetchDividendsHistory, Nothing, Application] =
    ZLayer.fromFunction(Application(_))

  def testRun(
      ticker: String,
      years: Int
  ): ZLayer[FetchDividendsHistory, Nothing, Application] =
    ZLayer {
      for
        to0 <- Clock.localDateTime
        from0 = to0.minusYears(years)
        fetchDividendsHistory <- ZIO.service[FetchDividendsHistory]
        (from, to) = (
          from0.toInstant(ZoneOffset.UTC),
          to0.toInstant(ZoneOffset.UTC)
        )
      yield new Application(fetchDividendsHistory) {
        override def run(): Task[Unit] =
          fetchDividendsHistory(ticker.toTicker)(from, to).flatMap(v =>
            ZIO.debug(v)
          )
      }
    }
