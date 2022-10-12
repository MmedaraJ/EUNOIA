package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserRoutineRelationshipSoundPresetBackend
import com.example.eunoia.backend.UserSoundRelationshipBackend
import com.example.eunoia.dashboard.sound.resetAll
import com.example.eunoia.dashboard.sound.updateCurrentUserSoundRelationshipUsageTimeStamp
import com.example.eunoia.dashboard.sound.updatePreviousUserSoundRelationship
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.sound.resetGlobalControlButtons
import com.example.eunoia.ui.navigation.globalViewModel_

object SoundForRoutine{
    private const val TAG = "SoundForRoutine"

    fun playSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets != null) {
            if(
                routineActivitySoundUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                            .soundPresetData.id
                ]!!.isEmpty()
            ) {
                if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!.isEmpty()) {
                    getRoutinePresets(index) {
                        retrieveSoundUris(
                            soundMediaPlayerService,
                            generalMediaPlayerService,
                            index,
                            context
                        )
                    }
                }else{
                    retrieveSoundUris(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                startSound(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            getRoutinePresets(index) {
                retrieveSoundUris(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }

    fun pauseSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        index: Int
    ) {
        if(soundMediaPlayerService.areMediaPlayersInitialized()) {
            if(soundMediaPlayerService.areMediaPlayersPlaying()) {
                globalViewModel_!!.soundPlaytimeTimer.pause()
                soundMediaPlayerService.pauseMediaPlayers()
                com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(3)
                globalViewModel_!!.isCurrentSoundPlaying = false
                Log.i(TAG, "Pausedz sound")
            }
        }
    }

    private fun getRoutinePresets(index: Int, completed: () -> Unit){
        getRoutinePresetsBasedOnRoutine(
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!
        ) { routinePresets ->
            if(routinePresets.isNotEmpty()) {
                for (routinePreset in routinePresets) {
                    routineActivitySoundUrisMapList[index][routinePreset!!.soundPresetData.id] =
                        mutableListOf()
                    routineActivitySoundUriVolumes[index][routinePreset.soundPresetData.id] =
                        routinePreset.soundPresetData.volumes
                }
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets = routinePresets
                completed()
            }
        }
    }

    private fun getRoutinePresetsBasedOnRoutine(
        userRoutineRelationship: UserRoutineRelationship,
        completed: (userRoutineRelationshipPresetList: MutableList<UserRoutineRelationshipSoundPreset?>) -> Unit
    ) {
        UserRoutineRelationshipSoundPresetBackend.queryUserRoutineRelationshipSoundPresetBasedOnUserRoutineRelationship(userRoutineRelationship) { userRoutineRelationshipPresets ->
            completed(userRoutineRelationshipPresets.toMutableList())
        }
    }

    private fun retrieveSoundUris(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        SoundBackend.querySoundBasedOnId(
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                    [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                .soundPresetData.soundId
        ){
            if(it.isNotEmpty()) {
                SoundBackend.listS3Sounds(
                    it[0]!!.audioKeyS3,
                    it[0]!!.soundOwner.amplifyAuthUserId
                ) { s3List ->
                    s3List.items.forEachIndexed { i, item ->
                        SoundBackend.retrieveAudio(
                            item.key,
                            it[0]!!.soundOwner.amplifyAuthUserId
                        ) { uri ->
                            routineActivitySoundUrisMapList[index][
                                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                                        .soundPresetData.id
                            ]!!.add(uri)
                            if (
                                routineActivitySoundUrisMapList[index][
                                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                                [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                                            .soundPresetData.id
                                ]!!.size ==
                                s3List.items.size
                            ) {
                                startSound(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    index,
                                    context
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
    ) {
        if(
            !routineActivitySoundUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                        .soundPresetData.id
            ].isNullOrEmpty()
        ) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()){
                soundMediaPlayerService.startMediaPlayers()
            }else{
                initializeMediaPlayers(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }

            routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
            soundMediaPlayerService.loopMediaPlayers()
            globalViewModel_!!.soundPlaytimeTimer.start()
            setGlobalPropertiesAfterPlayingSound(index, context)
        }else{
            retrieveSoundUris(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun initializeMediaPlayers(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
    ) {
        updatePreviousUserSoundRelationship {}
        getSoundAssociatedWithUserSoundRelationship()

        soundMediaPlayerService.onDestroy()
        soundMediaPlayerService.setAudioUris(
            routineActivitySoundUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                        .soundPresetData.id
            ]!!
        )
        soundMediaPlayerService.setVolumes(
            routineActivitySoundUriVolumes[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                        .soundPresetData.id
            ]!!
        )
        val intent = Intent()
        intent.action = "PLAY"
        soundMediaPlayerService.onStartCommand(intent, 0, 0)
        resetAll(context, soundMediaPlayerService)
        resetGlobalControlButtons()

        if(globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer == null) {
            startSoundCDT(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun startSoundCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
    ){
        startSoundCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.eachSoundPlayTime.toLong(),
            soundMediaPlayerService
        ){
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                soundMediaPlayerService.onDestroy()
            }
            resetGlobalControlButtons()
            globalViewModel_!!.isCurrentSoundPlaying = false
            globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = null

            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex =
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!! + 1
            if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!! >
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!.indices.last){
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex = 0
            }

            routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE

            playSound(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun startSoundCountDownTimer(
        context: Context,
        time: Long,
        soundMediaPlayerService: SoundMediaPlayerService,
        completed: () -> Unit
    ){
        if(globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer == null){
            globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Sound has been going for $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "sound Timer stopped")
                }
            }
        }
        globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer!!.start()
    }

    private fun getSoundAssociatedWithUserSoundRelationship(){
        SoundBackend.querySoundBasedOnId(
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                    [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                .soundPresetData.soundId
        ){
            if(it.isNotEmpty()){
                updateRecentlyPlayedUserSoundRelationshipWithSound(it[0]!!){}
            }
        }
    }

    private fun setGlobalPropertiesAfterPlayingSound(index: Int, context: Context) {
        globalViewModel_!!.currentSoundPlayingPreset =
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!
            ]!!.soundPresetData

        globalViewModel_!!.currentSoundPlayingSliderPositions.clear()

        globalViewModel_!!.soundSliderVolumes =
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!![
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!
            ]!!.soundPresetData.volumes

        for (volume in globalViewModel_!!.soundSliderVolumes!!) {
            globalViewModel_!!.currentSoundPlayingSliderPositions.add(
                mutableStateOf(volume.toFloat())
            )
        }

        globalViewModel_!!.currentSoundPlayingUris =
            routineActivitySoundUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                            [globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                        .soundPresetData.id
            ]

        globalViewModel_!!.currentSoundPlayingContext = context
        globalViewModel_!!.isCurrentSoundPlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true

        com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(3)
        com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(1)
        com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(0)
    }

    fun updateRecentlyPlayedUserSoundRelationshipWithSound(
        soundData: SoundData,
        completed: (userSoundRelationship: UserSoundRelationship) -> Unit
    ){
        globalViewModel_!!.currentSoundPlaying = soundData
        UserSoundRelationshipBackend.queryUserSoundRelationshipBasedOnUserAndSound(
            globalViewModel_!!.currentUser!!,
            soundData
        ) { userSoundRelationship ->
            if(userSoundRelationship.isNotEmpty()) {
                updateCurrentUserSoundRelationshipUsageTimeStamp(userSoundRelationship[0]!!) {
                    globalViewModel_!!.previouslyPlayedUserSoundRelationship = it
                    completed(it)
                }
            }else{
                UserSoundRelationshipBackend.createUserSoundRelationshipObject(
                    soundData
                ){ newUserSoundRelationship ->
                    updateCurrentUserSoundRelationshipUsageTimeStamp(newUserSoundRelationship) {
                        globalViewModel_!!.previouslyPlayedUserSoundRelationship = it
                        completed(it)
                    }
                }
            }
        }
    }

    fun updateRecentlyPlayedUserSoundRelationshipWithUserSoundRelationship(
        userSoundRelationship: UserSoundRelationship,
        completed: (userSoundRelationship: UserSoundRelationship) -> Unit
    ){
        updateCurrentUserSoundRelationshipUsageTimeStamp(userSoundRelationship) {
            globalViewModel_!!.previouslyPlayedUserSoundRelationship = it
            completed(it)
        }
    }
}