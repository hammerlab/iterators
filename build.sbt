name := "iterator"

version := "1.2.2"

addScala212

deps ++= Seq(
  libs.value('commons_math),
  kryo.value,
  "com.chuusai" %% "shapeless" % "2.3.2",
  libs.value('spire)
)
