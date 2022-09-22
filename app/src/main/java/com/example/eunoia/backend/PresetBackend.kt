package com.example.eunoia.backend

import android.content.Context
import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.PresetPublicityStatus
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.models.PresetObject
import com.example.eunoia.models.SoundObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.navigation.globalViewModel_
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

    fun queryPresetsWithCommentsBasedOnSound(
        sound: SoundData,
        completed: (presets: MutableList<PresetData>) -> Unit
    ) {
        scope.launch {
            val presetList = mutableListOf<PresetData>()
            Amplify.API.query(
                ModelQuery.list(
                    PresetData::class.java,
                    PresetData.SOUND.eq(sound.id)
                        .and(PresetData.PRESET_OWNER.ne(globalViewModel_!!.currentUser!!.id))
                        .and(PresetData.PUBLICITY_STATUS.eq(PresetPublicityStatus.PUBLIC))
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (presetData in response.data) {
                                Log.i(TAG, presetData.toString())
                                presetList.add(presetData)
                            }
                        }
                        completed(presetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryPublicPresetsBasedOnDisplayNameAndSound(
        presetName: String,
        soundData: SoundData,
        completed: (presets: MutableList<PresetData>) -> Unit
    ) {
        scope.launch {
            val presetList = mutableListOf<PresetData>()
            Amplify.API.query(
                ModelQuery.list(
                    PresetData::class.java,
                    PresetData.KEY.eq(presetName)
                        .and(PresetData.SOUND.eq(soundData.id))
                        .and(PresetData.PUBLICITY_STATUS.eq(PresetPublicityStatus.PUBLIC))
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (presetData in response.data) {
                                Log.i(TAG, presetData.toString())
                                presetList.add(presetData)
                            }
                        }
                        completed(presetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserPresetsBasedOnSound(
        sound: SoundData,
        user: UserData,
        completed: (presets: MutableList<PresetData>) -> Unit
    ) {
        scope.launch {
            val presetList = mutableListOf<PresetData>()
            Amplify.API.query(
                ModelQuery.list(
                    PresetData::class.java,
                    PresetData.SOUND.eq(sound.id)
                        .and(PresetData.PRESET_OWNER.eq(user.id))
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (presetData in response.data) {
                                Log.i(TAG, "Preset received $presetData")
                                presetList.add(presetData)
                            }
                        }
                        completed(presetList)
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