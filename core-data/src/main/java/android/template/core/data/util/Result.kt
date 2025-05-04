package android.template.core.data.util

/**
 * A generic class that encapsulates success and error results.
 */
sealed class Result<out R> {

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    data class Success<out T>(
        val data: T
    ) : Result<T>()

    data class Error(
        val exception: Exception
    ) : Result<Nothing>()
}
