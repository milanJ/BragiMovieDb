package android.template.feature.filters

import android.template.core.data.GenreModel
import android.template.core.data.GenresRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Handles the logic for the [FiltersScreen]
 */
@HiltViewModel
class FiltersViewModel @Inject constructor(
    genresRepository: GenresRepository
) : ViewModel() {

    val uiState: StateFlow<GenresUiState> = genresRepository
        .getGenres()
        .map<List<GenreModel>, GenresUiState> {
            val uiModels = it.map { genre ->
                genre.toGenreUiModel()
            }
            GenresUiState.Success(data = uiModels)
        }
        .catch {
            emit(GenresUiState.Error(it))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GenresUiState.Loading)
}

sealed interface GenresUiState {

    object Loading : GenresUiState

    data class Error(
        val throwable: Throwable
    ) : GenresUiState

    data class Success(
        val data: List<GenreUiModel>
    ) : GenresUiState
}

data class GenreUiModel(
    val id: Int,
    val name: String
)

private fun GenreModel.toGenreUiModel() = GenreUiModel(
    id = id,
    name = name
)
