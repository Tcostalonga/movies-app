@file:OptIn(ExperimentalMaterial3Api::class)

package costalonga.tarsila.moviesapp.movie.ui.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import costalonga.tarsila.moviesapp.R
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import costalonga.tarsila.moviesapp.core.theme.MoviesTheme

@Composable
fun MainScreenTopAppBar(showAsVerticalList: Boolean, onShowAsVerticalListChange: () -> Unit) {
    TopAppBar(
        title = {},
        actions = {
            ChangeVisualizationComponent(
                showAsVerticalList = showAsVerticalList,
                onShowAsVerticalListChange = onShowAsVerticalListChange
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = MoviesTheme.colors.primary)
    )
}

@Composable
private fun ChangeVisualizationComponent(showAsVerticalList: Boolean, onShowAsVerticalListChange: () -> Unit) {
    IconButton(onClick = {
        onShowAsVerticalListChange()
    }) {
        Icon(
            tint =
                if (showAsVerticalList) {
                    MoviesTheme.colors.onBackground
                } else {
                    MoviesTheme.colors.onBackground.copy(alpha = 0.3f)
                },
            painter = painterResource(R.drawable.ic_bulleted_list_24),
            contentDescription = "Vertical list visualization"
        )
    }

    IconButton(onClick = {
        onShowAsVerticalListChange()
    }) {
        Icon(
            tint =
                if (showAsVerticalList.not()) {
                    MoviesTheme.colors.onBackground
                } else {
                    MoviesTheme.colors.onBackground.copy(alpha = 0.3f)
                },
            painter = painterResource(R.drawable.ic_view_carousel_24),
            contentDescription = "Vertical list visualization"
        )
    }
}

@Preview
@Composable
private fun MainScreenTopAppBarPreview() {
    MoviesAppTheme {
        MainScreenTopAppBar(false, {})
    }
}