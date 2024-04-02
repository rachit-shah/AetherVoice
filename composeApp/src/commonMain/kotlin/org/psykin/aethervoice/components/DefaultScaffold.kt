package org.psykin.aethervoice.components

import aethervoice.composeapp.generated.resources.Res
import aethervoice.composeapp.generated.resources.aethervoice
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.psykin.aethervoice.screens.SettingsScreen

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DefaultScaffold(
    navigator: Navigator,
    pageHeading: String,
    onAddButtonClick: () -> Unit = {},
    onSearchButtonClick: () -> Unit = {},
    onSortButtonClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    MaterialTheme (
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = pageHeading,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                        )
                    },
                    actions = {
                        IconButton(onClick = { navigator.popUntilRoot() }) {
                            Icon(
                                painterResource(Res.drawable.aethervoice),
                                contentDescription = "App Logo",
                                modifier = Modifier.width(50.dp)
                            )
                        }
                    }
                )
            },
            content = { paddingValues -> content(paddingValues) },
            bottomBar = {
                BottomAppBar(tonalElevation = 20.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navigator.popUntilRoot() }) {
                            Icon(Icons.Rounded.Home, contentDescription = "Library")
                        }
                        if (onSearchButtonClick != {}) {
                            IconButton(onClick = onSearchButtonClick) {
                                Icon(Icons.Rounded.Search, contentDescription = "Search")
                            }
                        }
                        if (onSortButtonClick != {}) {
                            IconButton(onClick = onSortButtonClick) {
                                Icon(Icons.AutoMirrored.Rounded.Sort, contentDescription = "Sort")
                            }
                        }
                        IconButton(onClick = { navigator.push(SettingsScreen()) }) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                        }
                    }
                }
            },
            floatingActionButton = {
                if (onAddButtonClick != {}) {
                    FloatingActionButton(onClick = onAddButtonClick) {
                        Icon(Icons.Rounded.Add, contentDescription = "Add Document", tint = Color.Black)
                    }
                }
            }
        )
    }
}