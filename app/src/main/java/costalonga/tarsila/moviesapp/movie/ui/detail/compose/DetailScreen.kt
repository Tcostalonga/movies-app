@file:OptIn(ExperimentalMaterial3Api::class)

package costalonga.tarsila.moviesapp.movie.ui.detail.compose

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import costalonga.tarsila.moviesapp.R
import costalonga.tarsila.moviesapp.core.compose.CollapsingHeaderBox
import costalonga.tarsila.moviesapp.core.compose.rememberCollapsingHeaderConnection
import costalonga.tarsila.moviesapp.core.compose.removeHorizontalPadding
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme
import costalonga.tarsila.moviesapp.movie.domain.model.MovieDetail
import costalonga.tarsila.moviesapp.movie.ui.detail.DetailUiState
import costalonga.tarsila.moviesapp.movie.ui.detail.DetailViewModel
import costalonga.tarsila.moviesapp.movie.ui.main.compose.animation.PulseAnimation
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
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        val collapsingHeader = rememberCollapsingHeaderConnection()
        val scrollState = rememberLazyListState()

        val isToolbarVisible by remember {
            derivedStateOf {
                collapsingHeader.currentAlpha == 1f
            }
        }

        val view = LocalView.current

        if (view.isInEditMode.not() && isSystemInDarkTheme().not()) {
            LocalActivity.current?.window?.let { window ->
                WindowCompat.getInsetsController(window, view)
                    .isAppearanceLightStatusBars = isToolbarVisible
            }
        }

        val isFirstItemVisible by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex == 0 &&
                        scrollState.firstVisibleItemScrollOffset == 0
            }
        }
        collapsingHeader.canScrollHeader = isFirstItemVisible

        val statusBarGradientColors = listOf(Color(0x99000000), Color(0x00ffffff))
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        val movieDetail = uiState.movieDetail

        when {
            uiState.isLoading ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    PulseAnimation(
                        modifier = Modifier
                            .size(60.dp)
                    )
                }

            uiState.isError ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    NotFoundAnimation(modifier = Modifier.size(100.dp))
                }

            else ->
                CollapsingHeaderBox(
                    collapsingHeader = collapsingHeader,
                    modifier = Modifier.padding(innerPadding),
                    topBar = {
                        val topBarBackgroundColor = MoviesTheme.colors.background
                        TopAppBar(
                            title = {
                                Text(
                                    text = movieDetail.title,
                                    color = LocalContentColor.current.copy(alpha = collapsingHeader.currentAlpha)
                                )

                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        onBackClick()
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = topBarBackgroundColor.copy(alpha = 0.5f),
                                    ),
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                        contentDescription = null,
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                            ),
                            modifier = Modifier.drawBehind {
                                drawRect(topBarBackgroundColor.copy(alpha = collapsingHeader.currentAlpha))
                            },
                        )

                        Box(
                            modifier = Modifier
                                .alpha(1f - collapsingHeader.currentAlpha)
                                .fillMaxWidth()
                                .background(Brush.verticalGradient(colors = statusBarGradientColors))
                                .height(statusBarHeight),
                        )
                    },
                    header = {
                        Box(
                            modifier = modifier
                        ) {
                            SectionPosterAndTitle(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .removeHorizontalPadding(MoviesTheme.spacing.dp18),
                                poster = uiState.movieDetail.poster,
                            )

                            SectionMiniPosterAndTitle(
                                modifier = Modifier
                                    .padding(MoviesTheme.spacing.dp16)
                                    .align(Alignment.BottomStart),
                                poster = uiState.movieDetail.poster,
                                title = uiState.movieDetail.title
                            )

                        }
                    },
                    content = {
                        LazyColumn(
                            state = scrollState,
                            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                            modifier = Modifier
                                .padding(horizontal = MoviesTheme.spacing.dp18)
                        ) {
                            item {
                                SectionGenres(movieDetail.genre)
                                Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))
                            }

                            item {
                                SectionBasicStatistics(movieDetail)
                                Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))
                            }

                            item {
                                SectionResume(movieDetail)
                                Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))
                            }
                        }
                    })
        }
    }
}

@Composable
private fun SectionMiniPosterAndTitle(modifier: Modifier, poster: String, title: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp16)
    ) {

        AsyncImage(
            model = poster,
            contentDescription = "Small movie poster image",
            modifier = Modifier
                .clip(MoviesTheme.shapes.small)
                .width(90.dp)
                .height(120.dp),
            contentScale = ContentScale.Crop,
        )

        Text(
            modifier = Modifier,
            text = title,
            style = MoviesTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
        )
    }
}

@Composable
private fun SectionPosterAndTitle(
    modifier: Modifier = Modifier, poster: String
) {
    val posterGradientColors = listOf(
        Color(0x00121212),
        Color(0x81121212),
        Color(0xB7000000),
        Color(0xF0121212),
        Color(0xFF121212)
    )
    val posterAsyncPainter = rememberAsyncImagePainter(poster)
    val posterAsyncState by posterAsyncPainter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .height(500.dp)
            .fillMaxWidth(),
    ) {
        when (posterAsyncState) {
            is AsyncImagePainter.State.Loading -> {
                PulseAnimation(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = posterAsyncPainter,
                    contentDescription = "Movie Poster Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(colors = posterGradientColors))
                        .height(200.dp),
                )
            }

            is AsyncImagePainter.State.Error ->
                TODO("Add error placeholder")

            else -> Unit
        }
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
        repeat(3) { index ->
            val (label, value) = when (index) {
                0 -> Pair(R.string.movie_detail_header_imdb_rating, "${movie.imdbRating}/10")
                1 -> Pair(R.string.movie_detail_header_runtime, movie.runtime)
                2 -> Pair(R.string.movie_detail_header_released, movie.released)
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

    Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp16))

    Text(
        text = stringResource(R.string.country),
        style = MoviesTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp4))

    Text(
        text = movie.country,
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
