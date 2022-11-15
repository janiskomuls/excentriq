package com.excentriq.stocks.dividends

import com.excentriq.stocks.*
import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.dividends.yahoo.YahooHttpClient
import com.excentriq.stocks.dividends.yahoo.YahooInterval.ThreeMonths
import zio.*

import java.time.Instant

class FetchDividendsHistory(client: YahooHttpClient):
  def apply(ticker: StockTicker)(
      from: Instant,
      to: Instant
  ): Task[List[DividendsHistory]] =
    client.request(ticker, from, to, ThreeMonths)

object FetchDividendsHistory:

  val live: ZLayer[YahooHttpClient, Nothing, FetchDividendsHistory] =
    ZLayer.fromFunction(FetchDividendsHistory(_))
