package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.BedtimeStoryObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object BedtimeStoryBackend {
    private const val TAG = "BedtimeStoryBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createBedtimeStory(
        bedtimeStory: BedtimeStoryObject.BedtimeStory,
        completed: (bedtimeStoryData: BedtimeStoryInfoData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(bedtimeStory.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create bedtimeStory ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created bedtimeStory with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun queryBedtimeStoryBasedOnDisplayName(
        displayName: String,
        completed: (bedtimeStory: List<BedtimeStoryInfoData?>) -> Unit
    ) {
        scope.launch {
            val bedtimeStoryList = mutableListOf<BedtimeStoryInfoData?>()
            Amplify.API.query(
                ModelQuery.list(
                    BedtimeStoryInfoData::class.java,
                    BedtimeStoryInfoData.DISPLAY_NAME.eq(displayName),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (bedtimeStoryData in response.data) {
                            if(bedtimeStoryData != null) {
                                Log.i(TAG, bedtimeStoryData.toString())
                                bedtimeStoryList.add(bedtimeStoryData)
                            }
                        }
                    }
                    completed(bedtimeStoryList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryIncompleteBedtimeStoryBasedOnUser(
        user: UserData,
        completed: (bedtimeStory: List<BedtimeStoryInfoData?>) -> Unit
    ) {
        scope.launch {
            val bedtimeStoryList = mutableListOf<BedtimeStoryInfoData?>()
            Amplify.API.query(
                ModelQuery.list(
                    BedtimeStoryInfoData::class.java,
                    BedtimeStoryInfoData.BEDTIME_STORY_OWNER.eq(user.id)
                        .and(BedtimeStoryInfoData.CREATION_STATUS.eq(BedtimeStoryCreationStatus.INCOMPLETE))
                        .and(BedtimeStoryInfoData.APPROVAL_STATUS.eq(BedtimeStoryApprovalStatus.PENDING))
                        .and(BedtimeStoryInfoData.AUDIO_SOURCE.eq(BedtimeStoryAudioSource.RECORDED)),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (bedtimeStoryData in response.data) {
                            if(bedtimeStoryData != null) {
                                Log.i(TAG, bedtimeStoryData.toString())
                                bedtimeStoryList.add(bedtimeStoryData)
                            }
                        }
                    }
                    completed(bedtimeStoryList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryCompleteApprovedBedtimeStoryBasedOnUser(
        user: UserData,
        completed: (bedtimeStory: List<BedtimeStoryInfoData?>) -> Unit
    ) {
        scope.launch {
            val bedtimeStoryList = mutableListOf<BedtimeStoryInfoData?>()
            Amplify.API.query(
                ModelQuery.list(
                    BedtimeStoryInfoData::class.java,
                    BedtimeStoryInfoData.BEDTIME_STORY_OWNER.eq(user.id)
                        .and(BedtimeStoryInfoData.CREATION_STATUS.eq(BedtimeStoryCreationStatus.COMPLETED))
                        //.and(BedtimeStoryInfoData.APPROVAL_STATUS.eq(BedtimeStoryApprovalStatus.APPROVED))
                            //for now, allow pending
                        .and(BedtimeStoryInfoData.APPROVAL_STATUS.eq(BedtimeStoryApprovalStatus.PENDING)),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (bedtimeStoryData in response.data) {
                            if(bedtimeStoryData != null) {
                                Log.i(TAG, bedtimeStoryData.toString())
                                bedtimeStoryList.add(bedtimeStoryData)
                            }
                        }
                    }
                    completed(bedtimeStoryList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}