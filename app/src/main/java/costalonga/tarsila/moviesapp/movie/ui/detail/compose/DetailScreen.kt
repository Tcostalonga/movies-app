@file:OptIn(ExperimentalMaterial3Api::class)

package costalonga.tarsila.moviesapp.movie.ui.detail.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import costalonga.tarsila.moviesapp.R
import costalonga.tarsila.moviesapp.core.compose.removeHorizontalPadding
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme
import costalonga.tarsila.moviesapp.movie.domain.model.MovieDetail
import costalonga.tarsila.moviesapp.movie.ui.detail.DetailUiState
import costalonga.tarsila.moviesapp.movie.ui.detail.DetailViewModel
import kotlinx.serialization.Serializable

@Serializable
data class DetailScreenRoute(val id: String)

@Composable
fun DetailScreenRoot(movieId: String, onBackClick: () -> Unit) {
    val viewModel = hiltViewModel<DetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.getMovieById(movieId)
    }

    DetailScreen(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@Composable
private fun DetailScreen(uiState: DetailUiState, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,

        /*

                     SMALL PART HEADER
                     Row(
                             modifier = Modifier
                                 .padding(MoviesTheme.spacing.dp16)
                                 .fillMaxWidth()
                                 .align(Alignment.BottomStart)
                         ) {
                             AsyncImage(
                                 model = uiState.movieDetail.poster,
                                 contentDescription = "Small movie poster image",
                                 modifier = Modifier
                                     .width(100.dp)
                                     .height(120.dp)
                                     .clip(MoviesTheme.shapes.small),
                                 placeholder = painterResource(R.drawable.avatar),
                                 contentScale = ContentScale.Fit,
                             )
                             Text(
                                 modifier = Modifier
                                     .align(Alignment.CenterVertically)
                                     .padding(MoviesTheme.spacing.dp16),
                                 text = uiState.movieDetail.title,
                                 style = MoviesTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
                             )
                         }*/

    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val movieDetail = uiState.movieDetail


        val movie = uiState.movieDetail
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = MoviesTheme.spacing.dp18)
                .fillMaxSize()
        ) {
/*
            SectionPosterAndTitle(
                poster = uiState.movieDetail.poster,
                title = uiState.movieDetail.title,
                onBackClick = onBackClick
            )
*/

            SectionGenres(movie.genre)

            Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))

            SectionBasicStatistics(movie)

            Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))

            SectionResume(movie)
        }
    }
}

@Composable
private fun SectionPosterAndTitle(modifier: Modifier = Modifier, onBackClick: () -> Unit, poster: String, title: String) {
    val posterGradientColors = listOf(
        Color(0x00121212),
        Color(0x81121212),
        Color(0xB7000000),
        Color(0xF0121212),
        Color(0xFF121212)
    )

    Box(
        modifier = modifier.removeHorizontalPadding(MoviesTheme.spacing.dp18)
    ) {
        AsyncImage(
            model = poster,
            contentDescription = "Movie Poster Image",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp),
            placeholder = painterResource(R.drawable.avatar),
            contentScale = ContentScale.Crop,
        )

        IconButton(
            onClick = {
                onBackClick()
            },
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent.copy(alpha = 0.1f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                modifier = Modifier.size(MoviesTheme.spacing.dp32),
                tint = Color.White,
                contentDescription = null,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = posterGradientColors))
                .height(100.dp),
        )
    }
}

@Composable
private fun SectionGenres(genres: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp8, Alignment.CenterHorizontally),
        modifier = Modifier
            .padding(top = MoviesTheme.spacing.dp16)
            .removeHorizontalPadding(MoviesTheme.spacing.dp18)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        genres.forEach { genre ->
            val color = colorForGenre(genre, isSystemInDarkTheme())
            Box(modifier = Modifier.border(1.dp, color, MoviesTheme.shapes.extraSmall)) {
                Text(
                    modifier = Modifier
                        .padding(vertical = MoviesTheme.spacing.dp2, horizontal = MoviesTheme.spacing.dp4),
                    text = genre.uppercase(),
                    style = MoviesTheme.typography.bodySmall,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun SectionBasicStatistics(movie: MovieDetail) {
    Row(
        modifier = Modifier
            .removeHorizontalPadding(MoviesTheme.spacing.dp18)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp16, Alignment.CenterHorizontally)
    ) {
        repeat(4) { index ->
            val (label, value) = when (index) {
                0 -> Pair(R.string.movie_detail_header_imdb_rating, "${movie.imdbRating}/10")
                1 -> Pair(R.string.movie_detail_header_runtime, movie.runtime)
                2 -> Pair(R.string.movie_detail_header_country, movie.country)
                3 -> Pair(R.string.movie_detail_header_released, movie.released)
                else -> Pair(0, "")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(label),
                    style = MoviesTheme.typography.bodySmall.copy(
                        fontSize = 9.sp, fontWeight = FontWeight.Bold,
                        color = MoviesTheme.colors.onBackground.copy(alpha = 0.6f)
                    ),
                )
                Text(
                    text = value,
                    style = MoviesTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                )

            }
        }
    }
}

@Composable
private fun SectionResume(movie: MovieDetail) {
    Text(
        text = stringResource(R.string.about_the_movie),
        style = MoviesTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp4))

    Text(
        text = movie.plot,
        style = MoviesTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))

    Text(
        text = stringResource(R.string.director),
        style = MoviesTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp4))

    Text(
        text = movie.director,
        style = MoviesTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))

    Text(
        text = stringResource(R.string.actors),
        style = MoviesTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )

    Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp4))

    Text(
        text = movie.actors,
        style = MoviesTheme.typography.bodyMedium
    )
}

@PreviewLightDark
@Composable
private fun DetailScreenPreview() {
    MoviesAppTheme {
        DetailScreen(
            DetailUiState(
                movieDetail = MovieDetail(
                    title = "Ad Astra",
                    year = "2019",
                    imdbRating = "6.5",
                    released = "20 Sep 2019",
                    runtime = "123 min",
                    genre = listOf("Adventure", "Drama", "Mystery"),
                    director = "James Gray",
                    actors = "Brad Pitt, Tommy Lee Jones, Ruth Negga, Donald Sutherland",
                    plot = "Astronaut Roy McBride undertakes a mission across an unforgiving solar system to uncover " +
                            "the truth about his missing father and his doomed expedition that now, 30 years later," +
                            " threatens the universe.",
                    country = "United States",
                    imdbID = "tt2935510",
                    poster = "https://image.tmdb.org/t/p/w500/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg" // imagem do TMDb
                )
            ),
            {}
        )
    }
}