package costalonga.tarsila.moviesapp.movie.domain

interface MovieRepository {

    suspend fun getMovies(title: String, type: String, yearOfRelease: String): Result<List<Movie>>

}