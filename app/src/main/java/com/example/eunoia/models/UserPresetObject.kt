package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserPreset
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserPresetObject {
    @Parcelize
    data class UserPresetModel(
        val id: String,
        val user: @RawValue UserObject.User,
        val preset: @RawValue PresetObject.Preset,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserPreset
            get() = UserPreset.builder()
                .userData(this.user.data)
                .presetData(this.preset.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userPreset: UserPreset): UserPresetModel{
                val result = UserPresetModel(
                    userPreset.id,
                    UserObject.User.from(userPreset.userData),
                    PresetObject.Preset.from(userPreset.presetData),
                )
                return result
            }
        }
    }

    class UserPresetType : NavType<UserPresetModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserPresetModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserPresetModel {
            return Gson().fromJson(value, UserPresetModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserPresetModel) {
            bundle.putParcelable(key, value)
        }
    }
}