name := "PackageManager"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.5.2"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.102-R11"
libraryDependencies += "org.springframework.data" % "spring-data-jpa" % "1.8.1.RELEASE"
libraryDependencies += "org.scalafx" %% "scalafxml-core-sfx8" % "0.4"
libraryDependencies += "com.zsoltfabok" % "sqlite-dialect" % "1.0"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.19.3"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
