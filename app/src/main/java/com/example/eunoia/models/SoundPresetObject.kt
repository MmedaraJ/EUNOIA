package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.PresetPublicityStatus
import com.amplifyframework.datastore.generated.model.SoundPresetData
import com.amplifyframework.datastore.generated.model.SoundPresetPublicityStatus
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object SoundPresetObject{
    private const val TAG = "SoundPresetObject"

    @Parcelize
    data class SoundPreset(
        val id: String,
        val presetOwner: @RawValue UserObject.User,
        val presetOwnerId: String?,
        val key: String,
        val volumes: List<Int>,
        var soundId: String?,
        val publicityStatus: SoundPresetPublicityStatus
    ): Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: SoundPresetData
            get() = SoundPresetData.builder()
                .presetOwner(this.presetOwner.data)
                .key(this.key)
                .volumes(this.volumes)
                .presetOwnerId(this.presetOwnerId)
                .soundId(this.soundId)
                .publicityStatus(this.publicityStatus)
                .id(this.id)
                .build()

        companion object{
            fun from(presetData: SoundPresetData): SoundPreset{
                val result = SoundPreset(
                    presetData.id,
                    UserObject.User.from(presetData.presetOwner),
                    presetData.presetOwnerId,
                    presetData.key,
                    presetData.volumes,
                    presetData.soundId,
                    presetData.publicityStatus
                )
                return result
            }
        }
    }

    class SoundPresetType : NavType<SoundPreset>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): SoundPreset? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): SoundPreset {
            return Gson().fromJson(value, SoundPreset::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: SoundPreset) {
            bundle.putParcelable(key, value)
        }
    }
}