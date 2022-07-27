package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutine
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineObject {
    @Parcelize
    data class UserRoutineModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val routine: @RawValue RoutineObject.Routine
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutine
            get() = UserRoutine.builder()
                .userData(this.user.data)
                .routineData(this.routine.data)
                .id(this.id)
                .build()

        companion object{
            fun from(UserRoutine: UserRoutine): UserRoutineModel{
                val result = UserRoutineModel(
                    UserRoutine.id,
                    UserObject.User.from(UserRoutine.userData),
                    RoutineObject.Routine.from(UserRoutine.routineData)
                )
                return result
            }
        }
    }

    class UserRoutineType : NavType<UserRoutineModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineModel {
            return Gson().fromJson(value, UserRoutineModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineModel) {
            bundle.putParcelable(key, value)
        }
    }
}