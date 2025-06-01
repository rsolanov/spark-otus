ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

lazy val root = (project in file("."))
  .settings(
    name := "scala",
    javaOptions ++= Seq("-Xms1G", "-Xmx2G")
  )
  .settings(name := "RDDAPI")
  .settings(assembly / assemblyJarName := "RDDAPI.jar")

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.5" % "provided",
  "org.apache.spark" %% "spark-sql" % "3.5.5" % "provided"
)