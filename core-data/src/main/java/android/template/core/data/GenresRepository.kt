package android.template.core.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface to the movie genres data.
 */
interface GenresRepository {
    fun getGenres(): Flow<List<GenreModel>>
}
