package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.*
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserPrayerObject {
    @Parcelize
    data class UserPrayerModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val prayer: @RawValue PrayerObject.Prayer
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserPrayer
            get() = UserPrayer.builder()
                .userData(this.user.data)
                .prayerData(this.prayer.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userPrayer: UserPrayer): UserPrayerModel{
                val result = UserPrayerModel(
                    userPrayer.id,
                    UserObject.User.from(userPrayer.userData),
                    PrayerObject.Prayer.from(userPrayer.prayerData),
                )
                return result
            }
        }
    }

    class UserPrayerType : NavType<UserPrayerModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserPrayerModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserPrayerModel {
            return Gson().fromJson(value, UserPrayerModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserPrayerModel) {
            bundle.putParcelable(key, value)
        }
    }
}