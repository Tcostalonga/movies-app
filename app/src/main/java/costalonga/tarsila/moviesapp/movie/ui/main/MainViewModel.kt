@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package costalonga.tarsila.moviesapp.movie.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import costalonga.tarsila.moviesapp.movie.domain.model.SearchParams
import costalonga.tarsila.moviesapp.movie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private val _movies = MutableStateFlow(MainUiState())
    val movies = _movies.asStateFlow()

    val getCachedMovies = _movies
        .map { it.searchParams }
        .debounce(1300)
        .flatMapLatest { params ->
            val flow = movieRepository.getMoviesFlow(params)
            flow
        }
        .cachedIn(viewModelScope)

    fun onIntent(intent: MainScreenIntents) {
        when (intent) {
            is MainScreenIntents.OnSearchQueryChange -> {
                _movies.update {
                    it.copy(
                        searchParams = SearchParams(query = intent.newSearchQuery, type = "movie", yearOfRelease = ""),
                        showInitialState = false
                    )
                }
            }
        }
    }

    fun clearDatabase() =
        viewModelScope.launch {
            movieRepository.clearDatabase()
        }
}

data class MainUiState(
    val showInitialState: Boolean = true,
    val searchParams: SearchParams = SearchParams(),
)