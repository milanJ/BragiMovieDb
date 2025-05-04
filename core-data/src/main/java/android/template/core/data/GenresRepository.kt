package android.template.core.data

import android.template.core.data.util.Result

/**
 * Interface to the movie genres data.
 */
interface GenresRepository {
    suspend fun getGenres(): Result<List<GenreModel>>
}
