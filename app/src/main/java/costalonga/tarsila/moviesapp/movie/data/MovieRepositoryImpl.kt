package costalonga.tarsila.moviesapp.movie.data

import android.util.Log
import costalonga.tarsila.moviesapp.movie.data.api.MovieApi
import costalonga.tarsila.moviesapp.movie.data.model.toDomain
import costalonga.tarsila.moviesapp.movie.domain.Movie
import costalonga.tarsila.moviesapp.movie.domain.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : MovieRepository {
    override suspend fun getMovies(
        title: String,
        type: String,
        yearOfRelease: String
    ): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val searchResponse = movieApi.getMovies(title, type, yearOfRelease)
                Result.success(searchResponse.search.toDomain())
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e("MovieRepositoryImpl", "Error fetching from api: Cause: ${e.cause} --- Message: ${e.message}")
                Result.failure(e)
            }
        }
    }
}