package com.excentriq.stocks.dividends

import com.excentriq.stocks.dividends.yahoo.*

package yahoo:
  enum YahooInterval(val value: String):
    case Hour extends YahooInterval("1h")
    case Day extends YahooInterval("1d")
    case FiveDays extends YahooInterval("5d")
    case Week extends YahooInterval("1wk")
    case Empty extends YahooInterval("")

  enum YahooRange(val value: String):
    case Day extends YahooRange("1d")
    case FiveDays extends YahooRange("5d")
    case Month extends YahooRange("1mo")
    case TwoMonths extends YahooRange("3mo")
    case FiveMonths extends YahooRange("6mo")
    case Year extends YahooRange("1y")
    case TwoYears extends YahooRange("2y")
    case FiveYears extends YahooRange("5y")
    case TenYears extends YahooRange("10y")
    case YearToDate extends YahooRange("ytd")
    case Max extends YahooRange("max")
    case Empty extends YahooRange("")
