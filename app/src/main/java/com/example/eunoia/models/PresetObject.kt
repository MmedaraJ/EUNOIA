package com.example.eunoia.models

import android.net.Uri
import android.os.Parcelable
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.PresetPublicityStatus
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object PresetObject{
    private const val TAG = "PresetObject"

    @Parcelize
    data class Preset(
        val id: String,
        val presetOwner: @RawValue UserObject.User,
        val key: String,
        val volumes: List<Int>,
        var sound: @RawValue SoundObject.Sound?,
        val publicityStatus: PresetPublicityStatus
    ): Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: PresetData
            get() = PresetData.builder()
                .presetOwner(this.presetOwner.data)
                .key(this.key)
                .volumes(this.volumes)
                .sound(this.sound!!.data)
                .publicityStatus(this.publicityStatus)
                .id(this.id)
                .build()

        companion object{
            fun from(presetData: PresetData): Preset{
                val result = Preset(
                    presetData.id,
                    UserObject.User.from(presetData.presetOwner),
                    presetData.key,
                    presetData.volumes,
                    SoundObject.Sound.from(presetData.sound),
                    presetData.publicityStatus
                )
                return result
            }
        }
    }
}