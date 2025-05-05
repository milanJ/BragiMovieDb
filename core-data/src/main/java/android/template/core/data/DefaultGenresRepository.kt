package android.template.core.data

import android.template.core.data.remote.GenresRemoteDataSource
import android.template.core.data.remote.GetGenresResponse
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Concrete implementation of [GenresRepository].
 */
class DefaultGenresRepository @Inject constructor(
    private val remoteDataSource: GenresRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher,
) : GenresRepository {

    override fun getGenres(): Flow<List<GenreModel>> = flow {
        Log.d(TAG, "getGenres()")
        val results = remoteDataSource.getGenres()
            .results
            .toGenreModelsList()
            .toMutableList()
            .apply {
                add(0, GenreModel(id = -1, name = "All"))
            }
        emit(results)
    }
        .flowOn(coroutineDispatcher)
}

private fun List<GetGenresResponse.Genre>.toGenreModelsList(): List<GenreModel> = map {
    GenreModel(
        id = it.id,
        name = it.name,
    )
}

private const val TAG = "DefaultGenresRepository"
