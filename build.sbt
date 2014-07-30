libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "net.databinder" %% "unfiltered-netty-server" % "0.7.1",
  "org.json4s"   %% "json4s-native"  % "3.2.6",
  "org.json4s"   %% "json4s-jackson" % "3.2.6",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "com.typesafe.slick" %% "slick" % "2.0.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)

name := "big rest"

version := "0.1"

scalaVersion := "2.10.4"
