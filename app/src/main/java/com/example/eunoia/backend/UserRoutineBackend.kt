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

object UserRoutineBackend {
    private const val TAG = "UserRoutineBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createUserRoutine(
        userRoutineModel: UserRoutineObject.UserRoutineModel,
        completed: (userRoutine: UserRoutine) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userRoutineModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userRoutineModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineObject(routineData: RoutineData, completed: (userRoutine: UserRoutine) -> Unit){
        val userRoutineModel = UserRoutineObject.UserRoutineModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            RoutineObject.Routine.from(routineData),
        )
        createUserRoutine(userRoutineModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserRoutineBasedOnUser(
        userData: UserData,
        completed: (userRoutine: List<UserRoutine?>) -> Unit
    ) {
        scope.launch {
            val userRoutineList = mutableListOf<UserRoutine?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutine::class.java,
                    UserRoutine.USER_DATA.eq(userData.id),
                ),
                { response ->
                    Log.i(TAG, "2233 $response")
                    if(response.hasData()) {
                        for (userRoutineData in response.data) {
                            if(userRoutineData != null) {
                                Log.i(TAG, userRoutineData.toString())
                                userRoutineList.add(userRoutineData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineBasedOnRoutineAndUser(
        userData: UserData,
        routineData: RoutineData,
        completed: (userRoutine: List<UserRoutine?>) -> Unit
    ) {
        scope.launch {
            val userRoutineList = mutableListOf<UserRoutine?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutine::class.java,
                    UserRoutine.USER_DATA.eq(userData.id)
                        .and(UserRoutine.ROUTINE_DATA.eq(routineData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineData in response.data) {
                            if(userRoutineData != null) {
                                Log.i(TAG, userRoutineData.toString())
                                userRoutineList.add(userRoutineData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun updateUserRoutine(userRoutine: UserRoutine, completed: (userRoutine: UserRoutine) -> Unit){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(userRoutine),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Successfully updated user routine: ${response.data}")
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                {
                    Log.i(TAG, "Error while updating user routine: ", it)
                }
            )
        }
    }
}