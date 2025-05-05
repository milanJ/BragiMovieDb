package android.template.core.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Maps the response from the TMDB API `https://api.themoviedb.org/3/configuration` to a Kotlin data class.
 */
data class GetConfigurationResponse(
    @SerializedName("images") val imagesConfiguration: ImagesConfiguration
) {

    data class ImagesConfiguration(
        @SerializedName("secure_base_url") val secureBaseUrl: String,
        @SerializedName("backdrop_sizes") val backdropSizes: List<String>,
        @SerializedName("poster_sizes") val posterSizes: List<String>
    )
}
