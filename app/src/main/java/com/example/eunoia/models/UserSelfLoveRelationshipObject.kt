package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.UserSelfLoveRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserSelfLoveRelationshipObject {
    @Parcelize
    data class UserSelfLoveRelationshipModel(
        val id: String,
        val userSelfLoveRelationshipOwner: @RawValue UserObject.User,
        val userSelfLoveRelationshipSelfLove: @RawValue SelfLoveObject.SelfLove,
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

        val data: UserSelfLoveRelationship
            get() = UserSelfLoveRelationship.builder()
                .userSelfLoveRelationshipOwner(this.userSelfLoveRelationshipOwner.data)
                .userSelfLoveRelationshipSelfLove(this.userSelfLoveRelationshipSelfLove.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .totalPlayTime(this.totalPlayTime.toInt())
                .continuePlayingTime(this.continuePlayingTime)
                .currentlyListening(this.currentlyListening)
                .usageTimestamps(this.usageTimeStamp)
                .usagePlayTimes(this.usagePlayTimes)
                .id(this.id)
                .build()

        companion object{
            fun from(userSelfLoveRelationship: UserSelfLoveRelationship): UserSelfLoveRelationshipModel{
                val result = UserSelfLoveRelationshipModel(
                    userSelfLoveRelationship.id,
                    UserObject.User.from(userSelfLoveRelationship.userSelfLoveRelationshipOwner),
                    SelfLoveObject.SelfLove.from(userSelfLoveRelationship.userSelfLoveRelationshipSelfLove),
                    userSelfLoveRelationship.numberOfTimesPlayed,
                    userSelfLoveRelationship.totalPlayTime.toLong(),
                    userSelfLoveRelationship.continuePlayingTime,
                    userSelfLoveRelationship.currentlyListening,
                    userSelfLoveRelationship.usageTimestamps,
                    userSelfLoveRelationship.usagePlayTimes
                )
                return result
            }
        }
    }

    class UserSelfLoveRelationshipType : NavType<UserSelfLoveRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserSelfLoveRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserSelfLoveRelationshipModel {
            return Gson().fromJson(value, UserSelfLoveRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserSelfLoveRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}