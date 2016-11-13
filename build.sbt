name := "iterator"
version := "1.0.0"
libraryDependencies <++= libraries { v => Seq(
  v('spire),
  "org.apache.commons" % "commons-math3" % "3.6.1",
  "com.esotericsoftware.kryo" % "kryo" % "2.21"
)}
