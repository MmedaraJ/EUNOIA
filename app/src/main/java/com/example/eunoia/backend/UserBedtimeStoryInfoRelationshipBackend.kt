package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserBedtimeStoryInfoRelationshipObject
import com.example.eunoia.ui.navigation.globalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserBedtimeStoryInfoRelationshipBackend {
    private const val TAG = "UserBedtimeStoryInfoRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createUserBedtimeStoryInfoRelationship(
        userBedtimeStoryInfoRelationshipModel: UserBedtimeStoryInfoRelationshipObject.UserBedtimeStoryInfoRelationshipModel,
        completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userBedtimeStoryInfoRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userBedtimeStoryInfoRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userBedtimeStoryInfoRelationshipModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserBedtimeStoryInfoRelationshipObject(
        bedtimeStoryInfo: BedtimeStoryInfoData,
        completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
    ){
        val userBedtimeStoryInfoRelationshipModel = UserBedtimeStoryInfoRelationshipObject.UserBedtimeStoryInfoRelationshipModel(
            id = UUID.randomUUID().toString(),
            userBedtimeStoryInfoRelationshipOwner = UserObject.User.from(globalViewModel!!.currentUser!!),
            userBedtimeStoryInfoRelationshipBedtimeStoryInfo = BedtimeStoryObject.BedtimeStory.from(bedtimeStoryInfo),
            numberOfTimesPlayed = 0,
            totalPlayTime = 0,
            continuePlayingTime = 0,
            currentlyListening = false,
            usageTimeStamp = listOf(),
            usagePlayTimes = listOf()
        )
        createUserBedtimeStoryInfoRelationship(userBedtimeStoryInfoRelationshipModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun updateUserBedtimeStoryInfoRelationship(
        userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship,
        completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
    ){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(userBedtimeStoryInfoRelationship),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Successfully updated userBedtimeStoryInfoRelationship: ${response.data}")
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                {
                    Log.i(TAG, "Error while updating userBedtimeStoryInfoRelationship: ", it)
                }
            )
        }
    }

    fun queryApprovedUserBedtimeStoryInfoRelationshipBasedOnUser(
        userData: UserData,
        completed: (userBedtimeStoryInfoRelationship: List<UserBedtimeStoryInfoRelationship?>) -> Unit
    ) {
        scope.launch {
            val userBedtimeStoryInfoRelationshipList = mutableListOf<UserBedtimeStoryInfoRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserBedtimeStoryInfoRelationship::class.java,
                    UserBedtimeStoryInfoRelationship.USER_BEDTIME_STORY_INFO_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userBedtimeStoryInfoRelationshipData in response.data) {
                            //TODO change pending to approved
                            if(userBedtimeStoryInfoRelationshipData != null) {
                                if (
                                    userBedtimeStoryInfoRelationshipData.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.approvalStatus == BedtimeStoryApprovalStatus.PENDING &&
                                    userBedtimeStoryInfoRelationshipData.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.creationStatus == BedtimeStoryCreationStatus.COMPLETED
                                ) {
                                    Log.i(TAG, userBedtimeStoryInfoRelationshipData.toString())
                                    userBedtimeStoryInfoRelationshipList.add(userBedtimeStoryInfoRelationshipData)
                                }
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userBedtimeStoryInfoRelationshipList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryApprovedUserBedtimeStoryInfoRelationshipBasedOnId(
        id: String,
        completed: (userBedtimeStoryInfoRelationship: List<UserBedtimeStoryInfoRelationship?>) -> Unit
    ) {
        scope.launch {
            val userBedtimeStoryInfoRelationshipList = mutableListOf<UserBedtimeStoryInfoRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserBedtimeStoryInfoRelationship::class.java,
                    UserBedtimeStoryInfoRelationship.ID.eq(id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userBedtimeStoryInfoRelationshipData in response.data) {
                            //TODO change pending to approved
                            if(userBedtimeStoryInfoRelationshipData != null) {
                                if (
                                    userBedtimeStoryInfoRelationshipData.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.approvalStatus == BedtimeStoryApprovalStatus.PENDING &&
                                    userBedtimeStoryInfoRelationshipData.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.creationStatus == BedtimeStoryCreationStatus.COMPLETED
                                ) {
                                    Log.i(TAG, userBedtimeStoryInfoRelationshipData.toString())
                                    userBedtimeStoryInfoRelationshipList.add(userBedtimeStoryInfoRelationshipData)
                                }
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userBedtimeStoryInfoRelationshipList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserBedtimeStoryInfoRelationshipBasedOnUserAndBedtimeStoryInfo(
        userData: UserData,
        bedtimeStoryInfo: BedtimeStoryInfoData,
        completed: (userBedtimeStoryInfoRelationship: List<UserBedtimeStoryInfoRelationship?>) -> Unit
    ) {
        scope.launch {
            val userBedtimeStoryInfoRelationshipList = mutableListOf<UserBedtimeStoryInfoRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserBedtimeStoryInfoRelationship::class.java,
                    UserBedtimeStoryInfoRelationship.USER_BEDTIME_STORY_INFO_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserBedtimeStoryInfoRelationship.USER_BEDTIME_STORY_INFO_RELATIONSHIP_BEDTIME_STORY_INFO.eq(bedtimeStoryInfo.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userBedtimeStoryInfoRelationshipData in response.data) {
                            if(userBedtimeStoryInfoRelationshipData != null) {
                                Log.i(TAG, userBedtimeStoryInfoRelationshipData.toString())
                                userBedtimeStoryInfoRelationshipList.add(userBedtimeStoryInfoRelationshipData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userBedtimeStoryInfoRelationshipList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun deleteUserBedtimeStoryInfoRelationship(
        userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship,
        completed: (successful: Boolean) -> Unit
    ){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.delete(userBedtimeStoryInfoRelationship),
                { response ->
                    Log.i(TAG, "Deleted $response")
                    mainScope.launch {
                        completed(true)
                    }
                },
                { error ->
                    Log.e(TAG, "Deletion failed", error)
                    mainScope.launch {
                        completed(true)
                    }
                }
            )
        }
    }
}