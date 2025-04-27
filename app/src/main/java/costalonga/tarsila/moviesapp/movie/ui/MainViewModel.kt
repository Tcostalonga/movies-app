package costalonga.tarsila.moviesapp.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import costalonga.tarsila.moviesapp.movie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class MainViewModel @Inject constructor(val movieRepository: MovieRepository) : ViewModel() {

    private val _movies = MutableStateFlow(MainUiState())
    val movies = _movies.asStateFlow()

    var job: Job = Job()

    fun getMovies(title: String, type: String, yearOfRelease: String) {
        job.cancel()
        job = viewModelScope.launch {
            _movies.update {
                it.copy(isLoading = true)
            }
            delay(1500)
            val result = movieRepository.getMovies(title, type, yearOfRelease)
            result.onSuccess { listOfMovies ->
                _movies.update {
                    it.copy(isLoading = false, movies = listOfMovies)
                }
            }
            result.onFailure {
                _movies.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun onIntent(intent: MainScreenIntents) {
        when (intent) {
            is MainScreenIntents.OnSearchQueryChange -> {
                _movies.update {
                    it.copy(searchQuery = intent.newSearchQuery)
                }
                getMovies(title = intent.newSearchQuery, type = "movie", yearOfRelease = "")
            }

        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val searchQuery: String = "",
)