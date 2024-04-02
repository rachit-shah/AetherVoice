import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun imageResources(image: String): Painter = painterResource(DrawableResource("drawable/$image"))
@Composable
@OptIn(ExperimentalResourceApi::class)
actual fun imageResources(image: DrawableResource): Painter = painterResource(image)