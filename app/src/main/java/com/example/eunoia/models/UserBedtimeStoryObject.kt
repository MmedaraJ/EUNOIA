package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserBedtimeStory
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserBedtimeStoryObject {
    @Parcelize
    data class UserBedtimeStoryModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val bedtimeStory: @RawValue BedtimeStoryObject.BedtimeStory,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserBedtimeStory
            get() = UserBedtimeStory.builder()
                .userData(this.user.data)
                .bedtimeStoryData(this.bedtimeStory.data)
                .id(this.id)
                .build()

        companion object{
            fun from(UserBedtimeStory: UserBedtimeStory): UserBedtimeStoryModel{
                val result = UserBedtimeStoryModel(
                    UserBedtimeStory.id,
                    UserObject.User.from(UserBedtimeStory.userData),
                    BedtimeStoryObject.BedtimeStory.from(UserBedtimeStory.bedtimeStoryData),
                )
                return result
            }
        }
    }

    class UserBedtimeStoryType : NavType<UserBedtimeStoryModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserBedtimeStoryModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserBedtimeStoryModel {
            return Gson().fromJson(value, UserBedtimeStoryModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserBedtimeStoryModel) {
            bundle.putParcelable(key, value)
        }
    }
}