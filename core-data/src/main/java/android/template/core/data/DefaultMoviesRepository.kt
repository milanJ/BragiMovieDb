package android.template.core.data

import android.template.core.data.remote.MoviesRemoteDataSource
import android.template.core.data.remote.paging.MoviesPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Concrete implementation of [MoviesRepository].
 */
class DefaultMoviesRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher,
) : MoviesRepository {

    override fun getMoviesPagingSource(
        genreName: String?,
    ): MoviesPagingSource = MoviesPagingSource(genreName, remoteDataSource, coroutineDispatcher)
}
