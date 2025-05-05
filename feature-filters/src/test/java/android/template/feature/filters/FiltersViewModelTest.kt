package android.template.feature.filters

import android.template.core.data.GenreModel
import android.template.core.data.GenresRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit test for the [FiltersViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FiltersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState starts with Loading`() = runTest {
        val repository = object : GenresRepository {

            private val data = mutableListOf<GenreModel>()

            override fun getGenres(): Flow<List<GenreModel>> = flow { emit(data.toList()) }
        }

        val viewModel = FiltersViewModel(repository)
        assertEquals(viewModel.uiState.first(), GenresUiState.Loading)
    }

    @Test
    fun `uiState emits Error when repository throws`() = runTest {
        val repository = object : GenresRepository {
            override fun getGenres(): Flow<List<GenreModel>> = flow {
                throw Exception("Something went wrong.")
            }
        }

        val viewModel = FiltersViewModel(repository)

        val state = viewModel.uiState.first { it is GenresUiState.Error }
        state as GenresUiState.Error

        assertEquals(state.throwable.message, "Something went wrong.")
    }

    @Test
    fun `uiState emits Success when genres are returned`() = runTest {
        val repository = object : GenresRepository {
            override fun getGenres(): Flow<List<GenreModel>> = flow {
                emit(
                    listOf(
                        GenreModel(id = 1, name = "Action"),
                        GenreModel(id = 2, name = "Comedy")
                    )
                )
            }
        }

        val viewModel = FiltersViewModel(repository)

        val state = viewModel.uiState.first { it is GenresUiState.Success }
        state as GenresUiState.Success

        assertEquals(2, state.data.size)
        assertEquals(1, state.data[0].id)
        assertEquals("Action", state.data[0].name)
        assertEquals(2, state.data[1].id)
        assertEquals("Comedy", state.data[1].name)
    }
}
