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
    private val configurationRepository: ConfigurationRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
) : MoviesRepository {

    override fun getMoviesPagingSource(
        genreId: Int
    ): MoviesPagingSource = MoviesPagingSource(genreId, remoteDataSource, configurationRepository, coroutineDispatcher)
}
