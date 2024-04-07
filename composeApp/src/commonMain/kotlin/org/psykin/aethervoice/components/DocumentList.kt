package org.psykin.aethervoice.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import org.psykin.aethervoice.model.Document

@Composable
fun DocumentList(
    documents: List<Document>,
    onItemClick: (Document) -> Unit,
    onRenameClick: (Document) -> Unit,
    onDeleteClick: (Document) -> Unit,
) {
    LazyColumn {
        itemsIndexed(documents) { _, document ->
            StatefulDocument(
                document = document,
                onItemClick,
                onRenameClick,
                onDeleteClick
            )
            HorizontalDivider()
        }
    }
}