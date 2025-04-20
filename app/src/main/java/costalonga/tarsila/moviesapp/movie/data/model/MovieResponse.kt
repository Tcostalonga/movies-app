package costalonga.tarsila.moviesapp.movie.data.model

import costalonga.tarsila.moviesapp.movie.domain.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("year") val year: String,
    @SerialName("runTime") val runTime: String,
    @SerialName("genre") val genre: String,
    @SerialName("director") val director: String,
    @SerialName("actors") val actors: String,
    @SerialName("plot") val plot: String,
    @SerialName("language") val language: String,
    @SerialName("poster") val poster: String
)

fun MovieResponse.toDomain() = Movie(
    id = id,
    title = title,
    year = year,
    runTime = runTime,
    genre = genre,
    director = director,
    actors = actors,
    plot = plot,
    language = language,
    poster = poster
)

fun List<MovieResponse>.toDomain(): List<Movie> {
    return map { it.toDomain() }
}