package android.template.core.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Maps the response from the TMDB API `https://api.themoviedb.org/3/movie/{movie_id}` to a Kotlin data class.
 */
data class GetMovieDetailsResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("budget") val budget: Int,
    @SerializedName("revenue") val revenue: Int
)
