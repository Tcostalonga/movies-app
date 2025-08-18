package costalonga.tarsila.moviesapp.movie.domain.model

data class MovieDetail(
    val title: String = "",
    val year: String = "",
    val released: String = "",
    val runtime: String = "",
    val genre: List<String> = emptyList(),
    val director: String = "",
    val actors: String = "",
    val plot: String = "",
    val country: String = "",
    val imdbID: String = "",
    val imdbRating: String = "",
    val poster: String = "",
)
