name := "iterator"

version := "1.3.0-SNAPSHOT"

addScala212

deps ++= Seq(
  cats,
  commons_math,
  shapeless,
  spire
)

testDeps += kryo

testUtilsVersion := "1.2.4-SNAPSHOT"
