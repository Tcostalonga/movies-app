package costalonga.tarsila.moviesapp.movie.data.api

import costalonga.tarsila.moviesapp.movie.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(".")
    suspend fun getAllMovies(
        @Query("s") title: String,
        @Query("type") type: String,
        @Query("y") yearOfRelease: String,
    ): SearchResponse

}