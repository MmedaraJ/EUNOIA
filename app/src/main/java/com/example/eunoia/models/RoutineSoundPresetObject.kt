package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutinePreset
import com.amplifyframework.datastore.generated.model.RoutineSoundPreset
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutineSoundPresetObject {
    @Parcelize
    data class RoutineSoundPresetModel(
        val id: String,
        val preset: @RawValue SoundPresetObject.SoundPreset,
        val routine: @RawValue RoutineObject.Routine,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutineSoundPreset
            get() = RoutineSoundPreset.builder()
                .routineData(this.routine.data)
                .soundPresetData(this.preset.data)
                .id(this.id)
                .build()

        companion object{
            fun from(routineSoundPreset: RoutineSoundPreset): RoutineSoundPresetModel{
                val result = RoutineSoundPresetModel(
                    routineSoundPreset.id,
                    SoundPresetObject.SoundPreset.from(routineSoundPreset.soundPresetData),
                    RoutineObject.Routine.from(routineSoundPreset.routineData),
                )
                return result
            }
        }
    }

    class RoutineSoundPresetType : NavType<RoutineSoundPresetModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutineSoundPresetModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutineSoundPresetModel {
            return Gson().fromJson(value, RoutineSoundPresetModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutineSoundPresetModel) {
            bundle.putParcelable(key, value)
        }
    }
}