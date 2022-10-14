package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object RoutineSelfLoveBackend {
    private const val TAG = "RoutineSelfLoveBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createRoutineSelfLove(
        routineSelfLoveModel: RoutineSelfLoveObject.RoutineSelfLoveModel,
        completed: (routineSelfLove: RoutineSelfLove) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(routineSelfLoveModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create routineSelfLoveModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created routineSelfLoveModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createRoutineSelfLoveObject(
        selfLoveData: SelfLoveData,
        routineData: RoutineData,
        completed: (routineSelfLove: RoutineSelfLove) -> Unit
    ){
        val routineSelfLoveModel = RoutineSelfLoveObject.RoutineSelfLoveModel(
            UUID.randomUUID().toString(),
            RoutineObject.Routine.from(routineData),
            SelfLoveObject.SelfLove.from(selfLoveData),
        )
        createRoutineSelfLove(routineSelfLoveModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryRoutineSelfLoveBasedOnRoutine(
        routineData: RoutineData,
        completed: (routineSelfLove: List<RoutineSelfLove?>) -> Unit
    ) {
        scope.launch {
            val routineSelfLoveList = mutableListOf<RoutineSelfLove?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineSelfLove::class.java,
                    RoutineSelfLove.ROUTINE_DATA.eq(routineData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routineSelfLoveData in response.data) {
                            if(routineSelfLoveData != null) {
                                Log.i(TAG, routineSelfLoveData.toString())
                                routineSelfLoveList.add(routineSelfLoveData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(routineSelfLoveList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryRoutineSelfLoveBasedOnSelfLoveAndRoutine(
        routineData: RoutineData,
        selfLoveData: SelfLoveData,
        completed: (routineSelfLove: List<RoutineSelfLove?>) -> Unit
    ) {
        scope.launch {
            val routineSelfLoveList = mutableListOf<RoutineSelfLove?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineSelfLove::class.java,
                    RoutineSelfLove.SELF_LOVE_DATA.eq(selfLoveData.id)
                        .and(RoutineSelfLove.ROUTINE_DATA.eq(routineData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routineSelfLoveData in response.data) {
                            if(routineSelfLoveData != null) {
                                Log.i(TAG, routineSelfLoveData.toString())
                                routineSelfLoveList.add(routineSelfLoveData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(routineSelfLoveList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}