package com.example.eunoia.models

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData

object UserObject {
    private const val TAG = "UserObject"

    // a sound data class
    data class User(
        val id: String,
        val username: String,
        val givenName: String,
        val familyName: String,
        val middleName: String,
        val email: String,
        val profile_picture_key: String,
        val address: String,
        val birthdate: String,
        val gender: String,
        val nickname: String,
        val phoneNumber: String
    ) {
        override fun toString(): String = username
        //return an API UserData from this User object
        val data: UserData
            get() = UserData.builder()
                .username(this.username)
                .givenName(this.givenName)
                .familyName(this.familyName)
                .email(this.email)
                .middleName(this.middleName)
                .profilePictureKey(this.profile_picture_key)
                .address(this.address)
                .birthdate(this.birthdate)
                .gender(this.gender)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .id(this.id)
                .build()

        companion object {
            fun from(userData: UserData): User{
                val result = User(
                    userData.id,
                    userData.username,
                    userData.givenName,
                    userData.familyName,
                    userData.middleName,
                    userData.email,
                    userData.profilePictureKey,
                    userData.address,
                    userData.birthdate,
                    userData.gender,
                    userData.nickname,
                    userData.phoneNumber,
                )
                return result
            }
        }
    }
}