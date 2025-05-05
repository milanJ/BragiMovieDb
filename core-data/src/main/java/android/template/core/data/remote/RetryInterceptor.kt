package android.template.core.data.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Interceptor to add retry logic to network requests.
 */
class RetryInterceptor(
    private val maxRetries: Int = DEFAULT_MAX_RETRIES
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        // Only retry `discover/movie` endpoint requests.
        if (!chain.request().url.encodedPath.contains("discover/movie")) {
            return chain.proceed(chain.request())
        }

        val request: Request = chain.request()
        var attempt = 0
        var lastException: Exception? = null

        while (attempt < maxRetries) {
            try {
                return chain.proceed(request)
            } catch (e: Exception) {
                lastException = e
                attempt++
            }
        }

        throw lastException ?: IOException("Unknown network error")
    }
}

private const val DEFAULT_MAX_RETRIES = 3
