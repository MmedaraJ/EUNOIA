package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.*
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutinePrayerObject {
    @Parcelize
    data class RoutinePrayerModel(
        val id: String,
        val routine: @RawValue RoutineObject.Routine,
        val prayer: @RawValue PrayerObject.Prayer,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutinePrayer
            get() = RoutinePrayer.builder()
                .routineData(this.routine.data)
                .prayerData(this.prayer.data)
                .id(this.id)
                .build()

        companion object{
            fun from(routinePrayer: RoutinePrayer): RoutinePrayerModel{
                val result = RoutinePrayerModel(
                    routinePrayer.id,
                    RoutineObject.Routine.from(routinePrayer.routineData),
                    PrayerObject.Prayer.from(routinePrayer.prayerData),
                )
                return result
            }
        }
    }

    class RoutinePrayerType : NavType<RoutinePrayerModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutinePrayerModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutinePrayerModel {
            return Gson().fromJson(value, RoutinePrayerModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutinePrayerModel) {
            bundle.putParcelable(key, value)
        }
    }
}