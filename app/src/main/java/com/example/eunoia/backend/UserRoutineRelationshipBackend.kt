package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import com.example.eunoia.ui.navigation.globalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserRoutineRelationshipBackend {
    private const val TAG = "UserRoutineRelationshipBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createUserRoutineRelationship(
        userRoutineRelationshipModel: UserRoutineRelationshipObject.UserRoutineRelationshipModel,
        completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userRoutineRelationshipModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userRoutineRelationshipModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userRoutineRelationshipModel with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserRoutineRelationshipObject(routine: RoutineData, completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit){
        val userRoutineRelationshipModel = UserRoutineRelationshipObject.UserRoutineRelationshipModel(
            id = UUID.randomUUID().toString(),
            userRoutineRelationshipOwner = UserObject.User.from(globalViewModel!!.currentUser!!),
            userRoutineRelationshipRoutine = RoutineObject.Routine.from(routine),
            numberOfTimesPlayed = 0,
            totalPlayTime = 0,
            fullPlayTime = routine.fullPlayTime.toLong(),
            numberOfSteps = routine.numberOfSteps,
            currentlyListening = false,
            playSoundDuringStretch = routine.playSoundDuringStretch,
            playSoundDuringPrayer = routine.playSoundDuringPrayer,
            playSoundDuringBreathing = routine.playSoundDuringBreathing,
            playSoundDuringSelfLove = routine.playSoundDuringSelfLove,
            playSoundDuringBedtimeStory = routine.playSoundDuringBedtimeStory,
            playSoundDuringSleep = routine.playSoundDuringSleep,
            eachSoundPlayTime = routine.eachSoundPlayTime,
            prayerPlayTime = routine.prayerPlayTime,
            bedtimeStoryPlayTime = routine.bedtimeStoryPlayTime,
            selfLovePlayTime = routine.selfLovePlayTime,
            stretchTime = routine.stretchTime,
            breathingTime = routine.breathingTime,
            currentBedtimeStoryPlayingIndex = routine.currentBedtimeStoryPlayingIndex,
            currentBedtimeStoryContinuePlayingTime = routine.currentBedtimeStoryContinuePlayingTime,
            currentSelfLovePlayingIndex = routine.currentSelfLovePlayingIndex,
            currentSelfLoveContinuePlayingTime = routine.currentSelfLoveContinuePlayingTime,
            currentPrayerPlayingIndex = routine.currentPrayerPlayingIndex,
            currentPrayerContinuePlayingTime = routine.currentPrayerContinuePlayingTime,
            usageTimeStamp = listOf(),
            usagePlayTimes = listOf(),
            playingOrder =  routine.playingOrder
        )
        createUserRoutineRelationship(userRoutineRelationshipModel){
            mainScope.launch {
                completed(it)
            }
        }
    }

    fun queryUserRoutineRelationshipBasedOnUser(
        userData: UserData,
        completed: (userRoutineRelationship: List<UserRoutineRelationship?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipList = mutableListOf<UserRoutineRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationship::class.java,
                    UserRoutineRelationship.USER_ROUTINE_RELATIONSHIP_OWNER.eq(userData.id),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipData in response.data) {
                            if(userRoutineRelationshipData != null) {
                                Log.i(TAG, userRoutineRelationshipData.toString())
                                userRoutineRelationshipList.add(userRoutineRelationshipData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryUserRoutineRelationshipBasedOnUserAndRoutine(
        userData: UserData,
        routineData: RoutineData,
        completed: (userRoutineRelationship: List<UserRoutineRelationship?>) -> Unit
    ) {
        scope.launch {
            val userRoutineRelationshipList = mutableListOf<UserRoutineRelationship?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserRoutineRelationship::class.java,
                    UserRoutineRelationship.USER_ROUTINE_RELATIONSHIP_OWNER.eq(userData.id)
                        .and(UserRoutineRelationship.USER_ROUTINE_RELATIONSHIP_ROUTINE.eq(routineData.id)),
                ),
                { response ->
                    if(response.hasData()) {
                        for (userRoutineRelationshipData in response.data) {
                            if(userRoutineRelationshipData != null) {
                                Log.i(TAG, userRoutineRelationshipData.toString())
                                userRoutineRelationshipList.add(userRoutineRelationshipData)
                            }
                        }
                    }
                    mainScope.launch {
                        completed(userRoutineRelationshipList)
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun updateUserRoutineRelationship(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit
    ){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(userRoutineRelationship),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Successfully updated userRoutineRelationship: ${response.data}")
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                {
                    Log.i(TAG, "Error while updating userRoutineRelationship: ", it)
                }
            )
        }
    }
}