import sbt.Keys._
import sbt.Package.ManifestAttributes
import sbt._

object ThisBuildSettings {
  val Name = "Excentriq"
  val NormalizedName = "live-extrachilli"
  val ScalaVersion = "3.2.1"
  val OrganizationPackage = "com.excentriq"

  def apply(): Seq[Setting[_]] = instance

  private val instance: Seq[Setting[_]] = inThisBuild(
    Seq(
      scalaVersion := ScalaVersion,
      organization := OrganizationPackage,
      description := Name,
      startYear := Some(2022),
      shellPrompt := { s => s"${Project.extract(s).currentProject.id} > " },
      run / fork := true,
      Compile / packageDoc / publishArtifact := false,
      Compile / doc / sources := Seq.empty,
      Test / publishArtifact := false,
      packageOptions := Seq(
        ManifestAttributes(
          ("Implementation-Version", (ThisProject / version).value)
        )
      ),
      excludeDependencies ++= Seq.empty,
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
      Compile / scalacOptions := Seq(
        "UTF-8",
        "-language:higherKinds",
        "-language:existentials",
        "-deprecation",
        "-explain",
        "-explaintypes",
        "-feature",
        "-unchecked",
      )
    )
  )
}
