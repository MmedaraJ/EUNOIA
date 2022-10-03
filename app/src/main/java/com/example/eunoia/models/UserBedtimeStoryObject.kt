package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfo
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

        val data: UserBedtimeStoryInfo
            get() = UserBedtimeStoryInfo.builder()
                .userData(this.user.data)
                .bedtimeStoryInfoData(this.bedtimeStory.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userBedtimeStory: UserBedtimeStoryInfo): UserBedtimeStoryModel{
                val result = UserBedtimeStoryModel(
                    userBedtimeStory.id,
                    UserObject.User.from(userBedtimeStory.userData),
                    BedtimeStoryObject.BedtimeStory.from(userBedtimeStory.bedtimeStoryInfoData),
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