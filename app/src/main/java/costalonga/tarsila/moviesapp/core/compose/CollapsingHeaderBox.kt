package costalonga.tarsila.moviesapp.core.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import costalonga.tarsila.moviesapp.R
import costalonga.tarsila.moviesapp.core.theme.MoviesAppTheme
import kotlinx.coroutines.launch

@Composable
fun rememberCollapsingHeaderConnection(): CollapsingHeaderConnection {
    val density = LocalDensity.current
    return rememberSaveable(saver = CollapsingHeaderConnection.saver(density)) {
        CollapsingHeaderConnection(density)
    }
}

class CollapsingHeaderConnection internal constructor(private val density: Density) : NestedScrollConnection {

    var collapsedHeight: Dp = 0.dp
        internal set
    var expandedHeight: Dp = 0.dp
        internal set(value) {
            field = value
            if (currentOffset == 0.dp) {
                currentOffset = expandedHeight
            }
            if (currentOffset > expandedHeight) {
                currentOffset = expandedHeight
            }
        }
    var currentOffset by mutableStateOf(0.dp)
        internal set
    var currentAlpha by mutableFloatStateOf(0f)
        internal set

    var canScrollHeader: Boolean = true
    var showTopBarAtHeight: Dp? = null

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        // scroll amount
        val delta = available.y
        // if cannot scroll header, return zero to scroll the content instead of header
        if (canScrollHeader.not()) return Offset.Zero
        // if there is no header, return zero to scroll the content instead of header
        if (expandedHeight < collapsedHeight) return Offset.Zero

        // calculate the new offset based on delta scrolled
        val newOffset = currentOffset + with(density) { delta.toDp() }
        val previousOffset = currentOffset
        // save the new calculated offset and coerse between minimum and maximum available height
        currentOffset = if (expandedHeight < collapsedHeight) {
            collapsedHeight
        } else {
            newOffset.coerceIn(collapsedHeight, expandedHeight)
        }

        // if the new offset is already the maximum or minimum size, the preScroll is consumed (return 0)
        val consumed = currentOffset - previousOffset

        // calculate the delta of the current scroll distance
        val offsetDelta = if (expandedHeight < collapsedHeight) {
            1f
        } else {
            val maxTopBarAtHeight = expandedHeight - collapsedHeight
            val topBarAtHeight = showTopBarAtHeight
            val positionWithoutAlpha = if (topBarAtHeight != null && topBarAtHeight in 1.dp..maxTopBarAtHeight) {
                expandedHeight - topBarAtHeight
            } else {
                collapsedHeight
            }

            (currentOffset - positionWithoutAlpha) / (expandedHeight - positionWithoutAlpha)
        }

        // set a alpha value based on offset delta
        currentAlpha = 1f - offsetDelta.coerceIn(0f, 1f)

        return if (consumed != 0.dp) {
            available
        } else {
            Offset.Zero
        }
    }

    companion object {
        internal fun saver(density: Density): Saver<CollapsingHeaderConnection, *> {
            return listSaver(
                save = {
                    listOf(
                        it.currentOffset.value,
                        it.currentAlpha,
                    )
                },
                restore = {
                    CollapsingHeaderConnection(density).apply {
                        currentOffset = it[0].dp
                        currentAlpha = it[1]
                    }
                },
            )
        }
    }
}

/**
 * A composable that creates a collapsing header effect.
 *
 * This function provides a layout that allows a header to collapse and expand based on scroll interactions.
 *
 * @param topBar A composable function representing the top bar. This typically contains elements like
 *   titles or navigation icons and remains fixed at the top.
 * @param header A composable function representing the header. This is the part that collapses and
 *   expands as the user scrolls.
 * @param collapsingHeader A [CollapsingHeaderConnection] instance that manages the collapsing
 *   behavior, such as the collapsed and expanded heights, and the current offset.
 * @param modifier A [Modifier] for this composable, allowing for customization of the layout.
 * @param scrollHeader Boolean flag to indicate whether the header should scroll (collapse/expand).
 *   If `true`, the header moves according to the scroll offset. If `false`, the header remains static.
 * @param content A composable function representing the main content area, which is scrollable and
 *   appears below the header and top bar.
 */
