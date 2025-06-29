package costalonga.tarsila.moviesapp.movie.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import costalonga.tarsila.moviesapp.movie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private var _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    fun getMovieById(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                movieRepository.getMovieById(id)
            }
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            movieDetail = result,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isError = true,
                            isLoading = false,
                        )
                    }
                }
        }
    }
}