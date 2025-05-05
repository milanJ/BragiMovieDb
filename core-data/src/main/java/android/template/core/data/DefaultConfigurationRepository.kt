package android.template.core.data

import android.template.core.data.remote.ConfigurationRemoteDataSource
import android.template.core.data.remote.GetConfigurationResponse
import android.template.core.data.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Concrete implementation of [ConfigurationRepository].
 */
class DefaultConfigurationRepository @Inject constructor(
    private val remoteDataSource: ConfigurationRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher,
) : ConfigurationRepository {

    private var cachedConfiguration: GetConfigurationResponse? = null

    override suspend fun getImageBaseUrl(): Result<String> {
        return try {
            val configuration = getConfiguration()
            val selectedPosterSize = configuration.imagesConfiguration.posterSizes[configuration.imagesConfiguration.posterSizes.size / 2 + 1]
            return Result.Success(configuration.imagesConfiguration.secureBaseUrl + selectedPosterSize)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun getConfiguration(): GetConfigurationResponse = withContext(coroutineDispatcher) {
        if (cachedConfiguration != null) {
            return@withContext cachedConfiguration!!
        }

        cachedConfiguration = remoteDataSource.getConfiguration()
        return@withContext cachedConfiguration!!
    }
}

private const val TAG = "DefaultConfigurationRepository"
