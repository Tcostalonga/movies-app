package costalonga.tarsila.moviesapp.movie.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("Search") val search: List<MovieResponse>,
    @SerialName("totalResults") val totalResults: Int,
)