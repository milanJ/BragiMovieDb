package android.template.core.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Maps the response from the TMDB API `https://api.themoviedb.org/3/genre/movie/list` to a Kotlin data class.
 */
data class GetGenresResponse(
    @SerializedName("genres") val results: List<Genre>
) {

    data class Genre(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String
    )
}
