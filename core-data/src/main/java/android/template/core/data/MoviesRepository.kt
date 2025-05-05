package android.template.core.data

import android.template.core.data.remote.paging.MoviesPagingSource

/**
 * Interface to the movies data.
 */
interface MoviesRepository {

    fun getMoviesPagingSource(
        genreName: String?
    ): MoviesPagingSource
}
