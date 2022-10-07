package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.StretchData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object StretchObject{
    @Parcelize
    data class Stretch(
        val id: String,
        val stretchOwner: @RawValue UserObject.User?,
        val stretchOwnerId: String?,
        val displayName: String,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: StretchData
            get() = StretchData.builder()
                .stretchOwner(this.stretchOwner!!.data)
                .displayName(this.displayName)
                .stretchOwnerId(this.stretchOwnerId)
                .id(this.id)
                .build()

        companion object{
            fun from(stretchData: StretchData): Stretch{
                val result = Stretch(
                    stretchData.id,
                    UserObject.User.from(stretchData.stretchOwner),
                    stretchData.stretchOwnerId,
                    stretchData.displayName
                )
                return result
            }
        }
    }

    class StretchType : NavType<Stretch>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Stretch? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): Stretch {
            return Gson().fromJson(value, Stretch::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: Stretch) {
            bundle.putParcelable(key, value)
        }
    }
}