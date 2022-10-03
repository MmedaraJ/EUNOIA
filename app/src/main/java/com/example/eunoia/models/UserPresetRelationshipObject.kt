package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserPresetRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserPresetRelationshipObject {
    @Parcelize
    data class UserPresetRelationshipModel(
        val id: String,
        val userPresetRelationshipOwner: @RawValue UserObject.User,
        val userPresetRelationshipPreset: @RawValue PresetObject.Preset,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserPresetRelationship
            get() = UserPresetRelationship.builder()
                .userPresetRelationshipOwner(this.userPresetRelationshipOwner.data)
                .userPresetRelationshipPreset(this.userPresetRelationshipPreset.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .totalPlayTime(this.totalPlayTime.toInt())
                .id(this.id)
                .build()

        companion object{
            fun from(userPresetRelationship: UserPresetRelationship): UserPresetRelationshipModel{
                val result = UserPresetRelationshipModel(
                    userPresetRelationship.id,
                    UserObject.User.from(userPresetRelationship.userPresetRelationshipOwner),
                    PresetObject.Preset.from(userPresetRelationship.userPresetRelationshipPreset),
                    userPresetRelationship.numberOfTimesPlayed,
                    userPresetRelationship.totalPlayTime.toLong()
                )
                return result
            }
        }
    }

    class UserPresetRelationshipType : NavType<UserPresetRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserPresetRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserPresetRelationshipModel {
            return Gson().fromJson(value, UserPresetRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserPresetRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}