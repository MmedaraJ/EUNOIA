package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.BreathingData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object BreathingObject{
    @Parcelize
    data class Breathing(
        val id: String,
        val breathingOwner: @RawValue UserObject.User,
        val displayName: String,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: BreathingData
            get() = BreathingData.builder()
                .breathingOwner(this.breathingOwner.data)
                .displayName(this.displayName)
                .id(this.id)
                .build()

        companion object{
            fun from(breathingData: BreathingData): Breathing{
                val result = Breathing(
                    breathingData.id,
                    UserObject.User.from(breathingData.breathingOwner),
                    breathingData.displayName
                )
                return result
            }
        }
    }

    class BreathingType : NavType<Breathing>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Breathing? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): Breathing {
            return Gson().fromJson(value, Breathing::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: Breathing) {
            bundle.putParcelable(key, value)
        }
    }
}