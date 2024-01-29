import $meta._

import mill._, scalalib._

import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import $ivy.`com.mchange::mill-daemon:0.0.1`

import $ivy.`com.mchange::untemplate-mill:0.1.2`

import untemplate.mill._
import com.mchange.milldaemon.DaemonModule

import scala.util.control.NonFatal

object feedletter extends RootModule with DaemonModule with UntemplateModule {
  def scalaVersion = "3.3.1"

  override def scalacOptions = T{ Seq("-deprecation") }

  val pidFilePathFile = os.pwd / ".feedletter-pid-file-path"

  override def runDaemonPidFile = {
    if ( os.exists( pidFilePathFile ) )
      try Some( os.Path( os.read( pidFilePathFile ).trim ) )
      catch {
        case NonFatal(t) =>
          throw new Exception( s"Could not parse absolute path of desired PID file from contents of ${pidFilePathFile}. Please repair or remove this file.", t )
      }
    else
      Some( os.pwd / "feedletter.pid" )
  }

  def ivyDeps = Agg(
    ivy"com.mchange::feedletter:0.0.7"
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

