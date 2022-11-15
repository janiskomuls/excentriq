package com.excentriq.stocks

import com.excentriq.stocks.dividends.FetchDividendsHistory
import zio.*

import java.time.temporal.TemporalAmount
import java.time.{Instant, ZoneOffset}

class App(fetchDividendsHistory: FetchDividendsHistory):
  def run(): Task[Unit] = ???

object App:
  val live: ZLayer[FetchDividendsHistory, Nothing, App] =
    ZLayer.fromFunction(App(_))

  def testRun(
      ticker: String,
      years: Int
  ): ZLayer[FetchDividendsHistory, Nothing, App] =
    ZLayer {
      for
        to0 <- Clock.localDateTime
        from0 = to0.minusYears(years)
        fetchDividendsHistory <- ZIO.service[FetchDividendsHistory]
        (from, to) = (
          from0.toInstant(ZoneOffset.UTC),
          to0.toInstant(ZoneOffset.UTC)
        )
      yield new App(fetchDividendsHistory) {
        override def run(): Task[Unit] =
          fetchDividendsHistory(StockTicker(ticker))(from, to).flatMap(v =>
            ZIO.debug(v)
          )
      }
    }
