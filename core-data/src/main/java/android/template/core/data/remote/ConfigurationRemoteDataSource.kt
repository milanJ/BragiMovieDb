package android.template.core.data.remote

/**
 * Main entry point for accessing TMDB API configuration from the network.
 */
interface ConfigurationRemoteDataSource {
    suspend fun getConfiguration(): GetConfigurationResponse
}
