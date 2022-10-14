package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.RoutineObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object RoutineBackend {
    private const val TAG = "RoutineBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createRoutine(routine: RoutineObject.Routine, completed: (routine: RoutineData) -> Unit) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(routine.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create routine ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created routine with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun updateRoutine(routine: RoutineData, completed: (routine: RoutineData) -> Unit){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(routine),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Successfully updated routine: ${response.data}")
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                {
                    Log.i(TAG, "Error while updating routine: ", it)
                }
            )
        }
    }

    fun queryRoutinesBasedOnDisplayName(
        routineName: String,
        completed: (routines: MutableList<RoutineData>) -> Unit
    ) {
        scope.launch {
            val routineList = mutableListOf<RoutineData>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineData::class.java,
                    RoutineData.DISPLAY_NAME.eq(routineName)
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (routineData in response.data) {
                                if(routineData != null) {
                                    Log.i(TAG, routineData.toString())
                                    routineList.add(routineData)
                                }
                            }
                        }
                        mainScope.launch {
                            completed(routineList)
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}