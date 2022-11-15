import sbt._

object Dependencies {

  val ExcentriqRoot: Seq[ModuleID] = ZIO() ++ Sttp()
  val StocksDependencies: Seq[ModuleID] = ZIO() ++ Sttp()

  object Sttp extends Deps {
    val deps: Seq[ModuleID] = Seq(
      "com.softwaremill.sttp.client3" %% "zio" % "3.8.3"
    )
  }

  object ZIO extends Deps {
    val Version = "2.0.2"
    val deps: Seq[ModuleID] = Seq(
      "dev.zio" %% "zio" % Version,
      "dev.zio" %% "zio-streams" % Version,
      "dev.zio" %% "zio-cache" % Version,
      "dev.zio" %% "zio-test" % Version,
      "dev.zio" %% "zio-test" % Version % Test,
      "dev.zio" %% "zio-test-sbt" % Version % Test
    )
  }

  trait Deps {
    def apply(): Seq[ModuleID] = deps
    val deps: Seq[ModuleID]
  }
}
