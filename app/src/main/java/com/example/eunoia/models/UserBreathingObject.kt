package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserBreathing
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserBreathingObject {
    @Parcelize
    data class UserBreathingModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val breathing: @RawValue BreathingObject.Breathing
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserBreathing
            get() = UserBreathing.builder()
                .userData(this.user.data)
                .breathingData(this.breathing.data)
                .id(this.id)
                .build()

        companion object{
            fun from(UserBreathing: UserBreathing): UserBreathingModel{
                val result = UserBreathingModel(
                    UserBreathing.id,
                    UserObject.User.from(UserBreathing.userData),
                    BreathingObject.Breathing.from(UserBreathing.breathingData),
                )
                return result
            }
        }
    }

    class UserBreathingType : NavType<UserBreathingModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserBreathingModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserBreathingModel {
            return Gson().fromJson(value, UserBreathingModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserBreathingModel) {
            bundle.putParcelable(key, value)
        }
    }
}