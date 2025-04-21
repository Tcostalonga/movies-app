package costalonga.tarsila.moviesapp.movie.domain.repository

import costalonga.tarsila.moviesapp.movie.domain.model.Movie

interface MovieRepository {

    suspend fun getMovies(title: String, type: String, yearOfRelease: String): Result<List<Movie>>

}