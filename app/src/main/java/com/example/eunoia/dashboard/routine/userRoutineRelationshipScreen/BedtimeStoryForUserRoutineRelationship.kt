package com.example.eunoia.dashboard.routine.userRoutineRelationshipScreen

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfoRelationship
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipBedtimeStoryInfo
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserRoutineRelationshipBackend
import com.example.eunoia.backend.UserRoutineRelationshipBedtimeStoryInfoBackend
import com.example.eunoia.dashboard.home.BedtimeStoryForRoutine
import com.example.eunoia.dashboard.bedtimeStory.resetOtherGeneralMediaPlayerUsersExceptBedtimeStory
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.bedtimeStory.activateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.bedtimeStory.deActivateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.activateSelfLoveGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.utils.getCurrentlyPlayingTime
import com.example.eunoia.utils.timerFormatMS

object BedtimeStoryForUserRoutineRelationship {
    private const val TAG = "BedtimeStoryForUserRoutineRelationship"

    fun playOrPauseBedtimeStoryAccordingly(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        Log.i(TAG, "play button 1 BedtimeStory is $playButtonText")
        if(playButtonText == START_ROUTINE) {
            playButtonText = WAIT_FOR_ROUTINE

            if(thisUserRoutineRelationship!!.playingOrder.contains("sound")){
                if(thisUserRoutineRelationship!!.playSoundDuringBedtimeStory){
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

            playBedtimeStory(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context
            )

        }else if(
            playButtonText == PAUSE_ROUTINE ||
            playButtonText == WAIT_FOR_ROUTINE
        ){
            Log.i(TAG, "Pausing BedtimeStory and sound?")
            pauseBedtimeStory(generalMediaPlayerService)

            SoundForUserRoutineRelationship.pauseSound(soundMediaPlayerService)
            playButtonText = START_ROUTINE
        }
    }

    private fun pauseBedtimeStory(
        generalMediaPlayerService: GeneralMediaPlayerService,
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
        context: Context
    ) {
        if(bedtimeStories!!.isNotEmpty()) {
            if(bedtimeStoryUri[bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.id] != "".toUri()) {
                Log.i(TAG, "that BedtimeStory uriz is null")
                if(bedtimeStories!!.isEmpty()) {
                    getRoutineBedtimeStories{
                        retrieveBedtimeStoryUris(
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            context
                        )
                    }
                }else{
                    Log.i(TAG, "needs presets for null BedtimeStory")
                    retrieveBedtimeStoryUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        context
                    )
                }
            }else{
                Log.i(TAG, "that BedtimeStory uriz is noooooot null")
                startBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }else{
            getRoutineBedtimeStories{
                retrieveBedtimeStoryUris(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }
    }

    private fun getRoutineBedtimeStories(completed: () -> Unit){
        getRoutinebedtimeStoriesBasedOnRoutine(
            thisUserRoutineRelationship!!
        ) { UserRoutineRelationshipBedtimeStoryInfos ->
            for( UserRoutineRelationshipBedtimeStoryInfo in UserRoutineRelationshipBedtimeStoryInfos){
                bedtimeStoryUri[UserRoutineRelationshipBedtimeStoryInfo!!.bedtimeStoryInfoData.id] = "".toUri()
            }
            bedtimeStories = UserRoutineRelationshipBedtimeStoryInfos
            completed()
        }
    }

    private fun getRoutinebedtimeStoriesBasedOnRoutine(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (UserRoutineRelationshipBedtimeStoryInfosBedtimeStoryList: MutableList<UserRoutineRelationshipBedtimeStoryInfo?>) -> Unit
    ) {
        UserRoutineRelationshipBedtimeStoryInfoBackend.queryUserRoutineRelationshipBedtimeStoryInfoBasedOnUserRoutineRelationship(userRoutineRelationship) { UserRoutineRelationshipBedtimeStoryInfos ->
            completed(UserRoutineRelationshipBedtimeStoryInfos.toMutableList())
        }
    }

    private fun retrieveBedtimeStoryUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ) {
        SoundBackend.retrieveAudio(
            bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.audioKeyS3,
            bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
        ) {
            bedtimeStoryUri[bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.id] =
                it
            startBedtimeStory(
                generalMediaPlayerService,
                soundMediaPlayerService,
                context
            )
        }
    }

    private fun afterPlayingBedtimeStory(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ){
        playButtonText = PAUSE_ROUTINE
        //generalMediaPlayerService.loopMediaPlayer()
        bedtimeStoryViewModel!!.bedtimeStoryTimer.start()
        globalViewModel!!.generalPlaytimeTimer.start()

        if (bedtimeStoryCountDownTimer == null) {
            startBedtimeStoryCDT(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context
            )
        }

        setGlobalPropertiesAfterPlayingBedtimeStory()
    }

    private fun startBedtimeStory(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ) {
        if(bedtimeStoryUri[bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.id] != "".toUri()) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                prayerViewModel!!.currentPrayerPlaying == null &&
                selfLoveViewModel!!.currentSelfLovePlaying == null
            ){
                globalViewModel!!.remainingPlayTime =
                    bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition

                bedtimeStoryViewModel!!.remainingPlayTime = bedtimeStoryViewModel!!.playTimeSoFar.toInt()

                generalMediaPlayerService.startMediaPlayer()
                afterPlayingBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }else{
                initializeBedtimeStoryMediaPlayers(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }else{
            retrieveBedtimeStoryUris(
                generalMediaPlayerService,
                soundMediaPlayerService,
                context
            )
        }
    }

    private fun updatePreviousAndCurrentBedtimeStoryRelationship(
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
    ){
        updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
            BedtimeStoryForRoutine.updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(
                bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData
            ) {
                completed(it)
            }
        }
    }

    private fun initializeBedtimeStoryMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ){
        updatePreviousAndCurrentBedtimeStoryRelationship(generalMediaPlayerService) { userBedtimeStoryRelationship ->
            UserRoutineRelationshipBedtimeStoryInfoBackend.queryUserRoutineRelationshipBedtimeStoryInfoBasedOnBedtimeStory(
                userBedtimeStoryRelationship.userBedtimeStoryInfoRelationshipBedtimeStoryInfo
            ) { userRoutineRelationshipBedtimeStoryInfo ->
                if(userRoutineRelationshipBedtimeStoryInfo.isNotEmpty()) {
                    bedtimeStories!![bedtimeStoriesIndex] = userRoutineRelationshipBedtimeStoryInfo[0]
                }

                globalViewModel!!.resetCDT()

                generalMediaPlayerService.onDestroy()
                generalMediaPlayerService.setAudioUri(
                    bedtimeStoryUri[bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.id]!!

                )

                val seekToPos = BedtimeStoryForRoutine.getSeekToPos(userBedtimeStoryRelationship)
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
                    bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.fullPlayTime.toLong()
                )

                globalViewModel!!.remainingPlayTime =
                    bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.fullPlayTime -
                            userBedtimeStoryRelationship.continuePlayingTime

                bedtimeStoryViewModel!!.remainingPlayTime = thisUserRoutineRelationship!!.bedtimeStoryPlayTime

                bedtimeStoryViewModel!!.bedtimeStoryTimeDisplay = timerFormatMS(
                    userBedtimeStoryRelationship.continuePlayingTime.toLong()
                )

                resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

                afterPlayingBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }

    }

    private fun startBedtimeStoryCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        generalBedtimeStoryTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )

        individualBedtimeStoryTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }

    private fun individualBedtimeStoryTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        globalViewModel!!.startTheCDT(
            globalViewModel!!.remainingPlayTime.toLong(),
            generalMediaPlayerService
        ){
            individualCDTDone(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
            )
        }
    }

    fun individualCDTDone(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context,
    ){
        if (generalMediaPlayerService.isMediaPlayerInitialized()) {
            generalMediaPlayerService.onDestroy()
        }

        globalViewModel!!.resetCDT()

        deActivateSelfLoveGlobalControlButton(0)
        activateSelfLoveGlobalControlButton(2)

        bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
        bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null

        bedtimeStoriesIndex += 1
        if (bedtimeStoriesIndex > bedtimeStories!!.indices.last) {
            bedtimeStoriesIndex = 0
        }
        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = bedtimeStoriesIndex

        val routine = thisUserRoutineRelationship!!.copyOfBuilder()
                .currentBedtimeStoryPlayingIndex(bedtimeStoriesIndex)
                .build()

        updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                thisUserRoutineRelationship = it
                routineViewModel!!.currentUserRoutineRelationshipPlaying = it
                playButtonText = START_ROUTINE

                bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = null
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
                bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false

                playOrPauseBedtimeStoryAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }
    }

    private fun resetBtsCDT(){
        if(bedtimeStoryCountDownTimer != null){
            bedtimeStoryCountDownTimer!!.cancel()
            bedtimeStoryCountDownTimer = null
        }
    }

    private fun generalBedtimeStoryTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        startBedtimeStoryCountDownTimer(
            context,
            bedtimeStoryViewModel!!.remainingPlayTime.toLong(),
            generalMediaPlayerService
        ){
            globalViewModel!!.resetCDT()

            val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)

            deActivateBedtimeStoryGlobalControlButton(0)
            activateBedtimeStoryGlobalControlButton(2)

            bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
            bedtimeStoryCountDownTimer = null
            routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer = bedtimeStoryCountDownTimer

            if(nextBedtimeStoryCountDownTimer != null) {
                nextBedtimeStoryCountDownTimer = null
                routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = nextBedtimeStoryCountDownTimer
            }

            val routine = thisUserRoutineRelationship!!.copyOfBuilder()
                .currentBedtimeStoryContinuePlayingTime(continuePlayingTime)
                .build()

            updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    thisUserRoutineRelationship = it
                    routineViewModel!!.currentUserRoutineRelationshipPlaying = it
                    playButtonText = START_ROUTINE

                    resetBtsCDT()

                    bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = null
                    bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
                    bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false

                    incrementPlayingOrderIndex(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context
                    )
                }
            }
        }
    }

    private fun startBedtimeStoryCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(bedtimeStoryCountDownTimer == null){
            bedtimeStoryCountDownTimer = object : CountDownTimer(time, 1) {
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
        bedtimeStoryCountDownTimer!!.start()
        routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer = bedtimeStoryCountDownTimer
    }

    private fun startNextBedtimeStoryCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(nextBedtimeStoryCountDownTimer == null){
            nextBedtimeStoryCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "next BedtimeStory routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        nextBedtimeStoryCountDownTimer!!.start()
        routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = nextBedtimeStoryCountDownTimer
    }

    private fun setGlobalPropertiesAfterPlayingBedtimeStory(){
        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories = bedtimeStories
        bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData
        bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = bedtimeStoryUri[bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.id]
        bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = true
        routineViewModel!!.isCurrentRoutinePlaying = true

        deActivateBedtimeStoryGlobalControlButton(0)
        deActivateBedtimeStoryGlobalControlButton(2)
    }
}