package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.selfLove.resetOtherGeneralMediaPlayerUsersExceptSelfLove
import com.example.eunoia.dashboard.selfLove.updateCurrentUserSelfLoveRelationshipUsageTimeStamp
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.selfLove.activateSelfLoveGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.navigation.globalViewModel_

object SelfLoveForRoutine {
    private const val TAG = "SelfLoveForRoutine"

    fun playOrPauseSelfLoveAccordingly(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(routineActivityPlayButtonTexts[index]!!.value == START_ROUTINE) {
            routineActivityPlayButtonTexts[index]!!.value = WAIT_FOR_ROUTINE

            if(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.playingOrder.contains("sound")){
                if(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.playSoundDuringSelfLove){
                    SoundForRoutine.playSound(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        index,
                        context
                    )
                }else{
                    SoundForRoutine.pauseSound(
                        soundMediaPlayerService,
                        index
                    )
                }
            }

            playSelfLove(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }else if(
            routineActivityPlayButtonTexts[index]!!.value == PAUSE_ROUTINE ||
            routineActivityPlayButtonTexts[index]!!.value == WAIT_FOR_ROUTINE
        ){
            Log.i(TAG, "Pausing SelfLove and sound?")
            //pause SelfLove and sound
            pauseSelfLove(
                generalMediaPlayerService,
                index,
            )

            SoundForRoutine.pauseSound(
                soundMediaPlayerService,
                index
            )
            routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
        }
    }

    private fun pauseSelfLove(
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int
    ) {
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentBedtimeStoryPlaying == null &&
            globalViewModel_!!.currentPrayerPlaying == null
        ) {
            if(generalMediaPlayerService.isMediaPlayerPlaying()) {
                generalMediaPlayerService.pauseMediaPlayer()
                globalViewModel_!!.selfLoveTimer.pause()
                globalViewModel_!!.generalPlaytimeTimer.pause()
                globalViewModel_!!.isCurrentSelfLovePlaying = false
                activateSelfLoveGlobalControlButton(2)
            }
        }
    }

