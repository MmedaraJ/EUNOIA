package com.example.eunoia.dashboard.routine.userRoutineRelationshipScreen

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.home.PrayerForRoutine.updateRecentlyPlayedUserPrayerRelationshipWithPrayer
import com.example.eunoia.dashboard.prayer.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.prayer.activatePrayerGlobalControlButton
import com.example.eunoia.ui.bottomSheets.prayer.deActivatePrayerGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.activateSelfLoveGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.navigation.globalViewModel_

object PrayerForUserRoutineRelationship{
    private const val TAG = "PrayerForUserRoutineRelationship"

    fun playOrPausePrayerAccordingly(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        Log.i(TAG, "play button 1 prayer is $playButtonText")
        if(playButtonText == START_ROUTINE) {
            playButtonText = WAIT_FOR_ROUTINE

            if(thisUserRoutineRelationship!!.playingOrder.contains("sound")){
                if(thisUserRoutineRelationship!!.playSoundDuringPrayer){
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

            playPrayer(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context
            )

        }else if(
            playButtonText == PAUSE_ROUTINE ||
            playButtonText == WAIT_FOR_ROUTINE
        ){
            Log.i(TAG, "Pausing prayer and sound?")
            pausePrayer(generalMediaPlayerService)

            SoundForUserRoutineRelationship.pauseSound(soundMediaPlayerService)
            playButtonText = START_ROUTINE
        }
    }

    private fun pausePrayer(
        generalMediaPlayerService: GeneralMediaPlayerService,
    ) {
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentBedtimeStoryPlaying == null &&
            globalViewModel_!!.currentSelfLovePlaying == null
        ) {
            if(generalMediaPlayerService.isMediaPlayerPlaying()) {
                generalMediaPlayerService.pauseMediaPlayer()
                globalViewModel_!!.prayerTimer.pause()
                globalViewModel_!!.generalPlaytimeTimer.pause()
                globalViewModel_!!.isCurrentPrayerPlaying = false
                activatePrayerGlobalControlButton(2)
            }
        }
    }

    private fun playPrayer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        if(prayers!!.isNotEmpty()) {
            if(prayerUri[prayers!![prayersIndex]!!.prayerData.id] != "".toUri()) {
                Log.i(TAG, "that prayer uriz is null")
                if(prayers!!.isEmpty()) {
                    getRoutinePrayers{
                        retrievePrayerUris(
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            context
                        )
                    }
                }else{
                    Log.i(TAG, "needs presets for null prayer")
                    retrievePrayerUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        context
                    )
                }
            }else{
                Log.i(TAG, "that prayer uriz is noooooot null")
                startPrayer(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }else{
            getRoutinePrayers{
                retrievePrayerUris(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }
    }

    private fun getRoutinePrayers(completed: () -> Unit){
        getRoutinePrayersBasedOnRoutine(
            thisUserRoutineRelationship!!
        ) { userRoutineRelationshipPrayers ->
            for( userRoutineRelationshipPrayer in userRoutineRelationshipPrayers){
                prayerUri[userRoutineRelationshipPrayer!!.prayerData.id] = "".toUri()
            }
            prayers = userRoutineRelationshipPrayers
            completed()
        }
    }

    private fun getRoutinePrayersBasedOnRoutine(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipPrayersPrayerList: MutableList<UserRoutineRelationshipPrayer?>) -> Unit
    ) {
        UserRoutineRelationshipPrayerBackend.queryUserRoutineRelationshipPrayerBasedOnUserRoutineRelationship(userRoutineRelationship) { userRoutineRelationshipPrayers ->
            completed(userRoutineRelationshipPrayers.toMutableList())
        }
    }

    private fun retrievePrayerUris(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ) {
        Log.i(TAG, "About to retrieve prayera uris for: ${prayers!![prayersIndex]!!.prayerData.displayName}")
            SoundBackend.retrieveAudio(
                prayers!![prayersIndex]!!.prayerData.audioKeyS3,
                prayers!![prayersIndex]!!.prayerData.prayerOwner.amplifyAuthUserId
            ) {
                prayerUri[prayers!![prayersIndex]!!.prayerData.id] = it
                startPrayer(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        /*}else{
            //if recorded
        }*/
    }

    private fun afterPlayingPrayer(generalMediaPlayerService: GeneralMediaPlayerService, ){
        playButtonText = PAUSE_ROUTINE
        generalMediaPlayerService.loopMediaPlayer()
        globalViewModel_!!.prayerTimer.start()
        globalViewModel_!!.generalPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingPrayer()
    }

    private fun startPrayer(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ) {
        if(prayerUri[prayers!![prayersIndex]!!.prayerData.id] != "".toUri()) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentSelfLovePlaying == null
            ){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingPrayer(generalMediaPlayerService)
            }else{
                initializePrayerMediaPlayers(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    context
                )
            }
        }else{
            retrievePrayerUris(
                generalMediaPlayerService,
                soundMediaPlayerService,
                context
            )
        }
    }

    private fun updatePreviousAndCurrentPrayerRelationship(completed: () -> Unit){
        updatePreviousUserPrayerRelationship {
            updateRecentlyPlayedUserPrayerRelationshipWithPrayer(
                prayers!![prayersIndex]!!.prayerData
            ) {
                completed()
            }
        }
    }

    private fun initializePrayerMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ){
        updatePreviousAndCurrentPrayerRelationship {
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(
                prayerUri[prayers!![prayersIndex]!!.prayerData.id]!!

            )
            val intent = Intent()
            intent.action = "PLAY"
            generalMediaPlayerService.onStartCommand(intent, 0, 0)
            Log.i(TAG, "prayer index = $prayersIndex")

            globalViewModel_!!.prayerTimer.setMaxDuration(
                prayers!![prayersIndex]!!.prayerData.fullPlayTime.toLong()
            )
            resetOtherGeneralMediaPlayerUsersExceptPrayer()

            if (prayerCountDownTimer == null) {
                startPrayerCDT(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }

        afterPlayingPrayer(generalMediaPlayerService)
    }

    private fun startPrayerCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        generalPrayerTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )

        individualPrayerTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }

    private fun individualPrayerTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        startNextPrayerCountDownTimer(
            context,
            prayers!![prayersIndex]!!.prayerData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            globalViewModel_!!.isCurrentPrayerPlaying = false
            globalViewModel_!!.currentPrayerPlaying = null
            prayerCountDownTimer = null
            globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer = prayerCountDownTimer

            prayersIndex += 1
            if (prayersIndex > prayers!!.indices.last) {
                prayersIndex = 0
            }
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = prayersIndex

            val routine =
                thisUserRoutineRelationship!!.copyOfBuilder()
                    .currentPrayerPlayingIndex(prayersIndex)
                    .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                thisUserRoutineRelationship = it
                globalViewModel_!!.currentUserRoutineRelationshipPlaying = it
                playButtonText = START_ROUTINE
                playOrPausePrayerAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }
    }

