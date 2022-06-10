package com.example.eunoia.models

import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.datastore.generated.model.SoundData

object SoundObject {
    private const val TAG = "SoundData"
    private val _sounds = MutableLiveData<MutableList<Sound>>(mutableListOf())
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.postValue(this.value)
    }

    fun notifyObserver() {
        this._sounds.notifyObserver()
    }

    fun sounds() : LiveData<MutableList<Sound>>  = _sounds

    /**
     * Used when user adds a sound
     */
    fun addSound(sound : Sound) {
        val sounds = _sounds.value
        if (sounds != null) {
            sounds.add(sound)
            _sounds.notifyObserver()
        } else {
            Log.e(TAG, "addSound: sound collection is null !!")
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
        val owner_username: String,
        val original_name: String,
        val display_name: String,
        val short_description: String,
        val long_description: String,
        val key: String,
        val comment: String,
        val icon: Int, //drawable
        val fullPlayTime: Int, //minutes
        val visible_to_others: Boolean,
        val original_volumes: List<Int>,
        val current_volumes: List<Int>,
    ) {
        override fun toString(): String = "$owner_username - $display_name"
        var audios: List<MediaStore.Audio?>? = null
        // return an API SoundData from this Sound object
        val data: SoundData
            get() = SoundData.builder()
                .ownerUsername(this.owner_username)
                .originalName(this.original_name)
                .displayName(this.display_name)
                .shortDescription(this.short_description)
                .longDescription(this.long_description)
                .key(this.key)
                .icon(this.icon)
                .fullPlayTime(this.fullPlayTime)
                .visibleToOthers(this.visible_to_others)
                .originalVolumes(this.original_volumes)
                .currentVolumes(this.current_volumes)
                .id(this.id)
                .build()

        companion object{
            fun from(soundData: SoundData): Sound{
                val result = Sound(
                    soundData.id,
                    soundData.ownerUsername,
                    soundData.originalName,
                    soundData.displayName,
                    soundData.shortDescription,
                    soundData.longDescription,
                    soundData.key,
                    soundData.comment,
                    soundData.icon,
                    soundData.fullPlayTime,
                    soundData.visibleToOthers,
                    soundData.originalVolumes,
                    soundData.currentVolumes
                )
                return result
            }
        }
    }
}