package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.PrayerData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipPrayer
import com.example.eunoia.models.PrayerObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import com.example.eunoia.models.UserRoutineRelationshipPrayerObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserRoutineRelationshipPrayerBackend {
    private const val TAG = "UserRoutineRelationshipPrayerBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    private fun createUserRoutineRelationshipPrayer(
        userRoutineRelationshipPrayerModel: UserRoutineRelationshipPrayerObject.UserRoutineRelationshipPrayerModel,
        completed: (userRoutineRelationshipPrayer: UserRoutineRelationshipPrayer) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineRelationshipPrayerModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create UserRoutineRelationshipPrayerModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created UserRoutineRelationshipPrayerModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineRelationshipPrayerObject(
        prayerData: PrayerData,
        userRoutineRelationship: UserRoutineRelationship,
        completed: (UserRoutineRelationshipPrayer: UserRoutineRelationshipPrayer) -> Unit
    ){
        val userRoutineRelationshipPrayerModel = UserRoutineRelationshipPrayerObject.UserRoutineRelationshipPrayerModel(
            UUID.randomUUID().toString(),
            UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationship),
            PrayerObject.Prayer.from(prayerData),
        )
        createUserRoutineRelationshipPrayer(userRoutineRelationshipPrayerModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserRoutineRelationshipPrayerBasedOnUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (UserRoutineRelationshipPrayer: List<UserRoutineRelationshipPrayer?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipPrayerList = mutableListOf<UserRoutineRelationshipPrayer?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipPrayer::class.java,
                    UserRoutineRelationshipPrayer.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipPrayerData in response.data) {
                            if(userRoutineRelationshipPrayerData != null) {
                                Log.i(TAG, userRoutineRelationshipPrayerData.toString())
                                userRoutineRelationshipPrayerList.add(userRoutineRelationshipPrayerData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipPrayerList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipPrayerBasedOnPrayerAndUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        prayerData: PrayerData,
        completed: (UserRoutineRelationshipPrayer: List<UserRoutineRelationshipPrayer?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipPrayerList = mutableListOf<UserRoutineRelationshipPrayer?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationshipPrayer::class.java,
                    UserRoutineRelationshipPrayer.PRAYER_DATA.eq(prayerData.id)
                        .and(UserRoutineRelationshipPrayer.USER_ROUTINE_RELATIONSHIP.eq(userRoutineRelationship.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipPrayerData in response.data) {
                            if(userRoutineRelationshipPrayerData != null) {
                                Log.i(TAG, userRoutineRelationshipPrayerData.toString())
                                userRoutineRelationshipPrayerList.add(userRoutineRelationshipPrayerData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipPrayerList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}