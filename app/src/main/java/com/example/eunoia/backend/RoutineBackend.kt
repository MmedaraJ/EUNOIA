package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.RoutineData
import com.example.eunoia.models.RoutineObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object RoutineBackend {
    private const val TAG = "RoutineBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

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
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }
}