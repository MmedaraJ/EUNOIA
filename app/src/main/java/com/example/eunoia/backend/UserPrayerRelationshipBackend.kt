package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.PrayerApprovalStatus
import com.amplifyframework.datastore.generated.model.PrayerData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserPrayerRelationship
import com.example.eunoia.models.PrayerObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserPrayerRelationshipObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserPrayerRelationshipBackend {
    private const val TAG = "UserPrayerRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserPrayerRelationship(
        userPrayerRelationshipModel: UserPrayerRelationshipObject.UserPrayerRelationshipModel,
        completed: (userPrayerRelationship: UserPrayerRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userPrayerRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userPrayerRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userPrayerRelationshipModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserPrayerRelationshipObject(prayer: PrayerData, completed: (userPrayerRelationship: UserPrayerRelationship) -> Unit){
        val userPrayerRelationshipModel = UserPrayerRelationshipObject.UserPrayerRelationshipModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            PrayerObject.Prayer.from(prayer),
            0,
            0
        )
        createUserPrayerRelationship(userPrayerRelationshipModel){
            completed(it)
        }
    }

    fun queryApprovedUserPrayerRelationshipBasedOnUser(
        userData: UserData,
        completed: (userPrayerRelationship: List<UserPrayerRelationship?>) -> Unit
    ) {
        scope.launch {
            val userPrayerRelationshipList = mutableListOf<UserPrayerRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPrayerRelationship::class.java,
                    UserPrayerRelationship.USER_PRAYER_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPrayerRelationshipData in response.data) {
                            //TODO change pending to approved
                            if(userPrayerRelationshipData != null) {
                                if (userPrayerRelationshipData.userPrayerRelationshipPrayer.approvalStatus == PrayerApprovalStatus.PENDING) {
                                    Log.i(TAG, userPrayerRelationshipData.toString())
                                    userPrayerRelationshipList.add(userPrayerRelationshipData)
                                }
                            }
                        }
                    }
                    completed(userPrayerRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserPrayerRelationshipBasedOnUserAndPrayer(
        userData: UserData,
        prayer: PrayerData,
        completed: (userPrayerRelationship: List<UserPrayerRelationship?>) -> Unit
    ) {
        scope.launch {
            val userPrayerRelationshipList = mutableListOf<UserPrayerRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPrayerRelationship::class.java,
                    UserPrayerRelationship.USER_PRAYER_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserPrayerRelationship.USER_PRAYER_RELATIONSHIP_PRAYER.eq(prayer.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPrayerRelationshipData in response.data) {
                            if(userPrayerRelationshipData != null) {
                                Log.i(TAG, userPrayerRelationshipData.toString())
                                userPrayerRelationshipList.add(userPrayerRelationshipData)
                            }
                        }
                    }
                    completed(userPrayerRelationshipList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}