package org.psykin.aethervoice.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.psykin.aethervoice.model.Document
import org.psykin.aethervoice.viewmodel.HomeScreenModel

@Composable
fun RenameDocumentDialog(
    screenModel: HomeScreenModel,
    onDismiss: () -> Unit,
    onRename: (Document) -> Unit
) {
    val selectedDoc = screenModel.renameDocument.value
    if (selectedDoc != null) {
        var title by remember { mutableStateOf(selectedDoc.title) }
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Rename Document") },
            text = {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDoc.title = title
                        onRename(selectedDoc)
                    }
                ) {
                    Text("Rename")
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