import androidx.compose.ui.window.ComposeUIViewController
import org.psykin.aethervoice.di.AppModule

fun MainViewController() = ComposeUIViewController { App(appModule = AppModule()) }