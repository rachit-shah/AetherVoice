package org.psykin.aethervoice.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import org.psykin.aethervoice.viewmodel.HomeScreenModel

@Composable
public fun SearchDialog(
    screenModel: HomeScreenModel,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (screenModel.isSearchDialogVisible.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Search") },
            text = {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = { Text("Search query") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    screenModel.performSearch()
                    onDismiss()
                }) {
                    Text("Search")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}