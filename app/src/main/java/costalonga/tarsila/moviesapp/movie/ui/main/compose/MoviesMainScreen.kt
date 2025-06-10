@file:OptIn(ExperimentalFoundationApi::class)

package costalonga.tarsila.moviesapp.movie.ui.main.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
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
import costalonga.tarsila.moviesapp.movie.ui.main.MainScreenIntents
import costalonga.tarsila.moviesapp.movie.ui.main.compose.animation.PulseAnimation
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object MainScreen

@Composable
fun MoviesMainScreen(
    showAsVerticalList: Boolean,
    movies: LazyPagingItems<Movie>?,
    searchParams: SearchParams,
    lazyColumnState: LazyListState,
    onIntent: (MainScreenIntents) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { movies?.itemCount ?: 0 })
    val focusManager = LocalFocusManager.current

    var savedIndex by rememberSaveable { mutableIntStateOf(0) }
    var savedOffset by rememberSaveable { mutableIntStateOf(0) }

    val shouldHideTheKeyboard by remember {
        derivedStateOf {
            lazyColumnState.isScrollInProgress
        }
    }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MoviesTheme.colors.outline,
        backgroundColor = MoviesTheme.colors.outline.copy(alpha = 0.4f)
    )

    LaunchedEffect(movies?.loadState?.refresh) {
        val isLoading = movies?.loadState?.refresh is LoadState.Loading
        if (isLoading) {
            lazyColumnState.scrollToItem(0)
            pagerState.scrollToPage(0)
        }
    }

    LaunchedEffect(showAsVerticalList) {
        if (showAsVerticalList) {
            scope.launch {
                lazyColumnState.scrollToItem(savedIndex, savedOffset)
            }
        }
    }

    if (shouldHideTheKeyboard) {
        focusManager.clearFocus()
    }

    LazyColumn(
        state = lazyColumnState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp16),
        contentPadding = PaddingValues(bottom = MoviesTheme.spacing.dp32)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(
                    bottomStart = MoviesTheme.spacing.dp24,
                    bottomEnd = MoviesTheme.spacing.dp24
                ),
                modifier = Modifier,
                color = MoviesTheme.colors.primary
            )
            {
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
                            .padding(bottom = MoviesTheme.spacing.dp18)
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
        }

        if (movies == null || searchParams.showRemoteData) {
            containerWrapper {
                Text(
                    stringResource(R.string.text_start_by_typing_a_title),
                    style = MoviesTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            when (movies.loadState.refresh) {
                is LoadState.Loading -> {
                    containerWrapper {
                        PulseAnimation(Modifier.size(60.dp), MoviesTheme.colors.outline)
                    }
                }

                is LoadState.Error -> {
                    containerWrapper {
                        Text(
                            text = stringResource(R.string.error_movie_not_found),
                            style = MoviesTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is LoadState.NotLoading -> {
                    if (movies.itemCount >= 1) {
                        if (showAsVerticalList) {
                            verticalListComponent(movies)
                        } else {
                            if (lazyColumnState.firstVisibleItemIndex != 0 && lazyColumnState.firstVisibleItemScrollOffset != 0) {
                                savedIndex = lazyColumnState.firstVisibleItemIndex
                                savedOffset = lazyColumnState.firstVisibleItemScrollOffset
                            }
                            carrouselListComponent(movies, pagerState)
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.containerWrapper(content: @Composable () -> Unit) {
    item {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoviesTheme.spacing.dp18)
        ) {
            content()
        }
    }
}

private fun LazyListScope.verticalListComponent(movies: LazyPagingItems<Movie>) {
    items(movies.itemCount, key = movies.itemKey { it.imdbID }) { index ->
        movies[index]?.let { movie ->
            MovieItem(
                title = movie.title,
                year = movie.year,
                posterUrl = movie.poster,
                modifier = Modifier.padding(horizontal = MoviesTheme.spacing.dp18)
            )
        }
    }
}

private fun LazyListScope.carrouselListComponent(movies: LazyPagingItems<Movie>, pagerState: PagerState) {
    item {
        ListCarrouselComponent(
            movies,
            pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillParentMaxHeight(0.8f)
        )
    }
}

@PreviewLightDark
@Composable
private fun MainScreenPreview(
    @PreviewParameter(MoviePagingDataPreviewParameter::class) pagingData: PagingData<Movie>,
) {
    val moviesPagingFlow = flowOf(
        pagingData,
    ).collectAsLazyPagingItems()

    MoviesAppTheme {
        Scaffold { _ ->
            MoviesMainScreen(
                showAsVerticalList = true,
                movies = moviesPagingFlow,
                searchParams = SearchParams(),
                rememberLazyListState(),
                onIntent = {},
            )
        }
    }
}
