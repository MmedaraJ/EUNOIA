package com.example.eunoia.backend

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.amazonaws.mobileconnectors.cognitoauth.Auth
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
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
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun getSoundWithID(id: String, completed: (sound: SoundData) -> Unit){
        scope.launch {
            Amplify.API.query(ModelQuery.get(SoundData::class.java, id),
                { response ->
                    Log.i(TAG, "Query results = ${(response.data as SoundData).displayName}")
                    mainScope.launch {
                        completed(response.data)
                    }
                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }
    }

    fun querySoundBasedOnDisplayName(
        display_name: String,
        completed: (sound: List<SoundData?>) -> Unit)
    {
        scope.launch {
            val soundList = mutableListOf<SoundData?>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundData::class.java,
                    SoundData.DISPLAY_NAME.eq(display_name)
                ),
                { response ->
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            if(soundData != null) {
                                Log.i(TAG, soundData.toString())
                                soundList.add(soundData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(soundList)
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
            val sounds = mutableListOf<SoundData>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundData::class.java,
                    SoundData.ORIGINAL_NAME.eq(original_name)
                ),
                { response ->
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            if(soundData != null) {
                                Log.i(TAG, soundData.toString())
                                sounds.add(soundData)
                            }
                        }
                        mainScope.launch {
                            completed(sounds)
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryOtherSoundsBasedOnOriginalName(
        original_name: String,
        sound: SoundData,
        completed: (soundList: List<SoundData>) -> Unit) {
        scope.launch {
            val sounds = mutableListOf<SoundData>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundData::class.java,
                    SoundData.ORIGINAL_NAME.eq(original_name)
                        .and(SoundData.ID.ne(sound.id))
                ),
                { response ->
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            if(soundData != null) {
                                Log.i(TAG, soundData.toString())
                                sounds.add(soundData)
                            }
                        }
                        mainScope.launch {
                            completed(sounds)
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryCompleteApprovedSoundBasedOnUser(
        user: UserData,
        completed: (sounds: List<SoundData?>) -> Unit
    ) {
        scope.launch {
            val soundList = mutableListOf<SoundData?>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundData::class.java,
                    SoundData.SOUND_OWNER.eq(user.id)
                        //.and(SoundData.APPROVAL_STATUS.eq(SoundApprovalStatus.APPROVED))
                        //for now, allow pending
                        //.or(SoundData.APPROVAL_STATUS.eq(SoundApprovalStatus.PENDING)),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            if(soundData != null) {
                                Log.i(TAG, soundData.toString())
                                soundList.add(soundData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(soundList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun querySoundBasedOnId(
        soundId: String,
        completed: (sounds: List<SoundData?>) -> Unit
    ) {
        scope.launch {
            val soundList = mutableListOf<SoundData?>()
            Amplify.API.query(
                ModelQuery.list(
                    SoundData::class.java,
                    SoundData.ID.eq(soundId)
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            if(soundData != null) {
                                Log.i(TAG, soundData.toString())
                                soundList.add(soundData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(soundList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun querySound() {
        Log.i(TAG, "Querying sounds")
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(SoundData::class.java),
                { response ->
                    Log.i(TAG, "Queried $response")
                    if(response.hasData()) {
                        for (soundData in response.data) {
                            if(soundData != null) {
                                Log.i(TAG, soundData.toString())
                            }
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
                        mainScope.launch {
                            completed(response.data)
                        }
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

    fun storeAudio(filePath: String, key: String, completed: (key: String) -> Unit) {
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
                { result ->
                    Log.i(TAG, "Successfully uploaded: " + result.key)
                    completed(result.key)
                },
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

    fun retrieveAudio(
        key: String,
        targetIdentityId: String,
        completed : (audioUri: Uri?) -> Unit
    ) {
        val options = StorageDownloadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PROTECTED)
            .targetIdentityId(targetIdentityId)
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
                    mainScope.launch {
                        completed(result.file.toUri())
                    }
                },
                { error ->
                    Log.e(TAG, "Download Failure", error)
                    completed(null)
                }
            )
        }
        Amplify.Auth.currentUser.userId
    }

    fun listS3Sounds(
        soundAudioPath: String,
        targetIdentityId: String,
        completed: (result: StorageListResult) -> Unit
    ){
        val options = StorageListOptions.builder()
            .accessLevel(StorageAccessLevel.PROTECTED)
            .targetIdentityId(targetIdentityId)
            .build()

        scope.launch {
            Amplify.Storage.list(
                soundAudioPath,
                options,
                { result ->
                    Log.i(TAG, "Successfully listed files ${result.items}")
                    mainScope.launch {
                        completed(result)
                    }
                },
                { Log.e(TAG, "List failure", it) }
            )
        }
    }
}