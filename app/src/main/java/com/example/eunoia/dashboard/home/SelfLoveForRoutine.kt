package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.bedtimeStory.getCurrentlyPlayingTime
import com.example.eunoia.dashboard.selfLove.resetOtherGeneralMediaPlayerUsersExceptSelfLove
import com.example.eunoia.dashboard.selfLove.updateCurrentUserSelfLoveRelationshipUsageTimeStamp
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.selfLove.activateSelfLoveGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.navigation.*

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

            if(routineViewModel!!.currentUsersRoutineRelationships!![index]!!.playingOrder.contains("sound")){
                if(routineViewModel!!.currentUsersRoutineRelationships!![index]!!.playSoundDuringSelfLove){
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
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
            prayerViewModel!!.currentPrayerPlaying == null
        ) {
            if(generalMediaPlayerService.isMediaPlayerPlaying()) {
                generalMediaPlayerService.pauseMediaPlayer()
                selfLoveViewModel!!.selfLoveTimer.pause()
                globalViewModel!!.generalPlaytimeTimer.pause()
                selfLoveViewModel!!.isCurrentSelfLovePlaying = false
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
        if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves != null) {
            if(
                routineActivitySelfLoveUrisMapList[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                            .selfLoveData.id
                ] != "".toUri()
            ) {
                if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!.isEmpty()) {
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
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!
        ) { routineSelfLoves ->
            for( routineSelfLove in routineSelfLoves){
                routineActivitySelfLoveUrisMapList[index][routineSelfLove!!.selfLoveData.id] = "".toUri()
            }
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves = routineSelfLoves
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
        Log.i(
            TAG, "About to retrieve self lovesa uris for: ${routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
            .selfLoveData.displayName}")
            SoundBackend.retrieveAudio(
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                    .selfLoveData.audioKeyS3,
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                    .selfLoveData.selfLoveOwner.amplifyAuthUserId
            ) {
                routineActivitySelfLoveUrisMapList[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                            .selfLoveData.id
                ] = it!!

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
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                        .selfLoveData.id
            ] != "".toUri()
        ) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
                prayerViewModel!!.currentPrayerPlaying == null
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
        selfLoveViewModel!!.selfLoveTimer.start()
        globalViewModel!!.generalPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingSelfLove(index)
    }

    private fun updatePreviousAndCurrentSelfLoveRelationship(
        continuePlayingTime: Int,
        completed: () -> Unit
    ){
        updatePreviousUserSelfLoveRelationship(continuePlayingTime) {
            updateRecentlyPlayedUserSelfLoveRelationshipWithSelfLove(
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
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
        val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)
        updatePreviousAndCurrentSelfLoveRelationship(continuePlayingTime) {
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(
                routineActivitySelfLoveUrisMapList[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                            .selfLoveData.id
                ]!!
            )
            val intent = Intent()
            intent.action = "PLAY"
            generalMediaPlayerService.onStartCommand(intent, 0, 0)
            selfLoveViewModel!!.selfLoveTimer.setMaxDuration(
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                        [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                    .selfLoveData.fullPlayTime.toLong()
            )
            resetOtherGeneralMediaPlayerUsersExceptSelfLove()

            if (routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer == null) {
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
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!![
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!
            ]!!.selfLoveData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ){
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            selfLoveViewModel!!.isCurrentSelfLovePlaying = false
            selfLoveViewModel!!.currentSelfLovePlaying = null
            routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer = null

            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex =
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!! + 1
            if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!! > routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!.indices.last){
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex = 0
            }

            val routine = routineViewModel!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .currentSelfLovePlayingIndex(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex)
                .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine){
                routineViewModel!!.currentUsersRoutineRelationships!![index] = it
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
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!.selfLovePlayTime.toLong(),
            generalMediaPlayerService
        ){
            val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)
            selfLoveViewModel!!.isCurrentSelfLovePlaying = false
            selfLoveViewModel!!.currentSelfLovePlaying = null

            routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer = null
            routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer = null

            val routine = routineViewModel!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .currentSelfLoveContinuePlayingTime(continuePlayingTime)
                .build()

            //update routine with new SelfLove info
            updatePreviousUserSelfLoveRelationship(continuePlayingTime) {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    routineViewModel!!.currentUsersRoutineRelationships!![index] = it
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
        if(routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer == null){
            routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "SelfLove routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer!!.start()
    }

    private fun startNextSelfLoveCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer == null){
            routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Next SelfLove routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "next self love Timer stopped")
                }
            }
        }
        routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingSelfLove(index: Int){
        selfLoveViewModel!!.currentSelfLovePlaying =
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!![
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!
            ]!!.selfLoveData

        selfLoveViewModel!!.currentSelfLovePlayingUri =
            routineActivitySelfLoveUrisMapList[index][
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!]!!
                        .selfLoveData.id
            ]

        selfLoveViewModel!!.isCurrentSelfLovePlaying = true
        routineViewModel!!.isCurrentRoutinePlaying = true
        deActivateSelfLoveGlobalControlButton(0)
        deActivateSelfLoveGlobalControlButton(2)
    }

    fun updateRecentlyPlayedUserSelfLoveRelationshipWithSelfLove(
        selfLoveData: SelfLoveData,
        completed: (userSelfLoveRelationship: UserSelfLoveRelationship) -> Unit
    ){
        selfLoveViewModel!!.currentSelfLovePlaying = selfLoveData
        UserSelfLoveRelationshipBackend.queryUserSelfLoveRelationshipBasedOnUserAndSelfLove(
            globalViewModel!!.currentUser!!,
            selfLoveData
        ) { userSelfLoveRelationship ->
            if(userSelfLoveRelationship.isNotEmpty()) {
                updateCurrentUserSelfLoveRelationshipUsageTimeStamp(userSelfLoveRelationship[0]!!) {
                    selfLoveViewModel!!.previouslyPlayedUserSelfLoveRelationship = it
                    completed(it)
                }
            }else{
                UserSelfLoveRelationshipBackend.createUserSelfLoveRelationshipObject(
                    selfLoveData
                ){ newUserSelfLoveRelationship ->
                    updateCurrentUserSelfLoveRelationshipUsageTimeStamp(newUserSelfLoveRelationship) {
                        selfLoveViewModel!!.previouslyPlayedUserSelfLoveRelationship = it
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
            selfLoveViewModel!!.previouslyPlayedUserSelfLoveRelationship = it
            completed(it)
        }
    }
}