package org.psykin.readability.extended.processor

import com.fleeksoft.ksoup.nodes.Element
import org.psykin.readability.extended.util.RegExUtilExtended
import org.psykin.readability.model.ReadabilityOptions
import org.psykin.readability.processor.ArticleGrabber

open class ArticleGrabberExtended(options: ReadabilityOptions, protected val regExExtended: RegExUtilExtended) : ArticleGrabber(options, regExExtended) {

  override fun shouldKeepSibling(sibling: Element): Boolean {
    return super.shouldKeepSibling(sibling) || containsImageToKeep(sibling)
  }

  protected open fun containsImageToKeep(element: Element): Boolean {
    val images = element.select("img")
    if(images.size > 0) {
      if(isImageElementToKeep(element)) {
        images.forEach { image ->
          if(isImageElementToKeep(image) == false) {
            return false
          }
        }

        return true
      }
    }

    return false
  }

  protected open fun isImageElementToKeep(element: Element): Boolean {
    val matchString = element.id() + " " + element.className()

    return regExExtended.keepImage(matchString)
  }
}
