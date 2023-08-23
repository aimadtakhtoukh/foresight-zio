ThisBuild / scalaVersion     := "3.3.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "fr.iai.foresight"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "foresight-zio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.18",
      "dev.zio" %% "zio-json" % "0.7.43",
      "dev.zio" %% "zio-http" % "3.3.0",
      "io.getquill" %% "quill-zio" % "4.8.6",
      "io.getquill" %% "quill-jdbc-zio" % "4.8.6",
      "org.postgresql" % "postgresql" % "42.7.5",
      "org.slf4j" % "slf4j-nop" % "2.0.17",
      "dev.zio" %% "zio-test" % "2.1.18" % Test,
      "com.github.jwt-scala" %% "jwt-core" % "10.0.4"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
