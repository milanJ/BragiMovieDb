package android.template.feature.movies

import android.template.core.data.GenreModel
import android.template.core.data.MovieModel
import android.template.core.data.MoviesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Handles the logic for the [MoviesScreen]
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    fun moviesFlow(
        genre: GenreModel
    ): Flow<PagingData<MovieUiModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { moviesRepository.getMoviesPagingSource(genre) }
    )
        .flow
        .map { pagingData ->
            pagingData.map { movie ->
                movie.toMovieUiModel()
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
