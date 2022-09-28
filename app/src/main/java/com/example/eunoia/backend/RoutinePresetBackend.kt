package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.RoutinePreset
import com.amplifyframework.datastore.generated.model.PresetData
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.RoutinePresetObject
import com.example.eunoia.models.PresetObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object RoutinePresetBackend {
    private const val TAG = "RoutinePresetBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createRoutinePreset(
        routinePresetModel: RoutinePresetObject.RoutinePresetModel,
        completed: (routinePreset: RoutinePreset) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(routinePresetModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create routinePresetModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created routinePresetModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createRoutinePresetObject(
        presetData: PresetData,
        routineData: RoutineData,
        completed: (routinePreset: RoutinePreset) -> Unit
    ){
        val routinePresetModel = RoutinePresetObject.RoutinePresetModel(
            UUID.randomUUID().toString(),
            PresetObject.Preset.from(presetData),
            RoutineObject.Routine.from(routineData)
        )
        createRoutinePreset(routinePresetModel){
            completed(it)
        }
    }

    fun queryRoutinePresetBasedOnRoutine(
        routineData: RoutineData,
        completed: (routinePreset: List<RoutinePreset?>) -> Unit
    ) {
        scope.launch {
            val routinePresetList = mutableListOf<RoutinePreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutinePreset::class.java,
                    RoutinePreset.ROUTINE_DATA.eq(routineData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routinePresetData in response.data) {
                            Log.i(TAG, routinePresetData.toString())
                            routinePresetList.add(routinePresetData)
                        }
                    }
                    completed(routinePresetList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryRoutinePresetBasedOnRoutineAndPreset(
        routineData: RoutineData,
        presetData: PresetData,
        completed: (routinePreset: List<RoutinePreset?>) -> Unit
    ) {
        scope.launch {
            val routinePresetList = mutableListOf<RoutinePreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutinePreset::class.java,
                    RoutinePreset.ROUTINE_DATA.eq(routineData.id)
                        .and(RoutinePreset.PRESET_DATA.eq(presetData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routinePresetData in response.data) {
                            Log.i(TAG, routinePresetData.toString())
                            routinePresetList.add(routinePresetData)
                        }
                    }
                    completed(routinePresetList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}