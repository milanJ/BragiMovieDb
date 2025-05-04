package android.template.core.data

import android.template.core.data.remote.paging.MoviesPagingSource
import javax.inject.Inject

class FakeMoviesRepository @Inject constructor() : MoviesRepository {

    override suspend fun getMoviesPagingSource(
        genre: GenreModel
    ): MoviesPagingSource {
        TODO("Not yet implemented")
    }
}
