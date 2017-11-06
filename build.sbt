name := "iterator"

addScala212

lazy val core = project.settings(
  name := "iterator",
  version := "2.0.0",
  scalameta,
  skipDoc,  // docs-JAR packaging crashes due to macro-expansions
  deps ++= Seq(
    cats,
    spire,
    types % "1.0.1"
  )
).dependsOn(macros)

lazy val macros = project.settings(
  name := "iterator-macros",
  version := "1.0.0",
  scalameta
)

lazy val root = rootProject("iterators-root", core, macros)
