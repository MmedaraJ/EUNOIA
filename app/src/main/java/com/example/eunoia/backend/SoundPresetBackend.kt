package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.SoundPresetObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object SoundPresetBackend {
    private const val TAG = "SoundPresetBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createSoundPreset(preset: SoundPresetObject.SoundPreset, completed: (presetData: SoundPresetData) -> Unit){
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

    fun querySoundPresetsWithCommentsBasedOnSound(
        sound: SoundData,
        completed: (presets: MutableList<SoundPresetData>) -> Unit
    ) {
        scope.launch {
            val presetList = mutableListOf<SoundPresetData>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundPresetData::class.java,
                    SoundPresetData.SOUND_ID.eq(sound.id)
                        //.and(SoundPresetData.PRESET_OWNER.ne(globalViewModel_!!.currentUser!!.id))
                        .and(SoundPresetData.PUBLICITY_STATUS.eq(SoundPresetPublicityStatus.PUBLIC))
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (presetData in response.data) {
                                if(presetData != null) {
                                    Log.i(TAG, presetData.toString())
                                    presetList.add(presetData)
                                }
                            }
                        }
                        completed(presetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryPublicSoundPresetsBasedOnDisplayNameAndSound(
        presetName: String,
        soundData: SoundData,
        completed: (presets: MutableList<SoundPresetData>) -> Unit
    ) {
        scope.launch {
            val presetList = mutableListOf<SoundPresetData>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundPresetData::class.java,
                    SoundPresetData.KEY.eq(presetName)
                        .and(SoundPresetData.SOUND_ID.eq(soundData.id))
                        .and(SoundPresetData.PUBLICITY_STATUS.eq(SoundPresetPublicityStatus.PUBLIC))
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (presetData in response.data) {
                                if(presetData != null) {
                                    Log.i(TAG, presetData.toString())
                                    presetList.add(presetData)
                                }
                            }
                        }
                        completed(presetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserSoundPresetsBasedOnSound(
        sound: SoundData,
        user: UserData,
        completed: (presets: MutableList<SoundPresetData>) -> Unit
    ) {
        scope.launch {
            val presetList = mutableListOf<SoundPresetData>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundPresetData::class.java,
                    SoundPresetData.SOUND_ID.eq(sound.id)
                        .and(SoundPresetData.PRESET_OWNER.eq(user.id))
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (presetData in response.data) {
                                if(presetData != null) {
                                    Log.i(TAG, "Preset received $presetData")
                                    presetList.add(presetData)
                                }
                            }
                        }
                        completed(presetList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun getSoundPresetWithID(id: String, completed: (preset: SoundPresetData) -> Unit){
        scope.launch {
            Amplify.API.query(
                ModelQuery.get(SoundPresetData::class.java, id),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Query results = ${(response.data as SoundPresetData).id}")
                        completed(response.data)
                    }
                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }
    }
}