    private fun generalPrayerTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        startPrayerCountDownTimer(
            context,
            thisUserRoutineRelationship!!.prayerPlayTime.toLong(),
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
            prayerCountDownTimer = null
            globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = prayerCountDownTimer

            if(nextPrayerCountDownTimer != null) {
                nextPrayerCountDownTimer = null
            }
            globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer = nextPrayerCountDownTimer

            val routine = thisUserRoutineRelationship!!.copyOfBuilder()
                .currentPrayerContinuePlayingTime(continuePlayingTime)
                .build()

            updatePreviousUserPrayerRelationship {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    thisUserRoutineRelationship = it
                    globalViewModel_!!.currentUserRoutineRelationshipPlaying = it
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

    private fun startPrayerCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(prayerCountDownTimer == null){
            prayerCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Prayer routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        prayerCountDownTimer!!.start()
        globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = prayerCountDownTimer
    }

    private fun startNextPrayerCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(nextPrayerCountDownTimer == null){
            nextPrayerCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "next Prayer routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        nextPrayerCountDownTimer!!.start()
        globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer = nextPrayerCountDownTimer
    }

    private fun setGlobalPropertiesAfterPlayingPrayer(){
        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers = prayers
        globalViewModel_!!.currentPrayerPlaying = prayers!![prayersIndex]!!.prayerData
        globalViewModel_!!.currentPrayerPlayingUri = prayerUri[prayers!![prayersIndex]!!.prayerData.id]
        globalViewModel_!!.isCurrentPrayerPlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true

        deActivatePrayerGlobalControlButton(0)
        deActivatePrayerGlobalControlButton(2)
    }
}