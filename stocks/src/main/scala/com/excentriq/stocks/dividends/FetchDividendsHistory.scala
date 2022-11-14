package com.excentriq.stocks.dividends

import com.excentriq.stocks.*
import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.dividends.yahoo.YahooHttpClient
import com.excentriq.stocks.dividends.yahoo.YahooInterval.ThreeMonths
import zio.*

import java.time.Instant
case class Config(desiredParallelism: Int)

object Config:
  val default: ZLayer[Any, Throwable, Config] = ZLayer.fromZIO {
    ZIO
      .succeed(java.lang.Runtime.getRuntime.availableProcessors)
      .map(Config.apply)
  }

class FetchDividendsHistory(client: YahooHttpClient, semaphore: Semaphore):
  def apply(ticker: StockTicker)(
      from: Instant,
      to: Instant
  ): Task[List[DividendsHistory]] =
    semaphore.withPermit {
      client.request(ticker, from, to, ThreeMonths)
    }

object FetchDividendsHistory:

  val live: ZLayer[YahooHttpClient & Config, Nothing, FetchDividendsHistory] =
    ZLayer {
      for
        config <- ZIO.service[Config]
        client <- ZIO.service[YahooHttpClient]
        semaphore <- Semaphore.make(config.desiredParallelism)
      yield FetchDividendsHistory(client, semaphore)
    }
