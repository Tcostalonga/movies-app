package costalonga.tarsila.moviesapp.movie.data.remote.mappers

import costalonga.tarsila.moviesapp.movie.data.remote.model.MovieDetailResponse
import costalonga.tarsila.moviesapp.movie.domain.model.MovieDetail

internal fun MovieDetailResponse.toDomain() = MovieDetail(
    title = title,
    year = year,
    released = released,
    runtime = runtime,
    genre = genre,
    director = director,
    actors = actors,
    plot = plot,
    imdbID = imdbID,
    poster = poster

)