package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipObject {
    @Parcelize
    data class UserRoutineRelationshipModel(
        val id: String,
        val userRoutineRelationshipOwner: @RawValue UserObject.User,
        val userRoutineRelationshipRoutine: @RawValue RoutineObject.Routine,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
        var currentlyListening: Boolean?,
        val playSoundDuringStretch: Boolean?,
        val playSoundDuringPrayer: Boolean?,
        val playSoundDuringBreathing: Boolean?,
        val playSoundDuringSelfLove: Boolean?,
        val playSoundDuringBedtimeStory: Boolean?,
        val playSoundDuringSleep: Boolean?,
        val eachSoundPlayTime: Int?,
        val prayerPlayTime: Int?,
        val bedtimeStoryPlayTime: Int?,
        val selfLovePlayTime: Int?,
        val stretchTime: Int?,
        val breathingTime: Int?,
        val currentBedtimeStoryPlayingIndex: Int?,
        val currentBedtimeStoryContinuePlayingTime: Int?,
        val currentSelfLovePlayingIndex: Int?,
        val currentSelfLoveContinuePlayingTime: Int?,
        val currentPrayerPlayingIndex: Int?,
        val currentPrayerContinuePlayingTime: Int?,
        val usageTimeStamp: @RawValue List<Temporal.DateTime>?,
        val usagePlayTimes: List<Int>?,
        val playingOrder: List<String>?
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationship
            get() = UserRoutineRelationship.builder()
                .userRoutineRelationshipOwner(this.userRoutineRelationshipOwner.data)
                .userRoutineRelationshipRoutine(this.userRoutineRelationshipRoutine.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .currentlyListening(this.currentlyListening)
                .totalPlayTime(this.totalPlayTime.toInt())
                .playSoundDuringStretch(this.playSoundDuringStretch)
                .playSoundDuringPrayer((this.playSoundDuringPrayer))
                .playSoundDuringBreathing(this.playSoundDuringBreathing)
                .playSoundDuringSelfLove(this.playSoundDuringSelfLove)
                .playSoundDuringBedtimeStory(this.playSoundDuringBedtimeStory)
                .playSoundDuringSleep(this.playSoundDuringSleep)
                .eachSoundPlayTime(this.eachSoundPlayTime)
                .prayerPlayTime(this.prayerPlayTime)
                .bedtimeStoryPlayTime(this.bedtimeStoryPlayTime)
                .selfLovePlayTime(this.selfLovePlayTime)
                .stretchTime(this.stretchTime)
                .breathingTime(this.breathingTime)
                .currentBedtimeStoryPlayingIndex(this.currentBedtimeStoryPlayingIndex)
                .currentBedtimeStoryContinuePlayingTime(this.currentBedtimeStoryContinuePlayingTime)
                .currentSelfLovePlayingIndex(this.currentSelfLovePlayingIndex)
                .currentSelfLoveContinuePlayingTime(this.currentSelfLoveContinuePlayingTime)
                .currentPrayerPlayingIndex(this.currentPrayerPlayingIndex)
                .currentPrayerContinuePlayingTime(this.currentPrayerContinuePlayingTime)
                .usageTimestamps(this.usageTimeStamp)
                .usagePlayTimes(this.usagePlayTimes)
                .playingOrder(this.playingOrder)
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationship: UserRoutineRelationship): UserRoutineRelationshipModel{
                val result = UserRoutineRelationshipModel(
                    userRoutineRelationship.id,
                    UserObject.User.from(userRoutineRelationship.userRoutineRelationshipOwner),
                    RoutineObject.Routine.from(userRoutineRelationship.userRoutineRelationshipRoutine),
                    userRoutineRelationship.numberOfTimesPlayed,
                    userRoutineRelationship.totalPlayTime.toLong(),
                    userRoutineRelationship.currentlyListening,
                    userRoutineRelationship.playSoundDuringStretch,
                    userRoutineRelationship.playSoundDuringPrayer,
                    userRoutineRelationship.playSoundDuringBreathing,
                    userRoutineRelationship.playSoundDuringSelfLove,
                    userRoutineRelationship.playSoundDuringBedtimeStory,
                    userRoutineRelationship.playSoundDuringSleep,
                    userRoutineRelationship.eachSoundPlayTime,
                    userRoutineRelationship.prayerPlayTime,
                    userRoutineRelationship.bedtimeStoryPlayTime,
                    userRoutineRelationship.selfLovePlayTime,
                    userRoutineRelationship.stretchTime,
                    userRoutineRelationship.breathingTime,
                    userRoutineRelationship.currentBedtimeStoryPlayingIndex,
                    userRoutineRelationship.currentBedtimeStoryContinuePlayingTime,
                    userRoutineRelationship.currentSelfLovePlayingIndex,
                    userRoutineRelationship.currentSelfLoveContinuePlayingTime,
                    userRoutineRelationship.currentPrayerPlayingIndex,
                    userRoutineRelationship.currentPrayerContinuePlayingTime,
                    userRoutineRelationship.usageTimestamps,
                    userRoutineRelationship.usagePlayTimes,
                    userRoutineRelationship.playingOrder
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipType : NavType<UserRoutineRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipModel {
            return Gson().fromJson(value, UserRoutineRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}