name := "iterator"

addScala212

lazy val core = project.settings(
  name := "iterator",
  version := "2.1.0-SNAPSHOT",
  scalameta,
  // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
  sources in (Compile, doc) := Seq(),
  deps ++= Seq(
    cats,
    spire,
    types % "1.0.1"
  )
).dependsOn(macros)

lazy val macros = project.settings(
  name := "iterator-macros",
  version := "1.1.0-SNAPSHOT",
  scalameta
)

lazy val root = rootProject("iterators-root", core, macros)
