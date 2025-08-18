package costalonga.tarsila.moviesapp.movie.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import costalonga.tarsila.moviesapp.movie.ui.detail.compose.DetailScreenRoot
import costalonga.tarsila.moviesapp.movie.ui.detail.compose.DetailScreenRoute
import costalonga.tarsila.moviesapp.movie.ui.main.MainViewModel
import costalonga.tarsila.moviesapp.movie.ui.main.compose.MainScreenRoute
import costalonga.tarsila.moviesapp.movie.ui.main.compose.MoviesMainRoot

@Composable
fun MoviesApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreenRoute) {

        composable<MainScreenRoute> {
            val viewModel = hiltViewModel<MainViewModel>()

            MoviesMainRoot(
                viewModel = viewModel,
                onMovieItemClick = {
                    navController.navigate(DetailScreenRoute(it))
                }
            )
        }

        composable<DetailScreenRoute> { args ->
            val movieId = args.toRoute<DetailScreenRoute>().id
            DetailScreenRoot(
                movieId,
                onBackClick = {
                    navController.navigateUp()
                })
        }
    }
}
