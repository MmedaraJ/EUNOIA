package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserPrayerRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserPrayerRelationshipObject {
    @Parcelize
    data class UserPrayerRelationshipModel(
        val id: String,
        val userPrayerRelationshipOwner: @RawValue UserObject.User,
        val userPrayerRelationshipPrayer: @RawValue PrayerObject.Prayer,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserPrayerRelationship
            get() = UserPrayerRelationship.builder()
                .userPrayerRelationshipOwner(this.userPrayerRelationshipOwner.data)
                .userPrayerRelationshipPrayer(this.userPrayerRelationshipPrayer.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .totalPlayTime(this.totalPlayTime.toInt())
                .id(this.id)
                .build()

        companion object{
            fun from(userPrayerRelationship: UserPrayerRelationship): UserPrayerRelationshipModel{
                val result = UserPrayerRelationshipModel(
                    userPrayerRelationship.id,
                    UserObject.User.from(userPrayerRelationship.userPrayerRelationshipOwner),
                    PrayerObject.Prayer.from(userPrayerRelationship.userPrayerRelationshipPrayer),
                    userPrayerRelationship.numberOfTimesPlayed,
                    userPrayerRelationship.totalPlayTime.toLong()
                )
                return result
            }
        }
    }

    class UserPrayerRelationshipType : NavType<UserPrayerRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserPrayerRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserPrayerRelationshipModel {
            return Gson().fromJson(value, UserPrayerRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserPrayerRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}