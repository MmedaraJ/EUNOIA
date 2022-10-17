package com.example.eunoia.dashboard.routine.userRoutineRelationshipScreen

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipSoundPreset
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserRoutineRelationshipSoundPresetBackend
import com.example.eunoia.dashboard.home.SoundForRoutine.updateRecentlyPlayedUserSoundRelationshipWithSound
import com.example.eunoia.dashboard.sound.resetAll
import com.example.eunoia.dashboard.sound.updatePreviousUserSoundRelationship
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.sound.resetGlobalControlButtons
import com.example.eunoia.ui.navigation.globalViewModel_

object SoundForUserRoutineRelationship{
    private const val TAG = "SoundForUserRoutineRelationship"

    fun playSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        if(presets != null && presetUris.isNotEmpty()) {
            if(presetUris[presets!![presetsIndex]!!.soundPresetData.id]!!.isEmpty()) {
                if(presets!!.isEmpty()) {
                    getRoutinePresets {
                        retrieveSoundUris(
                            soundMediaPlayerService,
                            generalMediaPlayerService,
                            context
                        )
                    }
                }else{
                    retrieveSoundUris(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context
                    )
                }
            }else{
                startSound(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }else{
            getRoutinePresets {
                retrieveSoundUris(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }
    }

    fun pauseSound(
        soundMediaPlayerService: SoundMediaPlayerService,
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

    private fun getRoutinePresets(completed: () -> Unit){
        getRoutinePresetsBasedOnRoutine(thisUserRoutineRelationship!!) { routinePresets ->
            if(routinePresets.isNotEmpty()) {
                for (routinePreset in routinePresets) {
                    presetUris[routinePreset!!.soundPresetData.id] = mutableListOf()
                    sliderVolumes[routinePreset.soundPresetData.id] = routinePreset.soundPresetData.volumes
                }
                presets = routinePresets
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
        context: Context
    ) {
        SoundBackend.querySoundBasedOnId(presets!![presetsIndex]!!.soundPresetData.soundId){
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
                            presetUris[presets!![presetsIndex]!!.soundPresetData.id]!!.add(uri)
                            if (
                                presetUris[presets!![presetsIndex]!!.soundPresetData.id]!!.size ==
                                s3List.items.size
                            ) {
                                startSound(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
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
        context: Context,
    ) {
        if(
            !presetUris[presets!![presetsIndex]!!.soundPresetData.id].isNullOrEmpty()
        ) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()){
                soundMediaPlayerService.startMediaPlayers()

                afterPlayingSound(
                    soundMediaPlayerService,
                    context
                )
            }else{
                initializeMediaPlayers(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }

        }else{
            retrieveSoundUris(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context
            )
        }
    }

    private fun afterPlayingSound(
        soundMediaPlayerService: SoundMediaPlayerService,
        context: Context
    ){
        playButtonText = PAUSE_ROUTINE
        soundMediaPlayerService.loopMediaPlayers()
        globalViewModel_!!.soundPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingSound(context)
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
        context: Context
    ) {
        updatePreviousAndCurrentSoundRelationship {
            soundMediaPlayerService.onDestroy()
            soundMediaPlayerService.setAudioUris(
                presetUris[presets!![presetsIndex]!!.soundPresetData.id]!!
            )
            soundMediaPlayerService.setVolumes(
                sliderVolumes[presets!![presetsIndex]!!.soundPresetData.id]!!
            )

            val intent = Intent()
            intent.action = "PLAY"
            resetAll(context, soundMediaPlayerService)
            resetGlobalControlButtons()
            soundMediaPlayerService.onStartCommand(intent, 0, 0)

            if (soundCountDownTimer == null) {
                startSoundCDT(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }

            afterPlayingSound(
                soundMediaPlayerService,
                context
            )
        }
    }

    private fun startSoundCDT(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context,
    ){
        startSoundCountDownTimer(
            context,
            thisUserRoutineRelationship!!.eachSoundPlayTime.toLong(),
            soundMediaPlayerService
        ){
            Log.i(TAG, "Sund timer just endedz")
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                soundMediaPlayerService.onDestroy()
            }
            resetGlobalControlButtons()

            globalViewModel_!!.isCurrentSoundPlaying = false
            globalViewModel_!!.currentSoundPlaying = null

            soundCountDownTimer!!.cancel()
            soundCountDownTimer = null
            globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = soundCountDownTimer

            presetsIndex += 1
            if (presetsIndex > presets!!.indices.last) {
                presetsIndex = 0
            }
            globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex = presetsIndex

            playButtonText = START_ROUTINE
            playSound(
                soundMediaPlayerService,
                generalMediaPlayerService,
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
        if(soundCountDownTimer == null){
            soundCountDownTimer = object : CountDownTimer(time, 10000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "Sound has been going for $millisUntilFinished")
                }
                override fun onFinish() {
                    completed()
                    Log.i(TAG, "sound Timer stopped")
                }
            }
        }
        soundCountDownTimer!!.start()
        globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = soundCountDownTimer
    }

    private fun getSoundAssociatedWithUserSoundRelationship(completed: () -> Unit){
        SoundBackend.querySoundBasedOnId(
            presets!![presetsIndex]!!.soundPresetData.soundId
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

    private fun setGlobalPropertiesAfterPlayingSound(context: Context) {
        SoundBackend.querySoundBasedOnId(
            presets!![presetsIndex]!!.soundPresetData.soundId
        ) {
            if (it.isNotEmpty()) {
                globalViewModel_!!.currentSoundPlaying = it[0]
                Log.i(TAG, "From sound from routine, currently playing is ${it[0]}")

                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets = presets
                globalViewModel_!!.currentSoundPlayingPreset = presets!![presetsIndex]!!.soundPresetData

                globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
                globalViewModel_!!.soundSliderVolumes = presets!![presetsIndex]!!.soundPresetData.volumes

                for (volume in globalViewModel_!!.soundSliderVolumes!!) {
                    globalViewModel_!!.currentSoundPlayingSliderPositions.add(
                        mutableStateOf(volume.toFloat())
                    )
                }

                globalViewModel_!!.currentSoundPlayingUris = presetUris[presets!![presetsIndex]!!.soundPresetData.id]

                globalViewModel_!!.currentSoundPlayingContext = context
                globalViewModel_!!.isCurrentSoundPlaying = true
                globalViewModel_!!.isCurrentRoutinePlaying = true

                com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(3)
                com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(1)
                com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(0)
            }
        }
    }
}