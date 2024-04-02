import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
actual fun imageResources(image: String): Painter = painterResource("drawable/$image")
@Composable
@OptIn(ExperimentalResourceApi::class)
actual fun imageResources(image: DrawableResource): Painter = org.jetbrains.compose.resources.painterResource(image)
