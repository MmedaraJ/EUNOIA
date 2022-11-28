package com.example.eunoia.dashboard.routine.userRoutineRelationshipScreen

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSelfLove
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserRoutineRelationshipBackend
import com.example.eunoia.backend.UserRoutineRelationshipSelfLoveBackend
import com.example.eunoia.dashboard.home.SelfLoveForRoutine
import com.example.eunoia.dashboard.selfLove.resetOtherGeneralMediaPlayerUsersExceptSelfLove
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.selfLove.activateSelfLoveGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.utils.getCurrentlyPlayingTime

object SelfLoveForUserRoutineRelationship {
    private const val TAG = "SelfLoveForUserRoutineRelationship"

    fun playOrPauseSelfLoveAccordingly(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        Log.i(TAG, "play button 1 selfLove is $playButtonText")
        if(playButtonText == START_ROUTINE) {
            playButtonText = WAIT_FOR_ROUTINE

            if(thisUserRoutineRelationship!!.playingOrder.contains("sound")){
                if(thisUserRoutineRelationship!!.playSoundDuringSelfLove){
                    SoundForUserRoutineRelationship.playSound(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context
                    )
                }else{
                    SoundForUserRoutineRelationship.pauseSound(
                        soundMediaPlayerService,
                    )
                }
            }

            playSelfLove(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context
            )

        }else if(
            playButtonText == PAUSE_ROUTINE ||
            playButtonText == WAIT_FOR_ROUTINE
        ){
            Log.i(TAG, "Pausing selfLove and sound?")
            pauseSelfLove(generalMediaPlayerService)

            SoundForUserRoutineRelationship.pauseSound(soundMediaPlayerService)
            playButtonText = START_ROUTINE
        }
    }

    private fun pauseSelfLove(
        generalMediaPlayerService: GeneralMediaPlayerService,
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
        context: Context
    ) {
        if(selfLoves!!.isNotEmpty()) {
            if(selfLoveUri[selfLoves!![selfLovesIndex]!!.selfLoveData.id] != "".toUri()) {
                Log.i(TAG, "that selfLove uriz is null")
                if(selfLoves!!.isEmpty()) {
                    getRoutineSelfLoves{
                        retrieveSelfLoveUris(
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            context
                        )
                    }
                }else{
                    Log.i(TAG, "needs presets for null selfLove")
                    retrieveSelfLoveUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        context
                    )
                }
            }else{
                Log.i(TAG, "that selfLove uriz is noooooot null")
                startSelfLove(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }else{
            getRoutineSelfLoves{
                retrieveSelfLoveUris(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }
    }

    private fun getRoutineSelfLoves(completed: () -> Unit){
        getRoutineSelfLovesBasedOnRoutine(
            thisUserRoutineRelationship!!
        ) { userRoutineRelationshipSelfLoves ->
            for( userRoutineRelationshipSelfLove in userRoutineRelationshipSelfLoves){
                selfLoveUri[userRoutineRelationshipSelfLove!!.selfLoveData.id] = "".toUri()
            }
            selfLoves = userRoutineRelationshipSelfLoves
            completed()
        }
    }

    private fun getRoutineSelfLovesBasedOnRoutine(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipSelfLovesSelfLoveList: MutableList<UserRoutineRelationshipSelfLove?>) -> Unit
    ) {
        UserRoutineRelationshipSelfLoveBackend.queryUserRoutineRelationshipSelfLoveBasedOnUserRoutineRelationship(userRoutineRelationship) { userRoutineRelationshipSelfLoves ->
            completed(userRoutineRelationshipSelfLoves.toMutableList())
        }
    }

    private fun retrieveSelfLoveUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ) {
        Log.i(TAG, "About to retrieve selfLovea uris for: ${selfLoves!![selfLovesIndex]!!.selfLoveData.displayName}")
        SoundBackend.retrieveAudio(
            selfLoves!![selfLovesIndex]!!.selfLoveData.audioKeyS3,
            selfLoves!![selfLovesIndex]!!.selfLoveData.selfLoveOwner.amplifyAuthUserId
        ) {
            selfLoveUri[selfLoves!![selfLovesIndex]!!.selfLoveData.id] = it
            startSelfLove(
                generalMediaPlayerService,
                soundMediaPlayerService,
                context
            )
        }
        /*}else{
            //if recorded
        }*/
    }

    private fun afterPlayingSelfLove(generalMediaPlayerService: GeneralMediaPlayerService, ){
        playButtonText = PAUSE_ROUTINE
        generalMediaPlayerService.loopMediaPlayer()
        selfLoveViewModel!!.selfLoveTimer.start()
        globalViewModel!!.generalPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingSelfLove()
    }

    private fun startSelfLove(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ) {
        if(selfLoveUri[selfLoves!![selfLovesIndex]!!.selfLoveData.id] != "".toUri()) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
                prayerViewModel!!.currentPrayerPlaying == null
            ){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingSelfLove(generalMediaPlayerService)
            }else{
                initializeSelfLoveMediaPlayers(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }else{
            retrieveSelfLoveUris(
                generalMediaPlayerService,
                soundMediaPlayerService,
                context
            )
        }
    }

    private fun updatePreviousAndCurrentSelfLoveRelationship(
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
            SelfLoveForRoutine.updateRecentlyPlayedUserSelfLoveRelationshipWithSelfLove(
                selfLoves!![selfLovesIndex]!!.selfLoveData
            ) {
                completed()
            }
        }
    }

