package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.bedtimeStory.resetOtherGeneralMediaPlayerUsersExceptBedtimeStory
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.bedtimeStory.activateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.bedtimeStory.deActivateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.navigation.globalViewModel_

object BedtimeStoryForRoutine{
    private const val TAG = "BedtimeStoryForRoutine"

    fun playOrPauseBedtimeStoryAccordingly(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(routineActivityPlayButtonTexts[index]!!.value == START_ROUTINE) {
            routineActivityPlayButtonTexts[index]!!.value = WAIT_FOR_ROUTINE

            if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playingOrder.contains("sound")){
                if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playSoundDuringBedtimeStory){
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

            playBedtimeStory(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }else if(
            routineActivityPlayButtonTexts[index]!!.value == PAUSE_ROUTINE ||
            routineActivityPlayButtonTexts[index]!!.value == WAIT_FOR_ROUTINE
        ){
            Log.i(TAG, "Pausing bedtime story and sound?")
            pauseBedtimeStory(
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

    private fun pauseBedtimeStory(
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int
    ) {
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentPrayerPlaying == null &&
            globalViewModel_!!.currentSelfLovePlaying == null
        ) {
            if(generalMediaPlayerService.isMediaPlayerPlaying()) {
                generalMediaPlayerService.pauseMediaPlayer()
                globalViewModel_!!.bedtimeStoryTimer.pause()
                globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
                activateBedtimeStoryGlobalControlButton(2)
            }
        }
    }

    fun playBedtimeStory(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories != null) {
            if(
                routineActivityBedtimeStoryUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!!
                                [globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!]!!
                            .bedtimeStoryInfoData.id
                ] != "".toUri()
            ) {
                if(globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!!.isEmpty()) {
                    getRoutineBedtimeStories(index) {
                        retrieveBedtimeStoryUris(
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            index,
                            context
                        )
                    }
                }else{
                    retrieveBedtimeStoryUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                startBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            getRoutineBedtimeStories(index) {
                retrieveBedtimeStoryUris(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    private fun getRoutineBedtimeStories(index: Int, completed: () -> Unit){
        getRoutineBedtimeStoriesBasedOnRoutine(
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData
        ) { routineBedtimeStories ->
            for( routineBedtimeStory in routineBedtimeStories){
                routineActivityBedtimeStoryUrisMapList[index][routineBedtimeStory!!.bedtimeStoryInfoData.id] = "".toUri()
            }
            globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories = routineBedtimeStories
            completed()
        }
    }

    fun getRoutineBedtimeStoriesBasedOnRoutine(
        routineData: RoutineData,
        completed: (routineBedtimeStoryList: MutableList<RoutineBedtimeStoryInfo?>) -> Unit
    ) {
        RoutineBedtimeStoryBackend.queryRoutineBedtimeStoryBasedOnRoutine(routineData) { routineBedtimeStoriess ->
            completed(routineBedtimeStoriess.toMutableList())
        }
    }

    private fun retrieveBedtimeStoryUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if (
            globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!![
                    globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData.audioSource == BedtimeStoryAudioSource.UPLOADED
        ) {
            SoundBackend.retrieveAudio(
                globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!![
                        globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!
                ]!!.bedtimeStoryInfoData.audioKeyS3,
                globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!![
                        globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!
                ]!!.bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
            ) { uri ->
                routineActivityBedtimeStoryUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!!
                                [globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!]!!
                            .bedtimeStoryInfoData.id
                ] = uri

                startBedtimeStory(
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

    fun startBedtimeStory(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            routineActivityBedtimeStoryUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!!
                            [globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ] != "".toUri()
        ) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentPrayerPlaying == null &&
                globalViewModel_!!.currentSelfLovePlaying == null
            ){
                generalMediaPlayerService.startMediaPlayer()
            }else{
                initializeBedtimeStoryMediaPlayers(
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
            setGlobalPropertiesAfterPlayingBedtimeStory(index)
        }
    }

    private fun initializeBedtimeStoryMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ){
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(
            routineActivityBedtimeStoryUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!!
                            [globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ]!!
        )
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        generalMediaPlayerService.seekToPos(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentBedtimeStoryContinuePlayingTime)
        globalViewModel_!!.bedtimeStoryTimer.setMaxDuration(
            globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!!
                    [globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!]!!
                .bedtimeStoryInfoData.fullPlayTime.toLong()
        )
        globalViewModel_!!.bedtimeStoryTimer.setDuration(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentBedtimeStoryContinuePlayingTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

        startBedtimeStoryCDT(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }

    fun startBedtimeStoryCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startBedtimeStoryCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.bedtimeStoryPlayTime.toLong(),
            generalMediaPlayerService
        ){
            var continuePlayingTime = -1
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                continuePlayingTime = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                generalMediaPlayerService.onDestroy()
            }
            deActivateBedtimeStoryGlobalControlButton(0)
            activateBedtimeStoryGlobalControlButton(2)
            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
            globalViewModel_!!.currentBedtimeStoryPlaying = null
            globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer = null

            val routine = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.copyOfBuilder()
                .currentBedtimeStoryContinuePlayingTime(continuePlayingTime)
                .build()

            RoutineBackend.updateRoutine(routine){

            }

            incrementPlayingOrderIndex(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun startBedtimeStoryCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer == null){
            globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "BedtimeStory routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingBedtimeStory(index: Int){
        globalViewModel_!!.currentBedtimeStoryPlaying =
            globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!![
                    globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData

        globalViewModel_!!.currentBedtimeStoryPlayingUri =
            routineActivityBedtimeStoryUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStories!!
                            [globalViewModel_!!.currentRoutinePlayingRoutineBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ]

        globalViewModel_!!.isCurrentBedtimeStoryPlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true
        deActivateBedtimeStoryGlobalControlButton(0)
        deActivateBedtimeStoryGlobalControlButton(2)
    }
}