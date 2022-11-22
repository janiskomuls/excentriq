package com.excentriq.stocks.yahoo

import zio.json.*
import zio.json.ast.Json
import zio.json.internal.RetractReader

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.util.Try

/**
 * Try to decode long values as epoh seconds or millis, otherwise try to parse string date time
 */
object InstantDecoder extends JsonDecoder[Instant]:
  def unsafeDecode(trace: List[JsonError], in: RetractReader): Instant =
    Try(JsonDecoder.long.unsafeDecode(trace, in))
      .flatMap(num => Try(instantFromNum(num)))
      .getOrElse(JsonDecoder.instant.unsafeDecode(trace, in))

  override final def fromJsonAST(json: Json): Either[String, Instant] =
    json match {
      case Json.Num(value) => Try(instantFromNum(value.longValue)).toEither.left.map(_.getMessage)
      case Json.Str(value) => JsonDecoder.instant.decodeJson(value)
      case _               => Left("Not a timestamp value")
    }

  private def instantFromNum(millisOrSeconds: Long): Instant =
    val fromSeconds = Instant.ofEpochSecond(millisOrSeconds)
    if (fromSeconds.truncatedTo(ChronoUnit.SECONDS).equals(fromSeconds))
      fromSeconds
    else Instant.ofEpochMilli(millisOrSeconds)
