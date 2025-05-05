package android.template.feature.movies

import android.template.core.data.GenreModel
import android.template.core.data.MovieModel
import android.template.core.data.MoviesRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Handles the logic for the [MoviesScreen]
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val selectedGenre: StateFlow<GenreModel?> = savedStateHandle.getStateFlow(SELECTED_GENRE_MODEL_KEY, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val moviesFlow = selectedGenre.flatMapLatest { genre ->
        val genreName: String? = if (genre == null || genre.id == -1L) null else genre.name

        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesRepository.getMoviesPagingSource(genreName) }
        )
            .flow
            .map { pagingData ->
                pagingData.map { movie ->
                    movie.toMovieUiModel()
                }
            }
    }
        .cachedIn(viewModelScope)
}

data class MovieUiModel(
    val id: Long,
    val imageURL: String,
    val title: String,
    val rating: Float,
    val revenue: Int,
    val budget: Int
)

private fun MovieModel.toMovieUiModel() = MovieUiModel(
    id = id,
    imageURL = image,
    title = title,
    rating = rating,
    revenue = revenue,
    budget = budget
)

private const val SELECTED_GENRE_MODEL_KEY = "selected_genre_model"
