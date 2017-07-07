name := "iterator"

version := "1.3.0-SNAPSHOT"

addScala212

deps ++= Seq(
  libs.value('commons_math),
  "com.chuusai" %% "shapeless" % "2.3.2",
  libs.value('spire)
)

testUtilsVersion := "1.2.4-SNAPSHOT"

testDeps += kryo.value
