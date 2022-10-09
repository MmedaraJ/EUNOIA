package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipStretch
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipStretchObject {
    @Parcelize
    data class UserRoutineRelationshipStretchModel(
        val id: String,
        val userRoutineRelationship: @RawValue UserRoutineRelationshipObject.UserRoutineRelationshipModel,
        val stretch: @RawValue StretchObject.Stretch,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationshipStretch
            get() = UserRoutineRelationshipStretch.builder()
                .userRoutineRelationship(this.userRoutineRelationship.data)
                .stretchData(this.stretch.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationshipStretch: UserRoutineRelationshipStretch): UserRoutineRelationshipStretchModel{
                val result = UserRoutineRelationshipStretchModel(
                    userRoutineRelationshipStretch.id,
                    UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationshipStretch.userRoutineRelationship),
                    StretchObject.Stretch.from(userRoutineRelationshipStretch.stretchData),
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipStretchType : NavType<UserRoutineRelationshipStretchModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipStretchModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipStretchModel {
            return Gson().fromJson(value, UserRoutineRelationshipStretchModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipStretchModel) {
            bundle.putParcelable(key, value)
        }
    }
}