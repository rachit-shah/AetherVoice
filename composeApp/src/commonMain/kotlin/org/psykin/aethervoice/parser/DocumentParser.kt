package org.psykin.aethervoice.parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Element
import org.psykin.aethervoice.helpers.ReadabilityUtil

abstract class DocumentParser {
    abstract suspend fun parseDocument(fileUri: String): Pair<String, String>
    suspend fun parseWebpage(url: String): Pair<String, Element> {
        val document = Ksoup.parseGetRequest(url = url) // suspend function
        val title = document.title()
        val text = ReadabilityUtil.parseToElement(document.html(), url) ?: document.body()
        println("Title: $title")
        println("Content: $text")
        return title to text
    }
}
