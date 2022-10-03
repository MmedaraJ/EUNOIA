package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SelfLoveApprovalStatus
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserSelfLoveRelationship
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserSelfLoveRelationshipObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserSelfLoveRelationshipBackend {
    private const val TAG = "UserSelfLoveRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserSelfLoveRelationship(
        userSelfLoveRelationshipModel: UserSelfLoveRelationshipObject.UserSelfLoveRelationshipModel,
        completed: (userSelfLoveRelationship: UserSelfLoveRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userSelfLoveRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userSelfLoveRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userSelfLoveRelationshipModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserSelfLoveRelationshipObject(selfLove: SelfLoveData, completed: (userSelfLoveRelationship: UserSelfLoveRelationship) -> Unit){
        val userSelfLoveRelationshipModel = UserSelfLoveRelationshipObject.UserSelfLoveRelationshipModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            SelfLoveObject.SelfLove.from(selfLove),
            0,
            0
        )
        createUserSelfLoveRelationship(userSelfLoveRelationshipModel){
            completed(it)
        }
    }

    fun queryApprovedUserSelfLoveRelationshipBasedOnUser(
        userData: UserData,
        completed: (userSelfLoveRelationship: List<UserSelfLoveRelationship?>) -> Unit
    ) {
        scope.launch {
            val userSelfLoveRelationshipList = mutableListOf<UserSelfLoveRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSelfLoveRelationship::class.java,
                    UserSelfLoveRelationship.USER_SELF_LOVE_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSelfLoveRelationshipData in response.data) {
                            //TODO change pending to approved
                            if(userSelfLoveRelationshipData != null) {
                                if (userSelfLoveRelationshipData.userSelfLoveRelationshipSelfLove.approvalStatus == SelfLoveApprovalStatus.PENDING) {
                                    Log.i(TAG, userSelfLoveRelationshipData.toString())
                                    userSelfLoveRelationshipList.add(userSelfLoveRelationshipData)
                                }
                            }
                        }
                    }
                    completed(userSelfLoveRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserSelfLoveRelationshipBasedOnUserAndSelfLove(
        userData: UserData,
        selfLove: SelfLoveData,
        completed: (userSelfLoveRelationship: List<UserSelfLoveRelationship?>) -> Unit
    ) {
        scope.launch {
            val userSelfLoveRelationshipList = mutableListOf<UserSelfLoveRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSelfLoveRelationship::class.java,
                    UserSelfLoveRelationship.USER_SELF_LOVE_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserSelfLoveRelationship.USER_SELF_LOVE_RELATIONSHIP_SELF_LOVE.eq(selfLove.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSelfLoveRelationshipData in response.data) {
                            if(userSelfLoveRelationshipData != null) {
                                Log.i(TAG, userSelfLoveRelationshipData.toString())
                                userSelfLoveRelationshipList.add(userSelfLoveRelationshipData)
                            }
                        }
                    }
                    completed(userSelfLoveRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}