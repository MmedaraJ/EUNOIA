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
import com.example.eunoia.ui.navigation.*

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

            if(routineViewModel!!.currentUsersRoutineRelationships!![index]!!.playingOrder.contains("sound")){
                if(routineViewModel!!.currentUsersRoutineRelationships!![index]!!.playSoundDuringPrayer){
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
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
            selfLoveViewModel!!.currentSelfLovePlaying == null
        ) {
            if(generalMediaPlayerService.isMediaPlayerPlaying()) {
                generalMediaPlayerService.pauseMediaPlayer()
                prayerViewModel!!.prayerTimer.pause()
                globalViewModel!!.generalPlaytimeTimer.pause()
                prayerViewModel!!.isCurrentPrayerPlaying = false
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
        if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers != null) {
            if(
                routineActivityPrayerUrisMapList[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                            .prayerData.id
                ] != "".toUri()
            ) {
                Log.i(TAG, "that prayer uriz is null")
                if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!.isEmpty()) {
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
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!
        ) { userRoutineRelationshipPrayers ->
            for( userRoutineRelationshipPrayer in userRoutineRelationshipPrayers){
                routineActivityPrayerUrisMapList[index][userRoutineRelationshipPrayer!!.prayerData.id] = "".toUri()
            }
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers = userRoutineRelationshipPrayers
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
        Log.i(TAG, "About to retrieve prayera uris for: ${routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
            .prayerData.displayName}")
        SoundBackend.retrieveAudio(
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                    [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                .prayerData.audioKeyS3,
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                    [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                .prayerData.prayerOwner.amplifyAuthUserId
        ) {
            routineActivityPrayerUrisMapList[index][
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                        .prayerData.id
            ] = it!!

            startPrayer(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun afterPlayingPrayer(
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int
    ){
        routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
        generalMediaPlayerService.loopMediaPlayer()
        prayerViewModel!!.prayerTimer.start()
        globalViewModel!!.generalPlaytimeTimer.start()
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
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                        .prayerData.id
            ] != "".toUri()
        ) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
                selfLoveViewModel!!.currentSelfLovePlaying == null
            ){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingPrayer(
                    generalMediaPlayerService,
                    index
                )
            }else{
                initializePrayerMediaPlayers(
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
    }

    private fun updatePreviousAndCurrentPrayerRelationship(completed: () -> Unit){
        updatePreviousUserPrayerRelationship {
            updateRecentlyPlayedUserPrayerRelationshipWithPrayer(
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                        [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
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
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                            .prayerData.id
                ]!!
            )
            val intent = Intent()
            intent.action = "PLAY"
            generalMediaPlayerService.onStartCommand(intent, 0, 0)
            Log.i(TAG, "prayer index = ${routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex}")
            prayerViewModel!!.prayerTimer.setMaxDuration(
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                        [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                    .prayerData.fullPlayTime.toLong()
            )
            resetOtherGeneralMediaPlayerUsersExceptPrayer()

            if (routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer == null) {
                startPrayerCDT(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }

        afterPlayingPrayer(
            generalMediaPlayerService,
            index
        )
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
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!![
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!
            ]!!.prayerData.fullPlayTime.toLong(),
            generalMediaPlayerService
        ) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.onDestroy()
            }

            deActivateSelfLoveGlobalControlButton(0)
            activateSelfLoveGlobalControlButton(2)

            prayerViewModel!!.isCurrentPrayerPlaying = false
            prayerViewModel!!.currentPrayerPlaying = null
            routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer = null

            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex =
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!! + 1
            if (routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!! >
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!.indices.last) {
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = 0
            }

            val routine =
                routineViewModel!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                    .currentPrayerPlayingIndex(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex)
                    .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                routineViewModel!!.currentUsersRoutineRelationships!![index] = it
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
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!.prayerPlayTime.toLong(),
            generalMediaPlayerService
        ){
            var continuePlayingTime = -1
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                continuePlayingTime = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                generalMediaPlayerService.onDestroy()
            }

            deActivatePrayerGlobalControlButton(0)
            activatePrayerGlobalControlButton(2)

            prayerViewModel!!.isCurrentPrayerPlaying = false
            prayerViewModel!!.currentPrayerPlaying = null

            routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer = null
            routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer = null

            val routine = routineViewModel!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .currentPrayerContinuePlayingTime(continuePlayingTime)
                .build()

            updatePreviousUserPrayerRelationship {
                UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine) {
                    routineViewModel!!.currentUsersRoutineRelationships!![index] = it
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
        if(routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer == null){
            routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Prayer routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer!!.start()
    }

    private fun startNextPrayerCountDownTimer(
        context: Context,
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ){
        if(routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer == null){
            routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "next Prayer routine timer: $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "Timer stopped")
                }
            }
        }
        routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer!!.start()
    }

    private fun setGlobalPropertiesAfterPlayingPrayer(index: Int){
        prayerViewModel!!.currentPrayerPlaying =
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!![
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!
            ]!!.prayerData

        prayerViewModel!!.currentPrayerPlayingUri =
            routineActivityPrayerUrisMapList[index][
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!]!!
                        .prayerData.id
            ]

        prayerViewModel!!.isCurrentPrayerPlaying = true
        routineViewModel!!.isCurrentRoutinePlaying = true
        deActivatePrayerGlobalControlButton(0)
        deActivatePrayerGlobalControlButton(2)
    }

    fun updateRecentlyPlayedUserPrayerRelationshipWithPrayer(
        prayerData: PrayerData,
        completed: (userPrayerRelationship: UserPrayerRelationship) -> Unit
    ){
        prayerViewModel!!.currentPrayerPlaying = prayerData
        UserPrayerRelationshipBackend.queryUserPrayerRelationshipBasedOnUserAndPrayer(
            globalViewModel!!.currentUser!!,
            prayerData
        ) { userPrayerRelationship ->
            if(userPrayerRelationship.isNotEmpty()) {
                updateCurrentUserPrayerRelationshipUsageTimeStamp(userPrayerRelationship[0]!!) {
                    prayerViewModel!!.previouslyPlayedUserPrayerRelationship = it
                    completed(it)
                }
            }else{
                UserPrayerRelationshipBackend.createUserPrayerRelationshipObject(
                    prayerData
                ){ newUserPrayerRelationship ->
                    updateCurrentUserPrayerRelationshipUsageTimeStamp(newUserPrayerRelationship) {
                        prayerViewModel!!.previouslyPlayedUserPrayerRelationship = it
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
            prayerViewModel!!.previouslyPlayedUserPrayerRelationship = it
            completed(it)
        }
    }
}