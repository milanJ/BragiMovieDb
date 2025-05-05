package android.template.core.data.remote.paging

import android.template.core.data.ConfigurationRepository
import android.template.core.data.MovieModel
import android.template.core.data.remote.GetMoviesResponse
import android.template.core.data.remote.MoviesRemoteDataSource
import android.template.core.data.util.Result
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * A [PagingSource] that loads movies from a remote data source.
 */
class MoviesPagingSource(
    private val genreName: String?,
    private val remoteDataSource: MoviesRemoteDataSource,
    private val configurationRepository: ConfigurationRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : PagingSource<Int, MovieModel>() {

    override fun getRefreshKey(
        state: PagingState<Int, MovieModel>
    ): Int? = state.anchorPosition?.let { position ->
        state.closestPageToPosition(position)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieModel> = withContext(coroutineDispatcher) {
        val page = params.key ?: 1

        return@withContext try {
            val items: List<MovieModel> = loadPage(page)
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun loadPage(
        page: Int
    ): List<MovieModel> {
        Log.d(TAG, "loadPage() :: page = $page, genre = $genreName")
        val movies = remoteDataSource.getMovies(page, genreName).results
        return mergeMovieDetails(movies)
    }

    /**
     * Fetch move details for each movie in the list. Do it in parallel and use structured concurrency.
     */
    private suspend fun mergeMovieDetails(
        movies: List<GetMoviesResponse.Movie>
    ): List<MovieModel> = coroutineScope {
        Log.d(TAG, "mergeMovieDetails() :: For ${movies.size} movies")
        val imageBaseUrlResponse = configurationRepository.getImageBaseUrl()
        if (imageBaseUrlResponse is Result.Error) {
            throw Exception("Couldn't fetch image base url.", imageBaseUrlResponse.exception)
        }
        val imageBaseUrl = (imageBaseUrlResponse as Result.Success).data

        val movieModels = movies.map { item ->
            async {
                Log.d(TAG, "mergeMovieDetails() :: Get details for '${item.title}'")
                val details = remoteDataSource.getMovieDetails(item.id)
                item.toMovieModel(details.budget, details.revenue, imageBaseUrl)
            }
        }
            .awaitAll()

        movieModels
    }
}

private fun GetMoviesResponse.Movie.toMovieModel(
    budget: Int,
    revenue: Int,
    imageBaseUrl: String
): MovieModel = MovieModel(
    id = this.id,
    image = "$imageBaseUrl${this.posterPath}",
    title = this.title,
    rating = this.voteAverage,
    revenue = budget,
    budget = revenue
)

private const val TAG = "MoviesPagingSource"
