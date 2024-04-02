import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
expect fun imageResources(image: String): Painter

@Composable
@OptIn(ExperimentalResourceApi::class)
expect fun imageResources(image: DrawableResource): Painter