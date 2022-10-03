package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserSoundRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserSoundRelationshipObject {
    @Parcelize
    data class UserSoundRelationshipModel(
        val id: String,
        val userSoundRelationshipOwner: @RawValue UserObject.User,
        val userSoundRelationshipSound: @RawValue SoundObject.Sound,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserSoundRelationship
            get() = UserSoundRelationship.builder()
                .userSoundRelationshipOwner(this.userSoundRelationshipOwner.data)
                .userSoundRelationshipSound(this.userSoundRelationshipSound.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .totalPlayTime(this.totalPlayTime.toInt())
                .id(this.id)
                .build()

        companion object{
            fun from(userSoundRelationship: UserSoundRelationship): UserSoundRelationshipModel{
                val result = UserSoundRelationshipModel(
                    userSoundRelationship.id,
                    UserObject.User.from(userSoundRelationship.userSoundRelationshipOwner),
                    SoundObject.Sound.from(userSoundRelationship.userSoundRelationshipSound),
                    userSoundRelationship.numberOfTimesPlayed,
                    userSoundRelationship.totalPlayTime.toLong()
                )
                return result
            }
        }
    }

    class UserSoundRelationshipType : NavType<UserSoundRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserSoundRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserSoundRelationshipModel {
            return Gson().fromJson(value, UserSoundRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserSoundRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}