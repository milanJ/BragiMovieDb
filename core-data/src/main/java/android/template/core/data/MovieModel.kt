package android.template.core.data

/**
 * A model class representing a movie. Exposes movie data to the upper layers of the app.
 */
data class MovieModel(
    val id: Long,
    val image: String,
    val title: String,
    val rating: Float,
    val revenue: Int,
    val budget: Int
)
