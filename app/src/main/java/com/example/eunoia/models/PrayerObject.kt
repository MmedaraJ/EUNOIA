package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.*
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object PrayerObject {
    @Parcelize
    data class Prayer(
        val id: String,
        val prayerOwner: @RawValue UserObject.User,
        val displayName: String,
        val description: String,
        val audioKeyS3: String,
        val icon: Int?,
        val fullPlayTime: Long,
        val visibleToOthers: Boolean,
        val religion: String?,
        val country: String?,
        val tags: List<String>?,
        val audioSource: PrayerAudioSource,
        val approvalStatus: PrayerApprovalStatus,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: PrayerData
            get() = PrayerData.builder()
                .prayerOwner(this.prayerOwner.data)
                .displayName(this.displayName)
                .description(this.description)
                .audioKeyS3(this.audioKeyS3)
                .icon(this.icon)
                .fullPlayTime(this.fullPlayTime.toInt())
                .visibleToOthers(this.visibleToOthers)
                .religion(this.religion)
                .country(this.country)
                .tags(this.tags)
                .audioSource(this.audioSource)
                .approvalStatus(this.approvalStatus)
                .id(this.id)
                .build()

        companion object{
            fun from(prayerData: PrayerData): Prayer{
                val result = Prayer(
                    prayerData.id,
                    UserObject.User.from(prayerData.prayerOwner),
                    prayerData.displayName,
                    prayerData.description,
                    prayerData.audioKeyS3,
                    prayerData.icon,
                    prayerData.fullPlayTime.toLong(),
                    prayerData.visibleToOthers,
                    prayerData.religion,
                    prayerData.country,
                    prayerData.tags,
                    prayerData.audioSource,
                    prayerData.approvalStatus,
                )
                return result
            }
        }
    }

    class PrayerType : NavType<Prayer>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Prayer? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): Prayer {
            return Gson().fromJson(value, Prayer::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: Prayer) {
            bundle.putParcelable(key, value)
        }
    }
}