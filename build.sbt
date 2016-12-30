name := "iterator"

version := "1.1.1"

deps ++= Seq(
  libs.value('commons_math),
  libs.value('kryo),
  libs.value('spire)
)
