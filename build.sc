import $meta._

import mill._, scalalib._

import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import $ivy.`com.mchange::untemplate-mill:0.1.2`
import untemplate.mill._

object feedletter extends RootModule with UntemplateModule {
  def scalaVersion = "3.3.1"

  override def scalacOptions = T{ Seq("-deprecation") }

  def ivyDeps = Agg(
    ivy"com.mchange::feedletter:0.0.4"
  )

  // we'll build an index!
  override def untemplateIndexNameFullyQualified : Option[String] = Some("com.mchange.feedletter.UserUntemplates")

  override def untemplateSelectCustomizer: untemplate.Customizer.Selector = { key =>
    var out = untemplate.Customizer.empty

    // to customize, examine key and modify the customer
    // with out = out.copy=...
    //
    // e.g. out = out.copy(extraImports=Seq("draft.*"))

    out = out.copy(extraImports=Seq("com.mchange.feedletter.*","com.mchange.feedletter.style.*"))

    out
  }

  /**
   * Update the millw script.
   * modified from https://github.com/lefou/millw
   */
  def overwriteLatestMillw() = T.command {
    import java.nio.file.attribute.PosixFilePermission._
    val target = mill.util.Util.download("https://raw.githubusercontent.com/lefou/millw/main/millw")
    val millw = build.millSourcePath / "millw"
    os.copy.over(target.path, millw)
    os.perms.set(millw, os.perms(millw) + OWNER_EXECUTE + GROUP_EXECUTE + OTHERS_EXECUTE)
    target
  }
}

