package com.excentriq

package object stocks:
  case class StockTicker(value: String) extends AnyVal

  // tmp, intellij goes bananas if only 1 class in package object
  case class StockTicker2(value: String) extends AnyVal
