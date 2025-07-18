package costalonga.tarsila.moviesapp.movie.ui.main.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import costalonga.tarsila.moviesapp.R
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
                error = painterResource(R.drawable.ic_image_not_found),
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