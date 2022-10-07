package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.SelfLoveAudioSource
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.RoutineSelfLove
import com.example.eunoia.backend.RoutineBackend
import com.example.eunoia.backend.RoutineSelfLoveBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.selfLove.resetOtherGeneralMediaPlayerUsersExceptSelfLove
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

            if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playingOrder.contains("sound")){
                if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playSoundDuringSelfLove){
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
        if(globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves != null) {
            if(
                routineActivitySelfLoveUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                                [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                            .selfLoveData.id
                ] != "".toUri()
            ) {
                if(globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!.isEmpty()) {
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
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData
        ) { routineSelfLoves ->
            for( routineSelfLove in routineSelfLoves){
                routineActivitySelfLoveUrisMapList[index][routineSelfLove!!.selfLoveData.id] = "".toUri()
            }
            globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves = routineSelfLoves
            completed()
        }
    }

    private fun getRoutineSelfLovesBasedOnRoutine(
        routineData: RoutineData,
        completed: (routineSelfLoveList: MutableList<RoutineSelfLove?>) -> Unit
    ) {
        RoutineSelfLoveBackend.queryRoutineSelfLoveBasedOnRoutine(routineData) { routineSelfLoves ->
            completed(routineSelfLoves.toMutableList())
        }
    }

    private fun retrieveSelfLoveUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                    [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                .selfLoveData.audioSource == SelfLoveAudioSource.UPLOADED
        ) {
            SoundBackend.retrieveAudio(
                globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                        [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                    .selfLoveData.audioKeyS3,
                globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                        [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                    .selfLoveData.selfLoveOwner.amplifyAuthUserId
            ) {
                routineActivitySelfLoveUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                                [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                            .selfLoveData.id
                ] = it

                startSelfLove(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            //if recorded
        }
    }

    private fun startSelfLove(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            routineActivitySelfLoveUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                            [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                        .selfLoveData.id
            ] != "".toUri()
        ) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ){
                generalMediaPlayerService.startMediaPlayer()
            }else{
                initializeSelfLoveMediaPlayers(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }

            routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
            Log.i(TAG, "Itzz was --pause-- right here")
            generalMediaPlayerService.loopMediaPlayer()
            //globalViewModel_!!.previouslyPlayedUserSoundRelationship = globalViewModel_!!.currentUsersSoundRelationships!![index]
            //globalViewModel_!!.generalPlaytimeTimer.start()
            setGlobalPropertiesAfterPlayingSelfLove(index)
        }
    }

    private fun initializeSelfLoveMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ){
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(
            routineActivitySelfLoveUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                            [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                        .selfLoveData.id
            ]!!
        )
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        //generalMediaPlayerService.getMediaPlayer()!!.seekTo(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentSelfLoveContinuePlayingTime)
        globalViewModel_!!.selfLoveTimer.setMaxDuration(
            globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                    [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                .selfLoveData.fullPlayTime.toLong()
        )
        globalViewModel_!!.selfLoveTimer.setDuration(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentSelfLoveContinuePlayingTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptSelfLove()

        startSelfLoveCDT(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }

    private fun startSelfLoveCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startSelfLoveCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.selfLovePlayTime.toLong(),
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

            val routine = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.copyOfBuilder()
                .currentSelfLoveContinuePlayingTime(continuePlayingTime)
                .build()

            //update routine with new SelfLove info
            RoutineBackend.updateRoutine(routine){

            }

            routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE

            incrementPlayingOrderIndex(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
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

    private fun setGlobalPropertiesAfterPlayingSelfLove(index: Int){
        globalViewModel_!!.currentSelfLovePlaying =
            globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!![
                    globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!
            ]!!.selfLoveData

        globalViewModel_!!.currentSelfLovePlayingUri =
            routineActivitySelfLoveUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutineSelfLoves!!
                            [globalViewModel_!!.currentRoutinePlayingRoutineSelfLovesIndex!!]!!
                        .selfLoveData.id
            ]

        globalViewModel_!!.isCurrentSelfLovePlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true
        deActivateSelfLoveGlobalControlButton(0)
        deActivateSelfLoveGlobalControlButton(2)
    }
}