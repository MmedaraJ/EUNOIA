package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipBedtimeStoryInfo
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.UserRoutineRelationshipBedtimeStoryInfoObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserRoutineRelationshipBedtimeStoryInfoBackend {
    private const val TAG = "UserRoutineRelationshipBedtimeStoryInfoBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private fun createUserRoutineRelationshipBedtimeStoryInfo(
        userRoutineRelationshipBedtimeStoryInfoModel: UserRoutineRelationshipBedtimeStoryInfoObject.UserRoutineRelationshipBedtimeStoryInfoModel,
        completed: (userRoutineRelationshipBedtimeStoryInfo: UserRoutineRelationshipBedtimeStoryInfo) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineRelationshipBedtimeStoryInfoModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create UserRoutineRelationshipBedtimeStoryInfoModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created UserRoutineRelationshipBedtimeStoryInfoModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineRelationshipBedtimeStoryInfoObject(
        bedtimeStoryData: BedtimeStoryInfoData,
        userRoutineRelationship: UserRoutineRelationship,
        completed: (UserRoutineRelationshipBedtimeStoryInfo: UserRoutineRelationshipBedtimeStoryInfo) -> Unit
    ){
        val userRoutineRelationshipBedtimeStoryInfoModel = UserRoutineRelationshipBedtimeStoryInfoObject.UserRoutineRelationshipBedtimeStoryInfoModel(
            UUID.randomUUID().toString(),
            BedtimeStoryObject.BedtimeStory.from(bedtimeStoryData),
            UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationship),
        )
        createUserRoutineRelationshipBedtimeStoryInfo(userRoutineRelationshipBedtimeStoryInfoModel){
            completed(it)
        }
    }

    fun queryUserRoutineRelationshipBedtimeStoryInfoBasedOnUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipBedtimeStoryInfo: List<UserRoutineRelationshipBedtimeStoryInfo?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipBedtimeStoryInfoList = mutableListOf<UserRoutineRelationshipBedtimeStoryInfo?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipBedtimeStoryInfo::class.java,
                    UserRoutineRelationshipBedtimeStoryInfo.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipBedtimeStoryInfoData in response.data) {
                            if(userRoutineRelationshipBedtimeStoryInfoData != null) {
                                Log.i(TAG, userRoutineRelationshipBedtimeStoryInfoData.toString())
                                userRoutineRelationshipBedtimeStoryInfoList.add(userRoutineRelationshipBedtimeStoryInfoData)
                            }
                        }
                    }
                    completed(userRoutineRelationshipBedtimeStoryInfoList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipBedtimeStoryInfoBasedOnBedtimeStory(
        bedtimeStoryData: BedtimeStoryInfoData,
        completed: (userRoutineRelationshipBedtimeStoryInfo: List<UserRoutineRelationshipBedtimeStoryInfo?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipBedtimeStoryInfoList = mutableListOf<UserRoutineRelationshipBedtimeStoryInfo?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipBedtimeStoryInfo::class.java,
                    UserRoutineRelationshipBedtimeStoryInfo.BEDTIME_STORY_INFO_DATA.eq(bedtimeStoryData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipBedtimeStoryInfoData in response.data) {
                            if(userRoutineRelationshipBedtimeStoryInfoData != null) {
                                Log.i(TAG, userRoutineRelationshipBedtimeStoryInfoData.toString())
                                userRoutineRelationshipBedtimeStoryInfoList.add(userRoutineRelationshipBedtimeStoryInfoData)
                            }
                        }
                    }
                    completed(userRoutineRelationshipBedtimeStoryInfoList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipBedtimeStoryInfoBasedOnBedtimeStoryAndUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        bedtimeStoryData: BedtimeStoryInfoData,
        completed: (UserRoutineRelationshipBedtimeStoryInfo: List<UserRoutineRelationshipBedtimeStoryInfo?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipBedtimeStoryInfoList = mutableListOf<UserRoutineRelationshipBedtimeStoryInfo?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipBedtimeStoryInfo::class.java,
                    UserRoutineRelationshipBedtimeStoryInfo.BEDTIME_STORY_INFO_DATA.eq(bedtimeStoryData.id)
                        .and(UserRoutineRelationshipBedtimeStoryInfo.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipBedtimeStoryInfoData in response.data) {
                            if(userRoutineRelationshipBedtimeStoryInfoData != null) {
                                Log.i(TAG, userRoutineRelationshipBedtimeStoryInfoData.toString())
                                userRoutineRelationshipBedtimeStoryInfoList.add(userRoutineRelationshipBedtimeStoryInfoData)
                            }
                        }
                    }
                    completed(userRoutineRelationshipBedtimeStoryInfoList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}