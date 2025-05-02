package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import costalonga.tarsila.moviesapp.movie.ui.MainViewModel

@Composable
fun MoviesApp() {
    val navController = rememberNavController()
    val backStack = navController.currentBackStackEntryAsState()
    val viewModel = hiltViewModel<MainViewModel>()

    Scaffold(modifier = Modifier) { _ ->
        val uiState by viewModel.movies.collectAsStateWithLifecycle()

        NavHost(navController = navController, startDestination = MainScreen) {
            composable<MainScreen> {
                MoviesMainScreen(uiState.movies, uiState.isLoading, uiState.isError, uiState.searchQuery, viewModel::onIntent)
            }

            /*            composable<DetailScreen> {
                            MovieDetailScreen()
                        }*/
        }
    }

}