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

object RoutineSoundPresetBackend {
    private const val TAG = "RoutineSoundPresetBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private fun createRoutineSoundPreset(
        routineSoundPresetModel: RoutineSoundPresetObject.RoutineSoundPresetModel,
        completed: (routineSoundPreset: RoutineSoundPreset) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(routineSoundPresetModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create routineSoundPresetModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created routineSoundPresetModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createRoutineSoundPresetObject(
        presetData: SoundPresetData,
        routineData: RoutineData,
        completed: (routineSoundPreset: RoutineSoundPreset) -> Unit
    ){
        val routineSoundPresetModel = RoutineSoundPresetObject.RoutineSoundPresetModel(
            UUID.randomUUID().toString(),
            SoundPresetObject.SoundPreset.from(presetData),
            RoutineObject.Routine.from(routineData)
        )
        createRoutineSoundPreset(routineSoundPresetModel){
            completed(it)
        }
    }

    fun queryRoutineSoundPresetBasedOnRoutine(
        routineData: RoutineData,
        completed: (routineSoundPreset: List<RoutineSoundPreset?>) -> Unit
    ) {
        scope.launch {
            val routineSoundPresetList = mutableListOf<RoutineSoundPreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineSoundPreset::class.java,
                    RoutineSoundPreset.ROUTINE_DATA.eq(routineData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routineSoundPresetData in response.data) {
                            if(routineSoundPresetData != null) {
                                Log.i(TAG, routineSoundPresetData.toString())
                                routineSoundPresetList.add(routineSoundPresetData)
                            }
                        }
                    }
                    completed(routineSoundPresetList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryRoutineSoundPresetBasedOnRoutineAndSoundPreset(
        routineData: RoutineData,
        presetData: SoundPresetData,
        completed: (routineSoundPreset: List<RoutineSoundPreset?>) -> Unit
    ) {
        scope.launch {
            val routineSoundPresetList = mutableListOf<RoutineSoundPreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineSoundPreset::class.java,
                    RoutineSoundPreset.ROUTINE_DATA.eq(routineData.id)
                        .and(RoutineSoundPreset.SOUND_PRESET_DATA.eq(presetData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routineSoundPresetData in response.data) {
                            if(routineSoundPresetData != null) {
                                Log.i(TAG, routineSoundPresetData.toString())
                                routineSoundPresetList.add(routineSoundPresetData)
                            }
                        }
                    }
                    completed(routineSoundPresetList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}