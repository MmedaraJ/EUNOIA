package com.example.eunoia.backend

import android.content.Context
import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.models.PresetObject
import com.example.eunoia.models.SoundObject
import com.example.eunoia.models.UserObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object PresetBackend {
    private const val TAG = "PresetBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createPreset(preset: PresetObject.Preset, completed: (presetData: PresetData) -> Unit){
        scope.launch{
            Amplify.API.mutate(
                ModelMutation.create(preset.data),
                { response ->
                    Log.i(TAG, "Created preset $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, response.errors.first().message)
                    } else {
                        Log.i(TAG, "Created preset with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun queryPresetBasedOnSound(sound: SoundData, completed: (preset: PresetData) -> Unit) {
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(PresetData::class.java, PresetData.SOUND.eq(sound.id)),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (presetData in response.data) {
                                Log.i(TAG, presetData.toString())
                                completed(presetData)
                                break
                            }
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun getPresetWithID(id: String, completed: (preset: PresetData) -> Unit){
        scope.launch {
            Amplify.API.query(
                ModelQuery.get(PresetData::class.java, id),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Query results = ${(response.data as PresetData).id}")
                        completed(response.data)
                    }
                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }
    }
}