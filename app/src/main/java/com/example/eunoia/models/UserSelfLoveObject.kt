package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserSelfLove
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserSelfLoveObject {
    @Parcelize
    data class UserSelfLoveModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val selfLove: @RawValue SelfLoveObject.SelfLove
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserSelfLove
            get() = UserSelfLove.builder()
                .userData(this.user.data)
                .selfLoveData(this.selfLove.data)
                .id(this.id)
                .build()

        companion object{
            fun from(UserSelfLove: UserSelfLove): UserSelfLoveModel{
                val result = UserSelfLoveModel(
                    UserSelfLove.id,
                    UserObject.User.from(UserSelfLove.userData),
                    SelfLoveObject.SelfLove.from(UserSelfLove.selfLoveData)
                )
                return result
            }
        }
    }

    class UserSelfLoveType : NavType<UserSelfLoveModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserSelfLoveModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserSelfLoveModel {
            return Gson().fromJson(value, UserSelfLoveModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserSelfLoveModel) {
            bundle.putParcelable(key, value)
        }
    }
}