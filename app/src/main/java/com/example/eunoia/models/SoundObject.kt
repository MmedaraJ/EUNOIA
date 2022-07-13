package com.example.eunoia.models

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.datastore.generated.model.CommentData
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.storage.result.StorageListResult
import com.example.eunoia.backend.SoundBackend
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

    // a sound data class
    data class Sound(
        val id: String,
        val original_owner: UserData,
        val current_owner: UserData,
        val original_name: String,
        val display_name: String,
        val short_description: String,
        val long_description: String,
        val audio_key_s3: String,
        val icon: Int,
        val fullPlayTime: Int,
        val visible_to_others: Boolean,
        val audio_names: List<String>,
    ) {
        override fun toString(): String = "${current_owner.username} - $display_name"
        var mediaPlayers = mutableListOf<MediaPlayer>()
        // return an API SoundData from this Sound object
        val data: SoundData
            get() = SoundData.builder()
                .originalOwner(this.original_owner)
                .currentOwner(this.current_owner)
                .originalName(this.original_name)
                .displayName(this.display_name)
                .shortDescription(this.short_description)
                .longDescription(this.long_description)
                .audioKeyS3(this.audio_key_s3)
                .icon(this.icon)
                .fullPlayTime(this.fullPlayTime)
                .visibleToOthers(this.visible_to_others)
                .audioNames(this.audio_names)
                .id(this.id)
                .build()

        companion object{
            fun from(soundData: SoundData): Sound{
                val audioUris = mutableListOf<Uri>()
                val result = Sound(
                    soundData.id,
                    soundData.originalOwner,
                    soundData.currentOwner,
                    soundData.originalName,
                    soundData.displayName,
                    soundData.shortDescription,
                    soundData.longDescription,
                    soundData.audioKeyS3,
                    soundData.icon,
                    soundData.fullPlayTime,
                    soundData.visibleToOthers,
                    soundData.audioNames
                )

                /*if(soundData.audioKey.isNotEmpty()){
                    SoundBackend.listEunoiaSounds(soundData.audioKey){ response ->
                        response.items.forEach { item ->
                            SoundBackend.retrieveAudio(item.key){ audioUri ->
                                audioUris.add(audioUri)
                            }
                        }
                        setMediaPlayers(audioUris, context, result)
                    }
                }*/

                return result
            }

            private fun setMediaPlayers(audioUris: MutableList<Uri>, context: Context, result: Sound){
                audioUris.forEachIndexed { index, audioUri ->
                    val mediaPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        setDataSource(context, audioUri)
                        prepare()
                    }
                    result.mediaPlayers[index] = mediaPlayer
                }
            }
        }
    }
}