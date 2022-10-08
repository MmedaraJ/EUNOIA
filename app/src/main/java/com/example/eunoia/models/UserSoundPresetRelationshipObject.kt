package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.UserPresetRelationship
import com.amplifyframework.datastore.generated.model.UserSoundPresetRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserSoundPresetRelationshipObject {
    @Parcelize
    data class UserSoundPresetRelationshipModel(
        val id: String,
        val userSoundPresetRelationshipOwner: @RawValue UserObject.User,
        val userSoundPresetRelationshipSoundPreset: @RawValue SoundPresetObject.SoundPreset,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
        val currentlyListening: Boolean?,
        val usageTimeStamp: @RawValue List<Temporal.DateTime>?,
        val usagePlayTimes: List<Int>?,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserSoundPresetRelationship
            get() = UserSoundPresetRelationship.builder()
                .userSoundPresetRelationshipOwner(this.userSoundPresetRelationshipOwner.data)
                .userSoundPresetRelationshipSoundPreset(this.userSoundPresetRelationshipSoundPreset.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .totalPlayTime(this.totalPlayTime.toInt())
                .currentlyListening(this.currentlyListening)
                .usageTimestamps(this.usageTimeStamp)
                .usagePlayTimes(this.usagePlayTimes)
                .id(this.id)
                .build()

        companion object{
            fun from(userSoundPresetRelationship: UserSoundPresetRelationship): UserSoundPresetRelationshipModel{
                val result = UserSoundPresetRelationshipModel(
                    userSoundPresetRelationship.id,
                    UserObject.User.from(userSoundPresetRelationship.userSoundPresetRelationshipOwner),
                    SoundPresetObject.SoundPreset.from(userSoundPresetRelationship.userSoundPresetRelationshipSoundPreset),
                    userSoundPresetRelationship.numberOfTimesPlayed,
                    userSoundPresetRelationship.totalPlayTime.toLong(),
                    userSoundPresetRelationship.currentlyListening,
                    userSoundPresetRelationship.usageTimestamps,
                    userSoundPresetRelationship.usagePlayTimes
                )
                return result
            }
        }
    }

    class UserSoundPresetRelationshipType : NavType<UserSoundPresetRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserSoundPresetRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserSoundPresetRelationshipModel {
            return Gson().fromJson(value, UserSoundPresetRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserSoundPresetRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}