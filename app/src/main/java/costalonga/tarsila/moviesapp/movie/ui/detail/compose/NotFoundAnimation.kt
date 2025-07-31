package costalonga.tarsila.moviesapp.movie.ui.detail.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme
import kotlinx.coroutines.delay

@Composable
fun NotFoundAnimation(
    modifier: Modifier = Modifier,
    text: String = "Not found",
) {
    val arcColor = MoviesTheme.colors.onBackground
    val interrogationPointColor = MoviesTheme.colors.outline

    var startAnimatedSweep by remember {
        mutableStateOf(false)
    }
    var startAnimatedVisibility by remember {
        mutableStateOf(false)
    }
    val animatedSweep by animateFloatAsState(
        targetValue = if (startAnimatedSweep) 360f else 0f,
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "animatedArc"
    )

    val animatedVisibility by animateFloatAsState(
        targetValue = if (startAnimatedVisibility) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing),
        label = "animatedArc"
    )

    LaunchedEffect(true) {
        startAnimatedSweep = true
        delay(800)
        startAnimatedVisibility = true
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = modifier) {
            drawArc(
                color = arcColor,
                startAngle = -90f,
                sweepAngle = animatedSweep,
                useCenter = false,
                size = Size(size.width, size.height),
                style = Stroke(25f)
            )

            drawLine(
                color = interrogationPointColor,
                start = Offset(this.size.width / 2, 50f),
                end = Offset(size.width / 2, size.height / 1.5f),
                strokeWidth = 20f,
                cap = StrokeCap.Round,
                alpha = animatedVisibility
            )

            drawCircle(
                color = interrogationPointColor,
                radius = 20f,
                center = Offset(size.width / 2, size.height / 1.20f),
                alpha = animatedVisibility
            )
        }
        Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp18))

        Text(
            text = text,
            modifier = Modifier
                .graphicsLayer {
                    alpha = animatedVisibility
                },
            style = MoviesTheme.typography.titleLarge
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewNotFoundAnimation() {
    MoviesAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            NotFoundAnimation(modifier = Modifier.size(100.dp))
        }
    }
}