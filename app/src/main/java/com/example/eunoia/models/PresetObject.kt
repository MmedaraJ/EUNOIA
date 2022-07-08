package com.example.eunoia.models

import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.PresetNameAndVolumesMapData
import com.amplifyframework.datastore.generated.model.SoundData

object PresetObject{
    private const val TAG = "PresetObject"

    data class Preset(
        val id: String,
        val sound: SoundData?
    ){
        //override fun toString(): String = presets.toString()
        //return an API PresetData from this PresetObject
        val data: PresetData
            get() = PresetData.builder()
                .sound(this.sound)
                .id(this.id)
                .build()

        companion object{
            fun from(presetData: PresetData): Preset{
                val result = Preset(
                    presetData.id,
                    presetData.sound
                )
                return result
            }
        }
    }
}