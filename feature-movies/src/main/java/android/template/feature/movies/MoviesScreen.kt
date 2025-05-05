package android.template.feature.movies

import android.icu.text.NumberFormat
import android.template.core.data.GenreModel
import android.template.core.ui.MyApplicationTheme
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
    val movies = viewModel.moviesFlow(GenreModel(-1, "All")).collectAsLazyPagingItems()
    when (movies.loadState.refresh) {
        is LoadState.Error -> {
            ErrorState(
                errorMessage = "Connection error. Please fix your connection and try again.",
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

@Composable
internal fun MoviesGrid(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<MovieUiModel>
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Open filter screen
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.movies_screen_filter_button_caption)
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            .fillMaxWidth()
            .aspectRatio(0.95f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = movie.imageURL,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Rating: ${movie.rating}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Revenue: ${formatCurrency(movie.revenue)}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Budget: ${formatCurrency(movie.budget)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
internal fun LoadingState() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ErrorState(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

private fun formatCurrency(
    value: Int
): String = NumberFormat.getCurrencyInstance(Locale.US).format(value)

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
