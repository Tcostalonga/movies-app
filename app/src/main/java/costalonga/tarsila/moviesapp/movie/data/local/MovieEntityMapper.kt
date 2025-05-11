package costalonga.tarsila.moviesapp.movie.data.local

import costalonga.tarsila.moviesapp.movie.data.remote.model.MovieResponse
import costalonga.tarsila.moviesapp.movie.domain.model.Movie

fun MovieResponse.toEntity() = MovieEntity(
    title = title,
    imdbID = imdbID,
    year = year,
    poster = poster
)

fun MovieEntity.toDomain() = Movie(
    imdbID = imdbID,
    title = title,
    year = year,
    poster = poster
)