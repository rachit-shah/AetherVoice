package org.psykin.aethervoice.subscreens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class FileManagerSubScreen(private val onFileSelected: (String) -> Unit) : Screen {
    @Composable
    override fun Content() {
        // Implement file manager UI for selecting a document
        // Call onFileSelected with the selected file URI
    }
}