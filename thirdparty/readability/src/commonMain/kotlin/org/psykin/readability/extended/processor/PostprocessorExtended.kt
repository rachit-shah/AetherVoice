package org.psykin.readability.extended.processor

import com.fleeksoft.ksoup.nodes.Attributes
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.parser.Tag
import org.psykin.readability.processor.Postprocessor

open class PostprocessorExtended : Postprocessor() {

  override fun postProcessContent(originalDocument: Document, articleContent: Element, articleUri: String, additionalClassesToPreserve: List<String>) {
    // call these methods before super.postProcessContent() so that afterwards relative urls are made absolute
    makeLazyLoadingUrlsEagerLoading(articleContent)

    fixAmpImageUris(articleContent)

    super.postProcessContent(originalDocument, articleContent, articleUri, additionalClassesToPreserve)
  }


  protected open fun makeLazyLoadingUrlsEagerLoading(articleContent: Element) {
    articleContent.select("img").forEach { imgElement ->
      makeLazyLoadingUrlEagerLoading(imgElement, "src",
        listOf("data-src", "data-original", "data-actualsrc", "data-lazy-src", "data-delayed-url",
          "data-li-src", "data-pagespeed-lazy-src"))
    }
  }

  protected open fun makeLazyLoadingUrlEagerLoading(element: Element, attributeToSet: String, lazyLoadingAttributes: List<String>) {
    lazyLoadingAttributes.forEach { lazyLoadingAttributeName ->
      val value = element.attr(lazyLoadingAttributeName)

      if (value.isNotBlank()) { // .attr() by default returns an empty string
        element.attr(attributeToSet, value)

        return // only set first found lazy loading attribute
      }
    }
  }

  protected open fun fixAmpImageUris(element: Element) {
    element.getElementsByTag("amp-img").forEach { amp_img ->

      if (amp_img.childNodeSize() == 0) {
        val attributes = Attributes()
        attributes.put("decoding", "async")
        attributes.put("alt", amp_img.attr("alt"))
        attributes.put("srcset", amp_img.attr("srcset").trim())

        amp_img.appendChild(Element(Tag.valueOf("img"), "", attributes))
      }
    }
  }


  override fun fixRelativeUris(originalDocument: Document, element: Element, scheme: String, prePath: String,
                               pathBase: String) {

    val baseUrl = originalDocument.head().select("base").first()?.attr("href")

    if (baseUrl != null) { // if a base URL is specified use that one
      super.fixRelativeUris(originalDocument, element, scheme, prePath, baseUrl)
    }
    else {
      super.fixRelativeUris(originalDocument, element, scheme, prePath, pathBase)
    }
  }

}
