package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSoundPreset
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipSoundPresetObject {
    @Parcelize
    data class UserRoutineRelationshipSoundPresetModel(
        val id: String,
        val userRoutineRelationship: @RawValue UserRoutineRelationshipObject.UserRoutineRelationshipModel,
        val preset: @RawValue SoundPresetObject.SoundPreset,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationshipSoundPreset
            get() = UserRoutineRelationshipSoundPreset.builder()
                .userRoutineRelationship(this.userRoutineRelationship.data)
                .soundPresetData(this.preset.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationshipSoundPreset: UserRoutineRelationshipSoundPreset): UserRoutineRelationshipSoundPresetModel{
                val result = UserRoutineRelationshipSoundPresetModel(
                    userRoutineRelationshipSoundPreset.id,
                    UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationshipSoundPreset.userRoutineRelationship),
                    SoundPresetObject.SoundPreset.from(userRoutineRelationshipSoundPreset.soundPresetData),
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipSoundPresetType : NavType<UserRoutineRelationshipSoundPresetModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipSoundPresetModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipSoundPresetModel {
            return Gson().fromJson(value, UserRoutineRelationshipSoundPresetModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipSoundPresetModel) {
            bundle.putParcelable(key, value)
        }
    }
}