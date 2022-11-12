package com.excentriq.stocks

import zhttp.http.*
import zio.*

import java.time.Instant

case class DividendsHistory()
case class StockTicker(value: String) extends AnyVal

object Ext {
  type OurHttp = Http[Any, Nothing, Request, Response]
}

import Ext._

trait Config {
  def desiredParallelism: Int
}

object Config:
  final case class ConfigLive(desiredParallelism: Int) extends Config

  val default: ZLayer[Any, Nothing, Config] =
    ZLayer.succeed(ConfigLive(42))

trait FetchDividendsHistory:
  def apply(
      ticker: StockTicker
  )(from: Instant, to: Instant): Task[List[DividendsHistory]]

object FetchDividendsHistory:

  def apply(ticker: StockTicker)(
      from: Instant,
      to: Instant
  ): RIO[FetchDividendsHistory, List[DividendsHistory]] =
    ZIO.serviceWithZIO(_(ticker)(from, to))

  final case class FetchDividendsHistoryLive(
      http: OurHttp,
      semaphore: Semaphore
  ) extends FetchDividendsHistory {
    def apply(ticker: StockTicker)(
        from: Instant,
        to: Instant
    ): Task[List[DividendsHistory]] = {
      semaphore.withPermit {
        ???
      }
    }
  }

  val live: ZLayer[OurHttp & Config, Nothing, FetchDividendsHistory] =
    ZLayer {
      for
        config <- ZIO.service[Config]
        http <- ZIO.service[OurHttp]
        semaphore <- Semaphore.make(config.desiredParallelism)
      yield FetchDividendsHistoryLive(http, semaphore)
    }
