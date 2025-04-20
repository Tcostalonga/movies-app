package costalonga.tarsila.moviesapp.movie.data.model

import costalonga.tarsila.moviesapp.movie.domain.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("imdbID") val imdbID: String,
    @SerialName("Poster") val poster: String
)

fun MovieResponse.toDomain() = Movie(
    title = title, year = year, imdbID = imdbID, poster = poster
)

fun List<MovieResponse>.toDomain(): List<Movie> {
    return map { it.toDomain() }
}