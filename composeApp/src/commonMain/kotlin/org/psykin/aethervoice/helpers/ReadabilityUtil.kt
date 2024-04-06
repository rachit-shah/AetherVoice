package org.psykin.aethervoice.helpers

import co.touchlab.kermit.Logger
import com.fleeksoft.ksoup.nodes.Element
import org.psykin.readability.extended.ReadabilityExtended

object ReadabilityUtil {

    fun parseToText(htmlContent: String?, uri: String?): String? {
        htmlContent ?: return null
        try {
            val article = ReadabilityExtended(uri ?: "", htmlContent).parse()
            val articleContent = article?.content ?: return null
            if (articleContent.isBlank()) return null
            return articleContent
        } catch (e: Exception) {
            Logger.e("Readability.parseToText '$uri' is error: ", e)
            return htmlContent
        }
    }

    fun parseToElement(htmlContent: String?, uri: String?): Element? {
        htmlContent ?: return null
        try {
            val article = ReadabilityExtended(uri ?: "", htmlContent).parse()
            return article?.articleContent
        } catch (e: Exception) {
            Logger.e("Readability.parseToElement '$uri' is error: ", e)
            return null
        }
    }
}
