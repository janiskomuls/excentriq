package com.excentriq.stocks.dividends

import com.excentriq.stocks.*
import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.domain.{StockSymbol, Timestamp, *}
import com.excentriq.stocks.yahoo.HistoryResponse.*
import com.excentriq.stocks.yahoo.YahooHttpClient
import zio.*

class FetchDividendsIncrease(client: YahooHttpClient):
  def apply(symbol: StockSymbol)(
    from: Timestamp,
    till: Timestamp
  ): Task[Option[DividendIncrease]] =
    client.historyRequest(symbol.yhSymbol, from.yhTimestamp, till.yhTimestamp).map { history =>
      val dividends = history.chart.result.flatMap(_.events.dividendsList)

      val increase = (for
        fromAmount <- dividends.minByOption(_.exDividendDate).map(_.amount)
        tillAmount <- dividends.maxByOption(_.exDividendDate).map(_.amount)
      yield DividendIncrease.from(
        symbol.toSymbol,
        fromAmount.toAmount,
        tillAmount.toAmount,
        from.toTimestamp,
        till.toTimestamp)
        ).flatten

      increase
    }

object FetchDividendsIncrease:

  val live: ZLayer[YahooHttpClient, Nothing, FetchDividendsIncrease] =
    ZLayer.fromFunction(FetchDividendsIncrease(_))
