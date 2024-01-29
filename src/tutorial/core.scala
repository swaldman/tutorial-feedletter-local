package tutorial

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document,Element}

import scala.jdk.CollectionConverters.*

import com.mchange.feedletter.*
import com.mchange.feedletter.style.Customizer

private def createDivEmbedded( link : String ) : Element =
  val div = new Element("div").attr("class","embedded")
  val a = new Element("a").attr("href",link)
  val linkText =
    if link.toLowerCase.contains("youtube.com/") then
      "Embedded YouTube video"
    else
      "Embedded item"
  a.append(linkText)
  div.appendChild(a)
  div

def iframeToDivEmbedded( html : String ) : String =
  val doc = Jsoup.parseBodyFragment( html )
  val iframes = doc.select("iframe").asScala
  iframes.foreach: ifr =>
    val src = ifr.attribute("src").getValue()
    ifr.replaceWith( createDivEmbedded(src) )
  doc.body().html()

val IframelessCustomizer : Customizer.Contents =
  ( subscribableName : SubscribableName, subscriptionManager : SubscriptionManager, withinTypeId : String, feedUrl : FeedUrl, contents : Seq[ItemContent] ) =>
    contents.map: ic =>
      ic.article match
        case Some( html ) => ic.withArticle( iframeToDivEmbedded( html ) )
        case None => ic
