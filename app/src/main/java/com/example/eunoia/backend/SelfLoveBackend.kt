package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.SelfLoveObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object SelfLoveBackend {
    private const val TAG = "SelfLoveBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createSelfLove(
        selfLove: SelfLoveObject.SelfLove,
        completed: (selfLoveData: SelfLoveData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(selfLove.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create SelfLove ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created SelfLove with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun querySelfLoveBasedOnDisplayName(
        displayName: String,
        completed: (selfLove: List<SelfLoveData?>) -> Unit
    ) {
        scope.launch {
            val selfLoveList = mutableListOf<SelfLoveData?>()
            Amplify.API.query(
                ModelQuery.list(
                    SelfLoveData::class.java,
                    SelfLoveData.DISPLAY_NAME.eq(displayName),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (selfLoveData in response.data) {
                            if(selfLoveData != null) {
                                Log.i(TAG, selfLoveData.toString())
                                selfLoveList.add(selfLoveData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(selfLoveList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryIncompleteSelfLoveBasedOnUser(
        user: UserData,
        completed: (selfLoves: List<SelfLoveData?>) -> Unit
    ) {
        scope.launch {
            val selfLoveList = mutableListOf<SelfLoveData?>()
            Amplify.API.query(
                ModelQuery.list(
                    SelfLoveData::class.java,
                    SelfLoveData.SELF_LOVE_OWNER.eq(user.id)
                        .and(SelfLoveData.CREATION_STATUS.eq(SelfLoveCreationStatus.INCOMPLETE))
                        .and(SelfLoveData.APPROVAL_STATUS.eq(SelfLoveApprovalStatus.PENDING))
                        .and(SelfLoveData.AUDIO_SOURCE.eq(SelfLoveAudioSource.RECORDED)),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (selfLoveData in response.data) {
                            if(selfLoveData != null) {
                                Log.i(TAG, selfLoveData.toString())
                                selfLoveList.add(selfLoveData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(selfLoveList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun updateSelfLove(
        selfLoveData: SelfLoveData,
        completed: (selfLoveData: SelfLoveData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(selfLoveData),
                { response ->
                    Log.i(TAG, "Updated $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from update SelfLoveData ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Updated SelfLoveData with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Update failed", error) }
            )
        }
    }

    fun deleteSelfLove(
        selfLoveData: SelfLoveData,
        completed: (successful: Boolean) -> Unit
    ){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.delete(selfLoveData),
                { response ->
                    Log.i(TAG, "Deleted $response")
                    mainScope.launch {
                        completed(true)
                    }
                },
                { error ->
                    Log.e(TAG, "Deletion failed", error)
                    mainScope.launch {
                        completed(false)
                    }
                }
            )
        }
    }
}