@file:OptIn(ExperimentalPagingApi::class)

package costalonga.tarsila.moviesapp.movie.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import costalonga.tarsila.moviesapp.movie.data.local.MovieDatabase
import costalonga.tarsila.moviesapp.movie.data.local.toDomain
import costalonga.tarsila.moviesapp.movie.data.remote.api.MovieApi
import costalonga.tarsila.moviesapp.movie.data.remote.mappers.toDomain
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import costalonga.tarsila.moviesapp.movie.domain.model.MovieDetail
import costalonga.tarsila.moviesapp.movie.domain.model.Plot
import costalonga.tarsila.moviesapp.movie.domain.model.SearchParams
import costalonga.tarsila.moviesapp.movie.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieRepository {

    override fun getMoviesFlow(
        params: SearchParams
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MovieRemoteMediator(params, movieDatabase, movieApi),
            pagingSourceFactory = {
                movieDatabase.movieDao.getMoviesPagingSource()
            }
        ).flow
            .map {
                it.map { movieEntity ->
                    movieEntity.toDomain()
                }
            }
            .catch {
                Log.e("MovieRepositoryImpl", "Error fetching from db: Cause: ${it.cause} --- Message: ${it.message}")
            }
    }

    override suspend fun getMovieById(imdbID: String): MovieDetail {
        return try {
            movieApi.getMovieById(imdbID, Plot.SHORT.name).toDomain()
        } catch (e: Exception) {
            Log.e("MovieRepositoryImpl", "Error fetching movie by id: Cause: ${e.cause} --- Message: ${e.message}")
            throw e
        }
    }

    override suspend fun clearDatabase() {
        movieDatabase.movieDao.clearAll()
    }
}
