package org.psykin.aethervoice.screens

import aethervoice.composeapp.generated.resources.Res
import aethervoice.composeapp.generated.resources.aethervoice
import aethervoice.composeapp.generated.resources.compose_multiplatform
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.psykin.aethervoice.di.AppModule

class SplashScreen(private val appModule: AppModule) : Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        LaunchedEffect(Unit) {
            delay(1500)
            navigator?.replace(HomeScreen(appModule))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painterResource(Res.drawable.aethervoice),
                modifier = Modifier.fillMaxWidth().size(width = 500.dp, height = 500.dp),
                contentScale = ContentScale.Fit,
                contentDescription = "aether-voice",
            )
        }
    }
}