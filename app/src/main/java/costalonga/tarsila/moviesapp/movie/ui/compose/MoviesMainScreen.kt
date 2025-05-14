@file:OptIn(ExperimentalSerializationApi::class)

package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import costalonga.tarsila.moviesapp.R
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme
import costalonga.tarsila.moviesapp.movie.domain.model.Movie
import costalonga.tarsila.moviesapp.movie.domain.model.SearchParams
import costalonga.tarsila.moviesapp.movie.ui.MainScreenIntents
import costalonga.tarsila.moviesapp.movie.ui.MainUiState
import costalonga.tarsila.moviesapp.movie.ui.compose.animation.PulseAnimation
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data object MainScreen

@Composable
fun MoviesMainScreen(
    movies: LazyPagingItems<Movie>?,
    searchParams: SearchParams,
    onIntent: (MainScreenIntents) -> Unit, modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyColumnState = rememberLazyListState()
    val pagerState = rememberPagerState(pageCount = { movies?.itemCount ?: 0 })

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MoviesTheme.colors.outline,
        backgroundColor = MoviesTheme.colors.outline.copy(alpha = 0.4f)
    )

    var showAsVerticalList by remember { mutableStateOf(true) }

    LaunchedEffect(movies?.loadState?.refresh) {
        val isLoading = movies?.loadState?.refresh is LoadState.Loading
        if (isLoading) {
            lazyColumnState.scrollToItem(0)
            pagerState.scrollToPage(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MoviesTheme.colors.primary)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = MoviesTheme.spacing.dp24)
        ) {
            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                OutlinedTextField(
                    value = searchParams.query,
                    onValueChange = {
                        onIntent(MainScreenIntents.OnSearchQueryChange(it))
                    },
                    placeholder = {
                        Text(text = "Search Movies")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MoviesTheme.colors.onBackground.copy(alpha = 0.7f)
                        )
                    },
                    shape = MoviesTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MoviesTheme.spacing.dp18),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = MoviesTheme.colors.outline,
                        focusedTextColor = MoviesTheme.colors.onBackground,
                        unfocusedTextColor = MoviesTheme.colors.onBackground,
                        focusedBorderColor = MoviesTheme.colors.outline,
                        unfocusedBorderColor = MoviesTheme.colors.secondary,
                        unfocusedContainerColor = MoviesTheme.colors.background,
                        focusedContainerColor = MoviesTheme.colors.background
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                )
            }
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = MoviesTheme.spacing.dp24,
                topEnd = MoviesTheme.spacing.dp24
            ),
            color = MoviesTheme.colors.background

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ChangeVisualizationComponent(
                    showAsVerticalList = showAsVerticalList,
                    onShowAsVerticalListChange = { showAsVerticalList = !showAsVerticalList })

                if (movies == null || searchParams.showRemoteData) {
                    Text(
                        stringResource(R.string.text_start_by_typing_a_title),
                        style = MoviesTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    when (movies.loadState.refresh) {
                        is LoadState.Loading -> {
                            PulseAnimation(Modifier.size(60.dp), MoviesTheme.colors.outline)
                        }

                        is LoadState.Error -> {
                            Text(
                                stringResource(R.string.error_movie_not_found),
                                style = MoviesTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }

                        is LoadState.NotLoading -> {
                            if (movies.itemCount >= 1) {
                                AnimatedContent(showAsVerticalList) { showAsGrid ->
                                    if (showAsGrid) {
                                        VerticalListComponent(lazyColumnState, movies)
                                    } else {
                                        CarrouselListComponent(movies, pagerState)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChangeVisualizationComponent(showAsVerticalList: Boolean, onShowAsVerticalListChange: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MoviesTheme.spacing.dp12, horizontal = MoviesTheme.spacing.dp18)
    ) {
        IconButton(onClick = {
            onShowAsVerticalListChange()
        }) {
            Icon(
                tint =
                    if (showAsVerticalList) {
                        MoviesTheme.colors.onBackground
                    } else {
                        MoviesTheme.colors.onBackground.copy(alpha = 0.3f)
                    },
                painter = painterResource(R.drawable.ic_bulleted_list_24),
                contentDescription = "Vertical list visualization"
            )
        }

        IconButton(onClick = {
            onShowAsVerticalListChange()
        }) {
            Icon(
                tint =
                    if (showAsVerticalList.not()) {
                        MoviesTheme.colors.onBackground
                    } else {
                        MoviesTheme.colors.onBackground.copy(alpha = 0.3f)
                    },
                painter = painterResource(R.drawable.ic_view_carousel_24),
                contentDescription = "Vertical list visualization"
            )
        }
    }
}

@Composable
private fun VerticalListComponent(lazyColumnState: LazyListState, movies: LazyPagingItems<Movie>) {
    LazyColumn(
        state = lazyColumnState,
        modifier = Modifier
            .padding(horizontal = MoviesTheme.spacing.dp18),
        verticalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp12),
        contentPadding = PaddingValues(bottom = MoviesTheme.spacing.dp32)
    ) {
        items(movies.itemCount, key = movies.itemKey { it.imdbID }) { index ->
            movies[index]?.let { movie ->
                MovieItem(
                    title = movie.title,
                    year = movie.year,
                    posterUrl = movie.poster
                )
            }
        }
    }
}

@Composable
private fun CarrouselListComponent(movies: LazyPagingItems<Movie>, pagerState: PagerState) {
    ListCarrouselComponent(
        movies,
        pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
    )
}

@PreviewLightDark
@Composable
private fun MainScreenPreview(
    @PreviewParameter(PreviewMainScreen::class) uiState: MainUiState,
) {
    MoviesAppTheme {
        Scaffold { _ ->
            MoviesMainScreen(
                movies = emptyFlow<PagingData<Movie>>().collectAsLazyPagingItems(),
                searchParams = SearchParams(),
                onIntent = {},
            )
        }
    }
}
