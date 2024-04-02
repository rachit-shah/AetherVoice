package org.psykin.aethervoice.screens

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.psykin.aethervoice.components.BottomUpDrawerComponent
import org.psykin.aethervoice.components.DefaultScaffold
import org.psykin.aethervoice.components.DocumentList
import org.psykin.aethervoice.di.AppModule
import org.psykin.aethervoice.dialog.AddDocumentDialog
import org.psykin.aethervoice.dialog.RenameDocumentDialog
import org.psykin.aethervoice.dialog.SearchDialog
import org.psykin.aethervoice.dialog.SortOptionsDialog
import org.psykin.aethervoice.dialog.UrlInputDialog
import org.psykin.aethervoice.viewmodel.HomeScreenModel

class HomeScreen(private val appModule: AppModule) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: throw RuntimeException("Unable to get current navigator")
        val screenModel = remember { HomeScreenModel(appModule.documentRepository, appModule.documentParser, navigator) }
        val documents by screenModel.documents.collectAsState()
        val searchQuery by screenModel.searchQuery.collectAsState()
        val selectedSortOption by screenModel.selectedSortOption.collectAsState()
        var showAddDocumentMenu by remember { mutableStateOf(false) }
        val renameDocument by screenModel.renameDocument.collectAsState()
        val urlInput by screenModel.urlInput.collectAsState()
        val isSearchDialogVisible by screenModel.isSearchDialogVisible.collectAsState()
        val isSortOptionsVisible by screenModel.isSortOptionsVisible.collectAsState()

        DefaultScaffold(
            navigator = navigator,
            pageHeading = "Library",
            onAddButtonClick = { showAddDocumentMenu = !showAddDocumentMenu },
            onSearchButtonClick = { screenModel.toggleSearchDialog() },
            onSortButtonClick = { screenModel.toggleSortOptions() }
        ) { paddingValues ->
            Surface(
                shape = MaterialTheme.shapes.small,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            ) {
                DocumentList(
                    documents = documents,
                    onItemClick = { document -> screenModel.openDocument(document) },
                    onRenameClick = { document ->
                        run {
                            screenModel.setRenameDocument(document)
                        }
                    }
                ) { document -> screenModel.deleteDocument(document.id) }
            }

            // Add Document Bottom Sheet
            BottomUpDrawerComponent(
                showBottomSheet = showAddDocumentMenu,
                onShowBottomSheetChange = { showAddDocumentMenu = it },
                onDrawerStateChange = { isActive -> showAddDocumentMenu = isActive },
            ) {
                AddDocumentDialog(
                    onDismiss = { showAddDocumentMenu = false },
                    onFileManagerClick = { screenModel.openFileManager() },
                    onUrlWebpageClick = { screenModel.toggleUrlInput() }
                )
            }

            // Rename Document Bottom Sheet
            BottomUpDrawerComponent(
                showBottomSheet = renameDocument != null,
                onShowBottomSheetChange = { },
                onDrawerStateChange = { },
            ) {
                RenameDocumentDialog(
                    screenModel = screenModel,
                    onDismiss = { screenModel.resetRenameDocument() }
                ) {
                    renameDocument?.let { it1 -> screenModel.renameDocumentTitle(it1.id, it1.title) }
                    screenModel.resetRenameDocument()
                }
            }

            // URL Input Bottom Sheet
            BottomUpDrawerComponent(
                showBottomSheet = urlInput,
                onShowBottomSheetChange = { },
                onDrawerStateChange = { },
            ) {
                UrlInputDialog(
                    onDismiss = {
                        screenModel.toggleUrlInput()
                        showAddDocumentMenu = false
                    }
                ) { url ->
                    screenModel.parseAndAddWebpage(url)
                    screenModel.toggleUrlInput()
                    showAddDocumentMenu = false
                }
            }

            // Search Bottom Sheet
            BottomUpDrawerComponent(
                showBottomSheet = isSearchDialogVisible,
                onShowBottomSheetChange = { },
                onDrawerStateChange = { },
            ) {
                SearchDialog(
                    screenModel = screenModel,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { screenModel.updateSearchQuery(it) },
                    onDismiss = { screenModel.toggleSearchDialog() }
                )
            }

            // Sort Options Bottom Sheet
            BottomUpDrawerComponent(
                showBottomSheet = isSortOptionsVisible,
                onShowBottomSheetChange = { },
                onDrawerStateChange = { },
            ) {
                SortOptionsDialog(
                    screenModel = screenModel,
                    selectedOption = selectedSortOption,
                    onSortOptionSelected = { screenModel.updateSortOption(it) },
                    onDismiss = { screenModel.toggleSortOptions() }
                )
            }
        }
    }
}