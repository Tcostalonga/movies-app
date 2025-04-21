package costalonga.tarsila.moviesapp.movie.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.movie.ui.compose.MoviesMainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MoviesAppTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                val uiState by viewModel.movies.collectAsStateWithLifecycle(MainUiState())

                Scaffold(modifier = Modifier) { _ ->
                    MoviesMainScreen(
                        uiState = uiState, onIntent = viewModel::onIntent, modifier = Modifier
                    )
                }
            }
        }
    }
}