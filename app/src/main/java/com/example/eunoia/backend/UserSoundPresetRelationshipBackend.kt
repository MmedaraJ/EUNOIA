package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.models.*
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserSoundPresetRelationshipBackend {
    private const val TAG = "UserSoundPresetRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private fun createUserSoundPresetRelationship(
        userSoundPresetRelationshipModel: UserSoundPresetRelationshipObject.UserSoundPresetRelationshipModel,
        completed: (userSoundPresetRelationship: UserSoundPresetRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userSoundPresetRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userSoundPresetRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userSoundPresetRelationshipModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserSoundPresetRelationshipObject(preset: SoundPresetData, completed: (userSoundPresetRelationship: UserSoundPresetRelationship) -> Unit){
        val userSoundPresetRelationshipModel = UserSoundPresetRelationshipObject.UserSoundPresetRelationshipModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            SoundPresetObject.SoundPreset.from(preset),
            0,
            0,
            false,
            listOf(),
            listOf()
        )
        createUserSoundPresetRelationship(userSoundPresetRelationshipModel){
            completed(it)
        }
    }

    fun queryApprovedUserSoundPresetRelationshipBasedOnUser(
        userData: UserData,
        completed: (userSoundPresetRelationship: List<UserSoundPresetRelationship?>) -> Unit
    ) {
        scope.launch {
            val userSoundPresetRelationshipList = mutableListOf<UserSoundPresetRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSoundPresetRelationship::class.java,
                    UserSoundPresetRelationship.USER_SOUND_PRESET_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSoundPresetRelationshipData in response.data) {
                            if(userSoundPresetRelationshipData != null) {
                                Log.i(TAG, userSoundPresetRelationshipData.toString())
                                userSoundPresetRelationshipList.add(userSoundPresetRelationshipData)
                            }
                        }
                    }
                    completed(userSoundPresetRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserSoundPresetRelationshipBasedOnUserAndSoundPreset(
        userData: UserData,
        preset: SoundPresetData,
        completed: (userSoundPresetRelationship: List<UserSoundPresetRelationship?>) -> Unit
    ) {
        scope.launch {
            val userSoundPresetRelationshipList = mutableListOf<UserSoundPresetRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSoundPresetRelationship::class.java,
                    UserSoundPresetRelationship.USER_SOUND_PRESET_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserSoundPresetRelationship.USER_SOUND_PRESET_RELATIONSHIP_SOUND_PRESET.eq(preset.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSoundPresetRelationshipData in response.data) {
                            if(userSoundPresetRelationshipData != null) {
                                Log.i(TAG, userSoundPresetRelationshipData.toString())
                                userSoundPresetRelationshipList.add(userSoundPresetRelationshipData)
                            }
                        }
                    }
                    completed(userSoundPresetRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}