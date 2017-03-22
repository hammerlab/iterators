name := "iterator"

version := "1.2.1"

addScala212

deps ++= Seq(
  libs.value('commons_math),
  libs.value('kryo),
  libs.value('spire)
)
