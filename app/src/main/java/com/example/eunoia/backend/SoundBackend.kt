package com.example.eunoia.backend

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageListOptions
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.result.StorageListResult
import com.example.eunoia.models.SoundObject
import kotlinx.coroutines.*
import java.io.File

object SoundBackend{
    private const val TAG = "SoundBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun getSoundWithID(id: String, completed: (sound: SoundData) -> Unit){
        scope.launch {
            Amplify.API.query(ModelQuery.get(SoundData::class.java, id),
                { response ->
                    Log.i(TAG, "Query results = ${(response.data as SoundData).displayName}")
                    completed(response.data)
                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }
    }

    /**
     * Used when navigating to a sound screen
     * Returns only one sound with the given display name
     */
    fun getSoundWithDisplayName(
        display_name: String,
        completed: (sound: SoundData) -> Unit){
        scope.launch{
            Amplify.API.query(ModelQuery.get(SoundData::class.java, "a6b4c130-b201-42a3-b99e-f8e896f218c8"),
                {response ->
                    Log.i(TAG, "getSoundWithDisplayName Response: $response")
                    if(response.hasData()) {
                        Log.i(TAG, "Query results = ${(response.data as SoundData).displayName}")
                        //mainScope.launch { completed(response.data) }
                        completed(response.data)
                    }
                },
                { Log.e(TAG, "Query failed", it) }
            )
        }
    }

    fun querySoundBasedOnDisplayName(
        display_name: String,
        context: Context,
        completed: (sound: SoundData) -> Unit) {
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(SoundData::class.java, SoundData.DISPLAY_NAME.eq(display_name)),
                { response ->
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            Log.i(TAG, soundData.toString())
                            completed(soundData)
                            break
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun querySoundBasedOnOriginalName(
        original_name: String,
        completed: (soundList: List<SoundData>) -> Unit) {
        scope.launch {
            var sounds = mutableListOf<SoundData>()
            Amplify.API.query(
                ModelQuery.list(SoundData::class.java, SoundData.ORIGINAL_NAME.eq(original_name)),
                { response ->
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            Log.i(TAG, soundData.toString())
                            sounds.add(soundData)
                        }
                        completed(sounds)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun querySound(context: Context) {
        Log.i(TAG, "Querying sounds")
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(SoundData::class.java),
                { response ->
                    Log.i(TAG, "Queried $response")
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            Log.i(TAG, soundData.toString())
                            // TODO should add all the sounds at once instead of one by one (each add triggers a UI refresh)
                            SoundObject.addSound(SoundObject.Sound.from(soundData))
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun createSound(sound: SoundObject.Sound, completed: (sound: SoundData) -> Unit) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(sound.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create sound ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created sound with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun deleteSound(sound: SoundObject.Sound?) {
        if (sound == null) return
        Log.i(TAG, "Deleting sound $sound")
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.delete(sound.data),
                { response ->
                    Log.i(TAG, "Deleted")
                    if (response.hasErrors()) {
                        Log.e(TAG, response.errors.first().message)
                    } else {
                        Log.i(TAG, "Deleted sound $response")
                    }
                },
                { error -> Log.e(TAG, "Delete failed", error) }
            )
        }
    }

    fun storeAudio(filePath: String, key: String) {
        val file = File(filePath)
        val options = StorageUploadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PROTECTED)
            .build()

        scope.launch {
            Amplify.Storage.uploadFile(
                key,
                file,
                options,
                { progress -> Log.i(TAG, "Fraction completed: ${progress.fractionCompleted}") },
                { result -> Log.i(TAG, "Successfully uploaded: " + result.key) },
                { error -> Log.e(TAG, "Upload failed", error) }
            )
        }
    }

    fun storeAudioUri(audioUri: Uri?, contentResolver: ContentResolver){
        val stream = contentResolver.openInputStream(audioUri!!)!!
        scope.launch {
            Amplify.Storage.uploadInputStream(
                "",
                stream,
                { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
                { Log.e("MyAmplifyApp", "Upload failed", it) }
            )
        }
    }

    fun deleteAudio(key : String) {
        val options = StorageRemoveOptions.builder()
            .accessLevel(StorageAccessLevel.PROTECTED)
            .build()

        scope.launch {
            Amplify.Storage.remove(
                key,
                options,
                { result -> Log.i(TAG, "Successfully removed: " + result.key) },
                { error -> Log.e(TAG, "Remove failure", error) }
            )
        }
    }

    fun retrieveAudio(key: String, completed : (audioUri: Uri) -> Unit) {
        val options = StorageDownloadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PROTECTED)
            .targetIdentityId("us-east-1:7dc83e15-97ea-4397-b424-ef090df376f3")
            .build()
        val file = File.createTempFile("audio", ".aac")

        scope.launch {
            Amplify.Storage.downloadFile(
                key,
                file,
                options,
                { progress -> Log.i(TAG, "Fraction completed: ${progress.fractionCompleted}") },
                { result ->
                    Log.i(TAG, "Successfully downloaded: ${result.file.name}")
                    completed(result.file.toUri())
                },
                { error -> Log.e(TAG, "Download Failure", error) }
            )
        }
    }

    fun listEunoiaSounds(soundAudioPath: String, completed: (result: StorageListResult) -> Unit){
        val options = StorageListOptions.builder()
            .accessLevel(StorageAccessLevel.PROTECTED)
            .targetIdentityId("us-east-1:7dc83e15-97ea-4397-b424-ef090df376f3")
            .build()

        scope.launch {
            Amplify.Storage.list(
                soundAudioPath,
                options,
                { result ->
                    Log.i(TAG, "Successfully listed files")
                    completed(result)
                },
                { Log.e(TAG, "List failure", it) }
            )
        }
    }
}