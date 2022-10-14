package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSelfLove
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.models.UserRoutineRelationshipSelfLoveObject
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserRoutineRelationshipSelfLoveBackend {
    private const val TAG = "UserRoutineRelationshipSelfLoveBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    private fun createUserRoutineRelationshipSelfLove(
        userRoutineRelationshipSelfLoveModel: UserRoutineRelationshipSelfLoveObject.UserRoutineRelationshipSelfLoveModel,
        completed: (userRoutineRelationshipSelfLove: UserRoutineRelationshipSelfLove) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineRelationshipSelfLoveModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create UserRoutineRelationshipSelfLoveModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created UserRoutineRelationshipSelfLoveModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineRelationshipSelfLoveObject(
        selfLoveData: SelfLoveData,
        userRoutineRelationship: UserRoutineRelationship,
        completed: (UserRoutineRelationshipSelfLove: UserRoutineRelationshipSelfLove) -> Unit
    ){
        val userRoutineRelationshipSelfLoveModel = UserRoutineRelationshipSelfLoveObject.UserRoutineRelationshipSelfLoveModel(
            UUID.randomUUID().toString(),
            UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationship),
            SelfLoveObject.SelfLove.from(selfLoveData),
        )
        createUserRoutineRelationshipSelfLove(userRoutineRelationshipSelfLoveModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserRoutineRelationshipSelfLoveBasedOnUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipSelfLove: List<UserRoutineRelationshipSelfLove?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipSelfLoveList = mutableListOf<UserRoutineRelationshipSelfLove?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipSelfLove::class.java,
                    UserRoutineRelationshipSelfLove.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipSelfLoveData in response.data) {
                            if(userRoutineRelationshipSelfLoveData != null) {
                                Log.i(TAG, userRoutineRelationshipSelfLoveData.toString())
                                userRoutineRelationshipSelfLoveList.add(userRoutineRelationshipSelfLoveData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipSelfLoveList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipSelfLoveBasedOnSelfLoveAndUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        selfLoveData: SelfLoveData,
        completed: (UserRoutineRelationshipSelfLove: List<UserRoutineRelationshipSelfLove?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipSelfLoveList = mutableListOf<UserRoutineRelationshipSelfLove?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipSelfLove::class.java,
                    UserRoutineRelationshipSelfLove.SELF_LOVE_DATA.eq(selfLoveData.id)
                        .and(UserRoutineRelationshipSelfLove.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipSelfLoveData in response.data) {
                            if(userRoutineRelationshipSelfLoveData != null) {
                                Log.i(TAG, userRoutineRelationshipSelfLoveData.toString())
                                userRoutineRelationshipSelfLoveList.add(userRoutineRelationshipSelfLoveData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipSelfLoveList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}