import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.psykin.aethervoice.di.AppModule
import org.psykin.aethervoice.screens.SplashScreen

@Composable
@Preview
fun App(appModule: AppModule) {
    MaterialTheme {
        Navigator(SplashScreen(appModule)){
            SlideTransition(it)
        }
    }
}