    private fun initializeSelfLoveMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ){
        updatePreviousAndCurrentSelfLoveRelationship(generalMediaPlayerService) {
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(
                selfLoveUri[selfLoves!![selfLovesIndex]!!.selfLoveData.id]!!

            )
            val intent = Intent()
            intent.action = "PLAY"
            generalMediaPlayerService.onStartCommand(intent, 0, 0)
            Log.i(TAG, "selfLove index = $selfLovesIndex")

            selfLoveViewModel!!.selfLoveTimer.setMaxDuration(
                selfLoves!![selfLovesIndex]!!.selfLoveData.fullPlayTime.toLong()
            )
            resetOtherGeneralMediaPlayerUsersExceptSelfLove()

            if (selfLoveCountDownTimer == null) {
                startSelfLoveCDT(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }

        afterPlayingSelfLove(generalMediaPlayerService)
    }

    private fun startSelfLoveCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        generalSelfLoveTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )

        individualSelfLoveTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }

    private fun individualSelfLoveTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        startNextSelfLoveCountDownTimer(
            context,
            selfLoves!![selfLovesIndex]!!.selfLoveData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            selfLoveViewModel!!.isCurrentSelfLovePlaying = false
            selfLoveViewModel!!.currentSelfLovePlaying = null

            selfLoveCountDownTimer = null
            routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer = selfLoveCountDownTimer

            selfLovesIndex += 1
            if (selfLovesIndex > selfLoves!!.indices.last) {
                selfLovesIndex = 0
            }
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex = selfLovesIndex

            val routine =
                thisUserRoutineRelationship!!.copyOfBuilder()
                    .currentSelfLovePlayingIndex(selfLovesIndex)
                    .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                thisUserRoutineRelationship = it
                routineViewModel!!.currentUserRoutineRelationshipPlaying = it
                playButtonText = START_ROUTINE
                playOrPauseSelfLoveAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }
    }

    private fun generalSelfLoveTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        startSelfLoveCountDownTimer(
            context,
            thisUserRoutineRelationship!!.selfLovePlayTime.toLong(),
            generalMediaPlayerService
        ){
            val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            selfLoveViewModel!!.isCurrentSelfLovePlaying = false
            selfLoveViewModel!!.currentSelfLovePlaying = null
            selfLoveCountDownTimer = null
            routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer = selfLoveCountDownTimer

            nextSelfLoveCountDownTimer = null
            routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer = nextSelfLoveCountDownTimer

            val routine = thisUserRoutineRelationship!!.copyOfBuilder()
                .currentSelfLoveContinuePlayingTime(continuePlayingTime)
                .build()

            updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    thisUserRoutineRelationship = it
                    routineViewModel!!.currentUserRoutineRelationshipPlaying = it
                    playButtonText = START_ROUTINE

                    incrementPlayingOrderIndex(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
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
        if(selfLoveCountDownTimer == null){
            selfLoveCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "SelfLove routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        selfLoveCountDownTimer!!.start()
        routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer = selfLoveCountDownTimer
    }

    private fun startNextSelfLoveCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(nextSelfLoveCountDownTimer == null){
            nextSelfLoveCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "next SelfLove routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        nextSelfLoveCountDownTimer!!.start()
        routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer = nextSelfLoveCountDownTimer
    }

    private fun setGlobalPropertiesAfterPlayingSelfLove(){
        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves = selfLoves
        selfLoveViewModel!!.currentSelfLovePlaying = selfLoves!![selfLovesIndex]!!.selfLoveData
        selfLoveViewModel!!.currentSelfLovePlayingUri = selfLoveUri[selfLoves!![selfLovesIndex]!!.selfLoveData.id]
        selfLoveViewModel!!.isCurrentSelfLovePlaying = true
        routineViewModel!!.isCurrentRoutinePlaying = true

        deActivateSelfLoveGlobalControlButton(0)
        deActivateSelfLoveGlobalControlButton(2)
    }
}