package android.template.data

import android.template.core.data.ConfigurationRepository
import android.template.core.data.DefaultMoviesRepository
import android.template.core.data.remote.MoviesRemoteDataSource
import android.template.core.data.remote.paging.MoviesPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [DefaultMoviesRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultMoviesRepositoryTest {

    private lateinit var remoteDataSource: TestMoviesRemoteDataSource
    private lateinit var configurationRepository: TestConfigurationRepository
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var repository: DefaultMoviesRepository

    @Before
    fun setUp() {
        remoteDataSource = TestMoviesRemoteDataSource()
        configurationRepository = TestConfigurationRepository()
        testDispatcher = StandardTestDispatcher()
        repository = DefaultMoviesRepository(
            remoteDataSource,
            configurationRepository,
            testDispatcher
        )
    }

    @Test
    fun `getMoviesPagingSource returns valid MoviesPagingSource`() = runTest {
        // When getting a paging source with a genre ID
        val genreId = 28 // Action genre
        val pagingSource = repository.getMoviesPagingSource(genreId)

        // Then the paging source should not be null
        assertNotNull(pagingSource)

        // And it should be an instance of MoviesPagingSource
        assertEquals(MoviesPagingSource::class.java, pagingSource.javaClass)
    }

    @Test
    fun `getMoviesPagingSource with special genre id (-1) returns valid MoviesPagingSource`() = runTest {
        // When getting a paging source with the special "all genres" ID
        val allGenresId = -1
        val pagingSource = repository.getMoviesPagingSource(allGenresId)

        // Then the paging source should not be null
        assertNotNull(pagingSource)

        // And it should be an instance of MoviesPagingSource
        assertEquals(MoviesPagingSource::class.java, pagingSource.javaClass)
    }

    /**
     * Test implementation of [MoviesRemoteDataSource]
     */
    private class TestMoviesRemoteDataSource : MoviesRemoteDataSource {
        override suspend fun getMovies(page: Int, genreIds: String?) =
            throw UnsupportedOperationException("Not used in this test")

        override suspend fun getMovieDetails(id: Long) =
            throw UnsupportedOperationException("Not used in this test")
    }

    /**
     * Test implementation of [ConfigurationRepository]
     */
    private class TestConfigurationRepository : ConfigurationRepository {
        override suspend fun getImageBaseUrl() =
            throw UnsupportedOperationException("Not used in this test")
    }
}
