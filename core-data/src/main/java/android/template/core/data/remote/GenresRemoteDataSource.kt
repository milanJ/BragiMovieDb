package android.template.core.data.remote

/**
 * Main entry point for accessing movies genres data from the network.
 */
interface GenresRemoteDataSource {
    suspend fun getGenres(): GetGenresResponse
}
