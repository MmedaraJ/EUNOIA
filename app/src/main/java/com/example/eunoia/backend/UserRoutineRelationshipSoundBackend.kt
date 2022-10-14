package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSound
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.models.UserRoutineRelationshipSoundObject
import com.example.eunoia.models.SoundObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserRoutineRelationshipSoundBackend {
    private const val TAG = "UserRoutineRelationshipSoundBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createUserRoutineRelationshipSound(
        userRoutineRelationshipSoundModel: UserRoutineRelationshipSoundObject.UserRoutineRelationshipSoundModel,
        completed: (userRoutineRelationshipSound: UserRoutineRelationshipSound) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineRelationshipSoundModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create UserRoutineRelationshipSoundModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created UserRoutineRelationshipSoundModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineRelationshipSoundObject(
        soundData: SoundData,
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipSound: UserRoutineRelationshipSound) -> Unit
    ){
        val userRoutineRelationshipSoundModel = UserRoutineRelationshipSoundObject.UserRoutineRelationshipSoundModel(
            UUID.randomUUID().toString(),
            SoundObject.Sound.from(soundData),
            UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationship)
        )
        createUserRoutineRelationshipSound(userRoutineRelationshipSoundModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserRoutineRelationshipSoundBasedOnUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (UserRoutineRelationshipSound: List<UserRoutineRelationshipSound?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipSoundList = mutableListOf<UserRoutineRelationshipSound?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipSound::class.java,
                    UserRoutineRelationshipSound.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipSoundData in response.data) {
                            if(userRoutineRelationshipSoundData != null) {
                                Log.i(TAG, userRoutineRelationshipSoundData.toString())
                                userRoutineRelationshipSoundList.add(userRoutineRelationshipSoundData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipSoundList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipSoundBasedOnUserRoutineRelationshipAndSound(
        userRoutineRelationship: UserRoutineRelationship,
        soundData: SoundData,
        completed: (UserRoutineRelationshipSound: List<UserRoutineRelationshipSound?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipSoundList = mutableListOf<UserRoutineRelationshipSound?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipSound::class.java,
                    UserRoutineRelationshipSound.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id)
                        .and(UserRoutineRelationshipSound.SOUND_DATA.eq(soundData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (UserRoutineRelationshipSoundData in response.data) {
                            if(UserRoutineRelationshipSoundData != null) {
                                Log.i(TAG, UserRoutineRelationshipSoundData.toString())
                                userRoutineRelationshipSoundList.add(UserRoutineRelationshipSoundData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipSoundList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}