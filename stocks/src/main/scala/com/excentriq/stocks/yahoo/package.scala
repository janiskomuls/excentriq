package com.excentriq.stocks

import zio.json.JsonDecoder

package object yahoo:
  enum Interval(val code: String):
    case Hour extends Interval("1h")
    case Day extends Interval("1d")
    case FiveDays extends Interval("5d")
    case Week extends Interval("1wk")
    case Empty extends Interval("")

  extension (obj: Interval.type)
    def byCode(code: String): Either[String, Interval] =
      obj.values
        .find(_.code == code)
        .map(Right.apply)
        .getOrElse(Left(s"No ${Interval.getClass.getSimpleName} found by code $code"))

  enum Range(val code: String):
    case Day extends Range("1d")
    case FiveDays extends Range("5d")
    case Month extends Range("1mo")
    case TwoMonths extends Range("3mo")
    case FiveMonths extends Range("6mo")
    case Year extends Range("1y")
    case TwoYears extends Range("2y")
    case FiveYears extends Range("5y")
    case TenYears extends Range("10y")
    case YearToDate extends Range("ytd")
    case Max extends Range("max")
    case Empty extends Range("")

  extension (obj: Range.type)
    def byCode(code: String): Either[String, Range] =
      obj.values
        .find(_.code == code)
        .map(Right.apply)
        .getOrElse(Left(s"No ${Range.getClass.getSimpleName} found by code $code"))

  implicit val intervalDecoder: JsonDecoder[Interval] = JsonDecoder.string.mapOrFail(Interval.byCode)
  implicit val rangeDecoder: JsonDecoder[Range] = JsonDecoder.string.mapOrFail(Range.byCode)
