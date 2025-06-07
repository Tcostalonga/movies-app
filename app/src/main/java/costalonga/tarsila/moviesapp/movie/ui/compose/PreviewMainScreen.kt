package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import costalonga.tarsila.moviesapp.movie.ui.MainUiState


internal class MoviePagingDataPreviewParameter : PreviewParameterProvider<PagingData<Movie>> {

    override val values = sequenceOf(
        PagingData.from(
            data = listOf(
                Movie(
                    title = "Interestelar", year = "2014", imdbID = "ridiculus", poster = "https://link.com/poster1.jpg"
                ),
                Movie(
                    title = "Duna", year = "2021", imdbID = "ridiculghjgjhgus", poster = "https://link.com/poster2.jpg"
                ),
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = false),
                append = LoadState.NotLoading(endOfPaginationReached = true),
            ),
        ),
    )
}

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