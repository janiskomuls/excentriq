package com.excentriq.stocks.dividends

import com.excentriq.stocks.*
import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.domain.StockTicker
import com.excentriq.stocks.yahoo.*
import com.excentriq.stocks.yahoo.ChartResponse.*
import zio.*

import java.time.Instant

class FetchDividendsHistory(client: YahooHttpClient):
  def apply(ticker: StockTicker)(
      from: Instant,
      to: Instant
  ): Task[List[DividendsHistory]] =
    client.request(ticker.toTicker, from.toTimestamp, to.toTimestamp, Interval.Week).map { _ =>
      List.empty[DividendsHistory]
    }

object FetchDividendsHistory:

  val live: ZLayer[YahooHttpClient, Nothing, FetchDividendsHistory] =
    ZLayer.fromFunction(FetchDividendsHistory(_))
