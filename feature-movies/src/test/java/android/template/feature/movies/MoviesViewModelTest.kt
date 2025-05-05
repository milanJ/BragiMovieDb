package android.template.feature.mymodel.ui.mymodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import android.template.core.data.MoviesRepository
import android.template.core.data.remote.paging.MoviesPagingSource
import android.template.feature.movies.MoviesViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class MoviesViewModelTest { // TODO: Implement tests

    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = MoviesViewModel(FakeMoviesRepository())
        assertEquals(viewModel.uiState.first(), MyModelUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = MyModelViewModel(FakeMoviesRepository())
        assertEquals(viewModel.uiState.first(), MyModelUiState.Loading)
    }
}

private class FakeMoviesRepository : MoviesRepository {

    private val data = mutableListOf<String>()

    override fun getMoviesPagingSource(
        genreId: Int
    ): MoviesPagingSource {
        TODO("Not yet implemented")
    }
}
