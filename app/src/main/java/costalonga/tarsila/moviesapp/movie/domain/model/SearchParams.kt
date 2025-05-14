package costalonga.tarsila.moviesapp.movie.domain.model

data class SearchParams(
    val query: String = "",
    val type: String = "",
    val yearOfRelease: String = ""
) {
    val showCache: Boolean
        get() = query.isEmpty()
    val showRemoteData: Boolean
        get() = query.isEmpty() && type.isNotEmpty()
}