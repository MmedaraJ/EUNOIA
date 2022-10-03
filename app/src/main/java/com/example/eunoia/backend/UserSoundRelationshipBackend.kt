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

object UserSoundRelationshipBackend {
    private const val TAG = "UserSoundRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserSoundRelationship(
        userSoundRelationshipModel: UserSoundRelationshipObject.UserSoundRelationshipModel,
        completed: (userSoundRelationship: UserSoundRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userSoundRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userSoundRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userSoundRelationshipModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserSoundRelationshipObject(sound: SoundData, completed: (userSoundRelationship: UserSoundRelationship) -> Unit){
        val userSoundRelationshipModel = UserSoundRelationshipObject.UserSoundRelationshipModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            SoundObject.Sound.from(sound),
            0,
            0
        )
        createUserSoundRelationship(userSoundRelationshipModel){
            completed(it)
        }
    }

    fun queryApprovedUserSoundRelationshipBasedOnUser(
        userData: UserData,
        completed: (userSoundRelationship: List<UserSoundRelationship?>) -> Unit
    ) {
        scope.launch {
            val userSoundRelationshipList = mutableListOf<UserSoundRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSoundRelationship::class.java,
                    UserSoundRelationship.USER_SOUND_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSoundRelationshipData in response.data) {
                            //TODO change pending to approved
                            if(userSoundRelationshipData != null) {
                                if (userSoundRelationshipData.userSoundRelationshipSound.approvalStatus == SoundApprovalStatus.PENDING) {
                                    Log.i(TAG, userSoundRelationshipData.toString())
                                    userSoundRelationshipList.add(userSoundRelationshipData)
                                }
                            }
                        }
                    }
                    completed(userSoundRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserSoundRelationshipBasedOnUserAndSound(
        userData: UserData,
        sound: SoundData,
        completed: (userSoundRelationship: List<UserSoundRelationship?>) -> Unit
    ) {
        scope.launch {
            val userSoundRelationshipList = mutableListOf<UserSoundRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSoundRelationship::class.java,
                    UserSoundRelationship.USER_SOUND_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserSoundRelationship.USER_SOUND_RELATIONSHIP_SOUND.eq(sound.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSoundRelationshipData in response.data) {
                            if(userSoundRelationshipData != null) {
                                Log.i(TAG, userSoundRelationshipData.toString())
                                userSoundRelationshipList.add(userSoundRelationshipData)
                            }
                        }
                    }
                    completed(userSoundRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}