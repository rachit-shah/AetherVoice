package org.psykin.aethervoice.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.psykin.aethervoice.model.Document

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatefulDocument(
    document: Document,
    onItemClick: (Document) -> Unit,
    onRenameClick: (Document) -> Unit,
    onDeleteClick: (Document) -> Unit,
) {
    val density = LocalDensity.current

    val defaultActionSize = 80.dp
    val actionSizePx = with(density) { defaultActionSize.toPx() }
    val endActionSizePx = with(density) { (defaultActionSize * 2).toPx() }

    val dragState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Start at -actionSizePx
                DragAnchors.Center at 0f
                DragAnchors.End at endActionSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    Box(
        modifier = Modifier
            .clickable {
                onItemClick(document)
            }
    ) {
        DocumentItem(dragState, defaultActionSize, actionSizePx, document, onRenameClick, onDeleteClick)
    }
}