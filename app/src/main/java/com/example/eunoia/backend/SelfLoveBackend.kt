package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.example.eunoia.models.SelfLoveObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object SelfLoveBackend {
    private const val TAG = "SelfLoveBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

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
                        completed(response.data)
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
                            Log.i(TAG, selfLoveData.toString())
                            selfLoveList.add(selfLoveData)
                        }
                    }
                    completed(selfLoveList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}