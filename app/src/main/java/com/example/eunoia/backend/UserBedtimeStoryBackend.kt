package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.models.*
import com.example.eunoia.ui.navigation.globalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserBedtimeStoryBackend {
    private const val TAG = "UserBedtimeStoryBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createUserBedtimeStory(
        userBedtimeStoryModel: UserBedtimeStoryObject.UserBedtimeStoryModel,
        completed: (userBedtimeStory: UserBedtimeStoryInfo) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userBedtimeStoryModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userBedtimeStoryModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userBedtimeStoryModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserBedtimeStoryObject(bedtimeStory: BedtimeStoryInfoData, completed: (userBedtimeStory: UserBedtimeStoryInfo) -> Unit){
        val userBedtimeStoryModel = UserBedtimeStoryObject.UserBedtimeStoryModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            BedtimeStoryObject.BedtimeStory.from(bedtimeStory),
        )
        createUserBedtimeStory(userBedtimeStoryModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryApprovedUserBedtimeStoryBasedOnUser(
        userData: UserData,
        completed: (userBedtimeStory: List<UserBedtimeStoryInfo?>) -> Unit
    ) {
        scope.launch {
            val userBedtimeStoryList = mutableListOf<UserBedtimeStoryInfo?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserBedtimeStoryInfo::class.java,
                    UserBedtimeStoryInfo.USER_DATA.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userBedtimeStoryData in response.data) {
                            //TODO change pending to approved
                            if(userBedtimeStoryData != null) {
                                if (userBedtimeStoryData.bedtimeStoryInfoData.approvalStatus == BedtimeStoryApprovalStatus.PENDING) {
                                    Log.i(TAG, userBedtimeStoryData.toString())
                                    userBedtimeStoryList.add(userBedtimeStoryData)
                                }
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userBedtimeStoryList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserBedtimeStoryBasedOnUserAndBedtimeStory(
        userData: UserData,
        bedtimeStoryInfo: BedtimeStoryInfoData,
        completed: (userBedtimeStory: List<UserBedtimeStoryInfo?>) -> Unit
    ) {
        scope.launch {
            val userBedtimeStoryList = mutableListOf<UserBedtimeStoryInfo?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserBedtimeStoryInfo::class.java,
                    UserBedtimeStoryInfo.USER_DATA.eq(userData.id)
                        .and(UserBedtimeStoryInfo.BEDTIME_STORY_INFO_DATA.eq(bedtimeStoryInfo.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userBedtimeStoryData in response.data) {
                            if(userBedtimeStoryData != null) {
                                Log.i(TAG, userBedtimeStoryData.toString())
                                userBedtimeStoryList.add(userBedtimeStoryData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userBedtimeStoryList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun deleteUserBedtimeStory(
        userBedtimeStory: UserBedtimeStoryInfo,
        completed: (successful: Boolean) -> Unit
    ){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.delete(userBedtimeStory),
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