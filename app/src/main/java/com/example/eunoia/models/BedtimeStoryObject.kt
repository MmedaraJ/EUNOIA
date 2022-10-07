package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.*
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object BedtimeStoryObject{
    @Parcelize
    data class BedtimeStory(
        val id: String,
        val bedtimeStoryOwner: @RawValue UserObject.User?,
        val bedtimeStoryOwnerId: String?,
        val displayName: String,
        val shortDescription: String?,
        val longDescription: String?,
        val audioKeyS3: String,
        val icon: Int,
        val fullPlayTime: Long,
        val visibleToOthers: Boolean,
        val tags: List<String>?,
        val audioSource: BedtimeStoryAudioSource,
        val approvalStatus: BedtimeStoryApprovalStatus,
        val creationStatus: BedtimeStoryCreationStatus
    ): Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: BedtimeStoryInfoData
            get() = BedtimeStoryInfoData.builder()
                .bedtimeStoryOwner(this.bedtimeStoryOwner!!.data)
                .displayName(this.displayName)
                .audioKeyS3(this.audioKeyS3)
                .icon(this.icon)
                .fullPlayTime(this.fullPlayTime.toInt())
                .visibleToOthers(this.visibleToOthers)
                .tags(this.tags)
                .audioSource(this.audioSource)
                .approvalStatus(this.approvalStatus)
                .creationStatus(this.creationStatus)
                .bedtimeStoryOwnerId(this.bedtimeStoryOwnerId)
                .shortDescription(this.shortDescription)
                .longDescription(this.longDescription)
                .id(this.id)
                .build()

        companion object{
            fun from(bedtimeStoryData: BedtimeStoryInfoData): BedtimeStory{
                val result = BedtimeStory(
                    bedtimeStoryData.id,
                    UserObject.User.from(bedtimeStoryData.bedtimeStoryOwner!!),
                    bedtimeStoryData.bedtimeStoryOwnerId,
                    bedtimeStoryData.displayName,
                    bedtimeStoryData.shortDescription,
                    bedtimeStoryData.longDescription,
                    bedtimeStoryData.audioKeyS3,
                    bedtimeStoryData.icon,
                    bedtimeStoryData.fullPlayTime.toLong(),
                    bedtimeStoryData.visibleToOthers,
                    bedtimeStoryData.tags,
                    bedtimeStoryData.audioSource,
                    bedtimeStoryData.approvalStatus,
                    bedtimeStoryData.creationStatus,
                )
                return result
            }
        }
    }

    class BedtimeStoryType : NavType<BedtimeStory>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): BedtimeStory? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): BedtimeStory {
            return Gson().fromJson(value, BedtimeStory::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: BedtimeStory) {
            bundle.putParcelable(key, value)
        }
    }
}