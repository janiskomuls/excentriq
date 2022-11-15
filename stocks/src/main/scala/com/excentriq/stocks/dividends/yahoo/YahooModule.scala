package com.excentriq.stocks.dividends.yahoo

import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3.*
import sttp.client3.httpclient.zio.*
import zio.*

object YahooModule {
  val live: ZLayer[SttpBackend[Task, Any], Throwable, YahooHttpClient] =
    ZLayer.service[SttpBackend[Task, Any]].flatMap { sttp =>
      ZLayer.make[YahooHttpClient](
        YahooConfig.default,
        YahooHttpClient.live,
        // can't provide ZLayer.service[SttpBackend[Task, Any]] directly, getting cycle error
        ZLayer.succeed(sttp.get)
      )
    }
}
