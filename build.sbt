name := "iterator"

version := "1.1.2-SNAPSHOT"

deps ++= Seq(
  libs.value('commons_math),
  libs.value('kryo),
  libs.value('spire)
)
