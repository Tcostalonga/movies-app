package costalonga.tarsila.moviesapp.movie.ui.detail.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable

@Serializable
data class DetailScreen(val id: String)

@Composable
fun DetailScreen(modifier: Modifier = Modifier) {

}


@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen()

}