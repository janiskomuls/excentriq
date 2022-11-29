package com.excentriq.stocks.dividends

import com.excentriq.stocks.*
import com.excentriq.stocks.dividends.*
import com.excentriq.stocks.domain.{StockSymbol, Timestamp, *}
import com.excentriq.stocks.yahoo.HistoryResponse.*
import com.excentriq.stocks.yahoo.{HistoryResponse, YahooHttpClient}
import zio.*

class FetchDividendsIncrease(client: YahooHttpClient):
  def apply(symbol: StockSymbol)(
    from: Timestamp,
    till: Timestamp
  ): Task[Option[DividendIncrease]] =
    client
      .historyRequest(symbol.yhSymbol, from.yhTimestamp, till.yhTimestamp)
      .map(toDividendIncrease(symbol, _)(from, till))

  def list(symbols: List[StockSymbol])(
    from: Timestamp,
    till: Timestamp
  ): Task[List[DividendIncrease]] =
    val values = symbols.map { s =>
      apply(s)(from, till).map(_.toList)
    }
    ZIO.collectAll(values).map(_.flatten)

  private def toDividendIncrease(symbol: StockSymbol, history: HistoryResponse)(
    from: Timestamp,
    till: Timestamp
  ): Option[DividendIncrease] =
    val dividends = history.chart.result.flatMap(_.events.dividendsList)

    (for
      fromAmount <- dividends.minByOption(_.exDividendDate).map(_.amount)
      tillAmount <- dividends.maxByOption(_.exDividendDate).map(_.amount)
    yield DividendIncrease.from(
      symbol.toSymbol,
      fromAmount.toAmount,
      tillAmount.toAmount,
      from.toTimestamp,
      till.toTimestamp)
      ).flatten


object FetchDividendsIncrease:

  val live: ZLayer[YahooHttpClient, Nothing, FetchDividendsIncrease] =
    ZLayer.fromFunction(FetchDividendsIncrease(_))
