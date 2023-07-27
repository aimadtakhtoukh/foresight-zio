ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "fr.iai.foresight"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "foresight-zio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.15",
      "dev.zio" %% "zio-json" % "0.6.0",
      "dev.zio" %% "zio-http" % "3.0.0-RC2",
      "io.getquill" %% "quill-zio" % "4.6.0.1",
      "io.getquill" %% "quill-jdbc-zio" % "4.6.0.1",
      "org.postgresql" % "postgresql" % "42.5.4",
      "org.slf4j" % "slf4j-nop" % "2.0.7",
      "dev.zio" %% "zio-test" % "2.0.15" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
