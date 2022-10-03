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

object RoutinePrayerBackend {
    private const val TAG = "RoutinePrayerBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createRoutinePrayer(
        routinePrayerModel: RoutinePrayerObject.RoutinePrayerModel,
        completed: (routinePrayer: RoutinePrayer) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(routinePrayerModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create routinePrayerModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created routinePrayerModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createRoutinePrayerObject(
        prayerData: PrayerData,
        routineData: RoutineData,
        completed: (routinePrayer: RoutinePrayer) -> Unit
    ){
        val routinePrayerModel = RoutinePrayerObject.RoutinePrayerModel(
            UUID.randomUUID().toString(),
            RoutineObject.Routine.from(routineData),
            PrayerObject.Prayer.from(prayerData),
        )
        createRoutinePrayer(routinePrayerModel){
            completed(it)
        }
    }

    fun queryRoutinePrayerBasedOnRoutine(
        routineData: RoutineData,
        completed: (routinePrayer: List<RoutinePrayer?>) -> Unit
    ) {
        scope.launch {
            val routinePrayerList = mutableListOf<RoutinePrayer?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutinePrayer::class.java,
                    RoutinePrayer.ROUTINE_DATA.eq(routineData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routinePrayerData in response.data) {
                            if(routinePrayerData != null) {
                                Log.i(TAG, routinePrayerData.toString())
                                routinePrayerList.add(routinePrayerData)
                            }
                        }
                    }
                    completed(routinePrayerList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryRoutinePrayerBasedOnPrayerAndRoutine(
        routineData: RoutineData,
        prayerData: PrayerData,
        completed: (routinePrayer: List<RoutinePrayer?>) -> Unit
    ) {
        scope.launch {
            val routinePrayerList = mutableListOf<RoutinePrayer?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutinePrayer::class.java,
                    RoutinePrayer.PRAYER_DATA.eq(prayerData.id)
                        .and(RoutinePrayer.ROUTINE_DATA.eq(routineData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routinePrayerData in response.data) {
                            if(routinePrayerData != null) {
                                Log.i(TAG, routinePrayerData.toString())
                                routinePrayerList.add(routinePrayerData)
                            }
                        }
                    }
                    completed(routinePrayerList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}