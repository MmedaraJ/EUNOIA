package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutineData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutineObject{
    @Parcelize
    data class Routine(
        val id: String,
        val routineOwner: @RawValue UserObject.User,
        val routineOwnerId: String?,
        val originalName: String?,
        val displayName: String?,
        val numberOfSteps: Int?,
        val fullPlayTime: Long?,
        val icon: Int?,
        val visibleToOthers: Boolean?,
        val colorHex: Int?,
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
        val playingOrder: List<String>?
    ): Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutineData
            get() = RoutineData.builder()
                .routineOwner(this.routineOwner.data)
                .routineOwnerId(this.routineOwnerId)
                .originalName(this.originalName)
                .displayName(this.displayName)
                .numberOfSteps(this.numberOfSteps)
                .fullPlayTime(this.fullPlayTime!!.toInt())
                .icon(this.icon)
                .visibleToOthers(this.visibleToOthers)
                .colorHex(this.colorHex)
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
                .playingOrder(this.playingOrder)
                .id(this.id)
                .build()

        companion object{
            fun from(routineData: RoutineData): Routine{
                val result = Routine(
                    routineData.id,
                    UserObject.User.from(routineData.routineOwner),
                    routineData.routineOwnerId,
                    routineData.originalName,
                    routineData.displayName,
                    routineData.numberOfSteps,
                    routineData.fullPlayTime.toLong(),
                    routineData.icon,
                    routineData.visibleToOthers,
                    routineData.colorHex,
                    routineData.playSoundDuringStretch,
                    routineData.playSoundDuringPrayer,
                    routineData.playSoundDuringBreathing,
                    routineData.playSoundDuringSelfLove,
                    routineData.playSoundDuringBedtimeStory,
                    routineData.playSoundDuringSleep,
                    routineData.eachSoundPlayTime,
                    routineData.prayerPlayTime,
                    routineData.bedtimeStoryPlayTime,
                    routineData.selfLovePlayTime,
                    routineData.stretchTime,
                    routineData.breathingTime,
                    routineData.currentBedtimeStoryPlayingIndex,
                    routineData.currentBedtimeStoryContinuePlayingTime,
                    routineData.currentSelfLovePlayingIndex,
                    routineData.currentSelfLoveContinuePlayingTime,
                    routineData.currentPrayerPlayingIndex,
                    routineData.currentPrayerContinuePlayingTime,
                    routineData.playingOrder
                )
                return result
            }
        }
    }

    class RoutineType : NavType<Routine>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Routine? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): Routine {
            return Gson().fromJson(value, Routine::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: Routine) {
            bundle.putParcelable(key, value)
        }
    }
}