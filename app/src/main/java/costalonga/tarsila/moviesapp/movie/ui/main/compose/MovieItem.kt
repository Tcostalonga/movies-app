package costalonga.tarsila.moviesapp.movie.ui.main.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme


@Composable
fun MovieItem(
    title: String,
    year: String,
    posterUrl: String,
    onMovieItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMovieItemClick() },
        elevation = CardDefaults.cardElevation(MoviesTheme.spacing.dp2),
        shape = MoviesTheme.shapes.extraSmall,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MoviesTheme.spacing.dp8, horizontal = MoviesTheme.spacing.dp12)
        ) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Movie Poster Image",
                modifier = Modifier
                    .height(80.dp)
                    .aspectRatio(14 / 9f)
                    .clip(RoundedCornerShape(MoviesTheme.spacing.dp2)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(MoviesTheme.spacing.dp12))

            Column(verticalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp2)) {
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MoviesTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )

                Text(
                    text = year,
                    style = MoviesTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovieItemPreview() {
    MoviesAppTheme {
        MovieItem(
            title = "Matrix Revolution",
            year = "2021",
            posterUrl = "https://search.yahoo.com/search?p=percipit",
            onMovieItemClick = {})
    }
}