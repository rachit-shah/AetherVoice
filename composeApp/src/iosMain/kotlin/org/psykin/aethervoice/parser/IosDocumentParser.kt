package org.psykin.aethervoice.parser

import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.convert
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.create
import platform.Foundation.lastPathComponent
import platform.Foundation.pathExtension
import platform.Foundation.stringWithContentsOfURL
import platform.PDFKit.PDFDocument
import platform.darwin.NSUInteger

class IosDocumentParser : DocumentParser() {
    override suspend fun parseDocument(fileUri: String): Pair<String, String> {
        val url = autoreleasepool {
            NSURL.URLWithString(fileUri)
        } ?: throw IllegalArgumentException("Invalid file URL: $fileUri")

        val fileName = url.lastPathComponent ?: ""
        val fileExtension = url.pathExtension?.lowercase() ?: ""

        val content = when (fileExtension) {
            "txt" -> parseTextDocument(url)
            "pdf" -> parsePdfDocument(url)
            "doc", "docx" -> parseWordDocument(url)
            else -> throw UnsupportedOperationException("Unsupported file format: $fileExtension")
        }
        println("Title: $fileName")
        println("Content: $content")

        return fileName to content
    }

    private fun parseTextDocument(url: NSURL): String {
        return autoreleasepool {
            val content = NSString.stringWithContentsOfURL(url, encoding = NSUTF8StringEncoding, error = null)
            content ?: ""
        }
    }

    private fun parsePdfDocument(url: NSURL): String {
        val pdfDocument = autoreleasepool {
            val data = NSData.dataWithContentsOfURL(url) ?: return@autoreleasepool null
            PDFDocument(data = data)
        } ?: throw IllegalArgumentException("Failed to create PDFDocument")

        val pageCount = pdfDocument.pageCount.toInt()
        var text = ""

        for (i in 0 until pageCount) {
            val page = pdfDocument.pageAtIndex(i.convert<NSUInteger>())
            val pageText = page?.string?.let { NSString.create(string = it) }?.toString() ?: ""
            text += pageText + "\n"
        }

        return text
    }

    private fun parseWordDocument(url: NSURL): String {
        // Implement parsing logic for Word documents on iOS
        // You may need to use a third-party library or custom parsing logic
        // Return the parsed content as a string
        throw UnsupportedOperationException("Parsing Word documents is not supported on iOS")
    }
}