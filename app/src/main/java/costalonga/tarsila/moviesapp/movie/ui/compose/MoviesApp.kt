@file:OptIn(ExperimentalMaterial3Api::class)

package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MoviesApp() {
    val navController = rememberNavController()
    val backStack = navController.currentBackStackEntryAsState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val lazyColumnState = rememberLazyListState()

    val viewModel = hiltViewModel<MainViewModel>()

    var showAsVerticalList by remember { mutableStateOf(true) }

    val shouldShowFab by remember {
        derivedStateOf {
            lazyColumnState.firstVisibleItemIndex > 20
        }
    }

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

    Scaffold(
        modifier = Modifier,
        topBar = {
            MainScreenTopAppBar(
                showAsVerticalList = showAsVerticalList,
                onShowAsVerticalListChange = { showAsVerticalList = it }
            )
        },
        floatingActionButton = {
            ScrollToTopFab(shouldShowFab, scope, lazyColumnState)
        }
    )
    { paddingValues ->
        val uiState by viewModel.movies.collectAsStateWithLifecycle()

        val moviesPagingData = if (uiState.showInitialState && context.isNetworkAvailable()) {
            null
        } else {
            viewModel.getCachedMovies.collectAsLazyPagingItems()
        }

        NavHost(navController = navController, startDestination = MainScreen) {
            composable<MainScreen> {
                MoviesMainScreen(
                    showAsVerticalList,
                    moviesPagingData,
                    uiState.searchParams,
                    lazyColumnState,
                    viewModel::onIntent,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            /*            composable<DetailScreen> {
                            MovieDetailScreen()
                        }*/
        }
    }

}

@Composable
private fun ScrollToTopFab(
    shouldShowFab: Boolean,
    scope: CoroutineScope,
    lazyColumnState: LazyListState
) {
    if (shouldShowFab) {
        FloatingActionButton(
            onClick = {
                scope.launch { lazyColumnState.animateScrollToItem(0) }
            }
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Scroll to the list top",
            )
        }
    }
}