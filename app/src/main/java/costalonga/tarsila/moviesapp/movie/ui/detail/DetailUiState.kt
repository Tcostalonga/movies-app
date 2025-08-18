package costalonga.tarsila.moviesapp.movie.ui.detail

import costalonga.tarsila.moviesapp.movie.domain.model.MovieDetail

data class DetailUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val movieDetail: MovieDetail = MovieDetail()
)