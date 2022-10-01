package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.*
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object SelfLoveObject {
    @Parcelize
    data class SelfLove(
        val id: String,
        val selfLoveOwner: @RawValue UserObject.User?,
        val displayName: String,
        val shortDescription: String?,
        val longDescription: String?,
        val audioKeyS3: String,
        val icon: Int?,
        val fullPlayTime: Long,
        val visibleToOthers: Boolean,
        val lyrics: List<String>?,
        val tags: List<String>?,
        val audioSource: SelfLoveAudioSource,
        val approvalStatus: SelfLoveApprovalStatus,
    ): Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: SelfLoveData
            get() = SelfLoveData.builder()
                .selfLoveOwner(this.selfLoveOwner!!.data)
                .displayName(this.displayName)
                .audioKeyS3(this.audioKeyS3)
                .icon(this.icon)
                .fullPlayTime(this.fullPlayTime.toInt())
                .visibleToOthers(this.visibleToOthers)
                .lyrics(this.lyrics)
                .tags(this.tags)
                .audioSource(this.audioSource)
                .approvalStatus(this.approvalStatus)
                .shortDescription(this.shortDescription)
                .longDescription(this.longDescription)
                .id(this.id)
                .build()

        companion object{
            fun from(selfLoveData: SelfLoveData): SelfLove {
                val result = SelfLove(
                    selfLoveData.id,
                    UserObject.User.from(selfLoveData.selfLoveOwner),
                    selfLoveData.displayName,
                    selfLoveData.shortDescription,
                    selfLoveData.longDescription,
                    selfLoveData.audioKeyS3,
                    selfLoveData.icon,
                    selfLoveData.fullPlayTime.toLong(),
                    selfLoveData.visibleToOthers,
                    selfLoveData.lyrics,
                    selfLoveData.tags,
                    selfLoveData.audioSource,
                    selfLoveData.approvalStatus,
                )
                return result
            }
        }
    }

    class SelfLoveType : NavType<SelfLove>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): SelfLove? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): SelfLove {
            return Gson().fromJson(value, SelfLove::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: SelfLove) {
            bundle.putParcelable(key, value)
        }
    }
}