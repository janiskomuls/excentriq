package com.excentriq.stocks.dividends

import com.excentriq.stocks.dividends.yahoo.*

package yahoo:
  enum YahooInterval(val value: String):
    case Hour extends YahooInterval("1h")
    case Day extends YahooInterval("1d")
    case FiveDays extends YahooInterval("5d")
    case Week extends YahooInterval("1wk")

  enum YahooRange(val value: String):
    case Hour extends YahooRange("1h")
    case Day extends YahooRange("1d")
    case FiveDays extends YahooRange("5d")
    case Week extends YahooRange("1wk")
    case Month extends YahooRange("1m")
    case TwoMonths extends YahooRange("2m")
    case FiveMonths extends YahooRange("5m")
    case FifteenMonths extends YahooRange("15m")
    case ThirtyMonths extends YahooRange("30m")
    case SixtyMonths extends YahooRange("60m")
    case NinetyMonths extends YahooRange("90m")
