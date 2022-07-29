package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserSound
import com.example.eunoia.models.SoundObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserSoundObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserSoundBackend {
    private const val TAG = "UserSoundBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserSound(
        userSoundModel: UserSoundObject.UserSoundModel,
        completed: (userSound: UserSound) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userSoundModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userSoundModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userSoundModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserSoundObject(soundData: SoundData, completed: (userSound: UserSound) -> Unit){
        val userSoundModel = UserSoundObject.UserSoundModel(
            UUID.randomUUID().toString(),
            SoundObject.Sound.from(soundData),
            UserObject.User.from(globalViewModel_!!.currentUser!!)
        )
        createUserSound(userSoundModel){
            completed(it)
        }
    }

    fun queryUserSoundBasedOnUser(
        userData: UserData,
        completed: (userSound: List<UserSound?>) -> Unit
    ) {
        scope.launch {
            val userSoundList = mutableListOf<UserSound?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSound::class.java,
                    UserSound.USER_DATA.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSoundData in response.data) {
                            Log.i(TAG, userSoundData.toString())
                            userSoundList.add(userSoundData)
                        }
                    }
                    completed(userSoundList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserSoundBasedOnSound(
        soundData: SoundData,
        completed: (userSound: List<UserSound?>) -> Unit
    ) {
        scope.launch {
            val userSoundList = mutableListOf<UserSound?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSound::class.java,
                    UserSound.SOUND_DATA.eq(soundData),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSoundData in response.data) {
                            Log.i(TAG, userSoundData.toString())
                            userSoundList.add(userSoundData)
                        }
                    }
                    completed(userSoundList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}