package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import costalonga.tarsila.moviesapp.movie.ui.MainUiState

class PreviewMainScreen : PreviewParameterProvider<MainUiState> {
    override val values: Sequence<MainUiState>
        get() = sequenceOf(
            MainUiState()
        )
}

val movies = listOf(
    Movie("Interestelar", "2014", "1", "https://link.com/poster1.jpg"),
    Movie("Duna", "2021", "2", "https://link.com/poster2.jpg"),
    Movie("Barbie", "2023", "3", "https://link.com/poster3.jpg")
)