    private fun playSelfLove(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves != null) {
            if(
                routineActivitySelfLoveUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                            .selfLoveData.id
                ] != "".toUri()
            ) {
                if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!.isEmpty()) {
                    getRoutineSelfLoves(index) {
                        retrieveSelfLoveUris(
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            index,
                            context
                        )
                    }
                }else{
                    retrieveSelfLoveUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                startSelfLove(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            getRoutineSelfLoves(index) {
                retrieveSelfLoveUris(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    private fun getRoutineSelfLoves(index: Int, completed: () -> Unit){
        getRoutineSelfLovesBasedOnRoutine(
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!
        ) { routineSelfLoves ->
            for( routineSelfLove in routineSelfLoves){
                routineActivitySelfLoveUrisMapList[index][routineSelfLove!!.selfLoveData.id] = "".toUri()
            }
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves = routineSelfLoves
            completed()
        }
    }

    private fun getRoutineSelfLovesBasedOnRoutine(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipSelfLoveList: MutableList<UserRoutineRelationshipSelfLove?>) -> Unit
    ) {
        UserRoutineRelationshipSelfLoveBackend.queryUserRoutineRelationshipSelfLoveBasedOnUserRoutineRelationship(userRoutineRelationship) { userRoutineRelationshipSelfLoves ->
            completed(userRoutineRelationshipSelfLoves.toMutableList())
        }
    }

    private fun retrieveSelfLoveUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        /*if(
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                    [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                .selfLoveData.audioSource == SelfLoveAudioSource.UPLOADED
        ) {*/
        Log.i(
            TAG, "About to retrieve self lovesa uris for: ${globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
            .selfLoveData.displayName}")
            SoundBackend.retrieveAudio(
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                    .selfLoveData.audioKeyS3,
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                    .selfLoveData.selfLoveOwner.amplifyAuthUserId
            ) {
                routineActivitySelfLoveUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                            .selfLoveData.id
                ] = it

                startSelfLove(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        /*}else{
            //if recorded
        }*/
    }

    private fun startSelfLove(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            routineActivitySelfLoveUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                        .selfLoveData.id
            ] != "".toUri()
        ) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingSelfLove(
                    index,
                    generalMediaPlayerService
                )
            }else{
                initializeSelfLoveMediaPlayers(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            retrieveSelfLoveUris(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun afterPlayingSelfLove(
        index: Int,
        generalMediaPlayerService: GeneralMediaPlayerService
    ){
        routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
        generalMediaPlayerService.loopMediaPlayer()
        globalViewModel_!!.selfLoveTimer.start()
        globalViewModel_!!.generalPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingSelfLove(index)
    }

    private fun updatePreviousAndCurrentSelfLoveRelationship(completed: () -> Unit){
        updatePreviousUserSelfLoveRelationship {
            updateRecentlyPlayedUserSelfLoveRelationshipWithSelfLove(
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                    .selfLoveData
            ) {
                completed()
            }
        }
    }

    private fun initializeSelfLoveMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ){
        updatePreviousAndCurrentSelfLoveRelationship {
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(
                routineActivitySelfLoveUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                            .selfLoveData.id
                ]!!
            )
            val intent = Intent()
            intent.action = "PLAY"
            generalMediaPlayerService.onStartCommand(intent, 0, 0)
            globalViewModel_!!.selfLoveTimer.setMaxDuration(
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                    .selfLoveData.fullPlayTime.toLong()
            )
            resetOtherGeneralMediaPlayerUsersExceptSelfLove()

            if (globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer == null) {
                startSelfLoveCDT(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }

        afterPlayingSelfLove(
            index,
            generalMediaPlayerService
        )
    }

    private fun startSelfLoveCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        generalSelfLoveTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )

        individualSelfLoveTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }

    private fun individualSelfLoveTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startNextSelfLoveCountDownTimer(
            context,
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!
            ]!!.selfLoveData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ){
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            globalViewModel_!!.isCurrentSelfLovePlaying = false
            globalViewModel_!!.currentSelfLovePlaying = null
            globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer = null

            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex =
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!! + 1
            if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!! > globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!.indices.last){
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex = 0
            }

            val routine = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .currentSelfLovePlayingIndex(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex)
                .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine){
                globalViewModel_!!.currentUsersRoutineRelationships!![index] = it
                routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
                playOrPauseSelfLoveAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    private fun generalSelfLoveTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startSelfLoveCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.selfLovePlayTime.toLong(),
            generalMediaPlayerService
        ){
            var continuePlayingTime = -1
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                continuePlayingTime = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                generalMediaPlayerService.onDestroy()
            }
            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)
            globalViewModel_!!.isCurrentSelfLovePlaying = false
            globalViewModel_!!.currentSelfLovePlaying = null

            globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer = null
            globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer = null

            val routine = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .currentSelfLoveContinuePlayingTime(continuePlayingTime)
                .build()

            //update routine with new SelfLove info
            updatePreviousUserBedtimeStoryRelationship {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    globalViewModel_!!.currentUsersRoutineRelationships!![index] = it
                    routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
                    incrementPlayingOrderIndex(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        index,
                        context
                    )
                }
            }
        }
    }

    private fun startSelfLoveCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer == null){
            globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "SelfLove routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer!!.start()
    }

    private fun startNextSelfLoveCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer == null){
            globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Next SelfLove routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "next self love Timer stopped")
                }
            }
        }
        globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingSelfLove(index: Int){
        globalViewModel_!!.currentSelfLovePlaying =
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!
            ]!!.selfLoveData

        globalViewModel_!!.currentSelfLovePlayingUri =
            routineActivitySelfLoveUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                        .selfLoveData.id
            ]

        globalViewModel_!!.isCurrentSelfLovePlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true
        deActivateSelfLoveGlobalControlButton(0)
        deActivateSelfLoveGlobalControlButton(2)
    }

    fun updateRecentlyPlayedUserSelfLoveRelationshipWithSelfLove(
        selfLoveData: SelfLoveData,
        completed: (userSelfLoveRelationship: UserSelfLoveRelationship) -> Unit
    ){
        globalViewModel_!!.currentSelfLovePlaying = selfLoveData
        UserSelfLoveRelationshipBackend.queryUserSelfLoveRelationshipBasedOnUserAndSelfLove(
            globalViewModel_!!.currentUser!!,
            selfLoveData
        ) { userSelfLoveRelationship ->
            if(userSelfLoveRelationship.isNotEmpty()) {
                updateCurrentUserSelfLoveRelationshipUsageTimeStamp(userSelfLoveRelationship[0]!!) {
                    globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship = it
                    completed(it)
                }
            }else{
                UserSelfLoveRelationshipBackend.createUserSelfLoveRelationshipObject(
                    selfLoveData
                ){ newUserSelfLoveRelationship ->
                    updateCurrentUserSelfLoveRelationshipUsageTimeStamp(newUserSelfLoveRelationship) {
                        globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship = it
                        completed(it)
                    }
                }
            }
        }
    }

    fun updateRecentlyPlayedUserSelfLoveRelationshipWithUserSelfLoveRelationship(
        userSelfLoveRelationship: UserSelfLoveRelationship,
        completed: (userSelfLoveRelationship: UserSelfLoveRelationship) -> Unit
    ){
        updateCurrentUserSelfLoveRelationshipUsageTimeStamp(userSelfLoveRelationship) {
            globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship = it
            completed(it)
        }
    }
}