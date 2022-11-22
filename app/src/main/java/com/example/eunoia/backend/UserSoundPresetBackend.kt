package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.models.*
import com.example.eunoia.ui.navigation.globalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserSoundPresetBackend {
    private const val TAG = "UserSoundPresetBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createUserSoundPreset(
        userSoundPresetModel: UserSoundPresetObject.UserSoundPresetModel,
        completed: (userSoundPreset: UserSoundPreset) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userSoundPresetModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userSoundPresetModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userSoundPresetModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserSoundPresetObject(presetData: SoundPresetData, completed: (userSoundPreset: UserSoundPreset) -> Unit){
        val userSoundPresetModel = UserSoundPresetObject.UserSoundPresetModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            SoundPresetObject.SoundPreset.from(presetData),
        )
        createUserSoundPreset(userSoundPresetModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserSoundPresetBasedOnUserAndSoundPreset(
        userData: UserData,
        presetData: SoundPresetData,
        completed: (userSoundPreset: List<UserSoundPreset?>) -> Unit
    ) {
        scope.launch {
            val userSoundPresetList = mutableListOf<UserSoundPreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSoundPreset::class.java,
                    UserSoundPreset.USER_DATA.eq(userData.id)
                        .and(UserSoundPreset.SOUND_PRESET_DATA.eq(presetData.id))
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSoundPresetData in response.data) {
                            if(userSoundPresetData != null) {
                                Log.i(TAG, userSoundPresetData.toString())
                                userSoundPresetList.add(userSoundPresetData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userSoundPresetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}