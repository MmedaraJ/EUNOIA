package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.PresetNameAndVolumesMapData
import com.example.eunoia.models.PresetNameAndVolumesMapObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object PresetNameAndVolumesMapBackend {
    private const val TAG = "PresetNameAndVolumesMapBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createPresetNameAndVolumesMap(
        presetNameAndVolumesMap: PresetNameAndVolumesMapObject.PresetNameAndVolumesMap,
        completed: (presetNameAndVolumesMapData: PresetNameAndVolumesMapData) -> Unit
    ){
        scope.launch{
            Amplify.API.mutate(
                ModelMutation.create(presetNameAndVolumesMap.data),
                { response ->
                    if (response.hasErrors()) {
                        Log.e(TAG, response.errors.first().message)
                    } else {
                        Log.i(TAG, "Created presetNameAndVolumesMap with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }
}