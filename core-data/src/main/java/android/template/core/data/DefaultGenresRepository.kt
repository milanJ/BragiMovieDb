package android.template.core.data

import android.template.core.data.remote.GenresRemoteDataSource
import android.template.core.data.remote.GetGenresResponse
import android.template.core.data.util.Result
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultGenresRepository @Inject constructor(
    private val remoteDataSource: GenresRemoteDataSource,
    private val coroutineDispatcher: CoroutineDispatcher,
) : GenresRepository {

    override suspend fun getGenres(): Result<List<GenreModel>> = withContext(coroutineDispatcher) {
        Log.d(TAG, "getGenres()")
        return@withContext try {
            val genres = remoteDataSource.getGenres().results
            Result.Success(genres.toGenreModelsList())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

private fun List<GetGenresResponse.Genre>.toGenreModelsList(): List<GenreModel> = map {
    GenreModel(
        id = it.id,
        name = it.name,
    )
}

private const val TAG = "DefaultGenresRepository"
