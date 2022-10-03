package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserSelfLoveObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserSelfLoveBackend {
    private const val TAG = "UserSelfLoveBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserSelfLove(
        userSelfLoveModel: UserSelfLoveObject.UserSelfLoveModel,
        completed: (userSelfLove: UserSelfLove) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userSelfLoveModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userSelfLoveModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userSelfLoveModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserSelfLoveObject(SelfLove: SelfLoveData, completed: (userSelfLove: UserSelfLove) -> Unit){
        val userSelfLoveModel = UserSelfLoveObject.UserSelfLoveModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            SelfLoveObject.SelfLove.from(SelfLove),
        )
        createUserSelfLove(userSelfLoveModel){
            completed(it)
        }
    }

    fun queryApprovedUserSelfLoveBasedOnUser(
        userData: UserData,
        completed: (userSelfLove: List<UserSelfLove?>) -> Unit
    ) {
        scope.launch {
            val userSelfLoveList = mutableListOf<UserSelfLove?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSelfLove::class.java,
                    UserSelfLove.USER_DATA.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSelfLoveData in response.data) {
                            //TODO change pending to approved
                            if(userSelfLoveData != null) {
                                if(userSelfLoveData.selfLoveData.approvalStatus == SelfLoveApprovalStatus.PENDING) {
                                    Log.i(TAG, userSelfLoveData.toString())
                                    userSelfLoveList.add(userSelfLoveData)
                                }
                            }
                        }
                    }
                    completed(userSelfLoveList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserSelfLoveBasedOnUserAndSelfLove(
        userData: UserData,
        selfLoveData: SelfLoveData,
        completed: (userSelfLove: List<UserSelfLove?>) -> Unit
    ) {
        scope.launch {
            val userSelfLoveList = mutableListOf<UserSelfLove?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserSelfLove::class.java,
                    UserSelfLove.USER_DATA.eq(userData.id)
                        .and(UserSelfLove.SELF_LOVE_DATA.eq(selfLoveData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userSelfLoveData in response.data) {
                            if(userSelfLoveData != null) {
                                Log.i(TAG, userSelfLoveData.toString())
                                userSelfLoveList.add(userSelfLoveData)
                            }
                        }
                    }
                    completed(userSelfLoveList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}