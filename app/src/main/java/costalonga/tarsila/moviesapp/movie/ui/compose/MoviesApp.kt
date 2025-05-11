package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import costalonga.tarsila.moviesapp.core.ext.isNetworkAvailable
import costalonga.tarsila.moviesapp.movie.ui.MainViewModel

@Composable
fun MoviesApp() {
    val navController = rememberNavController()
    val backStack = navController.currentBackStackEntryAsState()
    val viewModel = hiltViewModel<MainViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                if (context.isNetworkAvailable()) viewModel.clearDatabase()
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
    Scaffold(modifier = Modifier) { _ ->
        val uiState by viewModel.movies.collectAsStateWithLifecycle()

        val moviesPagingData = if (uiState.showInitialState && context.isNetworkAvailable()) {
            null
        } else {
            viewModel.getCachedMovies.collectAsLazyPagingItems()
        }

        NavHost(navController = navController, startDestination = MainScreen) {
            composable<MainScreen> {
                MoviesMainScreen(moviesPagingData, uiState.searchParams, viewModel::onIntent)
            }

            /*            composable<DetailScreen> {
                            MovieDetailScreen()
                        }*/
        }
    }

}