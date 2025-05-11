@file:OptIn(ExperimentalPagingApi::class, ExperimentalSerializationApi::class)

package costalonga.tarsila.moviesapp.movie.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import costalonga.tarsila.moviesapp.movie.data.local.MovieDatabase
import costalonga.tarsila.moviesapp.movie.data.local.MovieEntity
import costalonga.tarsila.moviesapp.movie.data.local.toEntity
import costalonga.tarsila.moviesapp.movie.data.remote.api.MovieApi
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.ExperimentalSerializationApi

class MovieRemoteMediator(
    private val title: String,
    private val type: String,
    private val year: String,
    private val movieDatabase: MovieDatabase,
    private val movieApi: MovieApi
) : RemoteMediator<Int, MovieEntity>() {

    val movieDao = movieDatabase.movieDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            if (title.isEmpty()) {
                movieDao.clearAll()
                return MediatorResult.Success(true)
            }

            var loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("MovieRemoteMediator", "passou no REFRESH")
                    1
                }

                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    Log.d("MovieRemoteMediator", "passou no APPEND - ${state.lastItemOrNull()}")
                    val lastItemCount = movieDao.countAll()
                    (lastItemCount / state.config.pageSize) + 1
                }
            }
            val data = movieApi.getMovies(title, type, year, loadKey)
            val totalResults = data.totalResults
            Log.d("MovieRemoteMediator", "totalResults: $totalResults")

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                }
                val entities = data.search.map {
                    it.toEntity()
                }
                entities.let { movieDao.upsertAll(it) }
            }

            MediatorResult.Success(
                endOfPaginationReached = movieDao.countAll() >= totalResults
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            MediatorResult.Error(e)
        }
    }
}