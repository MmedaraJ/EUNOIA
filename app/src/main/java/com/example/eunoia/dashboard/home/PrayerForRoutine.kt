package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.PrayerAudioSource
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.RoutinePrayer
import com.example.eunoia.backend.RoutineBackend
import com.example.eunoia.backend.RoutinePrayerBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.prayer.resetOtherGeneralMediaPlayerUsersExceptPrayer
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.prayer.activatePrayerGlobalControlButton
import com.example.eunoia.ui.bottomSheets.prayer.deActivatePrayerGlobalControlButton
import com.example.eunoia.ui.navigation.globalViewModel_

object PrayerForRoutine{
    private const val TAG = "PrayerForRoutine"

    fun playOrPausePrayerAccordingly(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(routineActivityPlayButtonTexts[index]!!.value == START_ROUTINE) {
            routineActivityPlayButtonTexts[index]!!.value = WAIT_FOR_ROUTINE

            if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playingOrder.contains("sound")){
                if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playSoundDuringPrayer){
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

            playPrayer(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }else if(
            routineActivityPlayButtonTexts[index]!!.value == PAUSE_ROUTINE ||
            routineActivityPlayButtonTexts[index]!!.value == WAIT_FOR_ROUTINE
        ){
            Log.i(TAG, "Pausing prayer and sound?")
            //pause prayer and sound
            pausePrayer(
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

    private fun pausePrayer(
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int
    ) {
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentBedtimeStoryPlaying == null &&
            globalViewModel_!!.currentSelfLovePlaying == null
        ) {
            if(generalMediaPlayerService.isMediaPlayerPlaying()) {
                generalMediaPlayerService.pauseMediaPlayer()
                globalViewModel_!!.prayerTimer.pause()
                globalViewModel_!!.isCurrentPrayerPlaying = false
                activatePrayerGlobalControlButton(2)
            }
        }
    }

    private fun playPrayer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(globalViewModel_!!.currentRoutinePlayingRoutinePrayers != null) {
            if(
                routineActivityPrayerUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                                [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                            .prayerData.id
                ] != "".toUri()
            ) {
                if(globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!.isEmpty()) {
                    getRoutinePrayers(index) {
                        retrievePrayerUris(
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            index,
                            context
                        )
                    }
                }else{
                    retrievePrayerUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                startPrayer(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            getRoutinePrayers(index) {
                retrievePrayerUris(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    private fun getRoutinePrayers(index: Int, completed: () -> Unit){
        getRoutinePrayersBasedOnRoutine(
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData
        ) { routinePrayers ->
            for( routinePrayer in routinePrayers){
                routineActivityPrayerUrisMapList[index][routinePrayer!!.prayerData.id] = "".toUri()
            }
            globalViewModel_!!.currentRoutinePlayingRoutinePrayers = routinePrayers
            completed()
        }
    }

    private fun getRoutinePrayersBasedOnRoutine(
        routineData: RoutineData,
        completed: (routinePrayerList: MutableList<RoutinePrayer?>) -> Unit
    ) {
        RoutinePrayerBackend.queryRoutinePrayerBasedOnRoutine(routineData) { routinePrayers ->
            completed(routinePrayers.toMutableList())
        }
    }

    private fun retrievePrayerUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                    [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                .prayerData.audioSource == PrayerAudioSource.UPLOADED
        ) {
            SoundBackend.retrieveAudio(
                globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                    .prayerData.audioKeyS3,
                globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                    .prayerData.prayerOwner.amplifyAuthUserId
            ) {
                routineActivityPrayerUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                                [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                            .prayerData.id
                ] = it

                startPrayer(
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

    private fun startPrayer(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            routineActivityPrayerUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                        .prayerData.id
            ] != "".toUri()
        ) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentSelfLovePlaying == null
            ){
                generalMediaPlayerService.startMediaPlayer()
            }else{
                initializePrayerMediaPlayers(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }

            routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
            generalMediaPlayerService.loopMediaPlayer()
            //globalViewModel_!!.previouslyPlayedUserSoundRelationship = globalViewModel_!!.currentUsersSoundRelationships!![index]
            //globalViewModel_!!.generalPlaytimeTimer.start()
            setGlobalPropertiesAfterPlayingPrayer(index)
        }
    }

    private fun initializePrayerMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ){
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(
            routineActivityPrayerUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                        .prayerData.id
            ]!!
        )
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        //generalMediaPlayerService.getMediaPlayer()!!.seekTo(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentPrayerContinuePlayingTime)
        globalViewModel_!!.prayerTimer.setMaxDuration(
            globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                    [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                .prayerData.fullPlayTime.toLong()
        )
        globalViewModel_!!.prayerTimer.setDuration(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentPrayerContinuePlayingTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptPrayer()

        startPrayerCDT(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }

    private fun startPrayerCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startPrayerCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.prayerPlayTime.toLong(),
            generalMediaPlayerService
        ){
            var continuePlayingTime = -1
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                continuePlayingTime = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                generalMediaPlayerService.onDestroy()
            }
            deActivatePrayerGlobalControlButton(0)
            activatePrayerGlobalControlButton(2)
            globalViewModel_!!.isCurrentPrayerPlaying = false
            globalViewModel_!!.currentPrayerPlaying = null
            globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = null

            val routine = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.copyOfBuilder()
                .currentPrayerContinuePlayingTime(continuePlayingTime)
                .build()

            //update routine with new prayer info
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

    private fun startPrayerCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer == null){
            globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Prayer routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingPrayer(index: Int){
        globalViewModel_!!.currentPrayerPlaying =
            globalViewModel_!!.currentRoutinePlayingRoutinePrayers!![
                    globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!
            ]!!.prayerData

        globalViewModel_!!.currentPrayerPlayingUri =
            routineActivityPrayerUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                        .prayerData.id
            ]

        globalViewModel_!!.isCurrentPrayerPlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true
        deActivatePrayerGlobalControlButton(0)
        deActivatePrayerGlobalControlButton(2)
    }
}