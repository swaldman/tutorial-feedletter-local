import com.mchange.feedletter.{UserUntemplates,Main}
import com.mchange.feedletter.style.{AllUntemplates,StyleMain}

import com.mchange.feedletter.style.Customizer

object PreMain:
  def main( args : Array[String] ) : Unit =
    AllUntemplates.add( UserUntemplates )
    Customizer.Contents.register("lgm", tutorial.IframelessCustomizer)
    Customizer.Contents.register("lgm-daily", tutorial.IframelessCustomizer)
    Customizer.Contents.register("atrios-three", tutorial.IframelessCustomizer)
    val styleExec =
      sys.env.get("FEEDLETTER_STYLE") match
        case Some( s ) => s.toBoolean
        case None      => false
    if styleExec then StyleMain.main(args) else Main.main(args)

