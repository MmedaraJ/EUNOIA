package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.RoutineBedtimeStoryInfo
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object RoutineBedtimeStoryObject {
    @Parcelize
    data class RoutineBedtimeStoryModel(
        val id: String,
        val routine: @RawValue RoutineObject.Routine,
        val bedtimeStory: @RawValue BedtimeStoryObject.BedtimeStory,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: RoutineBedtimeStoryInfo
            get() = RoutineBedtimeStoryInfo.builder()
                .bedtimeStoryInfoData(this.bedtimeStory.data)
                .routineData(this.routine.data)
                .id(this.id)
                .build()

        companion object{
            fun from(routineBedtimeStory: RoutineBedtimeStoryInfo): RoutineBedtimeStoryModel{
                val result = RoutineBedtimeStoryModel(
                    routineBedtimeStory.id,
                    RoutineObject.Routine.from(routineBedtimeStory.routineData),
                    BedtimeStoryObject.BedtimeStory.from(routineBedtimeStory.bedtimeStoryInfoData),
                )
                return result
            }
        }
    }

    class RoutineBedtimeStoryModelType : NavType<RoutineBedtimeStoryModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RoutineBedtimeStoryModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): RoutineBedtimeStoryModel {
            return Gson().fromJson(value, RoutineBedtimeStoryModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: RoutineBedtimeStoryModel) {
            bundle.putParcelable(key, value)
        }
    }
}