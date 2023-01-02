package com.excentriq.stocks.dividends

import com.excentriq.stocks.dividends.DividendIncrease.*
import com.excentriq.stocks.domain.*

import java.time.*
import java.time.temporal.ChronoUnit.*
import scala.concurrent.duration.*

case class DividendIncrease private(
  symbol: StockSymbol,
  amountFrom: Amount,
  amountTill: Amount,
  from: Timestamp,
  till: Timestamp,
  years: Long,
  overallIncrease: Percentage,
  annualizedIncrease: Percentage,
)

object DividendIncrease:
  private val PercentageScale: Int = 4

  def from(
    symbol: StockSymbol,
    amountFrom: Amount,
    amountTill: Amount,
    from: Timestamp,
    till: Timestamp,
  ): Option[DividendIncrease] =
    Option.when(amountFrom.isPositive && amountTill.isPositive && !till.isBefore(from)) {
      val years: Long = YEARS.between(
        LocalDate.ofInstant(from, ZoneOffset.UTC),
        LocalDate.ofInstant(till, ZoneOffset.UTC),
      )
      val increaseYears = years - 1 // since amountFrom is for 1st year, increase is for 1 year less

      val overallIncrease: Percentage = ((amountTill - amountFrom) / amountFrom).toPercentage(PercentageScale)

      val annualizedIncrease: Percentage =
        if (increaseYears.isPositive)
          (Math.pow((1 + overallIncrease).doubleValue, 1 / increaseYears.toDouble) - 1).toPercentage(PercentageScale)
        else overallIncrease

      DividendIncrease(symbol, amountFrom, amountTill, from, till, years, overallIncrease, annualizedIncrease)
    }
