package android.template.feature.movies

import android.icu.text.NumberFormat
import android.template.core.ui.MyApplicationTheme
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.flowOf
import java.util.Locale

/**
 * This screen displays a grid of movies. And is the main screen of the app.
 */
@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val movies = viewModel.moviesFlow.collectAsLazyPagingItems()
    when (movies.loadState.refresh) {
        is LoadState.Error -> {
            ErrorState(
                errorMessage = stringResource(R.string.movies_screen_error_message_connection),
                onRetry = { movies.retry() }
            )
        }

        LoadState.Loading -> {
            LoadingState()
        }

        else -> {
            MoviesGrid(modifier, movies)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoviesGrid(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<MovieUiModel>
) {
    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Open filter screen
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.movies_screen_button_caption_filter)
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(movies.itemCount) { index ->
                val movie = movies[index]
                if (movie != null) {
                    MovieCard(modifier, movie)
                }
            }
        }
    }
}

@Composable
internal fun MovieCard(
    modifier: Modifier = Modifier,
    movie: MovieUiModel,
) {
    Card(
        modifier = modifier
            .aspectRatio(0.7f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(0.dp)) {
            AsyncImage(
                model = movie.imageURL,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = stringResource(R.string.movies_screen_text_rating, formatRating(movie.rating)),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = stringResource(R.string.movies_screen_text_revenue, formatCurrency(movie.revenue)),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = stringResource(R.string.movies_screen_text_budget, formatCurrency(movie.budget)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
internal fun LoadingState() {
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
internal fun ErrorState(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.movies_screen_button_caption_retry))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar() {
    TopAppBar(
        title = {
            Text(stringResource(R.string.movies_screen_title))
        }
    )
}

private fun formatRating(
    rating: Float
): String = "%.1f".format(rating)

private fun formatCurrency(
    value: Int
): String {
    if(value > 1_000_000) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(value / 1_000_000) + "M"
    }

    return NumberFormat.getCurrencyInstance(Locale.US).format(value)
}

// Previews:

@Preview(showBackground = true)
@Composable
private fun MoviesGridPreview() {
    val pagerFlow = remember {
        flowOf(
            PagingData.from(
                listOf(
                    MovieUiModel(
                        id = 1,
                        imageURL = "https://example.com/image.jpg",
                        title = "Movie Title 1",
                        rating = 8.5F,
                        revenue = 1000000,
                        budget = 500000
                    ),
                    MovieUiModel(
                        id = 2,
                        imageURL = "https://example.com/image2.jpg",
                        title = "Movie Title 2",
                        rating = 1.5F,
                        revenue = 100000,
                        budget = 25000
                    ),
                    MovieUiModel(
                        id = 3,
                        imageURL = "https://example.com/image3.jpg",
                        title = "Movie Title 3",
                        rating = 10F,
                        revenue = 1000000,
                        budget = 250000
                    ),
                    MovieUiModel(
                        id = 4,
                        imageURL = "https://example.com/image3.jpg",
                        title = "Movie Title 3",
                        rating = 10F,
                        revenue = 1000000,
                        budget = 250000
                    )
                )
            )
        )
    }

    val movies = pagerFlow.collectAsLazyPagingItems()

    MyApplicationTheme {
        MoviesGrid(
            movies = movies
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    MyApplicationTheme {
        ErrorState(
            errorMessage = "Connection error. Please fix your connection and try again.",
            onRetry = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingStatePreview() {
    MyApplicationTheme {
        LoadingState()
    }
}
