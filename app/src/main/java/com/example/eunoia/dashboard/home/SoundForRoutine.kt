package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.*
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserRoutineRelationshipSoundPresetBackend
import com.example.eunoia.backend.UserSoundRelationshipBackend
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.sound.resetGlobalControlButtons
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.navigation.routineViewModel
import com.example.eunoia.ui.navigation.soundViewModel

object SoundForRoutine{
    private const val TAG = "SoundForRoutine"

    fun playSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets != null) {
            if(
                routineActivitySoundUrisMapList[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                            .soundPresetData.id
                ]!!.isEmpty()
            ) {
                if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!.isEmpty()) {
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
                globalViewModel!!.soundPlaytimeTimer.pause()
                soundMediaPlayerService.pauseMediaPlayers()
                com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(3)
                soundViewModel!!.isCurrentSoundPlaying = false
                Log.i(TAG, "Pausedz sound")
            }
        }
    }

    private fun getRoutinePresets(index: Int, completed: () -> Unit){
        getRoutinePresetsBasedOnRoutine(
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!
        ) { routinePresets ->
            if(routinePresets.isNotEmpty()) {
                for (routinePreset in routinePresets) {
                    routineActivitySoundUrisMapList[index][routinePreset!!.soundPresetData.id] =
                        mutableListOf()
                    routineActivitySoundUriVolumes[index][routinePreset.soundPresetData.id] =
                        routinePreset.soundPresetData.volumes
                }
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets = routinePresets
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
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                    [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
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
                                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                                        .soundPresetData.id
                            ]!!.add(uri)
                            if (
                                routineActivitySoundUrisMapList[index][
                                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
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
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                            [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                        .soundPresetData.id
            ].isNullOrEmpty()
        ) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()){
                soundMediaPlayerService.startMediaPlayers()

                afterPlayingSound(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }else{
                initializeMediaPlayers(
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
    }

    private fun afterPlayingSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ){
        routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
        //soundMediaPlayerService.loopMediaPlayers()
        globalViewModel!!.soundPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingSound(index, context)

        if (routineViewModel!!.currentRoutinePlayingSoundCountDownTimer == null) {
            startSoundCDT(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }

    private fun updatePreviousAndCurrentSoundRelationship(completed: () -> Unit){
        updatePreviousUserSoundRelationship {
            getSoundAssociatedWithUserSoundRelationship{
                completed()
            }
        }
    }

    private fun initializeMediaPlayers(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        updatePreviousAndCurrentSoundRelationship {
            resetAll(context, soundMediaPlayerService)
            resetGlobalControlButtons()

            soundMediaPlayerService.onDestroy()
            soundMediaPlayerService.setAudioUris(
                routineActivitySoundUrisMapList[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                            .soundPresetData.id
                ]!!
            )
            soundMediaPlayerService.setVolumes(
                routineActivitySoundUriVolumes[index][
                        routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                            .soundPresetData.id
                ]!!
            )
            val intent = Intent()
            intent.action = "PLAY"
            soundMediaPlayerService.onStartCommand(intent, 0, 0)

            afterPlayingSound(
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
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!.eachSoundPlayTime.toLong(),
            soundMediaPlayerService
        ){
            Log.i(TAG, "Sund timer just endedz")
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                soundMediaPlayerService.onDestroy()
            }
            resetGlobalControlButtons()
            soundViewModel!!.isCurrentSoundPlaying = false
            routineViewModel!!.currentRoutinePlayingSoundCountDownTimer = null

            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex =
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!! + 1
            if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!! >
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!.indices.last){
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex = 0
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
        if(routineViewModel!!.currentRoutinePlayingSoundCountDownTimer == null){
            routineViewModel!!.currentRoutinePlayingSoundCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Sound has been going for $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "sound Timer stopped")
                }
            }
        }
        routineViewModel!!.currentRoutinePlayingSoundCountDownTimer!!.start()
    }

    private fun getSoundAssociatedWithUserSoundRelationship(completed: () -> Unit){
        SoundBackend.querySoundBasedOnId(
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                    [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                .soundPresetData.soundId
        ){
            if(it.isNotEmpty()){
                updateRecentlyPlayedUserSoundRelationshipWithSound(it[0]!!){
                    completed()
                }
            }else{
                completed()
            }
        }
    }

    private fun setGlobalPropertiesAfterPlayingSound(index: Int, context: Context) {
        SoundBackend.querySoundBasedOnId(
            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                    [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                .soundPresetData.soundId
        ) {
            if (it.isNotEmpty()) {
                soundViewModel!!.currentSoundPlaying = it[0]
                Log.i(TAG, "From sound from routine, currently playing is ${it[0]}")

                soundViewModel!!.currentSoundPlayingPreset =
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!![
                            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!
                    ]!!.soundPresetData

                soundViewModel!!.currentSoundPlayingSliderPositions.clear()

                soundViewModel!!.soundSliderVolumes =
                    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!![
                            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!
                    ]!!.soundPresetData.volumes

                for (volume in soundViewModel!!.soundSliderVolumes!!) {
                    soundViewModel!!.currentSoundPlayingSliderPositions.add(
                        mutableStateOf(volume.toFloat())
                    )
                }

                soundViewModel!!.currentSoundPlayingUris =
                    routineActivitySoundUrisMapList[index][
                            routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
                                    [routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!]!!
                                .soundPresetData.id
                    ]

                soundViewModel!!.currentSoundPlayingContext = context
                soundViewModel!!.isCurrentSoundPlaying = true
                routineViewModel!!.isCurrentRoutinePlaying = true

                com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(3)
                com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(1)
                com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(0)
            }
        }
    }

    fun updateRecentlyPlayedUserSoundRelationshipWithSound(
        soundData: SoundData,
        completed: (userSoundRelationship: UserSoundRelationship) -> Unit
    ){
        soundViewModel!!.currentSoundPlaying = soundData
        UserSoundRelationshipBackend.queryUserSoundRelationshipBasedOnUserAndSound(
            globalViewModel!!.currentUser!!,
            soundData
        ) { userSoundRelationship ->
            if(userSoundRelationship.isNotEmpty()) {
                updateCurrentUserSoundRelationshipUsageTimeStamp(userSoundRelationship[0]!!) {
                    soundViewModel!!.previouslyPlayedUserSoundRelationship = it
                    completed(it)
                }
            }else{
                UserSoundRelationshipBackend.createUserSoundRelationshipObject(
                    soundData
                ){ newUserSoundRelationship ->
                    updateCurrentUserSoundRelationshipUsageTimeStamp(newUserSoundRelationship) {
                        soundViewModel!!.previouslyPlayedUserSoundRelationship = it
                        completed(it)
                    }
                }
            }
        }
    }

    //@Composable
    fun updateRecentlyPlayedUserSoundRelationshipWithUserSoundRelationship(
        userSoundRelationship: UserSoundRelationship,
        completed: (userSoundRelationship: UserSoundRelationship) -> Unit
    ){
        updateCurrentUserSoundRelationshipUsageTimeStamp(userSoundRelationship) {
            soundViewModel!!.previouslyPlayedUserSoundRelationship = it
            completed(it)
        }
    }
}