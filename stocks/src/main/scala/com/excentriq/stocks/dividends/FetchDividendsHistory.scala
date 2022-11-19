package com.excentriq.stocks.dividends

import com.excentriq.stocks.*
import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.dividends.yahoo.*
import com.excentriq.stocks.dividends.yahoo.ChartResponse.*
import com.excentriq.stocks.domain.StockTicker
import zio.*

import java.time.Instant

class FetchDividendsHistory(client: YahooHttpClient):
  def apply(ticker: StockTicker)(
      from: Instant,
      to: Instant
  ): Task[List[DividendsHistory]] =
    client.request(ticker.toTicker, from, to, YahooInterval.Week)

object FetchDividendsHistory:

  val live: ZLayer[YahooHttpClient, Nothing, FetchDividendsHistory] =
    ZLayer.fromFunction(FetchDividendsHistory(_))
