package costalonga.tarsila.moviesapp.movie.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import costalonga.tarsila.moviesapp.movie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    init {
        getMovieById("tt0944835")
    }

    private fun getMovieById(id: String) {
        viewModelScope.launch {
            val k = movieRepository.getMovieById(id)
            Log.d("DetailViewModel", "getMovieById: $k")
        }

    }

}