package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipPrayer
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipPrayerObject {
    @Parcelize
    data class UserRoutineRelationshipPrayerModel(
        val id: String,
        val userRoutineRelationship: @RawValue UserRoutineRelationshipObject.UserRoutineRelationshipModel,
        val prayer: @RawValue PrayerObject.Prayer,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationshipPrayer
            get() = UserRoutineRelationshipPrayer.builder()
                .userRoutineRelationship(this.userRoutineRelationship.data)
                .prayerData(this.prayer.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationshipPrayer: UserRoutineRelationshipPrayer): UserRoutineRelationshipPrayerModel{
                val result = UserRoutineRelationshipPrayerModel(
                    userRoutineRelationshipPrayer.id,
                    UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationshipPrayer.userRoutineRelationship),
                    PrayerObject.Prayer.from(userRoutineRelationshipPrayer.prayerData),
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipPrayerType : NavType<UserRoutineRelationshipPrayerModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipPrayerModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipPrayerModel {
            return Gson().fromJson(value, UserRoutineRelationshipPrayerModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipPrayerModel) {
            bundle.putParcelable(key, value)
        }
    }
}