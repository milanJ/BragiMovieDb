package android.template.core.data.remote.paging

import android.template.core.data.MovieModel
import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * A [PagingSource] that loads movies from a remote data source.
 */
abstract class MoviesPagingSource : PagingSource<Int, MovieModel>() {

    abstract suspend fun loadPage(
        page: Int
    ): List<MovieModel>

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieModel> {
        val page = params.key ?: 1

        return try {
            val items: List<MovieModel> = loadPage(page)
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, MovieModel>
    ): Int? = state.anchorPosition?.let { position ->
        state.closestPageToPosition(position)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
    }
}
