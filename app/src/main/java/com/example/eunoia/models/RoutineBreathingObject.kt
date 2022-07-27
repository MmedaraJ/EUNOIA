package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutineBreathing
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutineBreathingObject {
    @Parcelize
    data class RoutineBreathingModel(
        val id: String,
        val routine: @RawValue RoutineObject.Routine,
        val breathing: @RawValue BreathingObject.Breathing,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutineBreathing
            get() = RoutineBreathing.builder()
                .routineData(this.routine.data)
                .breathingData(this.breathing.data)
                .id(this.id)
                .build()

        companion object{
            fun from(routineBreathing: RoutineBreathing): RoutineBreathingModel{
                val result = RoutineBreathingModel(
                    routineBreathing.id,
                    RoutineObject.Routine.from(routineBreathing.routineData),
                    BreathingObject.Breathing.from(routineBreathing.breathingData),
                )
                return result
            }
        }
    }

    class RoutineBreathingType : NavType<RoutineBreathingModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutineBreathingModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutineBreathingModel {
            return Gson().fromJson(value, RoutineBreathingModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutineBreathingModel) {
            bundle.putParcelable(key, value)
        }
    }
}