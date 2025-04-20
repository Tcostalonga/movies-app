package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme
import costalonga.tarsila.moviesapp.movie.domain.Movie

@Composable
fun MoviesMainScreen(movies: List<Movie>, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MoviesTheme.spacing.dp18)
    ) {

        //Search view

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MoviesTheme.spacing.dp12),
        ) {
            items(movies, key = { it.imdbID }) { movie ->
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
fun MovieItem(
    title: String,
    year: String,
    posterUrl: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MoviesTheme.shapes.large
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
                    .size(width = 100.dp, height = 150.dp)
                    .clip(RoundedCornerShape(MoviesTheme.spacing.dp8)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(MoviesTheme.spacing.dp12))

            Column {
                Text(
                    text = title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.size(MoviesTheme.spacing.dp8))

                Text(
                    text = year,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    val movies = listOf(
        Movie("Interestelar", "2014", "1", "https://link.com/poster1.jpg"),
        Movie("Duna", "2021", "2", "https://link.com/poster2.jpg"),
        Movie("Barbie", "2023", "3", "https://link.com/poster3.jpg")
    )
    MoviesMainScreen(movies)
}
