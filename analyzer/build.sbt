name := "hackthon-analyzer"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "oss" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.7" % "test"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.3"

libraryDependencies += "com.typesafe.slick" %% "slick" % "2.1.0-M2"

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.7.2"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.9"

libraryDependencies += "org.json4s" % "json4s-ext_2.10" % "3.2.9"

libraryDependencies += "io.spray" % "spray-can" % "1.3.1"

libraryDependencies += "io.spray" % "spray-routing" % "1.3.1"

libraryDependencies += "io.spray" % "spray-client" % "1.3.1" % "test"

libraryDependencies += "io.spray" % "spray-json_2.10" % "1.2.6"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.31"

libraryDependencies += "com.gettyimages" %% "spray-swagger" % "0.4.3"

libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % "0.0.3.2-SNAPSHOT"

libraryDependencies += "org.deeplearning4j" % "deeplearning4j-scaleout-akka" % "0.0.3.2-SNAPSHOT"

libraryDependencies += "org.deeplearning4j" % "deeplearning4j-scaleout-akka-word2vec" % "0.0.3.2-SNAPSHOT"

libraryDependencies += "org.cleartk" % "cleartk-opennlp-tools" % "2.0.0"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.3"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.4"

libraryDependencies += "com.google.guava" % "guava" % "17.0"

