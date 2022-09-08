package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.models.*
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserBedtimeStoryBackend {
    private const val TAG = "UserBedtimeStoryBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

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
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineObject(bedtimeStory: BedtimeStoryInfoData, completed: (userBedtimeStory: UserBedtimeStoryInfo) -> Unit){
        val userBedtimeStoryModel = UserBedtimeStoryObject.UserBedtimeStoryModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            BedtimeStoryObject.BedtimeStory.from(bedtimeStory),
        )
        createUserBedtimeStory(userBedtimeStoryModel){
            completed(it)
        }
    }

    fun queryUserBedtimeStoryBasedOnUser(
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
                    Log.i(TAG, "2233 $response")
                    if(response.hasData()) {
                        for (userBedtimeStoryData in response.data) {
                            Log.i(TAG, userBedtimeStoryData.toString())
                            userBedtimeStoryList.add(userBedtimeStoryData)
                        }
                    }
                    completed(userBedtimeStoryList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}