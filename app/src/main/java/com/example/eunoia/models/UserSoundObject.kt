package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserSound
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserSoundObject {
    @Parcelize
    data class UserSoundModel(
        val id: String,
        val sound: @RawValue SoundObject.Sound,
        val user: @RawValue UserObject.User
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserSound
            get() = UserSound.builder()
                .soundData(this.sound.data)
                .userData(this.user.data)
                .id(this.id)
                .build()

        companion object{
            fun from(UserSound: UserSound): UserSoundModel{
                val result = UserSoundModel(
                    UserSound.id,
                    SoundObject.Sound.from(UserSound.soundData),
                    UserObject.User.from(UserSound.userData),
                )
                return result
            }
        }
    }

    class UserSoundType : NavType<UserSoundModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserSoundModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserSoundModel {
            return Gson().fromJson(value, UserSoundModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserSoundModel) {
            bundle.putParcelable(key, value)
        }
    }
}