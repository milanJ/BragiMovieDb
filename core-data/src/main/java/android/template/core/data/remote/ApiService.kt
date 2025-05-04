package android.template.core.data.remote

import retrofit2.http.*

/**
 * Exposes the TMDB API endpoints.
 */
interface ApiService {

    // ==================================== Movies: ================================================

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("with_genres") genres: String? = null
    ): GetMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Long,
        @Query("language") language: String = "en",
    ): GetMovieDetailsResponse

    // ==================================== Genres: ================================================

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "en",
    ): GetGenresResponse

    // =============================================================================================
}
