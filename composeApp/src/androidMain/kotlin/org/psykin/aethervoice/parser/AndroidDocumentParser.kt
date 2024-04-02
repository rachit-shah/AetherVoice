package org.psykin.aethervoice.parser

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AndroidDocumentParser(private val context: Context) : DocumentParser() {
    override suspend fun parseDocument(fileUri: String): Pair<String, String> {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(Uri.parse(fileUri))
            val fileExtension = getFileExtension(fileUri)

            val tempFile = createTempFile(inputStream, fileExtension)
            val fileName = getFileName(fileUri)

            val content = when (fileExtension) {
                "txt" -> tempFile.readText()
                "pdf" -> parsePdfDocument(tempFile)
                "doc", "docx" -> parseWordDocument(tempFile)
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

    private fun getFileExtension(fileUri: String): String {
        val uri = Uri.parse(fileUri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
        return extension ?: ""
    }

    private fun getFileName(fileUri: String): String {
        val uri = Uri.parse(fileUri)
        val fileName = uri.lastPathSegment
        return fileName ?: ""
    }

    private fun createTempFile(inputStream: InputStream?, fileExtension: String): File {
        val tempFile = File.createTempFile("temp", ".$fileExtension", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        outputStream.close()
        inputStream?.close()
        return tempFile
    }
}