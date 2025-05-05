package android.template.core.data

import android.os.Parcel
import android.os.Parcelable

/**
 * A model class representing a genre. Exposes movie genre data to the upper layers of the app.
 */
data class GenreModel(
    val id: Int,
    val name: String
) : Parcelable {

    constructor(
        source: Parcel
    ) : this(source.readInt(), source.readString() ?: "")

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(
        dest: Parcel,
        flags: Int
    ) {
        dest.writeInt(id)
        dest.writeString(name)
    }

    companion object {

        @JvmField
        final val CREATOR: Parcelable.Creator<GenreModel> = object : Parcelable.Creator<GenreModel> {

            override fun createFromParcel(
                source: Parcel
            ): GenreModel {
                return GenreModel(source)
            }

            override fun newArray(
                size: Int
            ): Array<GenreModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
