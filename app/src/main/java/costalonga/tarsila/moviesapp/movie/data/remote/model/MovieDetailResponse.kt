package costalonga.tarsila.moviesapp.movie.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    @SerialName("imdbID") val imdbID: String,
    @SerialName("imdbRating") val imdbRating: String,
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("Released") val released: String,
    @SerialName("Runtime") val runtime: String,
    @SerialName("Genre") val genre: String,
    @SerialName("Director") val director: String,
    @SerialName("Actors") val actors: String,
    @SerialName("Plot") val plot: String,
    @SerialName("Country") val country: String,
    @SerialName("Poster") val poster: String,
)
