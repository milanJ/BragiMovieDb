package android.template.core.data

import android.template.core.data.remote.paging.MoviesPagingSource
import javax.inject.Inject

/**
 * Fake implementation of [MoviesRepository] for testing purposes.
 */
class FakeMoviesRepository @Inject constructor() : MoviesRepository {

    override fun getMoviesPagingSource(
        genreName: String?
    ): MoviesPagingSource {
        TODO("Not yet implemented")
    }
}
