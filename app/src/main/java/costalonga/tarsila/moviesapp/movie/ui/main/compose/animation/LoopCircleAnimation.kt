package costalonga.tarsila.moviesapp.movie.ui.main.compose.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme

@Preview
@Composable
fun LoopCircleAnimation(
    modifier: Modifier = Modifier, borderWidth: Dp = 4.dp, size: Dp = 60.dp
) {
    val loopColors = Brush.sweepGradient(
        colors = listOf(
            MoviesTheme.colors.primary,
            MoviesTheme.colors.secondary,
            MoviesTheme.colors.tertiary,
            MoviesTheme.colors.outline,
        )
    )

    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        )
    )

    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                rotate(rotation) {
                    drawCircle(
                        brush = loopColors, style = Stroke(borderWidth.toPx())
                    )
                }
            }
            .clip(CircleShape))
}
