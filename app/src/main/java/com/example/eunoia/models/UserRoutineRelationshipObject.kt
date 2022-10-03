package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object UserRoutineRelationshipObject {
    @Parcelize
    data class UserRoutineRelationshipModel(
        val id: String,
        val userRoutineRelationshipOwner: @RawValue UserObject.User,
        val userRoutineRelationshipRoutine: @RawValue RoutineObject.Routine,
        val numberOfTimesPlayed: Int,
        val totalPlayTime: Long,
    ): Parcelable {
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: UserRoutineRelationship
            get() = UserRoutineRelationship.builder()
                .userRoutineRelationshipOwner(this.userRoutineRelationshipOwner.data)
                .userRoutineRelationshipRoutine(this.userRoutineRelationshipRoutine.data)
                .numberOfTimesPlayed(this.numberOfTimesPlayed)
                .totalPlayTime(this.totalPlayTime.toInt())
                .id(this.id)
                .build()

        companion object{
            fun from(userRoutineRelationship: UserRoutineRelationship): UserRoutineRelationshipModel{
                val result = UserRoutineRelationshipModel(
                    userRoutineRelationship.id,
                    UserObject.User.from(userRoutineRelationship.userRoutineRelationshipOwner),
                    RoutineObject.Routine.from(userRoutineRelationship.userRoutineRelationshipRoutine),
                    userRoutineRelationship.numberOfTimesPlayed,
                    userRoutineRelationship.totalPlayTime.toLong()
                )
                return result
            }
        }
    }

    class UserRoutineRelationshipType : NavType<UserRoutineRelationshipModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): UserRoutineRelationshipModel? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): UserRoutineRelationshipModel {
            return Gson().fromJson(value, UserRoutineRelationshipModel::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: UserRoutineRelationshipModel) {
            bundle.putParcelable(key, value)
        }
    }
}