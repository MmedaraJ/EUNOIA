package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.navigation.NavType
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipObject {
    private const val TAG = "UserRoutineRelationshipObject"

    @Parcelize
    data class UserRoutineRelationshipModel(
        val id: String,
        val userRoutineRelationshipOwner: @RawValue UserObject.User,
        val userRoutineRelationshipRoutine: @RawValue RoutineObject.Routine,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
        val fullPlayTime: Long?,
        val numberOfSteps: Int?,
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UserRoutineRelationshipModel

            if (id != other.id) return false
            if (userRoutineRelationshipOwner != other.userRoutineRelationshipOwner) return false
            if (userRoutineRelationshipRoutine != other.userRoutineRelationshipRoutine) return false
            if (numberOfTimesPlayed != other.numberOfTimesPlayed) return false
            if (totalPlayTime != other.totalPlayTime) return false
            if (fullPlayTime != other.fullPlayTime) return false
            if (numberOfSteps != other.numberOfSteps) return false
            if (currentlyListening != other.currentlyListening) return false
            if (playSoundDuringStretch != other.playSoundDuringStretch) return false
            if (playSoundDuringPrayer != other.playSoundDuringPrayer) return false
            if (playSoundDuringBreathing != other.playSoundDuringBreathing) return false
            if (playSoundDuringSelfLove != other.playSoundDuringSelfLove) return false
            if (playSoundDuringBedtimeStory != other.playSoundDuringBedtimeStory) return false
            if (playSoundDuringSleep != other.playSoundDuringSleep) return false
            if (eachSoundPlayTime != other.eachSoundPlayTime) return false
            if (prayerPlayTime != other.prayerPlayTime) return false
            if (bedtimeStoryPlayTime != other.bedtimeStoryPlayTime) return false
            if (selfLovePlayTime != other.selfLovePlayTime) return false
            if (stretchTime != other.stretchTime) return false
            if (breathingTime != other.breathingTime) return false
            if (currentBedtimeStoryPlayingIndex != other.currentBedtimeStoryPlayingIndex) return false
            if (currentBedtimeStoryContinuePlayingTime != other.currentBedtimeStoryContinuePlayingTime) return false
            if (currentSelfLovePlayingIndex != other.currentSelfLovePlayingIndex) return false
            if (currentSelfLoveContinuePlayingTime != other.currentSelfLoveContinuePlayingTime) return false
            if (currentPrayerPlayingIndex != other.currentPrayerPlayingIndex) return false
            if (currentPrayerContinuePlayingTime != other.currentPrayerContinuePlayingTime) return false
            if (usageTimeStamp != other.usageTimeStamp) return false
            if (usagePlayTimes != other.usagePlayTimes) return false
            if (playingOrder != other.playingOrder) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + userRoutineRelationshipOwner.hashCode()
            result = 31 * result + userRoutineRelationshipRoutine.hashCode()
            result = 31 * result + numberOfTimesPlayed
            result = 31 * result + totalPlayTime.hashCode()
            result = 31 * result + (fullPlayTime?.hashCode() ?: 0)
            result = 31 * result + (numberOfSteps ?: 0)
            result = 31 * result + (currentlyListening?.hashCode() ?: 0)
            result = 31 * result + (playSoundDuringStretch?.hashCode() ?: 0)
            result = 31 * result + (playSoundDuringPrayer?.hashCode() ?: 0)
            result = 31 * result + (playSoundDuringBreathing?.hashCode() ?: 0)
            result = 31 * result + (playSoundDuringSelfLove?.hashCode() ?: 0)
            result = 31 * result + (playSoundDuringBedtimeStory?.hashCode() ?: 0)
            result = 31 * result + (playSoundDuringSleep?.hashCode() ?: 0)
            result = 31 * result + (eachSoundPlayTime ?: 0)
            result = 31 * result + (prayerPlayTime ?: 0)
            result = 31 * result + (bedtimeStoryPlayTime ?: 0)
            result = 31 * result + (selfLovePlayTime ?: 0)
            result = 31 * result + (stretchTime ?: 0)
            result = 31 * result + (breathingTime ?: 0)
            result = 31 * result + (currentBedtimeStoryPlayingIndex ?: 0)
            result = 31 * result + (currentBedtimeStoryContinuePlayingTime ?: 0)
            result = 31 * result + (currentSelfLovePlayingIndex ?: 0)
            result = 31 * result + (currentSelfLoveContinuePlayingTime ?: 0)
            result = 31 * result + (currentPrayerPlayingIndex ?: 0)
            result = 31 * result + (currentPrayerContinuePlayingTime ?: 0)
            for(date in usageTimeStamp!!){
                Log.i(TAG, "$date.")
            }
            result = 31 * result + (usageTimeStamp?.hashCode() ?: 0)
            result = 31 * result + (usagePlayTimes?.hashCode() ?: 0)
            result = 31 * result + (playingOrder?.hashCode() ?: 0)
            return result
        }

        val data: UserRoutineRelationship
            get() = UserRoutineRelationship.builder()
                .userRoutineRelationshipOwner(this.userRoutineRelationshipOwner.data)
                .userRoutineRelationshipRoutine(this.userRoutineRelationshipRoutine.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .numberOfSteps(this.numberOfSteps)
                .fullPlayTime(this.fullPlayTime!!.toInt())
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
                .usageTimestamps(this.usageTimeStamp!!)
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
                    userRoutineRelationship.fullPlayTime.toLong(),
                    userRoutineRelationship.numberOfSteps,
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
                    userRoutineRelationship.usageTimestamps!!,
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