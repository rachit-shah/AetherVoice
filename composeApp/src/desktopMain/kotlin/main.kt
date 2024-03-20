import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.psykin.aethervoice.di.AppModule

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "AetherVoice") {
        App(appModule = AppModule())
    }
}