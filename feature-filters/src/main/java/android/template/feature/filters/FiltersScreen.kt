package android.template.feature.filters

import android.template.core.data.GenreModel
import android.template.core.ui.MyApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

/**
 * This screen displays a list of movie genres and lets the user pick one as a filter, to filter movies shown in the Movies screen.
 */
@Composable
fun FiltersScreen(
    genreId: Int,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FiltersViewModel = hiltViewModel()
) {
    val genres by viewModel.uiState.collectAsStateWithLifecycle()
    when (genres) {
        is GenresUiState.Loading -> {
            LoadingState()
        }

        is GenresUiState.Error -> {
            ErrorState(
                errorMessage = stringResource(R.string.filters_screen_error_message_connection)
            )
        }

        is GenresUiState.Success -> {
            GenresList(
                modifier = modifier,
                genres = (genres as GenresUiState.Success).data,
                genreId,
                onGenreSelected = { genre ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(SELECTED_GENRE_MODEL_KEY, genre.toGenreModel())
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GenresList(
    modifier: Modifier = Modifier,
    genres: List<GenreUiModel>,
    selectedGenreId: Int,
    onGenreSelected: (GenreUiModel) -> Unit = {}
) {
    Scaffold(
        topBar = { TopBar() },
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(genres.size) { index ->
                val item = genres[index]
                val isSelected = item.id == selectedGenreId

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onGenreSelected.invoke(item)
                        }
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .padding(16.dp)
                ) {
                    Text(
                        text = item.name,
                        style = if (isSelected) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        else MaterialTheme.typography.bodyLarge
                    )
                }
                HorizontalDivider()
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
                .padding(paddingValues),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.testTag("Loading")
            )
        }
    }
}

@Composable
internal fun ErrorState(
    errorMessage: String
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar() {
    TopAppBar(
        title = {
            Text(stringResource(R.string.filters_screen_title))
        }
    )
}

// Previews:

@Preview(showBackground = true)
@Composable
private fun MoviesGridPreview() {
    val genres = listOf(
        GenreUiModel(
            id = -1,
            name = "All"
        ),
        GenreUiModel(
            id = 2,
            name = "Action",
        ),
        GenreUiModel(
            id = 3,
            name = "Comedy",
        ),
        GenreUiModel(
            id = 4,
            name = "Drama"
        )
    )

    MyApplicationTheme {
        GenresList(
            genres = genres,
            selectedGenreId = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    MyApplicationTheme {
        ErrorState(
            errorMessage = stringResource(R.string.filters_screen_error_message_connection)
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

private fun GenreUiModel.toGenreModel() = GenreModel(
    id = id,
    name = name
)

private const val SELECTED_GENRE_MODEL_KEY = "selected_genre_model"
