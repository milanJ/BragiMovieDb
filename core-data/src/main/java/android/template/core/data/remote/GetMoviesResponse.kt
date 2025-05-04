package android.template.core.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Maps the response from the TMDB API `https://api.themoviedb.org/3/discover/movie` to a Kotlin data class.
 */
data class GetMoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
) {

    data class Movie(
        @SerializedName("id") val id: Long,
        @SerializedName("original_language") val originalLanguage: String,
        @SerializedName("original_title") val originalTitle: String,
        @SerializedName("overview") val overview: String,
        @SerializedName("popularity") val popularity: Float,
        @SerializedName("poster_path") val posterPath: String,
        @SerializedName("release_date") val releaseDate: String,
        @SerializedName("title") val title: String,
        @SerializedName("vote_average") val voteAverage: Float,
        @SerializedName("vote_count") val voteCount: Int
    )
}
