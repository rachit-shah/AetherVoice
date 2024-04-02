package org.psykin.aethervoice.screens

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.psykin.aethervoice.components.SettingsScaffold

class SettingsScreen() : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: throw RuntimeException("Unable to get current navigator")
        SettingsScaffold(
            navigator = navigator,
            pageHeading = "Settings"
        ) { paddingValues ->
            Surface(
                shape = MaterialTheme.shapes.small,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            ) {
                // TODO
            }
        }
    }
}