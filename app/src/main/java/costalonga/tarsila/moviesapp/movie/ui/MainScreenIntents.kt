package costalonga.tarsila.moviesapp.movie.ui

sealed interface MainScreenIntents {
    data class OnSearchQueryChange(val newSearchQuery: String) : MainScreenIntents

}