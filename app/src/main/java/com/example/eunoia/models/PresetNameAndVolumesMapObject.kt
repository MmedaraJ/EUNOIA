package com.example.eunoia.models

import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.PresetNameAndVolumesMapData

object PresetNameAndVolumesMapObject{
    private const val TAG = "PresetNameAndVolumesMapObject"

    data class PresetNameAndVolumesMap(
        val key: String,
        val volumes: List<Int>,
        val preset: PresetData
    ){
        override fun toString(): String = key
        //return an API PresetNameAndVolumesMapData from this PresetNameAndVolumesMapObject
        val data: PresetNameAndVolumesMapData
            get() = PresetNameAndVolumesMapData.builder()
                .key(this.key)
                .volumes(this.volumes)
                .preset(this.preset)
                .build()

        companion object{
            fun from(presetNameAndVolumesMapData: PresetNameAndVolumesMapData): PresetNameAndVolumesMap{
                val result = PresetNameAndVolumesMap(
                    presetNameAndVolumesMapData.key,
                    presetNameAndVolumesMapData.volumes,
                    presetNameAndVolumesMapData.preset
                )
                return result
            }
        }
    }
}