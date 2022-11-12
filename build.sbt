val ZIOVersion = "2.0.2"

val ScalaVersion = "3.2.0"

lazy val excentriq = project
  .in(file("."))
  .settings(
    name := "Excentriq",
    ThisBuildSettings(),
    libraryDependencies ++= Dependencies.ExcentriqRoot
  )
  .aggregate(stocks)

lazy val stocks = project
  .in(file("stocks"))
  .settings(
    name := "Stocks",
    ThisBuildSettings(),
    libraryDependencies ++= Dependencies.StocksDependencies
  )
