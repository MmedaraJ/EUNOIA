package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.BedtimeStoryData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object BedtimeStoryObject{
    @Parcelize
    data class BedtimeStory(
        val id: String,
        val bedtimeStoryOwner: @RawValue UserObject.User,
        val displayName: String,
    ): Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: BedtimeStoryData
            get() = BedtimeStoryData.builder()
                .bedtimeStoryOwner(this.bedtimeStoryOwner.data)
                .displayName(this.displayName)
                .id(this.id)
                .build()

        companion object{
            fun from(bedtimeStoryData: BedtimeStoryData): BedtimeStory{
                val result = BedtimeStory(
                    bedtimeStoryData.id,
                    UserObject.User.from(bedtimeStoryData.bedtimeStoryOwner),
                    bedtimeStoryData.displayName
                )
                return result
            }
        }
    }

    class BedtimeStoryType : NavType<BedtimeStory>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): BedtimeStory? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): BedtimeStory {
            return Gson().fromJson(value, BedtimeStory::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: BedtimeStory) {
            bundle.putParcelable(key, value)
        }
    }
}