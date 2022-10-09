package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipBedtimeStoryInfo
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipBedtimeStoryInfoObject {
    @Parcelize
    data class UserRoutineRelationshipBedtimeStoryInfoModel(
        val id: String,
        val bedtimeStory: @RawValue BedtimeStoryObject.BedtimeStory,
        val userRoutineRelationship: @RawValue UserRoutineRelationshipObject.UserRoutineRelationshipModel,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationshipBedtimeStoryInfo
            get() = UserRoutineRelationshipBedtimeStoryInfo.builder()
                .bedtimeStoryInfoData(this.bedtimeStory.data)
                .userRoutineRelationship(this.userRoutineRelationship.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationshipBedtimeStoryInfo: UserRoutineRelationshipBedtimeStoryInfo): UserRoutineRelationshipBedtimeStoryInfoModel{
                val result = UserRoutineRelationshipBedtimeStoryInfoModel(
                    userRoutineRelationshipBedtimeStoryInfo.id,
                    BedtimeStoryObject.BedtimeStory.from(userRoutineRelationshipBedtimeStoryInfo.bedtimeStoryInfoData),
                    UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationshipBedtimeStoryInfo.userRoutineRelationship),
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipBedtimeStoryInfoModelType : NavType<UserRoutineRelationshipBedtimeStoryInfoModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipBedtimeStoryInfoModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipBedtimeStoryInfoModel {
            return Gson().fromJson(value, UserRoutineRelationshipBedtimeStoryInfoModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipBedtimeStoryInfoModel) {
            bundle.putParcelable(key, value)
        }
    }
}