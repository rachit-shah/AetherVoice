package org.psykin.aethervoice.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun BottomRightDrawerComponent(
    taskPageContent: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Box(modifier = Modifier.fillMaxSize()) {

        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    } else {
                        drawerState.close()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), // Adjust padding to move the button away from the corners
            content = {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
        )

        if (drawerState.isOpen) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    taskPageContent()
                    Button(
                        onClick = { coroutineScope.launch { drawerState.close() } },
                        content = { Text("Close Drawer") }
                    )
                },
                content = {}
            )
        }
    }
}
@Composable
fun BottomUpDrawerComponent(
    showBottomSheet: Boolean,
    onShowBottomSheetChange: (Boolean) -> Unit,
    onDrawerStateChange: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val offset = animateDpAsState(targetValue = if (showBottomSheet) 0.dp else 500.dp)
    LaunchedEffect(showBottomSheet) {
        onDrawerStateChange(showBottomSheet)
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = showBottomSheet,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = offset.value)) {
                content()
            }
        }
    }
}