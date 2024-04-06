package org.psykin.aethervoice.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.fleeksoft.ksoup.Ksoup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.psykin.aethervoice.dao.DocumentRepository
import org.psykin.aethervoice.dialog.SortOption
import org.psykin.aethervoice.helpers.generateUniqueId
import org.psykin.aethervoice.helpers.getCurrentTimestampRFC3339
import org.psykin.aethervoice.helpers.getDocumentFormatFromUri
import org.psykin.aethervoice.model.Document
import org.psykin.aethervoice.model.DocumentFormat
import org.psykin.aethervoice.parser.DocumentParser
import org.psykin.aethervoice.screens.ReaderScreen
import org.psykin.aethervoice.subscreens.FileManagerSubScreen

class HomeScreenModel(
    private val documentRepository: DocumentRepository,
    private val documentParser: DocumentParser,
    private val navigator: Navigator
) : ScreenModel {

    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> = _documents.asStateFlow()

    private val _documentTitle = MutableStateFlow("")
    val documentTitle: StateFlow<String> = _documentTitle.asStateFlow()

    private val _selectedFormat = MutableStateFlow<DocumentFormat?>(null)
    val selectedFormat: StateFlow<DocumentFormat?> = _selectedFormat.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSortOption = MutableStateFlow(SortOption.NAME)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption.asStateFlow()

    private var _isSearchDialogVisible = MutableStateFlow(false)
    val isSearchDialogVisible: StateFlow<Boolean> = _isSearchDialogVisible.asStateFlow()

    private val _isSortOptionsVisible = MutableStateFlow(false)
    val isSortOptionsVisible: StateFlow<Boolean> = _isSortOptionsVisible.asStateFlow()

    private val _selectedDocumentId = MutableStateFlow<String?>(null)
    val selectedDocumentId: StateFlow<String?> = _selectedDocumentId.asStateFlow()

    private val _renameDocument = MutableStateFlow<Document?>(null)
    val renameDocument: StateFlow<Document?> = _renameDocument.asStateFlow()

    private val _urlInput = MutableStateFlow(false)
    val urlInput: StateFlow<Boolean> = _urlInput.asStateFlow()

    init {
        loadDocuments()
    }

    private fun loadDocuments() {
        screenModelScope.launch {
            val allDocuments = documentRepository.getDocuments()
            val filteredDocuments = filterDocuments(allDocuments)
            _documents.value = filteredDocuments
        }
    }

    private fun filterDocuments(documents: List<Document>): List<Document> {
        val format = _selectedFormat.value
        return if (format != null) {
            documents.filter { it.format == format }
        } else {
            documents
        }
    }

    fun openDocument(document: Document) {
        navigator.push(ReaderScreen(document))
    }

    fun toggleDocumentChecked(documentId: String) {
        val updatedDocuments = _documents.value.map { document ->
            if (document.id == documentId) {
                document.copy(checked = !document.checked)
            } else {
                document
            }
        }
        _documents.value = updatedDocuments
    }

    fun setDocumentTitle(title: String) {
        _documentTitle.value = title
    }

    fun selectFormat(format: DocumentFormat?) {
        _selectedFormat.value = format
        loadDocuments()
    }

    fun renameDocumentTitle(documentId: String, newTitle: String) {
        screenModelScope.launch {
            val document = _documents.value.find { it.id == documentId }
            if (document != null) {
                val updatedDocument = document.copy(title = newTitle)
                documentRepository.updateDocument(updatedDocument)
                loadDocuments()
            }
        }
    }


    fun deleteSelectedDocuments() {
        screenModelScope.launch {
            val checkedDocumentIds = _documents.value.filter { it.checked }.map { it.id }
            checkedDocumentIds.forEach { documentId ->
                documentRepository.deleteDocument(documentId)
            }
            loadDocuments()
        }
    }

    fun openFileManager() {
        navigator.push(FileManagerSubScreen { fileUri ->
            parseAndAddDocument(fileUri)
        })
    }

    private fun parseAndAddDocument(fileUri: String) {
        screenModelScope.launch {
            val currentTimestamp = getCurrentTimestampRFC3339()
            val (title, content) = documentParser.parseDocument(fileUri)
            val document = Document(
                id = generateUniqueId(),
                title = title,
                content = Ksoup.parse(content),
                format = getDocumentFormatFromUri(fileUri),
                createdAt = currentTimestamp,
                updatedAt = currentTimestamp,
                checked = false
            )
            documentRepository.addDocument(document)
            loadDocuments()
        }
    }

    fun parseAndAddWebpage(url: String) {
        screenModelScope.launch {
            val currentTimestamp = getCurrentTimestampRFC3339()
            val (title, content) = documentParser.parseWebpage(url)
            val document = Document(
                id = generateUniqueId(),
                title = title,
                content = content,
                format = DocumentFormat.WEB,
                createdAt = currentTimestamp,
                updatedAt = currentTimestamp,
                checked = false
            )
            documentRepository.addDocument(document)
            loadDocuments()
        }
    }

    fun deleteDocument(documentId: String) {
        screenModelScope.launch {
            documentRepository.deleteDocument(documentId)
            loadDocuments()
        }
    }

    fun toggleSearchDialog() {
        if (_isSearchDialogVisible.value) {
            _isSearchDialogVisible.value = false
            _searchQuery.value = ""
        } else {
            _isSearchDialogVisible.value = true
        }
    }

    fun toggleUrlInput() {
        _urlInput.value = !_urlInput.value
    }

    fun resetRenameDocument() {
        _renameDocument.value = null
    }

    fun setRenameDocument(document: Document) {
        _renameDocument.value = document
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun performSearch() {
        val query = _searchQuery.value.trim()
        if (query.isNotEmpty()) {
            // TODO: Implement search functionality
        }
    }

    fun toggleSortOptions() {
        _isSortOptionsVisible.value = !_isSortOptionsVisible.value
    }

    fun updateSortOption(option: SortOption) {
        _selectedSortOption.value = option
        sortDocuments()
    }

    private fun sortDocuments() {
        val sortedDocuments = when (_selectedSortOption.value) {
            SortOption.NAME -> _documents.value.sortedBy { it.title }
            SortOption.FORMAT -> _documents.value.sortedBy { it.format }
            SortOption.DATE -> _documents.value.sortedByDescending { it.createdAt }
        }
        _documents.value = sortedDocuments
    }

    fun showDocumentActions(documentId: String) {
        _selectedDocumentId.value = documentId
    }

    fun hideDocumentActions() {
        _selectedDocumentId.value = null
    }
}