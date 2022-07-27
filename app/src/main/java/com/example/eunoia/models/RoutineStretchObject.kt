package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutineStretch
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutineStretchObject {
    @Parcelize
    data class RoutineStretchModel(
        val id: String,
        val routine: @RawValue RoutineObject.Routine,
        val stretch: @RawValue StretchObject.Stretch,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutineStretch
            get() = RoutineStretch.builder()
                .routineData(this.routine.data)
                .stretchData(this.stretch.data)
                .id(this.id)
                .build()

        companion object{
            fun from(RoutineStretch: RoutineStretch): RoutineStretchModel{
                val result = RoutineStretchModel(
                    RoutineStretch.id,
                    RoutineObject.Routine.from(RoutineStretch.routineData),
                    StretchObject.Stretch.from(RoutineStretch.stretchData),
                )
                return result
            }
        }
    }

    class RoutineStretchType : NavType<RoutineStretchModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutineStretchModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutineStretchModel {
            return Gson().fromJson(value, RoutineStretchModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutineStretchModel) {
            bundle.putParcelable(key, value)
        }
    }
}