@Composable
fun CollapsingHeaderBox(
    topBar: @Composable () -> Unit,
    header: @Composable () -> Unit,
    collapsingHeader: CollapsingHeaderConnection,
    modifier: Modifier = Modifier,
    scrollHeader: Boolean = true,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    SubcomposeLayout(
        modifier = modifier.nestedScroll(collapsingHeader),
    ) { constraints ->
        // calculate top bar height
        val topBarPlaceables = subcompose("topBar", topBar).map {
            it.measure(constraints)
        }

        // set collapsing portion as the minimum height possible
        val topBarHeight = topBarPlaceables.sumOf { it.height }
        collapsingHeader.collapsedHeight = with(density) { topBarHeight.toDp() }

        // calculate header height
        val headerPlaceables = subcompose("header", header).map {
            it.measure(constraints)
        }

        // set collapsing portion as the maximum height possible
        val headerHeight = headerPlaceables.sumOf { it.height }
        collapsingHeader.expandedHeight = with(density) { headerHeight.toDp() }

        // calculate content height
        val contentPlaceables = subcompose("content", content).map {
            it.measure(
                constraints.copy(
                    // set the content height as the remaining height available
                    maxHeight = constraints.maxHeight - topBarHeight,
                ),
            )
        }

        if (headerHeight < topBarHeight) {
            collapsingHeader.currentOffset = with(density) { topBarHeight.toDp() }
            collapsingHeader.currentAlpha = 1f
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            // place header composable layout
            val topBarOffset = (collapsingHeader.currentOffset - collapsingHeader.expandedHeight)
            headerPlaceables.map {
                it.place(
                    x = 0,
                    y = if (scrollHeader) { // set if the header should move or not with the scroll
                        topBarOffset.roundToPx()
                    } else {
                        0
                    },
                )
            }

            // place top bar composable layout
            topBarPlaceables.map {
                it.place(0, 0)
            }

            // place content composable layout setting the calculated offset
            contentPlaceables.map {
                it.place(0, collapsingHeader.currentOffset.roundToPx())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CollapsingHeaderBoxPreview() {
    MoviesAppTheme {
        Scaffold(
            contentWindowInsets = WindowInsets(0),
        ) { innerPadding ->
            val collapsingHeader = rememberCollapsingHeaderConnection()
            val scope = rememberCoroutineScope()
            val scrollState = rememberLazyListState()

            val isFirstItemVisible by remember {
                derivedStateOf {
                    scrollState.firstVisibleItemIndex == 0 &&
                            scrollState.firstVisibleItemScrollOffset == 0
                }
            }
            collapsingHeader.canScrollHeader = isFirstItemVisible

            CollapsingHeaderBox(
                collapsingHeader = collapsingHeader,
                topBar = {
                    TopAppBar(
                        title = {
                            //    Text("AppBar")
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                        actions = {
                            IconButton(
                                onClick = {},
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.White,
                                ),
                            ) {
                                Icon(
                                    ImageVector.vectorResource(R.drawable.ic_bulleted_list_24),
                                    contentDescription = null,
                                )
                            }
                        },
                        modifier = Modifier.drawBehind {
                            drawRect(Color.White.copy(alpha = collapsingHeader.currentAlpha))
                        },
                    )
                },
                header = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(Color.Cyan)
                            .fillMaxWidth()
                            .height(300.dp),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background), null,
                            modifier = Modifier.size(920.dp),
                            contentScale = ContentScale.FillBounds
                        )
                        Text("BANNER")
                    }
                },
                content = {
                    val pagerState = rememberPagerState(pageCount = { 2 })
                    val tabs = listOf("Home", "Settings")
                    Column {
                        TabRow(
                            selectedTabIndex = pagerState.currentPage,
                        ) {
                            tabs.forEachIndexed { index, value ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        scope.launch {
                                            pagerState.scrollToPage(index)
                                        }
                                    },
                                    text = {
                                        Text(value)
                                    },
                                )
                            }
                        }
                        HorizontalPager(state = pagerState) { page ->
                            ItemLists(
                                state = if (page == 0) scrollState else rememberLazyListState(),
                                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                                modifier = Modifier.background(
                                    if (page == 0) Color.Red else Color.Blue,
                                ),
                            )
                        }
                    }
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun ItemLists(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        state = state,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(25) {
            Text(
                text = "Item $it",
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}
