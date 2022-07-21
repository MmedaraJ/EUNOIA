package com.example.eunoia.models

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData

object UserObject {
    private const val TAG = "UserObject"
    private val _signedInUser = MutableLiveData<User>()

    private fun <T> MutableLiveData<T>.notifyObserver(){
        this.postValue(this.value)
    }

    fun notifyObserver(){
        this._signedInUser.notifyObserver()
    }

    fun setSignedInUser(user: User){
        _signedInUser.value = user
        Log.i(TAG, "Signed in user value changed to $user")
    }

    fun signedInUser(): LiveData<User> = _signedInUser

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
        val phoneNumber: String,
        val authenticated: Boolean,
        val subscription: String,
    ) {
        override fun toString(): String = username
        //return an API UserData from this User object
        val data: UserData
            get() = UserData.builder()
                .username(this.username)
                .givenName(this.givenName)
                .familyName(this.familyName)
                .middleName(this.middleName)
                .email(this.email)
                .profilePictureKey(this.profile_picture_key)
                .address(this.address)
                .birthdate(this.birthdate)
                .gender(this.gender)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .authenticated(this.authenticated)
                .subscription(this.subscription)
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
                    userData.authenticated,
                    userData.subscription,
                )
                return result
            }
        }
    }
}