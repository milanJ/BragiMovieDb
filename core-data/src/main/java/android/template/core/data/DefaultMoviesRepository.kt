package android.template.core.data

import android.template.core.data.remote.GetMoviesResponse
import android.template.core.data.remote.MoviesRemoteDataSource
import android.template.core.data.remote.paging.MoviesPagingSource
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Concrete implementation of [MoviesRepository].
 */
class DefaultMoviesRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher,
) : MoviesRepository {

    override suspend fun getMoviesPagingSource(
        genre: GenreModel,
    ): MoviesPagingSource = object : MoviesPagingSource() {

        override suspend fun loadPage(
            page: Int
        ): List<MovieModel> = withContext(coroutineDispatcher) {
            Log.d(TAG, "getMoviesPagingSource() :: loadPage() :: page = $page, genre = $genre")
            val movies = remoteDataSource.getMovies(page, genre.name).results
            return@withContext mergeMovieDetails(movies)
        }
    }

    /**
     * Fetch move details for each movie in the list. Do it in parallel and use structured concurrency.
     */
    private suspend fun mergeMovieDetails(
        movies: List<GetMoviesResponse.Movie>
    ): List<MovieModel> = coroutineScope {
        val movieModels = movies.map { item ->
            async {
                val details = remoteDataSource.getMovieDetails(item.id)
                item.toMovieModel(details.budget, details.revenue)
            }
        }
            .awaitAll()

        movieModels
    }
}

private fun GetMoviesResponse.Movie.toMovieModel(
    budget: Int,
    revenue: Int
): MovieModel = MovieModel(
    id = this.id,
    image = "${BuildConfig.TMDB_BASE_URL}${this.posterPath}",
    title = this.title,
    rating = this.voteAverage,
    revenue = budget,
    budget = revenue
)

private const val TAG = "DefaultMoviesRepository"
