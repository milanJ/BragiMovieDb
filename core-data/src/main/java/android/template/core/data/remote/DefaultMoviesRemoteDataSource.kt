package android.template.core.data.remote

/**
 * Concrete implementation of [MoviesRemoteDataSource].
 */
open class DefaultMoviesRemoteDataSource internal constructor(
    private val apiService: ApiService
) : MoviesRemoteDataSource {

    override suspend fun getMovies(
        page: Int,
        genreIds: String?
    ): GetMoviesResponse = apiService.getMovies(page, genreIds)

    override suspend fun getMovieDetails(
        id: Long
    ): GetMovieDetailsResponse = apiService.getMovieDetails(id)
}
