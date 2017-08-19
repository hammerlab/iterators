name := "iterator"

version := "1.4.0-SNAPSHOT"

addScala212

deps ++=
  Seq(
    cats,
    shapeless,
    spire
  )
