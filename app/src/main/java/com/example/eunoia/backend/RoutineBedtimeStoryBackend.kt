package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.RoutineBedtimeStoryObject
import com.example.eunoia.models.BedtimeStoryObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object RoutineBedtimeStoryBackend {
    private const val TAG = "RoutineBedtimeStoryBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private fun createRoutineBedtimeStory(
        routineBedtimeStoryModel: RoutineBedtimeStoryObject.RoutineBedtimeStoryModel,
        completed: (routineBedtimeStory: RoutineBedtimeStoryInfo) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(routineBedtimeStoryModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create routineBedtimeStoryModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created routineBedtimeStoryModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createRoutineBedtimeStoryObject(
        bedtimeStoryData: BedtimeStoryInfoData,
        routineData: RoutineData,
        completed: (routineBedtimeStory: RoutineBedtimeStoryInfo) -> Unit
    ){
        val routineBedtimeStoryModel = RoutineBedtimeStoryObject.RoutineBedtimeStoryModel(
            UUID.randomUUID().toString(),
            RoutineObject.Routine.from(routineData),
            BedtimeStoryObject.BedtimeStory.from(bedtimeStoryData),
        )
        createRoutineBedtimeStory(routineBedtimeStoryModel){
            completed(it)
        }
    }

    fun queryRoutineBedtimeStoryBasedOnRoutine(
        routineData: RoutineData,
        completed: (routineBedtimeStory: List<RoutineBedtimeStoryInfo?>) -> Unit
    ) {
        scope.launch {
            val routineBedtimeStoryList = mutableListOf<RoutineBedtimeStoryInfo?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineBedtimeStoryInfo::class.java,
                    RoutineBedtimeStoryInfo.ROUTINE_DATA.eq(routineData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routineBedtimeStoryData in response.data) {
                            Log.i(TAG, routineBedtimeStoryData.toString())
                            routineBedtimeStoryList.add(routineBedtimeStoryData)
                        }
                    }
                    completed(routineBedtimeStoryList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}