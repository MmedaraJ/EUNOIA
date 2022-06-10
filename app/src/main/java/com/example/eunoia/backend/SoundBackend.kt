package com.example.eunoia.backend

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.example.eunoia.dashboard.upload_files.UploadFilesActivity
import com.example.eunoia.models.SoundObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

object SoundBackend{
    private const val TAG = "SoundBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun getSoundWithKey(key: String, completed: (sound: SoundData) -> Unit){
        Log.i(TAG, "Querying sound with key")
        scope.launch {
            Amplify.API.query(ModelQuery.get(SoundData::class.java, key),
                { response ->
                    Log.i("MyAmplifyApp", "Query results = ${(response.data as SoundData).displayName}")
                    completed(response.data)
                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            );
        }
    }

    fun querySound() {
        Log.i(TAG, "Querying sounds")
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(SoundData::class.java),
                { response ->
                    Log.i(TAG, "Queried")
                    for (soundData in response.data) {
                        Log.i(TAG, "${soundData.ownerUsername} = ${soundData.displayName}")
                        // TODO should add all the sounds at once instead of one by one (each add triggers a UI refresh)
                        SoundObject.addSound(SoundObject.Sound.from(soundData))
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun createSound(sound: SoundObject.Sound) {
        Log.i(TAG, "Creating sound")
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(sound.data),
                { response ->
                    Log.i(TAG, "Created")
                    if (response.hasErrors()) {
                        Log.e(TAG, response.errors.first().message)
                    } else {
                        Log.i(TAG, "Created sound with id: " + response.data.id)
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
                        Log.i(TAG, "Deleted Sound $response")
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
            Log.i(TAG, "About to start storing")
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
            Amplify.Storage.uploadInputStream("ExampleKey", stream,
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

    fun retrieveAudio(key: String, completed : (/*mp3: Audio*/) -> Unit) {
        val options = StorageDownloadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PROTECTED)
            .build()
        val file = File.createTempFile("pouring_rain", ".mp3")

        scope.launch {
            Amplify.Storage.downloadFile(
                key,
                file,
                options,
                { progress -> Log.i(TAG, "Fraction completed: ${progress.fractionCompleted}") },
                { result ->
                    Log.i(TAG, "Successfully downloaded: ${result.file.name}")
                    /*val imageStream = FileInputStream(file)
                    val image = BitmapFactory.decodeStream(imageStream)
                    completed(image)*/
                },
                { error -> Log.e(TAG, "Download Failure", error) }
            )
        }
    }
}