package org.psykin.aethervoice.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.psykin.aethervoice.model.Document

@Composable
fun DocumentList(
    documents: List<Document>,
    onItemClick: (Document) -> Unit,
    onRenameClick: (Document) -> Unit,
    onDeleteClick: (Document) -> Unit,
) {
    LazyColumn {
        itemsIndexed(documents) { index, document ->
            StatefulDocument(
                document = document,
                onItemClick,
                onRenameClick,
                onDeleteClick
            )
            Divider()
        }
    }
}