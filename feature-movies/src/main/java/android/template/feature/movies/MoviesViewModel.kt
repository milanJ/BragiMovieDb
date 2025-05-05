package android.template.feature.movies

import android.template.core.data.GenreModel
import android.template.core.data.MovieModel
import android.template.core.data.MoviesRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Handles the logic for the [MoviesScreen]
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val selectedGenre: MutableStateFlow<GenreModel?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val moviesFlow = selectedGenre.flatMapLatest { genre ->
        Log.d(TAG, "moviesFlow :: selectedGenre: $genre")

        // null value for genre means that not filter was applied, which is the same as GenreModel(-1, "All").
        val genreId: Int = genre?.id ?: -1

        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesRepository.getMoviesPagingSource(genreId) }
        )
            .flow
            .map { pagingData ->
                pagingData.map { movie ->
                    movie.toMovieUiModel()
                }
            }
    }
        .cachedIn(viewModelScope)

    fun filterByGenre(
        genre: GenreModel?
    ) {
        selectedGenre.value = genre
    }
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

private const val TAG = "MoviesViewModel"
