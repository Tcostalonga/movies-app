package costalonga.tarsila.moviesapp.movie.ui.detail.compose

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

private val GenreColorsDark = listOf(
    Color(0xFFEF9A9A),
    Color(0xFFF48FB1),
    Color(0xFFCE93D8),
    Color(0xFFB39DDB),
    Color(0xFF90CAF9),
    Color(0xFF81D4FA),
    Color(0xFF80DEEA),
    Color(0xFFA5D6A7),
    Color(0xFFE6EE9C),
    Color(0xFFFFF59D),
    Color(0xFFFFE082),
    Color(0xFFFFCC80),
    Color(0xFFFFAB91),
    Color(0xFFFF8A65),
    Color(0xFFFFB74D),
)
private val GenreColorsLight = listOf(
    Color(0xFFEF5350),
    Color(0xFFEC407A),
    Color(0xFFAB47BC),
    Color(0xFF7E57C2),
    Color(0xFF42A5F5),
    Color(0xFF29B6F6),
    Color(0xFF26C6DA),
    Color(0xFF66BB6A),
    Color(0xFFD4E157),
    Color(0xFFFFEE58),
    Color(0xFFFFCA28),
    Color(0xFFFFA726),
    Color(0xFFFF7043),
    Color(0xFFFF5722),
    Color(0xFFFF9800),
)

fun colorForGenre(genre: String, isDarkTheme: Boolean): Color {
    val colorByTheme = if (isDarkTheme) GenreColorsDark else GenreColorsLight
    val index = abs(genre.hashCode()) % colorByTheme.size
    return colorByTheme[index]
}