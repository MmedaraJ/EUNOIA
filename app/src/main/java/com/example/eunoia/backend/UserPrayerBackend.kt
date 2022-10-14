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

object UserPrayerBackend {
    private const val TAG = "UserPrayerBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createUserPrayer(
        userPrayerModel: UserPrayerObject.UserPrayerModel,
        completed: (userPrayer: UserPrayer) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userPrayerModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userPrayerModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userPrayerModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserPrayerObject(prayer: PrayerData, completed: (userPrayer: UserPrayer) -> Unit){
        val userPrayerModel = UserPrayerObject.UserPrayerModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            PrayerObject.Prayer.from(prayer),
        )
        createUserPrayer(userPrayerModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserPrayerBasedOnUser(
        userData: UserData,
        completed: (userPrayer: List<UserPrayer?>) -> Unit
    ) {
        scope.launch {
            val userPrayerList = mutableListOf<UserPrayer?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPrayer::class.java,
                    UserPrayer.USER_DATA.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPrayerData in response.data) {
                            if(userPrayerData != null) {
                                Log.i(TAG, userPrayerData.toString())
                                userPrayerList.add(userPrayerData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userPrayerList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryApprovedUserPrayerBasedOnUser(
        userData: UserData,
        completed: (userPrayer: List<UserPrayer?>) -> Unit
    ) {
        scope.launch {
            val userPrayerList = mutableListOf<UserPrayer?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPrayer::class.java,
                    UserPrayer.USER_DATA.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPrayerData in response.data) {
                            //TODO change pending to approved
                            if(userPrayerData != null) {
                                if(userPrayerData.prayerData.approvalStatus == PrayerApprovalStatus.PENDING) {
                                    Log.i(TAG, userPrayerData.toString())
                                    userPrayerList.add(userPrayerData)
                                }
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userPrayerList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserPrayerBasedOnUserAndPrayer(
        userData: UserData,
        prayerData: PrayerData,
        completed: (userPrayer: List<UserPrayer?>) -> Unit
    ) {
        scope.launch {
            val userPrayerList = mutableListOf<UserPrayer?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPrayer::class.java,
                    UserPrayer.USER_DATA.eq(userData.id)
                        .and(UserPrayer.PRAYER_DATA.eq(prayerData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPrayerData in response.data) {
                            if(userPrayerData != null) {
                                Log.i(TAG, userPrayerData.toString())
                                userPrayerList.add(userPrayerData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userPrayerList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}