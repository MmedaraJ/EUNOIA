package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutineSelfLove
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutineSelfLoveObject {
    @Parcelize
    data class RoutineSelfLoveModel(
        val id: String,
        val routine: @RawValue RoutineObject.Routine,
        val selfLove: @RawValue SelfLoveObject.SelfLove,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutineSelfLove
            get() = RoutineSelfLove.builder()
                .routineData(this.routine.data)
                .selfLoveData(this.selfLove.data)
                .id(this.id)
                .build()

        companion object{
            fun from(RoutineSelfLove: RoutineSelfLove): RoutineSelfLoveModel{
                val result = RoutineSelfLoveModel(
                    RoutineSelfLove.id,
                    RoutineObject.Routine.from(RoutineSelfLove.routineData),
                    SelfLoveObject.SelfLove.from(RoutineSelfLove.selfLoveData),
                )
                return result
            }
        }
    }

    class RoutineSelfLoveType : NavType<RoutineSelfLoveModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutineSelfLoveModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutineSelfLoveModel {
            return Gson().fromJson(value, RoutineSelfLoveModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutineSelfLoveModel) {
            bundle.putParcelable(key, value)
        }
    }
}