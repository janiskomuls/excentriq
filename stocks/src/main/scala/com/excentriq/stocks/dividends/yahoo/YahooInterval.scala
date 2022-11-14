package com.excentriq.stocks.dividends.yahoo

enum YahooInterval(val value: String):
  case Hour extends YahooInterval("1h")
  case Day extends YahooInterval("1d")
  case FiveDays extends YahooInterval("5d")
  case Week extends YahooInterval("1wk")
  case Month extends YahooInterval("1m")
  case TwoMonths extends YahooInterval("2m")
  case ThreeMonths extends YahooInterval("3m")
  case FiveMonths extends YahooInterval("5m")
  case FifteenMonths extends YahooInterval("15m")
  case ThirtyMonths extends YahooInterval("30m")
  case SixtyMonths extends YahooInterval("60m")
  case NinetyMonths extends YahooInterval("90m")
