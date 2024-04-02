package org.psykin.aethervoice.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File

class DesktopDocumentParser : DocumentParser() {
    override suspend fun parseDocument(fileUri: String): Pair<String, String> {
        return withContext(Dispatchers.IO) {
            val file = File(fileUri)
            val fileName = file.name
            val content = when (val fileExtension = file.extension) {
                "txt" -> file.readText()
                "pdf" -> parsePdfDocument(file)
                "doc", "docx" -> parseWordDocument(file)
                else -> throw UnsupportedOperationException("Unsupported file format: $fileExtension")
            }
            println("Title: $fileName")
            println("Content: $content")

            fileName to content
        }
    }

    private fun parsePdfDocument(file: File): String {
        val pdfDocument = PDDocument.load(file)
        val stripper = PDFTextStripper()
        val text = stripper.getText(pdfDocument)
        pdfDocument.close()
        return text
    }

    private fun parseWordDocument(file: File): String {
        val document = XWPFDocument(file.inputStream())
        val extractor = XWPFWordExtractor(document)
        val text = extractor.text
        extractor.close()
        return text
    }
}