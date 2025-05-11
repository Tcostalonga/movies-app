package costalonga.tarsila.moviesapp.movie.domain.repository

import androidx.paging.PagingData
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMoviesFlow(title: String, type: String, yearOfRelease: String): Flow<PagingData<Movie>>
    suspend fun clearDatabase()
}