lazy val core = project.settings(
  name := "iterator",
  v"2.1.0",
  scalameta,
  // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
  emptyDocJar,
  dep(
    cats,
    spire,
    types % "1.0.1"
  )
).dependsOn(macros)

lazy val macros = project.settings(
  name := "iterator-macros",
  r"1.1.0",
  scalameta
)

addScala212

lazy val root = rootProject("iterators", core, macros)
