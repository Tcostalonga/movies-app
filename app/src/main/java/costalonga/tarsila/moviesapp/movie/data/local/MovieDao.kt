package costalonga.tarsila.moviesapp.movie.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie_entity")
    fun getMoviesPagingSource(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movie_entity")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM movie_entity")
    suspend fun getAll(): Int


}