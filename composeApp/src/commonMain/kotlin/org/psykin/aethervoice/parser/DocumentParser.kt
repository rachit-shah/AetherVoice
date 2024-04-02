package org.psykin.aethervoice.parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest

abstract class DocumentParser {
    abstract suspend fun parseDocument(fileUri: String): Pair<String, String>
    suspend fun parseWebpage(url: String): Pair<String, String> {
        val document = Ksoup.parseGetRequest(url = url) // suspend function
        val title = document.title()
        val text = document.body().text()
        println("Title: $title")
        println("Content: $text")
        return title to text
    }
}
