package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.core.net.toUri
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.prayer.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.prayer.activatePrayerGlobalControlButton
import com.example.eunoia.ui.bottomSheets.prayer.deActivatePrayerGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.activateSelfLoveGlobalControlButton
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.navigation.globalViewModel_

object PrayerForRoutine{
    private const val TAG = "PrayerForRoutine"

    fun playOrPausePrayerAccordingly(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        Log.i(TAG, "play button 1 prayer is ${routineActivityPlayButtonTexts[index]!!.value}")
        if(routineActivityPlayButtonTexts[index]!!.value == START_ROUTINE) {
            routineActivityPlayButtonTexts[index]!!.value = WAIT_FOR_ROUTINE

            if(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.playingOrder.contains("sound")){
                if(globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.playSoundDuringPrayer){
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
                globalViewModel_!!.generalPlaytimeTimer.pause()
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
        if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers != null) {
            if(
                routineActivityPrayerUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                            .prayerData.id
                ] != "".toUri()
            ) {
                Log.i(TAG, "that prayer uriz is null")
                if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!.isEmpty()) {
                    getRoutinePrayers(index) {
                        retrievePrayerUris(
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            index,
                            context
                        )
                    }
                }else{
                    Log.i(TAG, "needs presets for null prayer")
                    retrievePrayerUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                Log.i(TAG, "that prayer uriz is noooooot null")
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
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!
        ) { userRoutineRelationshipPrayers ->
            for( userRoutineRelationshipPrayer in userRoutineRelationshipPrayers){
                routineActivityPrayerUrisMapList[index][userRoutineRelationshipPrayer!!.prayerData.id] = "".toUri()
            }
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers = userRoutineRelationshipPrayers
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
        index: Int,
        context: Context
    ) {
        /*if(
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                    [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                .prayerData.audioSource == PrayerAudioSource.UPLOADED
        ) {*/
        Log.i(TAG, "About to retrieve prayera uris for: ${globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
            .prayerData.displayName}")
            SoundBackend.retrieveAudio(
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                    .prayerData.audioKeyS3,
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                    .prayerData.prayerOwner.amplifyAuthUserId
            ) {
                routineActivityPrayerUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                            .prayerData.id
                ] = it

                startPrayer(
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

    private fun afterPlayingPrayer(
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int
    ){
        routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
        generalMediaPlayerService.loopMediaPlayer()
        globalViewModel_!!.prayerTimer.start()
        globalViewModel_!!.generalPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingPrayer(index)
    }

    private fun startPrayer(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(
            routineActivityPrayerUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
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

            afterPlayingPrayer(
                generalMediaPlayerService,
                index
            )

        }else{
            retrievePrayerUris(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun updatePreviousAndCurrentPrayerRelationship(completed: () -> Unit){
        updatePreviousUserPrayerRelationship {
            updateRecentlyPlayedUserPrayerRelationshipWithPrayer(
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                    .prayerData
            ) {
                completed()
            }
        }
    }

    private fun initializePrayerMediaPlayers(
        generalMediaPlayerService: GeneralMediaPlayerService,
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int,
        context: Context
    ){
        updatePreviousAndCurrentPrayerRelationship {
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(
                routineActivityPrayerUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                            .prayerData.id
                ]!!
            )
            val intent = Intent()
            intent.action = "PLAY"
            generalMediaPlayerService.onStartCommand(intent, 0, 0)
            Log.i(TAG, "prayer index = ${globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex}")
            globalViewModel_!!.prayerTimer.setMaxDuration(
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                        [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                    .prayerData.fullPlayTime.toLong()
            )
            resetOtherGeneralMediaPlayerUsersExceptPrayer()

            if (globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer == null) {
                startPrayerCDT(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    private fun startPrayerCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        generalPrayerTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )

        individualPrayerTimer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }

    private fun individualPrayerTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startNextPrayerCountDownTimer(
            context,
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!
            ]!!.prayerData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            globalViewModel_!!.isCurrentPrayerPlaying = false
            globalViewModel_!!.currentPrayerPlaying = null
            globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer = null

            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex =
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!! + 1
            if (globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!! > globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!.indices.last) {
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = 0
            }

            val routine =
                globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                    .currentPrayerPlayingIndex(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex)
                    .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                globalViewModel_!!.currentUsersRoutineRelationships!![index] = it
                routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
                playOrPausePrayerAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    private fun generalPrayerTimer(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        startPrayerCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.prayerPlayTime.toLong(),
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
            globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer!!.cancel()
            globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = null
            if(globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer != null) {
                globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer!!.cancel()
                globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer = null
            }

            val routine = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .currentPrayerContinuePlayingTime(continuePlayingTime)
                .build()

            updatePreviousUserPrayerRelationship {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    globalViewModel_!!.currentUsersRoutineRelationships!![index] = it
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

    private fun startNextPrayerCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer == null){
            globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "next Prayer routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingPrayer(index: Int){
        globalViewModel_!!.currentPrayerPlaying =
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!
            ]!!.prayerData

        globalViewModel_!!.currentPrayerPlayingUri =
            routineActivityPrayerUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                        .prayerData.id
            ]

        globalViewModel_!!.isCurrentPrayerPlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true
        deActivatePrayerGlobalControlButton(0)
        deActivatePrayerGlobalControlButton(2)
    }

    fun updateRecentlyPlayedUserPrayerRelationshipWithPrayer(
        prayerData: PrayerData,
        completed: (userPrayerRelationship: UserPrayerRelationship) -> Unit
    ){
        globalViewModel_!!.currentPrayerPlaying = prayerData
        UserPrayerRelationshipBackend.queryUserPrayerRelationshipBasedOnUserAndPrayer(
            globalViewModel_!!.currentUser!!,
            prayerData
        ) { userPrayerRelationship ->
            if(userPrayerRelationship.isNotEmpty()) {
                updateCurrentUserPrayerRelationshipUsageTimeStamp(userPrayerRelationship[0]!!) {
                    globalViewModel_!!.previouslyPlayedUserPrayerRelationship = it
                    completed(it)
                }
            }else{
                UserPrayerRelationshipBackend.createUserPrayerRelationshipObject(
                    prayerData
                ){ newUserPrayerRelationship ->
                    updateCurrentUserPrayerRelationshipUsageTimeStamp(newUserPrayerRelationship) {
                        globalViewModel_!!.previouslyPlayedUserPrayerRelationship = it
                        completed(it)
                    }
                }
            }
        }
    }

    fun updateRecentlyPlayedUserPrayerRelationshipWithUserPrayerRelationship(
        userPrayerRelationship: UserPrayerRelationship,
        completed: (userPrayerRelationship: UserPrayerRelationship) -> Unit
    ){
        updateCurrentUserPrayerRelationshipUsageTimeStamp(userPrayerRelationship) {
            globalViewModel_!!.previouslyPlayedUserPrayerRelationship = it
            completed(it)
        }
    }
}