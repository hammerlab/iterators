name := "iterator"

version := "2.0.0-SNAPSHOT"

addScala212

deps ++= Seq(
  cats,
  spire
)

testDeps += monoids % "1.0.0-SNAPSHOT"
