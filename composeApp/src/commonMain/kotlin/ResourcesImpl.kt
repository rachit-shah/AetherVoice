import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale

@Immutable
public class ResourcesImpl (
    private val locale: String
) {
    public fun getDrawableResources() = DrawableResources
}

@Immutable
public object DrawableResources {
    public val browser_image: String = "browser.svg"
}

val LocalResources = staticCompositionLocalOf { ResourcesImpl("EN") }

@ReadOnlyComposable
@Composable
public fun getDrawables(): DrawableResources = LocalResources.current.getDrawableResources()

@Composable
fun ApplicationTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalResources provides ResourcesImpl(Locale.current.language)) {
        content()
    }
}
