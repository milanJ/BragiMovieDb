package android.template.core.data

import android.template.core.data.util.Result

/**
 * Interface to the TMDB API configuration.
 */
interface ConfigurationRepository {
    suspend fun getImageBaseUrl(): Result<String>
}
