package org.psykin.aethervoice.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.psykin.aethervoice.components.ReaderScaffold
import org.psykin.aethervoice.model.Document

class ReaderScreen(private val document: Document) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: throw RuntimeException("Unable to get current navigator")
        ReaderScaffold(
            navigator = navigator,
            pageHeading = "Reader"
        ) { paddingValues ->
            Surface(
                shape = MaterialTheme.shapes.small,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(50.dp),
                    modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = document.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = document.content.wholeText(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}