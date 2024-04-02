import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
actual fun imageResources(image: String): Painter {
    val context = LocalContext.current
    val name = image.substringBefore(".")
    val drawable = context.resources.getIdentifier(name, "drawable", context.packageName)
    return painterResource(drawable)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun imageResources(image: DrawableResource): Painter {
    return org.jetbrains.compose.resources.painterResource(image)
}