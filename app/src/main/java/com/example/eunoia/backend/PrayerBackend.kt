package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.PrayerObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object PrayerBackend {
    private const val TAG = "PrayerBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createPrayer(
        prayer: PrayerObject.Prayer,
        completed: (prayerData: PrayerData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(prayer.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create prayer ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created prayer with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun queryPrayerBasedOnDisplayName(
        displayName: String,
        completed: (prayer: List<PrayerData?>) -> Unit
    ) {
        scope.launch {
            val prayerList = mutableListOf<PrayerData?>()
            Amplify.API.query(
                ModelQuery.list(
                    PrayerData::class.java,
                    PrayerData.DISPLAY_NAME.eq(displayName),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (prayerData in response.data) {
                            if(prayerData != null) {
                                Log.i(TAG, prayerData.toString())
                                prayerList.add(prayerData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(prayerList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryIncompletePrayerBasedOnUser(
        user: UserData,
        completed: (prayers: List<PrayerData?>) -> Unit
    ) {
        scope.launch {
            val prayerList = mutableListOf<PrayerData?>()
            Amplify.API.query(
                ModelQuery.list(
                    PrayerData::class.java,
                    PrayerData.PRAYER_OWNER.eq(user.id)
                        .and(PrayerData.CREATION_STATUS.eq(PrayerCreationStatus.INCOMPLETE))
                        .and(PrayerData.APPROVAL_STATUS.eq(PrayerApprovalStatus.PENDING))
                        .and(PrayerData.AUDIO_SOURCE.eq(PrayerAudioSource.RECORDED)),
                ),
                { response ->
                    Log.i(TAG, "Response: $response")
                    if(response.hasData()) {
                        for (prayerData in response.data) {
                            if(prayerData != null) {
                                Log.i(TAG, prayerData.toString())
                                prayerList.add(prayerData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(prayerList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun updatePrayer(
        prayerData: PrayerData,
        completed: (prayerData: PrayerData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(prayerData),
                { response ->
                    Log.i(TAG, "Updated $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from update PrayerData ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Updated PrayerData with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Update failed", error) }
            )
        }
    }

    fun deletePrayer(
        prayerData: PrayerData,
        completed: (successful: Boolean) -> Unit
    ){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.delete(prayerData),
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