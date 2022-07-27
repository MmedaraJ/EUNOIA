package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserStretch
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserStretchObject {
    @Parcelize
    data class UserStretchModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val stretch: @RawValue StretchObject.Stretch,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserStretch
            get() = UserStretch.builder()
                .userData(this.user.data)
                .stretchData(this.stretch.data)
                .id(this.id)
                .build()

        companion object{
            fun from(UserStretch: UserStretch): UserStretchModel{
                val result = UserStretchModel(
                    UserStretch.id,
                    UserObject.User.from(UserStretch.userData),
                    StretchObject.Stretch.from(UserStretch.stretchData),
                )
                return result
            }
        }
    }

    class UserStretchType : NavType<UserStretchModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserStretchModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserStretchModel {
            return Gson().fromJson(value, UserStretchModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserStretchModel) {
            bundle.putParcelable(key, value)
        }
    }
}