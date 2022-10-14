package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSoundPreset
import com.amplifyframework.datastore.generated.model.SoundPresetData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.models.UserRoutineRelationshipSoundPresetObject
import com.example.eunoia.models.SoundPresetObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserRoutineRelationshipSoundPresetBackend {
    private const val TAG = "UserRoutineRelationshipSoundPresetBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    private fun createUserRoutineRelationshipSoundPreset(
        userRoutineRelationshipSoundPresetModel: UserRoutineRelationshipSoundPresetObject.UserRoutineRelationshipSoundPresetModel,
        completed: (userRoutineRelationshipSoundPreset: UserRoutineRelationshipSoundPreset) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineRelationshipSoundPresetModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create UserRoutineRelationshipSoundPresetModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created UserRoutineRelationshipSoundPresetModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineRelationshipSoundPresetObject(
        presetData: SoundPresetData,
        userRoutineRelationship: UserRoutineRelationship,
        completed: (UserRoutineRelationshipSoundPreset: UserRoutineRelationshipSoundPreset) -> Unit
    ){
        val userRoutineRelationshipSoundPresetModel = UserRoutineRelationshipSoundPresetObject.UserRoutineRelationshipSoundPresetModel(
            UUID.randomUUID().toString(),
            UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationship),
            SoundPresetObject.SoundPreset.from(presetData),
        )
        createUserRoutineRelationshipSoundPreset(userRoutineRelationshipSoundPresetModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserRoutineRelationshipSoundPresetBasedOnUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipSoundPreset: List<UserRoutineRelationshipSoundPreset?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipSoundPresetList = mutableListOf<UserRoutineRelationshipSoundPreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipSoundPreset::class.java,
                    UserRoutineRelationshipSoundPreset.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipSoundPresetData in response.data) {
                            if(userRoutineRelationshipSoundPresetData != null) {
                                Log.i(TAG, userRoutineRelationshipSoundPresetData.toString())
                                userRoutineRelationshipSoundPresetList.add(userRoutineRelationshipSoundPresetData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipSoundPresetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipSoundPresetBasedOnRoutineAndSoundPreset(
        userRoutineRelationship: UserRoutineRelationship,
        presetData: SoundPresetData,
        completed: (UserRoutineRelationshipSoundPreset: List<UserRoutineRelationshipSoundPreset?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipSoundPresetList = mutableListOf<UserRoutineRelationshipSoundPreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipSoundPreset::class.java,
                    UserRoutineRelationshipSoundPreset.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id)
                        .and(UserRoutineRelationshipSoundPreset.SOUND_PRESET_DATA.eq(presetData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipSoundPresetData in response.data) {
                            if(userRoutineRelationshipSoundPresetData != null) {
                                Log.i(TAG, userRoutineRelationshipSoundPresetData.toString())
                                userRoutineRelationshipSoundPresetList.add(userRoutineRelationshipSoundPresetData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipSoundPresetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}