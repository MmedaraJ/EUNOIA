package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.RoutineSoundPreset
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.backend.RoutineSoundPresetBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.sound.resetAll
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.sound.resetGlobalControlButtons
import com.example.eunoia.ui.navigation.globalViewModel_

object SoundForRoutine{
    private const val TAG = "SoundForRoutine"
    var playingSoundNow: SoundData? = null

    fun playSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        if(globalViewModel_!!.currentRoutinePlayingRoutinePresets != null) {
            if(
                routineActivitySoundUrisMapList[index][
                        globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                                [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                            .soundPresetData.id
                ]!!.isEmpty()
            ) {
                if(globalViewModel_!!.currentRoutinePlayingRoutinePresets!!.isEmpty()) {
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
                globalViewModel_!!.generalPlaytimeTimer.pause()
                soundMediaPlayerService.pauseMediaPlayers()
                com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(3)
                globalViewModel_!!.isCurrentSoundPlaying = false
                Log.i(TAG, "Pausedz sound")
            }
        }
    }

    private fun getRoutinePresets(index: Int, completed: () -> Unit){
        getRoutinePresetsBasedOnRoutine(
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData
        ) { routinePresets ->
            if(routinePresets.isNotEmpty()) {
                for (routinePreset in routinePresets) {
                    routineActivitySoundUrisMapList[index][routinePreset!!.soundPresetData.id] =
                        mutableListOf()
                    routineActivitySoundUriVolumes[index][routinePreset.soundPresetData.id] =
                        routinePreset.soundPresetData.volumes
                }
                globalViewModel_!!.currentRoutinePlayingRoutinePresets = routinePresets
                completed()
            }
        }
    }

    fun getRoutinePresetsBasedOnRoutine(
        routineData: RoutineData,
        completed: (routinePresetList: MutableList<RoutineSoundPreset?>) -> Unit
    ) {
        RoutineSoundPresetBackend.queryRoutineSoundPresetBasedOnRoutine(routineData) { routinePresets ->
            completed(routinePresets.toMutableList())
        }
    }

    private fun retrieveSoundUris(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context
    ) {
        SoundBackend.querySoundBasedOnId(
            globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                    [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                .soundPresetData.soundId
        ){
            if(it.isNotEmpty()) {
                playingSoundNow = it[0]
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
                                    globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                                            [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                                        .soundPresetData.id
                            ]!!.add(uri)
                            if (i == s3List.items.indices.last) {
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
                    globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
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
            //globalViewModel_!!.previouslyPlayedUserSoundRelationship = globalViewModel_!!.currentUsersSoundRelationships!![index]
            //globalViewModel_!!.generalPlaytimeTimer.start()
            setGlobalPropertiesAfterPlayingSound(index, context)
        }
    }

    private fun initializeMediaPlayers(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
    ) {
        /*updatePreviousUserSoundRelationship {
            updateCurrentUserSoundRelationshipUsageTimeStamp(index) {}
            globalViewModel_!!.previouslyPlayedUserSoundRelationship = null
        }*/
        soundMediaPlayerService.onDestroy()
        soundMediaPlayerService.setAudioUris(
            routineActivitySoundUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                        .soundPresetData.id
            ]!!
        )
        soundMediaPlayerService.setVolumes(
            routineActivitySoundUriVolumes[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                        .soundPresetData.id
            ]!!
        )
        val intent = Intent()
        intent.action = "PLAY"
        soundMediaPlayerService.onStartCommand(intent, 0, 0)
        resetAll(context, soundMediaPlayerService)
        resetGlobalControlButtons()
        startSoundCDT(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }

    private fun startSoundCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        index: Int,
        context: Context,
    ){
        startSoundCountDownTimer(
            context,
            globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.eachSoundPlayTime.toLong(),
            soundMediaPlayerService
        ){
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                soundMediaPlayerService.onDestroy()
            }
            resetGlobalControlButtons()
            globalViewModel_!!.isCurrentSoundPlaying = false
            globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = null

            globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex = globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!! + 1
            if(globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!! > globalViewModel_!!.currentRoutinePlayingRoutinePresets!!.indices.last){
                globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex = 0
            }
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

    private fun setGlobalPropertiesAfterPlayingSound(index: Int, context: Context) {
        globalViewModel_!!.currentSoundPlaying = playingSoundNow

        globalViewModel_!!.currentSoundPlayingPreset =
            globalViewModel_!!.currentRoutinePlayingRoutinePresets!![
                    globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!
            ]!!.soundPresetData

        globalViewModel_!!.currentSoundPlayingSliderPositions.clear()

        globalViewModel_!!.soundSliderVolumes =
            globalViewModel_!!.currentRoutinePlayingRoutinePresets!![
                    globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!
            ]!!.soundPresetData.volumes

        for (volume in globalViewModel_!!.soundSliderVolumes!!) {
            globalViewModel_!!.currentSoundPlayingSliderPositions.add(
                mutableStateOf(volume.toFloat())
            )
        }

        globalViewModel_!!.currentSoundPlayingUris =
            routineActivitySoundUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                        .soundPresetData.id
            ]

        globalViewModel_!!.currentSoundPlayingContext = context
        globalViewModel_!!.isCurrentSoundPlaying = true
        globalViewModel_!!.isCurrentRoutinePlaying = true
        com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(3)
        com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(1)
        com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(0)
    }
}