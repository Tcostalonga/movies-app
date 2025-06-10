package costalonga.tarsila.moviesapp.movie.domain.repository

import androidx.paging.PagingData
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import costalonga.tarsila.moviesapp.movie.domain.model.MovieDetail
import costalonga.tarsila.moviesapp.movie.domain.model.SearchParams
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMoviesFlow(params: SearchParams): Flow<PagingData<Movie>>
    suspend fun getMovieById(imdbID: String): MovieDetail
    suspend fun clearDatabase()
}