package costalonga.tarsila.moviesapp.core.theme


import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Spacing {
    val dp0: Dp = 0.dp
    val dp1: Dp = 1.dp
    val dp2: Dp = 2.dp
    val dp4: Dp = 4.dp
    val dp8: Dp = 8.dp
    val dp12: Dp = 12.dp
    val dp16: Dp = 16.dp
    val dp18: Dp = 18.dp
    val dp24: Dp = 24.dp
    val dp32: Dp = 32.dp
    val dp40: Dp = 40.dp
    val dp48: Dp = 48.dp
    val dp64: Dp = 64.dp
    val dp72: Dp = 72.dp
}

val LocalThemeSpacing = staticCompositionLocalOf { Spacing }
