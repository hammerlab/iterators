
default(
  `2.12`.version := "2.12.4"  // required for scalameta macros
)

lazy val core =
  crossProject
    .settings(
      name := "iterator",
      v"2.2.0",
      // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
      emptyDocJar,
      scalameta,
      dep(
              cats,
        math.utils % "2.2.0",
             spire,
             types % "1.2.0"
      ),
      buildInfoKeys :=
        Seq[BuildInfoKey](
          name,
          version,
          scalaVersion,
          sbtVersion
        ),
      buildInfoPackage := "build_info",
      buildInfoObject := name.value
    )
    .dependsOn(
      macros
    )
    .enablePlugins(
      BuildInfoPlugin
    )
lazy val `core.js`  = core.js
lazy val `core.jvm` = core.jvm
lazy val `core-x`   = parent(`core.js`, `core.jvm`)

lazy val macros =
  crossProject
    .settings(
      subgroup("macros", "iterators"),
      r"1.0.0",
      scalameta,
      enableMacroParadise
    )
lazy val `macros.js`  = macros.js
lazy val `macros.jvm` = macros.jvm
lazy val `macros-x`   = parent(`macros.js`, `macros.jvm`)

lazy val iterators =
  root(
      `core-x`,
    `macros-x`
  )
