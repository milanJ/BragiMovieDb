package android.template.core.data.remote

/**
 * Concrete implementation of [GenresRemoteDataSource].
 */
open class DefaultGenresRemoteDataSource internal constructor(
    private val apiService: ApiService
) : GenresRemoteDataSource {

    override suspend fun getGenres(): GetGenresResponse = apiService.getGenres()
}
