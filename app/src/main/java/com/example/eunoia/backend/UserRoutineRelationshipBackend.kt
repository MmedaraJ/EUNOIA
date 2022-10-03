package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserRoutineRelationshipBackend {
    private const val TAG = "UserRoutineRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserRoutineRelationship(
        userRoutineRelationshipModel: UserRoutineRelationshipObject.UserRoutineRelationshipModel,
        completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userRoutineRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userRoutineRelationshipModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineRelationshipObject(routine: RoutineData, completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit){
        val userRoutineRelationshipModel = UserRoutineRelationshipObject.UserRoutineRelationshipModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            RoutineObject.Routine.from(routine),
            0,
            0
        )
        createUserRoutineRelationship(userRoutineRelationshipModel){
            completed(it)
        }
    }

    fun queryApprovedUserRoutineRelationshipBasedOnUser(
        userData: UserData,
        completed: (userRoutineRelationship: List<UserRoutineRelationship?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipList = mutableListOf<UserRoutineRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationship::class.java,
                    UserRoutineRelationship.USER_ROUTINE_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipData in response.data) {
                            if(userRoutineRelationshipData != null) {
                                Log.i(TAG, userRoutineRelationshipData.toString())
                                userRoutineRelationshipList.add(userRoutineRelationshipData)
                            }
                        }
                    }
                    completed(userRoutineRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipBasedOnUserAndRoutine(
        userData: UserData,
        routine: RoutineData,
        completed: (userRoutineRelationship: List<UserRoutineRelationship?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipList = mutableListOf<UserRoutineRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationship::class.java,
                    UserRoutineRelationship.USER_ROUTINE_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserRoutineRelationship.USER_ROUTINE_RELATIONSHIP_ROUTINE.eq(routine.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipData in response.data) {
                            if(userRoutineRelationshipData != null) {
                                Log.i(TAG, userRoutineRelationshipData.toString())
                                userRoutineRelationshipList.add(userRoutineRelationshipData)
                            }
                        }
                    }
                    completed(userRoutineRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}