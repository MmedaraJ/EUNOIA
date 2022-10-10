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

            if(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.playingOrder.contains("sound")){
                if(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.playSoundDuringBedtimeStory){
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

    private fun playBedtimeStory(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories != null) {
            if(
                routineActivityBedtimeStoryUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                            .bedtimeStoryInfoData.id
                ] != "".toUri()
            ) {
                if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!.isEmpty()) {
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
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!
        ) { routineBedtimeStories ->
            for( routineBedtimeStory in routineBedtimeStories){
                routineActivityBedtimeStoryUrisMapList[index][routineBedtimeStory!!.bedtimeStoryInfoData.id] = "".toUri()
            }
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories = routineBedtimeStories
            completed()
        }
    }

    private fun getRoutineBedtimeStoriesBasedOnRoutine(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipBedtimeStoryList: MutableList<UserRoutineRelationshipBedtimeStoryInfo?>) -> Unit
    ) {
        UserRoutineRelationshipBedtimeStoryInfoBackend.queryUserRoutineRelationshipBedtimeStoryInfoBasedOnUserRoutineRelationship(userRoutineRelationship) { userRoutineRelationships ->
            completed(userRoutineRelationships.toMutableList())
        }
    }

    private fun retrieveBedtimeStoryUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if (
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData.audioSource == BedtimeStoryAudioSource.UPLOADED
        ) {
            SoundBackend.retrieveAudio(
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
                ]!!.bedtimeStoryInfoData.audioKeyS3,
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
                ]!!.bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
            ) { uri ->
                routineActivityBedtimeStoryUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
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

    private fun startBedtimeStory(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            routineActivityBedtimeStoryUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
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
            Log.i(TAG, "play button 1 bedtime story is ${routineActivityPlayButtonTexts[index]!!.value}")
            generalMediaPlayerService.loopMediaPlayer()
            //globalViewModel_!!.previouslyPlayedUserSoundRelationship = globalViewModel_!!.currentUsersSoundRelationships!![index]
            //globalViewModel_!!.generalPlaytimeTimer.start()
            setGlobalPropertiesAfterPlayingBedtimeStory(index)
        }else{
            retrieveBedtimeStoryUris(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
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
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ]!!
        )
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        //generalMediaPlayerService.seekToPos(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.routineData.currentBedtimeStoryContinuePlayingTime)
        globalViewModel_!!.bedtimeStoryTimer.setMaxDuration(
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                    [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                .bedtimeStoryInfoData.fullPlayTime.toLong()
        )
        globalViewModel_!!.bedtimeStoryTimer.setDuration(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.currentBedtimeStoryContinuePlayingTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

        if(globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer == null) {
            startBedtimeStoryCDT(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun startBedtimeStoryCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        generalBedtimeStoryTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )

        individualBedtimeStoryTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }

    private fun individualBedtimeStoryTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startNextBedtimeStoryCountDownTimer(
            context,
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateBedtimeStoryGlobalControlButton(0)
            activateBedtimeStoryGlobalControlButton(2)

            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
            globalViewModel_!!.currentBedtimeStoryPlaying = null
            globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = null

            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex =
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!! + 1
            if (globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!! >
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!.indices.last) {
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = 0
            }

            val routine = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .currentBedtimeStoryPlayingIndex(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex)
                .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {}

            routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE

            Log.i(
                TAG,
                "About to go play bedtime story with index ${globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex}"
            )
            playOrPauseBedtimeStoryAccordingly(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun generalBedtimeStoryTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startBedtimeStoryCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.bedtimeStoryPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            var continuePlayingTime = -1
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                continuePlayingTime = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                generalMediaPlayerService.onDestroy()
            }
            deActivateBedtimeStoryGlobalControlButton(0)
            activateBedtimeStoryGlobalControlButton(2)
            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
            globalViewModel_!!.currentBedtimeStoryPlaying = null
            globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer!!.cancel()
            globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer = null
            if (globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer != null) {
                globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer!!.cancel()
                globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = null
            }

            val routine =
                globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                    .currentBedtimeStoryContinuePlayingTime(continuePlayingTime)
                    .build()

            //update routine with new BedtimeStory info
            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                globalViewModel_!!.currentUsersRoutineRelationships!![index] = it
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

    private fun startNextBedtimeStoryCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer == null){
            globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Next BedtimeStory routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingBedtimeStory(index: Int){
        globalViewModel_!!.currentBedtimeStoryPlaying =
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData

        globalViewModel_!!.currentBedtimeStoryPlayingUri =
            routineActivityBedtimeStoryUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ]

        globalViewModel_!!.isCurrentBedtimeStoryPlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true
        deActivateBedtimeStoryGlobalControlButton(0)
        deActivateBedtimeStoryGlobalControlButton(2)
    }
}