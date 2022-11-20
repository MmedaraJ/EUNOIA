package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfo
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfoRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserBedtimeStoryInfoRelationshipObject {
    @Parcelize
    data class UserBedtimeStoryInfoRelationshipModel(
        val id: String,
        val userBedtimeStoryInfoRelationshipOwner: @RawValue UserObject.User,
        val userBedtimeStoryInfoRelationshipBedtimeStoryInfo: @RawValue BedtimeStoryObject.BedtimeStory,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
        val continuePlayingTime: Int,
        val currentlyListening: Boolean?,
        val usageTimeStamp: @RawValue List<Temporal.DateTime>?,
        val usagePlayTimes: List<Int>?,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserBedtimeStoryInfoRelationship
            get() = UserBedtimeStoryInfoRelationship.builder()
                .userBedtimeStoryInfoRelationshipOwner(this.userBedtimeStoryInfoRelationshipOwner.data)
                .userBedtimeStoryInfoRelationshipBedtimeStoryInfo(this.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .totalPlayTime(this.totalPlayTime.toInt())
                .continuePlayingTime(this.continuePlayingTime)
                .currentlyListening(this.currentlyListening)
                .usageTimestamps(this.usageTimeStamp)
                .usagePlayTimes(this.usagePlayTimes)
                .id(this.id)
                .build()

        companion object{
            fun from(userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship): UserBedtimeStoryInfoRelationshipModel{
                val result = UserBedtimeStoryInfoRelationshipModel(
                    userBedtimeStoryInfoRelationship.id,
                    UserObject.User.from(userBedtimeStoryInfoRelationship.userBedtimeStoryInfoRelationshipOwner),
                    BedtimeStoryObject.BedtimeStory.from(userBedtimeStoryInfoRelationship.userBedtimeStoryInfoRelationshipBedtimeStoryInfo),
                    userBedtimeStoryInfoRelationship.numberOfTimesPlayed,
                    userBedtimeStoryInfoRelationship.totalPlayTime.toLong(),
                    userBedtimeStoryInfoRelationship.continuePlayingTime,
                    userBedtimeStoryInfoRelationship.currentlyListening,
                    userBedtimeStoryInfoRelationship.usageTimestamps,
                    userBedtimeStoryInfoRelationship.usagePlayTimes
                )
                return result
            }
        }
    }

    class UserBedtimeStoryInfoRelationshipType : NavType<UserBedtimeStoryInfoRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserBedtimeStoryInfoRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserBedtimeStoryInfoRelationshipModel {
            return Gson().fromJson(value, UserBedtimeStoryInfoRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserBedtimeStoryInfoRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}