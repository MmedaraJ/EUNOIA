package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.BedtimeStoryApprovalStatus
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfoRelationship
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserBedtimeStoryInfoRelationshipObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserBedtimeStoryInfoRelationshipBackend {
    private const val TAG = "UserBedtimeStoryInfoRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

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
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserBedtimeStoryInfoRelationshipObject(bedtimeStoryInfo: BedtimeStoryInfoData, completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit){
        val userBedtimeStoryInfoRelationshipModel = UserBedtimeStoryInfoRelationshipObject.UserBedtimeStoryInfoRelationshipModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            BedtimeStoryObject.BedtimeStory.from(bedtimeStoryInfo),
            0,
            0
        )
        createUserBedtimeStoryInfoRelationship(userBedtimeStoryInfoRelationshipModel){
            completed(it)
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
                                if (userBedtimeStoryInfoRelationshipData.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.approvalStatus == BedtimeStoryApprovalStatus.PENDING) {
                                    Log.i(TAG, userBedtimeStoryInfoRelationshipData.toString())
                                    userBedtimeStoryInfoRelationshipList.add(userBedtimeStoryInfoRelationshipData)
                                }
                            }
                        }
                    }
                    completed(userBedtimeStoryInfoRelationshipList)
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
                    completed(userBedtimeStoryInfoRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}