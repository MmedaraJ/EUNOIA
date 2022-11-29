package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.bedtimeStory.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.bedtimeStory.activateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.bedtimeStory.deActivateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.utils.getCurrentlyPlayingTime
import com.example.eunoia.utils.timerFormatMS

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

            if(routineViewModel!!.currentUsersRoutineRelationships!![index]!!.playingOrder.contains("sound")){
                if(routineViewModel!!.currentUsersRoutineRelationships!![index]!!.playSoundDuringBedtimeStory){
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
            prayerViewModel!!.currentPrayerPlaying == null &&
            selfLoveViewModel!!.currentSelfLovePlaying == null
        ) {
            if(generalMediaPlayerService.isMediaPlayerPlaying()) {
                generalMediaPlayerService.pauseMediaPlayer()
                bedtimeStoryViewModel!!.bedtimeStoryTimer.pause()
                globalViewModel!!.generalPlaytimeTimer.pause()
                bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
                globalViewModel!!.resetCDT()
                resetBtsCDT()
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
        if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories != null) {
            if(
                routineActivityBedtimeStoryUrisMapList[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                            .bedtimeStoryInfoData.id
                ] != "".toUri()
            ) {
                if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!.isEmpty()) {
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
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!
        ) { routineBedtimeStories ->
            for( routineBedtimeStory in routineBedtimeStories){
                routineActivityBedtimeStoryUrisMapList[index][routineBedtimeStory!!.bedtimeStoryInfoData.id] = "".toUri()
            }
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories = routineBedtimeStories
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
        SoundBackend.retrieveAudio(
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData.audioKeyS3,
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
        ) { uri ->
            routineActivityBedtimeStoryUrisMapList[index][
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ] = uri!!

            startBedtimeStory(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
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
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ] != "".toUri()
        ) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                prayerViewModel!!.currentPrayerPlaying == null &&
                selfLoveViewModel!!.currentSelfLovePlaying == null
            ){
                globalViewModel!!.remainingPlayTime =
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
                    ]!!.bedtimeStoryInfoData.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition

                bedtimeStoryViewModel!!.remainingPlayTime = bedtimeStoryViewModel!!.playTimeSoFar.toInt()

                generalMediaPlayerService.startMediaPlayer()

                afterPlayingBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context,
                )
            }else{
                initializeBedtimeStoryMediaPlayers(
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
    }

    private fun afterPlayingBedtimeStory(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ){
        routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
        //generalMediaPlayerService.loopMediaPlayer()
        bedtimeStoryViewModel!!.bedtimeStoryTimer.start()
        globalViewModel!!.generalPlaytimeTimer.start()

        if (routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer == null) {
            startBedtimeStoryCDT(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context,
            )
        }

        setGlobalPropertiesAfterPlayingBedtimeStory(index)
    }

    private fun updatePreviousAndCurrentBedtimeStoryRelationship(
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
    ){
        updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
            updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                        [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                    .bedtimeStoryInfoData
            ) { userBedtimeStoryInfoRelationship ->
                completed(userBedtimeStoryInfoRelationship)
            }
        }
    }

    fun getSeekToPos(
        userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship,
    ): Int{
        var seekToPos = 0
        if(userBedtimeStoryInfoRelationship.continuePlayingTime != null){
            seekToPos = userBedtimeStoryInfoRelationship.continuePlayingTime
        }
        return seekToPos
    }

    private fun initializeBedtimeStoryMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context,
    ){
        updatePreviousAndCurrentBedtimeStoryRelationship(generalMediaPlayerService) { userBedtimeStoryRelationship ->
            UserRoutineRelationshipBedtimeStoryInfoBackend.queryUserRoutineRelationshipBedtimeStoryInfoBasedOnBedtimeStory(
                userBedtimeStoryRelationship.userBedtimeStoryInfoRelationshipBedtimeStoryInfo
            ) { userRoutineRelationshipBedtimeStoryInfo ->
                if(userRoutineRelationshipBedtimeStoryInfo.isNotEmpty()) {
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
                    ] = userRoutineRelationshipBedtimeStoryInfo[0]
                }

                globalViewModel!!.resetCDT()

                generalMediaPlayerService.onDestroy()
                generalMediaPlayerService.setAudioUri(
                    routineActivityBedtimeStoryUrisMapList[index][
                            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                                    [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                                .bedtimeStoryInfoData.id
                    ]!!
                )

                val seekToPos = getSeekToPos(userBedtimeStoryRelationship)
                generalMediaPlayerService.setSeekToPos(
                    seekToPos
                )

                val intent = Intent()
                intent.action = "PLAY"
                generalMediaPlayerService.onStartCommand(intent, 0, 0)

                bedtimeStoryViewModel!!.bedtimeStoryTimer.setDuration(
                    userBedtimeStoryRelationship.continuePlayingTime.toLong()
                )

                bedtimeStoryViewModel!!.bedtimeStoryTimer.setMaxDuration(
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.fullPlayTime.toLong()
                )

                globalViewModel!!.remainingPlayTime =
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
                    ]!!.bedtimeStoryInfoData.fullPlayTime - userBedtimeStoryRelationship.continuePlayingTime

                bedtimeStoryViewModel!!.remainingPlayTime =
                    routineViewModel!!.currentUsersRoutineRelationships!![index]!!.bedtimeStoryPlayTime

                bedtimeStoryViewModel!!.bedtimeStoryTimeDisplay = timerFormatMS(
                    userBedtimeStoryRelationship.continuePlayingTime.toLong()
                )

                resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

                afterPlayingBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context,
                )
            }
        }
    }

    private fun startBedtimeStoryCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
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
            context,
        )
    }

    private fun individualBedtimeStoryTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
    ) {
        globalViewModel!!.startTheCDT(
            globalViewModel!!.remainingPlayTime.toLong(),
            generalMediaPlayerService
        ){
            Log.i(TAG, "2. Bts about to be updated from routine")
            individualCDTDone(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context,
            )
        }
    }

    fun individualCDTDone(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
    ){
        if (generalMediaPlayerService.isMediaPlayerInitialized()) {
            generalMediaPlayerService.onDestroy()
        }

        globalViewModel!!.resetCDT()

        deActivateBedtimeStoryGlobalControlButton(0)
        activateBedtimeStoryGlobalControlButton(2)

        bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
        bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
        routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = null

        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex =
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!! + 1

        if (routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!! >
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!.indices.last) {
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = 0
        }

        val routine = routineViewModel!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
            .currentBedtimeStoryPlayingIndex(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex)
            .build()

        updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                routineViewModel!!.currentUsersRoutineRelationships!![index] = it
                routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE

                Log.i(TAG, "Invidul tne de")

                bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = null
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
                bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false

                playOrPauseBedtimeStoryAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    fun generalBedtimeStoryTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startBedtimeStoryCountDownTimer(
            context,
            bedtimeStoryViewModel!!.remainingPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            globalViewModel!!.resetCDT()

            Log.i(TAG, "General don finish bts routine")
            val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)
            deActivateBedtimeStoryGlobalControlButton(0)
            activateBedtimeStoryGlobalControlButton(2)
            bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null

            routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer = null
            if (routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer != null) {
                routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer!!.cancel()
                routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = null
            }

            val routine =
                routineViewModel!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                    .currentBedtimeStoryContinuePlayingTime(continuePlayingTime)
                    .build()

            updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    Log.i(TAG, "General don finish bts routine 11")
                    routineViewModel!!.currentUsersRoutineRelationships!![index] = it
                    routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE

                    resetBtsCDT()

                    bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = null
                    bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
                    bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false

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

    fun resetBtsCDT(){
        if(routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer != null){
            routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer!!.cancel()
            routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer = null
        }
    }

    fun resetNextBtsCDT(){
        if(routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer != null){
            routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer!!.cancel()
            routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = null
        }
    }

    private fun startBedtimeStoryCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer == null){
            routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer = object : CountDownTimer(time, 1) {
                override fun onTick(millisUntilFinished: Long) {
                    bedtimeStoryViewModel!!.playTimeSoFar = millisUntilFinished
                    if(millisUntilFinished % 10000 == 0L) {
                        Log.i(TAG, "BedtimeStory routine timer: $millisUntilFinished")
                    }
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer!!.start()
    }

    private fun startNextBedtimeStoryCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer == null){
            routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Next BedtimeStory routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingBedtimeStory(index: Int){
        bedtimeStoryViewModel!!.currentBedtimeStoryPlaying =
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!![
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
            ]!!.bedtimeStoryInfoData

        bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri =
            routineActivityBedtimeStoryUrisMapList[index][
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!]!!
                        .bedtimeStoryInfoData.id
            ]

        bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = true
        routineViewModel!!.isCurrentRoutinePlaying = true
        deActivateBedtimeStoryGlobalControlButton(0)
        deActivateBedtimeStoryGlobalControlButton(2)
    }

    fun updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(
        bedtimeStoryInfoData: BedtimeStoryInfoData,
        completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
    ){
        bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = bedtimeStoryInfoData
        UserBedtimeStoryInfoRelationshipBackend.queryUserBedtimeStoryInfoRelationshipBasedOnUserAndBedtimeStoryInfo(
            globalViewModel!!.currentUser!!,
            bedtimeStoryInfoData
        ) { userBedtimeStoryInfoRelationship ->
            if(userBedtimeStoryInfoRelationship.isNotEmpty()) {
                updateCurrentUserBedtimeStoryInfoRelationshipUsageTimeStamp(userBedtimeStoryInfoRelationship[0]!!) {
                    bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship = it
                    completed(it)
                }
            }else{
                UserBedtimeStoryInfoRelationshipBackend.createUserBedtimeStoryInfoRelationshipObject(
                    bedtimeStoryInfoData
                ){ newUserBedtimeStoryInfoRelationship ->
                    updateCurrentUserBedtimeStoryInfoRelationshipUsageTimeStamp(newUserBedtimeStoryInfoRelationship) {
                        bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship = it
                        completed(it)
                    }
                }
            }
        }
    }

    fun updateRecentlyPlayedUserBedtimeStoryRelationshipWithUserBedtimeStoryRelationship(
        userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship,
        completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
    ){
        updateCurrentUserBedtimeStoryInfoRelationshipUsageTimeStamp(userBedtimeStoryInfoRelationship) {
            bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship = it
            completed(it)
        }
    }
}