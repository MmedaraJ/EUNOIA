package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSelfLove
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipSelfLoveObject {
    @Parcelize
    data class UserRoutineRelationshipSelfLoveModel(
        val id: String,
        val userRoutineRelationship: @RawValue UserRoutineRelationshipObject.UserRoutineRelationshipModel,
        val selfLove: @RawValue SelfLoveObject.SelfLove,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationshipSelfLove
            get() = UserRoutineRelationshipSelfLove.builder()
                .userRoutineRelationship(this.userRoutineRelationship.data)
                .selfLoveData(this.selfLove.data)
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationshipSelfLove: UserRoutineRelationshipSelfLove): UserRoutineRelationshipSelfLoveModel{
                val result = UserRoutineRelationshipSelfLoveModel(
                    userRoutineRelationshipSelfLove.id,
                    UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationshipSelfLove.userRoutineRelationship),
                    SelfLoveObject.SelfLove.from(userRoutineRelationshipSelfLove.selfLoveData),
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipSelfLoveType : NavType<UserRoutineRelationshipSelfLoveModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipSelfLoveModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipSelfLoveModel {
            return Gson().fromJson(value, UserRoutineRelationshipSelfLoveModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipSelfLoveModel) {
            bundle.putParcelable(key, value)
        }
    }
}