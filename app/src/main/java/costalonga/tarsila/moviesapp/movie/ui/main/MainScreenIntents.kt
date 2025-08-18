package costalonga.tarsila.moviesapp.movie.ui.main

sealed interface MainScreenIntents {
    data class OnSearchQueryChange(val newSearchQuery: String) : MainScreenIntents

}