package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSound
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipSoundObject {
    @Parcelize
    data class UserRoutineRelationshipSoundModel(
        val id: String,
        val sound: @RawValue SoundObject.Sound,
        val userRoutineRelationship: @RawValue UserRoutineRelationshipObject.UserRoutineRelationshipModel,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationshipSound
            get() = UserRoutineRelationshipSound.builder()
                .soundData(this.sound.data)
                .userRoutineRelationship(this.userRoutineRelationship.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationshipSound: UserRoutineRelationshipSound): UserRoutineRelationshipSoundModel{
                val result = UserRoutineRelationshipSoundModel(
                    userRoutineRelationshipSound.id,
                    SoundObject.Sound.from(userRoutineRelationshipSound.soundData),
                    UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationshipSound.userRoutineRelationship),
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipSoundType : NavType<UserRoutineRelationshipSoundModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipSoundModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipSoundModel {
            return Gson().fromJson(value, UserRoutineRelationshipSoundModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipSoundModel) {
            bundle.putParcelable(key, value)
        }
    }
}