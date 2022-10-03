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

object RoutineSoundBackend {
    private const val TAG = "RoutineSoundBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createRoutineSound(
        routineSoundModel: RoutineSoundObject.RoutineSoundModel,
        completed: (routineSound: RoutineSound) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(routineSoundModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create routineSoundModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created routineSoundModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createRoutineSoundObject(
        soundData: SoundData,
        routineData: RoutineData,
        completed: (routineSound: RoutineSound) -> Unit
    ){
        val routineSoundModel = RoutineSoundObject.RoutineSoundModel(
            UUID.randomUUID().toString(),
            SoundObject.Sound.from(soundData),
            RoutineObject.Routine.from(routineData)
        )
        createRoutineSound(routineSoundModel){
            completed(it)
        }
    }

    fun queryRoutineSoundBasedOnRoutine(
        routineData: RoutineData,
        completed: (routineSound: List<RoutineSound?>) -> Unit
    ) {
        scope.launch {
            val routineSoundList = mutableListOf<RoutineSound?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineSound::class.java,
                    RoutineSound.ROUTINE_DATA.eq(routineData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routineSoundData in response.data) {
                            if(routineSoundData != null) {
                                Log.i(TAG, routineSoundData.toString())
                                routineSoundList.add(routineSoundData)
                            }
                        }
                    }
                    completed(routineSoundList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryRoutineSoundBasedOnRoutineAndSound(
        routineData: RoutineData,
        soundData: SoundData,
        completed: (routineSound: List<RoutineSound?>) -> Unit
    ) {
        scope.launch {
            val routineSoundList = mutableListOf<RoutineSound?>()
            Amplify.API.query(
                ModelQuery.list(
                    RoutineSound::class.java,
                    RoutineSound.ROUTINE_DATA.eq(routineData.id)
                        .and(RoutineSound.SOUND_DATA.eq(soundData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (routineSoundData in response.data) {
                            if(routineSoundData != null) {
                                Log.i(TAG, routineSoundData.toString())
                                routineSoundList.add(routineSoundData)
                            }
                        }
                    }
                    completed(routineSoundList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}