package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme
import costalonga.tarsila.moviesapp.movie.ui.MainScreenIntents
import costalonga.tarsila.moviesapp.movie.ui.MainUiState
import costalonga.tarsila.moviesapp.movie.ui.compose.animation.PulseAnimation

@Composable
fun MoviesMainScreen(uiState: MainUiState, onIntent: (MainScreenIntents) -> Unit, modifier: Modifier = Modifier) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MoviesTheme.colors.outline,
        backgroundColor = MoviesTheme.colors.outline.copy(alpha = 0.4f)
    )

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
                    value = uiState.searchQuery,
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
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                AnimatedContent(uiState.isLoading) { isLoading ->
                    when (isLoading) {
                        true -> PulseAnimation(Modifier.size(60.dp), MoviesTheme.colors.outline)
                        false -> LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = MoviesTheme.spacing.dp18),
                            verticalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp12),
                            contentPadding = PaddingValues(vertical = MoviesTheme.spacing.dp24)
                        ) {
                            items(uiState.movies, key = { it.imdbID }) { movie ->
                                MovieItem(
                                    title = movie.title,
                                    year = movie.year,
                                    posterUrl = movie.poster
                                )
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun MovieItem(
    title: String,
    year: String,
    posterUrl: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(MoviesTheme.spacing.dp2),
        shape = MoviesTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MoviesTheme.colors.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MoviesTheme.spacing.dp16)
        ) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Movie Poster Image",
                modifier = Modifier
                    .size(width = 100.dp, height = 130.dp)
                    .clip(RoundedCornerShape(MoviesTheme.spacing.dp8)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(MoviesTheme.spacing.dp12))

            Column {
                Text(
                    text = title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis, style = MoviesTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold, color = MoviesTheme.colors.primary
                    )
                )

                Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp8))

                Text(
                    text = year, style = MoviesTheme.typography.bodyMedium.copy(
                        color = MoviesTheme.colors.secondary, fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MainScreenPreview(
    @PreviewParameter(PreviewMainScreen::class) uiState: MainUiState,
) {
    MoviesAppTheme {
        Scaffold { _ ->
            MoviesMainScreen(uiState, {})
        }
    }
}
