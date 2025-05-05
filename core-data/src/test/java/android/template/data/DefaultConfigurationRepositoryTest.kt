package android.template.data

import android.template.core.data.DefaultConfigurationRepository
import android.template.core.data.remote.ConfigurationRemoteDataSource
import android.template.core.data.remote.GetConfigurationResponse
import android.template.core.data.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Unit tests for [DefaultConfigurationRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultConfigurationRepositoryTest {

    private lateinit var remoteDataSource: TestConfigurationRemoteDataSource
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var repository: DefaultConfigurationRepository

    @Before
    fun setup() {
        remoteDataSource = TestConfigurationRemoteDataSource()
        testDispatcher = StandardTestDispatcher()
        repository = DefaultConfigurationRepository(
            remoteDataSource = remoteDataSource,
            coroutineDispatcher = testDispatcher
        )
    }

    @Test
    fun getImageBaseUrl_returnsCorrectUrl() = runTest(testDispatcher) {
        val posterSizes = listOf("w92", "w154", "w185", "w342", "w500", "w780", "original")
        val secureBaseUrl = "https://image.tmdb.org/t/p/"
        val expectedSize = posterSizes[posterSizes.size / 2 + 1] // w500 based on the algorithm
        val expectedUrl = secureBaseUrl + expectedSize

        remoteDataSource.configurationResponse = GetConfigurationResponse(
            imagesConfiguration = GetConfigurationResponse.ImagesConfiguration(
                secureBaseUrl = secureBaseUrl,
                posterSizes = posterSizes,
                backdropSizes = listOf("w300", "w780", "w1280", "original")
            )
        )

        val result = repository.getImageBaseUrl()
        assertTrue(result is Result.Success)
        assertEquals(expectedUrl, (result as Result.Success).data)
    }

    @Test
    fun getImageBaseUrl_remoteDataSourceThrowsException_returnsError() = runTest(testDispatcher) {
        val exception = IOException("Network error")
        remoteDataSource.shouldThrowException = true
        remoteDataSource.exception = exception

        val result = repository.getImageBaseUrl()
        assertTrue(result is Result.Error)
        assertEquals(exception.message, (result as Result.Error).exception.message)
    }

    @Test
    fun getImageBaseUrl_cachesConfiguration() = runTest(testDispatcher) {
        val posterSizes = listOf("w92", "w154", "w185", "w342", "w500", "w780", "original")
        val secureBaseUrl = "https://image.tmdb.org/t/p/"

        remoteDataSource.configurationResponse = GetConfigurationResponse(
            imagesConfiguration = GetConfigurationResponse.ImagesConfiguration(
                secureBaseUrl = secureBaseUrl,
                posterSizes = posterSizes,
                backdropSizes = listOf("w300", "w780", "w1280", "original")
            )
        )

        repository.getImageBaseUrl()

        // Reset access count to verify cache is used
        remoteDataSource.accessCount = 0
        repository.getImageBaseUrl()

        // Then - remote data source is only accessed once due to caching
        assertEquals(0, remoteDataSource.accessCount)
    }

    /**
     * Test implementation of [ConfigurationRemoteDataSource]
     */
    private class TestConfigurationRemoteDataSource : ConfigurationRemoteDataSource {

        var configurationResponse = GetConfigurationResponse(
            imagesConfiguration = GetConfigurationResponse.ImagesConfiguration(
                secureBaseUrl = "",
                posterSizes = emptyList(),
                backdropSizes = emptyList()
            )
        )
        var shouldThrowException = false
        var exception = IOException("Test exception")
        var accessCount = 0

        override suspend fun getConfiguration(): GetConfigurationResponse {
            accessCount++
            if (shouldThrowException) {
                throw exception
            }
            return configurationResponse
        }
    }
}
