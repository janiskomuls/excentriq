import sbt._

object Dependencies {

  val ExcentriqRoot: Seq[ModuleID] = ZIO.Deps
  val StocksDependencies: Seq[ModuleID] = ZIO.Deps

  object ZIO {
    val Version = "2.0.2"
    val Http: Seq[ModuleID] = Seq(
      "io.d11" %% "zhttp" % "2.0.0-RC11"
    )
    val Deps: Seq[ModuleID] = Seq(
      "dev.zio" %% "zio" % Version,
      "dev.zio" %% "zio-streams" % Version,
      "dev.zio" %% "zio-test" % Version,
      "dev.zio" %% "zio-test" % Version % Test,
      "dev.zio" %% "zio-test-sbt" % Version % Test
    ) ++ Http
  }
}
