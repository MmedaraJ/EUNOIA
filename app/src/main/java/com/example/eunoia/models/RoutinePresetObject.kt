package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutinePreset
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutinePresetObject {
    @Parcelize
    data class RoutinePresetModel(
        val id: String,
        val preset: @RawValue PresetObject.Preset,
        val routine: @RawValue RoutineObject.Routine,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutinePreset
            get() = RoutinePreset.builder()
                .routineData(this.routine.data)
                .presetData(this.preset.data)
                .id(this.id)
                .build()

        companion object{
            fun from(routinePreset: RoutinePreset): RoutinePresetModel{
                val result = RoutinePresetModel(
                    routinePreset.id,
                    PresetObject.Preset.from(routinePreset.presetData),
                    RoutineObject.Routine.from(routinePreset.routineData),
                )
                return result
            }
        }
    }

    class RoutinePresetType : NavType<RoutinePresetModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutinePresetModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutinePresetModel {
            return Gson().fromJson(value, RoutinePresetModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutinePresetModel) {
            bundle.putParcelable(key, value)
        }
    }
}