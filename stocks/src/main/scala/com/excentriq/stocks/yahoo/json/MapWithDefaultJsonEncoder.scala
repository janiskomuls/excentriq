package com.excentriq.stocks.yahoo.json

import zio.json.*

// for JsonDecoder.map default values set in case class does not work, so this hack
object MapWithDefaultJsonEncoder:
  def gen[K: JsonFieldDecoder, V: JsonDecoder]: JsonDecoder[Map[K, V]] =
    implicit val mapDecoder: JsonDecoder[Map[K, V]] = JsonDecoder.map[K, V]
    JsonDecoder.option[Map[K, V]].map(_.getOrElse(Map.empty))
