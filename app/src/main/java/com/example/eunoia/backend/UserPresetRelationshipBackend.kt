package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserPresetRelationship
import com.example.eunoia.models.PresetObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserPresetRelationshipObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserPresetRelationshipBackend {
    private const val TAG = "UserPresetRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserPresetRelationship(
        userPresetRelationshipModel: UserPresetRelationshipObject.UserPresetRelationshipModel,
        completed: (userPresetRelationship: UserPresetRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userPresetRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userPresetRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userPresetRelationshipModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserPresetRelationshipObject(Preset: PresetData, completed: (userPresetRelationship: UserPresetRelationship) -> Unit){
        val userPresetRelationshipModel = UserPresetRelationshipObject.UserPresetRelationshipModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            PresetObject.Preset.from(Preset),
            0,
            0
        )
        createUserPresetRelationship(userPresetRelationshipModel){
            completed(it)
        }
    }

    fun queryApprovedUserPresetRelationshipBasedOnUser(
        userData: UserData,
        completed: (userPresetRelationship: List<UserPresetRelationship?>) -> Unit
    ) {
        scope.launch {
            val userPresetRelationshipList = mutableListOf<UserPresetRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPresetRelationship::class.java,
                    UserPresetRelationship.USER_PRESET_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPresetRelationshipData in response.data) {
                            if(userPresetRelationshipData != null) {
                                Log.i(TAG, userPresetRelationshipData.toString())
                                userPresetRelationshipList.add(userPresetRelationshipData)
                            }
                        }
                    }
                    completed(userPresetRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserPresetRelationshipBasedOnUserAndPreset(
        userData: UserData,
        Preset: PresetData,
        completed: (userPresetRelationship: List<UserPresetRelationship?>) -> Unit
    ) {
        scope.launch {
            val userPresetRelationshipList = mutableListOf<UserPresetRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPresetRelationship::class.java,
                    UserPresetRelationship.USER_PRESET_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserPresetRelationship.USER_PRESET_RELATIONSHIP_PRESET.eq(Preset.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPresetRelationshipData in response.data) {
                            if(userPresetRelationshipData != null) {
                                Log.i(TAG, userPresetRelationshipData.toString())
                                userPresetRelationshipList.add(userPresetRelationshipData)
                            }
                        }
                    }
                    completed(userPresetRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}