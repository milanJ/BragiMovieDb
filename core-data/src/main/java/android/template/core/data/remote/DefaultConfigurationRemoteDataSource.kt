package android.template.core.data.remote

/**
 * Concrete implementation of [ConfigurationRemoteDataSource].
 */
open class DefaultConfigurationRemoteDataSource internal constructor(
    private val apiService: ApiService
) : ConfigurationRemoteDataSource {

    override suspend fun getConfiguration(): GetConfigurationResponse = apiService.getConfiguration()
}
