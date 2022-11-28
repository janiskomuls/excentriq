package com.excentriq.stocks

import java.time.Instant
import scala.concurrent.duration.FiniteDuration
import scala.math.BigDecimal.RoundingMode

package object domain:
  opaque type StockSymbol <: String = String
  opaque type Percentage <: BigDecimal = BigDecimal
  opaque type Amount <: BigDecimal = BigDecimal
  opaque type Timestamp <: Instant = Instant

  extension (value: String) def toSymbol: StockSymbol = value

  extension (value: BigDecimal)
    def scaled(scale: Int) = value.setScale(scale, RoundingMode.HALF_UP)
    def toPercentage: Percentage = value
    def toAmount: Amount = value
    def isNegative: Boolean = value < 0

  extension (value: Int) def toAmount: Amount = BigDecimal(value)

  extension (value: Instant) def toTimestamp: Timestamp = value
