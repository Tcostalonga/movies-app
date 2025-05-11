package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.util.lerp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import costalonga.tarsila.moviesapp.movie.ui.MainUiState
import kotlin.math.absoluteValue
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListCarrouselComponent(movies: LazyPagingItems<Movie>, pagerState: PagerState, modifier: Modifier = Modifier) {

    val pageOffset = pagerState.getOffsetDistanceInPages(pagerState.currentPage).absoluteValue
    val fling = PagerDefaults.flingBehavior(state = pagerState, pagerSnapDistance = PagerSnapDistance.atMost(5))

    Column {
        HorizontalPager(state = pagerState, flingBehavior = fling, modifier = modifier) { index ->
            val movie = movies[index]
            movie?.let {
                MovieItemCarrousel(movie = movie, pageOffset, modifier = Modifier)
            }
        }

        FlowRow(
            Modifier
                .fillMaxWidth()
                .padding(top = MoviesTheme.spacing.dp48)
                .padding(horizontal = MoviesTheme.spacing.dp18),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { iteration ->
                val color = if (iteration == 0) {
                    MoviesTheme.colors.outline
                } else {
                    MoviesTheme.colors.surface
                }

                Box(
                    modifier = Modifier
                        .padding(MoviesTheme.spacing.dp2)
                        .clip(CircleShape)
                        .background(color)
                        .size(MoviesTheme.spacing.dp12)
                )
            }
        }
    }
}

@Composable
fun MovieItemCarrousel(movie: Movie, pageOffset: Float, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val scale = lerp(1.0f, 1.1f, pageOffset)
                scaleX /= scale
                scaleY /= scale
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = movie.poster,
            contentDescription = "Movie Poster Image",
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(MoviesTheme.spacing.dp12)),
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp24))
        Text(
            text = movie.title,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis, style = MoviesTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold, color = MoviesTheme.colors.primary
            )
        )

        Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp8))

        Text(
            text = movie.year, style = MoviesTheme.typography.bodyMedium.copy(
                color = MoviesTheme.colors.secondary, fontWeight = FontWeight.Medium
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun ListCarrouselComponentPreview(
    @PreviewParameter(PreviewMainScreen::class) uiState: MainUiState,
) {
    MoviesAppTheme {
        Surface {
            ListCarrouselComponent(
                movies = emptyFlow<PagingData<Movie>>().collectAsLazyPagingItems(),
                rememberPagerState(pageCount = { 1 })
            )
        }
    }
}