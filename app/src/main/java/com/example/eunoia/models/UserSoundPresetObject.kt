package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserPreset
import com.amplifyframework.datastore.generated.model.UserSoundPreset
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserSoundPresetObject {
    @Parcelize
    data class UserSoundPresetModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val preset: @RawValue SoundPresetObject.SoundPreset,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserSoundPreset
            get() = UserSoundPreset.builder()
                .userData(this.user.data)
                .soundPresetData(this.preset.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userSoundPreset: UserSoundPreset): UserSoundPresetModel{
                val result = UserSoundPresetModel(
                    userSoundPreset.id,
                    UserObject.User.from(userSoundPreset.userData),
                    SoundPresetObject.SoundPreset.from(userSoundPreset.soundPresetData),
                )
                return result
            }
        }
    }

    class UserSoundPresetType : NavType<UserSoundPresetModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserSoundPresetModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserSoundPresetModel {
            return Gson().fromJson(value, UserSoundPresetModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserSoundPresetModel) {
            bundle.putParcelable(key, value)
        }
    }
}