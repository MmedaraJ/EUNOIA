package com.example.eunoia.models

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.*
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.storage.result.StorageListResult
import com.example.eunoia.backend.SoundBackend
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.net.CacheResponse

object SoundObject {
    private const val TAG = "SoundObject"
    private val _sounds = MutableLiveData<MutableList<Sound>>(mutableListOf())
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.postValue(this.value)
    }

    fun notifyObserver() {
        this._sounds.notifyObserver()
    }

    fun sounds() : LiveData<MutableList<Sound>>  = _sounds

    /**
     * Used when user adds a user
     */
    fun addSound(sound : Sound) {
        val sounds = _sounds.value
        if (sounds != null) {
            sounds.add(sound)
            _sounds.notifyObserver()
        } else {
            Log.e(TAG, "addSound: user collection is null !!")
        }
    }

    /**
     * Used when user deletes a sound
     */
    fun deleteSound(at: Int) : Sound?  {
        val sound = _sounds.value?.removeAt(at)
        _sounds.notifyObserver()
        return sound
    }

    /**
     * Used when user clears all sounds
     */
    fun resetSound() {
        this._sounds.value?.clear()
        _sounds.notifyObserver()
    }

    @Parcelize
    // a sound data class
    data class Sound(
        val id: String,
        val soundOwner: @RawValue UserObject.User,
        val original_name: String,
        val display_name: String,
        val short_description: String,
        val long_description: String,
        val audio_key_s3: String,
        val icon: Int,
        val fullPlayTime: Int,
        val visible_to_others: Boolean,
        val audio_names: List<String>,
        val approvalStatus: SoundApprovalStatus
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }
        // return an API SoundData from this Sound object
        val data: SoundData
            get() = SoundData.builder()
                .soundOwner(this.soundOwner.data)
                .originalName(this.original_name)
                .displayName(this.display_name)
                .shortDescription(this.short_description)
                .longDescription(this.long_description)
                .audioKeyS3(this.audio_key_s3)
                .icon(this.icon)
                .fullPlayTime(this.fullPlayTime)
                .visibleToOthers(this.visible_to_others)
                .audioNames(this.audio_names)
                .approvalStatus(this.approvalStatus)
                .id(this.id)
                .build()

        companion object{
            fun from(soundData: SoundData): Sound{
                val result = Sound(
                    soundData.id,
                    UserObject.User.from(soundData.soundOwner),
                    soundData.originalName,
                    soundData.displayName,
                    soundData.shortDescription,
                    soundData.longDescription,
                    soundData.audioKeyS3,
                    soundData.icon,
                    soundData.fullPlayTime,
                    soundData.visibleToOthers,
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