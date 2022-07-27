package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutineSound
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutineSoundObject {
    @Parcelize
    data class RoutineSoundModel(
        val id: String,
        val sound: @RawValue SoundObject.Sound,
        val routine: @RawValue RoutineObject.Routine,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutineSound
            get() = RoutineSound.builder()
                .soundData(this.sound.data)
                .routineData(this.routine.data)
                .id(this.id)
                .build()

        companion object{
            fun from(RoutineSound: RoutineSound): RoutineSoundModel{
                val result = RoutineSoundModel(
                    RoutineSound.id,
                    SoundObject.Sound.from(RoutineSound.soundData),
                    RoutineObject.Routine.from(RoutineSound.routineData),
                )
                return result
            }
        }
    }

    class RoutineSoundType : NavType<RoutineSoundModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutineSoundModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutineSoundModel {
            return Gson().fromJson(value, RoutineSoundModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutineSoundModel) {
            bundle.putParcelable(key, value)
        }
    }
}