package com.excentriq.stocks.dividends.yahoo

import zio.{ZIO, ZLayer}

case class YahooConfig(desiredParallelism: Int)

object YahooConfig:
  val default: ZLayer[Any, Throwable, YahooConfig] = ZLayer.fromZIO {
    ZIO // TODO: get from config getOrElse availableProcessors?
      .succeed(java.lang.Runtime.getRuntime.availableProcessors)
      .map(YahooConfig.apply)
  }
