package android.template.core.data.remote

/**
 * Main entry point for accessing movies data from the network.
 */
interface MoviesRemoteDataSource {

    suspend fun getMovies(
        page: Int,
        genreIds: String? = null
    ): GetMoviesResponse

    suspend fun getMovieDetails(
        id: Long
    ): GetMovieDetailsResponse
}
