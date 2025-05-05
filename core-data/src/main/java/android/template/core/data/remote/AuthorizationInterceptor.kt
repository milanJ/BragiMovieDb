package android.template.core.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests.
 */
class AuthorizationInterceptor(
    private val token: String
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        val requestBuilder = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
        return chain.proceed(requestBuilder.build())
    }
}
