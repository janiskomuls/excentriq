package com.excentriq.stocks.dividends

import com.excentriq.stocks.dividends.DividendIncrease.from
import com.excentriq.stocks.domain.*

import java.time.Instant
import scala.concurrent.duration.*

case class DividendIncrease(
  symbol: StockSymbol,
  increase: Percentage,
  from: Timestamp,
  till: Timestamp
)

object DividendIncrease:
  val PercentageScale: Int = 4

  def from(
    symbol: StockSymbol,
    amountFrom: Amount,
    amountTill: Amount,
    from: Timestamp,
    till: Timestamp
  ): Option[DividendIncrease] =
    Option.when(!amountFrom.isNegative && !amountTill.isNegative && !till.isBefore(from)) {
      val amountFrom_ = if (amountFrom == 0) amountTill else amountFrom
      val increase = (amountTill - amountFrom_) / amountFrom_
      DividendIncrease(symbol, increase.scaled(PercentageScale).toPercentage, from, till)
    }
