package costalonga.tarsila.moviesapp.movie.data.remote.api

import costalonga.tarsila.moviesapp.movie.data.remote.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(".")
    suspend fun getMovies(
        @Query("s") title: String,
        @Query("type") type: String,
        @Query("y") yearOfRelease: String,
        @Query("page") page: Int?,
    ): SearchResponse

}