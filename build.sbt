
default(
  `2.12`.version := "2.12.4"  // required for scalameta macros
)

lazy val core =
  cross
    .settings(
      name := "iterator",
      v"2.3.0",
      // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
      emptyDocJar,
      scalameta,
      dep(
              cats,
        math.utils % "2.3.0",
             spire,
             types % "1.4.0"
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
lazy val `core-x` = core.x

lazy val macros =
  cross
    .settings(
      subgroup("macros", "iterators"),
      r"1.0.0",
      scalameta,
      enableMacroParadise
    )
lazy val `macros-x` = macros.x

lazy val iterators =
  root(
      `core-x`,
    `macros-x`
  )
