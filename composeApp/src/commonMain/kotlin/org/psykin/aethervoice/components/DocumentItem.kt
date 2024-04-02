package org.psykin.aethervoice.components

import aethervoice.composeapp.generated.resources.Res
import aethervoice.composeapp.generated.resources.docx_file_icon
import aethervoice.composeapp.generated.resources.ebook_icon
import aethervoice.composeapp.generated.resources.html_file_icon
import aethervoice.composeapp.generated.resources.pdf_files_icon
import aethervoice.composeapp.generated.resources.txt_file_icon
import aethervoice.composeapp.generated.resources.unknown_file_icon
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.psykin.aethervoice.model.Document
import org.psykin.aethervoice.model.DocumentFormat
import kotlin.math.roundToInt


@OptIn(ExperimentalResourceApi::class)
@Composable
private fun getDocumentFormatIcon(format: DocumentFormat): DrawableResource {
    return when (format) {
        DocumentFormat.TXT -> Res.drawable.txt_file_icon
        DocumentFormat.PDF -> Res.drawable.pdf_files_icon
        DocumentFormat.DOCX -> Res.drawable.docx_file_icon
        DocumentFormat.WEB -> Res.drawable.html_file_icon
        DocumentFormat.EPUB -> Res.drawable.ebook_icon
        DocumentFormat.UNKNOWN -> Res.drawable.unknown_file_icon
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun DocumentItem(
    state: AnchoredDraggableState<DragAnchors>,
    defaultActionSize: Dp,
    actionSizePx: Float,
    document: Document,
    onRenameClick: (Document) -> Unit,
    onDeleteClick: (Document) -> Unit,
) {
    SwipeableRow(
        state = state,
        startAction = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart),
            ) {
                Action(
                    actionName = "Rename",
                    imageVector = Icons.Filled.Edit,
                    modifier = Modifier
                        .width(defaultActionSize)
                        .fillMaxHeight()
                        .offset {
                            IntOffset(
                                ((-state
                                    .requireOffset() - actionSizePx))
                                    .roundToInt(), 0
                            )
                        }
                        .background(Color.Blue),
                    onItemClick = { onRenameClick(document) }
                )
            }
        },
        endAction = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd),
                ) {
                Action(
                    actionName = "Delete",
                    imageVector = Icons.Filled.Delete,
                    Modifier
                        .width(defaultActionSize)
                        .fillMaxHeight()
                        .offset {
                            IntOffset(
                                ((-state
                                    .requireOffset() * 0.5f) + actionSizePx)
                                    .roundToInt(), 0
                            )
                        }
                        .background(Color.Red),
                    onItemClick = { onDeleteClick(document) }
                )
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                start = 10.dp,
                end = 10.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(getDocumentFormatIcon(document.format)),
                contentDescription = document.format.name,
                modifier = Modifier.height(50.dp).width(50.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = document.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(text = document.createdAt.substringBefore("T"), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}