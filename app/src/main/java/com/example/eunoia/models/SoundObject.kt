package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.*
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

private const val TAG = "SoundObject"

object SoundObject {
    @Parcelize
    data class Sound(
        val id: String,
        val soundOwner: @RawValue UserObject.User?,
        val original_name: String,
        val display_name: String,
        val short_description: String,
        val long_description: String,
        val audio_key_s3: String,
        val icon: Int,
        val colorHex: Int,
        val fullPlayTime: Long,
        val visible_to_others: Boolean,
        val tags: List<String>,
        val audio_names: List<String>,
        val approvalStatus: SoundApprovalStatus
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: SoundData
            get() = SoundData.builder()
                .soundOwner(this.soundOwner!!.data)
                .originalName(this.original_name)
                .displayName(this.display_name)
                .audioKeyS3(this.audio_key_s3)
                .icon(this.icon)
                .colorHex(this.colorHex)
                .fullPlayTime(this.fullPlayTime.toInt())
                .visibleToOthers(this.visible_to_others)
                .tags(this.tags)
                .audioNames(this.audio_names)
                .approvalStatus(this.approvalStatus)
                .longDescription(this.long_description)
                .shortDescription(this.short_description)
                .id(this.id)
                .build()

        companion object{
            fun from(soundData: SoundData): Sound{
                Log.i(TAG, "from sound data is $soundData")
                Log.i(TAG, "from sound data owner is ${soundData.soundOwner}")
                val result = Sound(
                    soundData.id,
                    UserObject.User.from(soundData.soundOwner),
                    soundData.originalName,
                    soundData.displayName,
                    soundData.shortDescription,
                    soundData.longDescription,
                    soundData.audioKeyS3,
                    soundData.icon,
                    soundData.colorHex,
                    soundData.fullPlayTime.toLong(),
                    soundData.visibleToOthers,
                    soundData.tags,
                    soundData.audioNames,
                    soundData.approvalStatus
                )
                return result
            }
        }
    }

    class SoundType : NavType<Sound>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Sound? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): Sound {
            return Gson().fromJson(value, Sound::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: Sound) {
            bundle.putParcelable(key, value)
        }
    }
}