package com.example.eunoia.dashboard.routine.userRoutineRelationshipScreen

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
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

    private fun afterPlayingBedtimeStory(generalMediaPlayerService: GeneralMediaPlayerService, ){
        playButtonText = PAUSE_ROUTINE
        generalMediaPlayerService.loopMediaPlayer()
        bedtimeStoryViewModel!!.bedtimeStoryTimer.start()
        globalViewModel!!.generalPlaytimeTimer.start()
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
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingBedtimeStory(generalMediaPlayerService)
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
        completed: () -> Unit
    ){
        updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
            BedtimeStoryForRoutine.updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(
                bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData
            ) {
                completed()
            }
        }
    }

    private fun initializeBedtimeStoryMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ){
        updatePreviousAndCurrentBedtimeStoryRelationship(generalMediaPlayerService) {
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(
                bedtimeStoryUri[bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.id]!!

            )
            val intent = Intent()
            intent.action = "PLAY"
            generalMediaPlayerService.onStartCommand(intent, 0, 0)
            Log.i(TAG, "BedtimeStory index = $bedtimeStoriesIndex")

            bedtimeStoryViewModel!!.bedtimeStoryTimer.setMaxDuration(
                bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.fullPlayTime.toLong()
            )
            resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

            if (bedtimeStoryCountDownTimer == null) {
                startBedtimeStoryCDT(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }

        afterPlayingBedtimeStory(generalMediaPlayerService)
    }

    private fun startBedtimeStoryCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        generalbedtimeStoryTimer(
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
        startNextBedtimeStoryCountDownTimer(
            context,
            bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
            if(bedtimeStoryCountDownTimer != null) {
                bedtimeStoryCountDownTimer!!.cancel()
                bedtimeStoryCountDownTimer = null
            }
            routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = bedtimeStoryCountDownTimer

            bedtimeStoriesIndex += 1
            if (bedtimeStoriesIndex > bedtimeStories!!.indices.last) {
                bedtimeStoriesIndex = 0
            }
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = bedtimeStoriesIndex

            val routine =
                thisUserRoutineRelationship!!.copyOfBuilder()
                    .currentBedtimeStoryPlayingIndex(bedtimeStoriesIndex)
                    .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                thisUserRoutineRelationship = it
                routineViewModel!!.currentUserRoutineRelationshipPlaying = it
                playButtonText = START_ROUTINE
                playOrPauseBedtimeStoryAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }
    }

    private fun generalbedtimeStoryTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        startbedtimeStoryCountDownTimer(
            context,
            thisUserRoutineRelationship!!.bedtimeStoryPlayTime.toLong(),
            generalMediaPlayerService
        ){
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

                    incrementPlayingOrderIndex(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context
                    )
                }
            }
        }
    }

    private fun startbedtimeStoryCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(bedtimeStoryCountDownTimer == null){
            bedtimeStoryCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "BedtimeStory routine timer: $millisUntilFinished")
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