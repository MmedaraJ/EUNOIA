package com.example.eunoia.ui.bottomSheets.sound

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amplifyframework.datastore.generated.model.SoundPresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.home.SoundForRoutine.updateRecentlyPlayedUserSoundRelationshipWithSound
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.models.SoundObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.components.AnImageWithColor
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.navigation.soundViewModel
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.formatMilliSecond
import kotlinx.coroutines.CoroutineScope
import kotlin.concurrent.fixedRateTimer

private const val TAG = "bottomSheetSoundControl"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetSoundControlPanel(
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
): Boolean{
    var showing = false
    if(soundViewModel!!.currentSoundPlaying != null &&
        soundViewModel!!.currentSoundPlayingPreset != null &&
        soundViewModel!!.currentSoundPlayingContext != null) {
        showing = true
        Card(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(115.dp)
                .fillMaxWidth()
                .clickable {
                    if(globalViewModel!!.navController != null){
                        closeBottomSheet(scope, state)
                        globalViewModel!!.navController!!.navigate(
                            "${Screen.SoundScreen.screen_route}/sound=${SoundObject.Sound.from(soundViewModel!!.currentSoundPlaying!!)}"
                        )
                    }
                },
            shape = MaterialTheme.shapes.small,
            elevation = 8.dp
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PeriwinkleGray.copy(alpha = 0.3F),
                                Color(0xFFCBCBE8).copy(alpha = 0.4F),
                                Mischka.copy(alpha = 0.6F),
                            ),
                            center = Offset.Unspecified,
                            radius = Float.POSITIVE_INFINITY,
                            tileMode = TileMode.Clamp
                        ),
                    ),
            ) {
                val (
                    title,
                    mode,
                    controls
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        }
                ) {
                    NormalText(
                        text = soundViewModel!!.currentSoundPlaying!!.displayName,
                        color = Black,
                        fontSize = 12,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(mode) {
                            top.linkTo(title.bottom, margin = 8.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        }
                ) {
                    LightText(
                        text = "Mode: play on the background always",
                        color = Black,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(controls) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            end.linkTo(parent.end, margin = 32.dp)
                            start.linkTo(parent.start, margin = 32.dp)
                        }
                ) {
                    BottomSheetSoundControls(
                        soundViewModel!!.currentSoundPlaying!!,
                        soundViewModel!!.currentSoundPlayingPreset!!,
                        soundViewModel!!.currentSoundPlayingContext!!,
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                    )
                }
            }
        }
    }
    return showing
}

@Composable
fun BottomSheetSoundControls(
    sound: SoundData,
    preset: SoundPresetData,
    applicationContext: Context,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    createMeditationBellMediaPlayer(applicationContext)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        soundViewModel!!!!.soundScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            soundViewModel!!!!.soundScreenBackgroundControlColor1[index].value,
                            soundViewModel!!!!.soundScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            soundViewModel!!!!.soundScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    soundViewModel!!!!.soundScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        sound,
                        index,
                        applicationContext,
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                    )
                }
            }
        }
    }
}

private fun activateControls(
    soundData: SoundData,
    index: Int,
    context: Context,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    when(index){
        0 -> loopSounds(soundData, soundMediaPlayerService)
        1 -> resetSounds(
            soundMediaPlayerService,
            soundData,
            context
        )
        2 -> changeTimerTime(
            index,
            soundData,
            context,
            generalMediaPlayerService,
            soundMediaPlayerService,
        )
        3 -> {
            playOrPauseAccordingly(
                soundData,
                soundMediaPlayerService,
                generalMediaPlayerService,
                context
            )
        }
        4 -> increaseSliderLevels(
            soundData,
            soundMediaPlayerService,
        )
        5 -> decreaseSliderLevels(
            soundData,
            soundMediaPlayerService,
        )
        6 -> ringMeditationBell(
            context,
            index,
            soundData
        )
    }
}

private fun loopSounds(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    if(soundViewModel!!!!.currentSoundPlaying != null) {
        if (soundViewModel!!!!.currentSoundPlaying!!.id == soundData.id) {
            if (soundMediaPlayerService.areMediaPlayersInitialized()) {
                soundMediaPlayerService.toggleLoopMediaPlayers()
                if (soundMediaPlayerService.areMediaPlayersLooping()) {
                    activateGlobalControlButton(0)
                } else {
                    deActivateGlobalControlButton(0)
                }
            }
        }
    }
}

private fun resetSounds(
    soundMediaPlayerService: SoundMediaPlayerService,
    soundData: SoundData,
    context: Context
){
    if(soundViewModel!!!!.currentSoundPlaying != null){
        if(soundViewModel!!!!.currentSoundPlaying!!.id == soundData.id){
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                resetSliders()
                soundViewModel!!!!.soundMeditationBellInterval = 0
                resetBothLocalAndGlobalControlButtonsAfterReset()
                soundViewModel!!!!.soundTimerTime = 0
                startCountDownTimer(context, soundViewModel!!!!.soundTimerTime, soundMediaPlayerService)
                soundViewModel!!!!.isCurrentSoundPlaying = false
                soundMediaPlayerService.onDestroy()
            }
        }
    }
}

private fun resetSliders(){
    soundViewModel!!!!.currentSoundPlayingSliderPositions.forEachIndexed { index, sliderPosition ->
        sliderPosition!!.value = soundViewModel!!!!.currentSoundPlayingPreset!!.volumes[index].toFloat()
    }
}

private fun startCountDownTimer(
    context: Context,
    time: Long,
    soundMediaPlayerService: SoundMediaPlayerService
){
    if(soundViewModel!!!!.soundCountDownTimer != null){
        soundViewModel!!!!.soundCountDownTimer!!.cancel()
    }

    soundViewModel!!!!.soundCountDownTimer = object : CountDownTimer(time, 100) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i(TAG, "Timer has been going for $millisUntilFinished")
        }
        override fun onFinish() {
            soundMediaPlayerService.onDestroy()
            soundViewModel!!!!.soundMeditationBellInterval = 0
            resetBothLocalAndGlobalControlButtons()
            soundViewModel!!!!.isCurrentSoundPlaying = false
            Toast.makeText(context, "Sound: timer stopped", Toast.LENGTH_SHORT).show()
            soundViewModel!!!!.soundTimerTime = 0
        }
    }

    soundViewModel!!!!.soundCountDownTimer!!.start()
}

private fun changeTimerTime(
    index: Int,
    soundData: SoundData,
    context: Context,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    if (soundViewModel!!!!.currentSoundPlaying != null) {
        if (soundViewModel!!!!.currentSoundPlaying!!.id == soundData.id) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                soundViewModel!!!!.soundTimerTime += 60000L
                Log.i(TAG, "Timer time set to ${formatMilliSecond(soundViewModel!!!!.soundTimerTime)} minutes")
                if (soundViewModel!!!!.soundTimerTime in 60000L..300000L) {
                    activateGlobalControlButton(index)
                    if (!soundMediaPlayerService.areMediaPlayersPlaying()) {
                        playOrPauseAccordingly(
                            soundData,
                            soundMediaPlayerService,
                            generalMediaPlayerService,
                            context
                        )
                    }
                    startCountDownTimer(
                        context,
                        soundViewModel!!!!.soundTimerTime,
                        soundMediaPlayerService
                    )
                } else {
                    soundViewModel!!!!.soundTimerTime = 0L
                    startCountDownTimer(
                        context,
                        soundViewModel!!!!.soundTimerTime,
                        soundMediaPlayerService
                    )
                    deActivateGlobalControlButton(index)
                }
                Toast.makeText(
                    context,
                    "Sound: timer set to ${formatMilliSecond(soundViewModel!!!!.soundTimerTime)} minutes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

private fun playOrPauseAccordingly(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context
) {
    if(soundViewModel!!!!.currentSoundPlaying != null) {
        if(soundViewModel!!!!.currentSoundPlaying!!.id == soundData.id) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                if (soundMediaPlayerService.areMediaPlayersPlaying()) {
                    pauseSoundScreenSounds(
                        soundMediaPlayerService
                    )
                } else {
                    startSoundScreenSounds(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context,
                        soundData
                    )
                }
            }else {
                startSoundScreenSounds(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context,
                    soundData
                )
            }
        }else{
            retrieveSoundAudio(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
                soundData
            )
        }
    }else{
        retrieveSoundAudio(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context,
            soundData
        )
    }
}

private fun retrieveSoundAudio(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    soundData: SoundData
) {
    soundViewModel!!!!.currentSoundPlayingUris!!.clear()
    SoundBackend.listS3Sounds(
        soundData.audioKeyS3,
        soundData.soundOwner.amplifyAuthUserId
    ){ s3List ->
        s3List.items.forEachIndexed { i, item ->
            SoundBackend.retrieveAudio(
                item.key,
                soundData.soundOwner.amplifyAuthUserId
            ) { uri ->
                soundViewModel!!!!.currentSoundPlayingUris!!.add(uri)
                if(i == s3List.items.size - 1){
                    startSoundScreenSounds(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context,
                        soundData
                    )
                }
            }
        }
    }
}

private fun startSoundScreenSounds(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    soundData: SoundData
) {
    if(soundViewModel!!!!.currentSoundPlayingUris!!.isNotEmpty()){
        if(soundMediaPlayerService.areMediaPlayersInitialized()){
            if(soundViewModel!!!!.currentSoundPlaying!!.id == soundData.id){
                soundMediaPlayerService.startMediaPlayers()
                afterPlayingSound()
            }else{
                initializeMediaPlayers(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context,
                    soundData
                )
            }
        }else{
            initializeMediaPlayers(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
                soundData
            )
        }
    }
}

private fun afterPlayingSound(){
    deActivateGlobalControlButton(3)
    deActivateGlobalControlButton(1)
    globalViewModel!!.soundPlaytimeTimer.start()
    soundViewModel!!.isCurrentSoundPlaying = true
}

private fun updatePreviousAndCurrentSoundRelationship(
    soundData: SoundData,
    completed: () -> Unit
){
    updatePreviousUserSoundRelationship {
        updateRecentlyPlayedUserSoundRelationshipWithSound(soundData) {
            completed()
        }
    }
}

private fun initializeMediaPlayers(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    soundData: SoundData
){
    updatePreviousAndCurrentSoundRelationship(soundData) {
        generalMediaPlayerService.onDestroy()
        soundMediaPlayerService.onDestroy()
        soundMediaPlayerService.setAudioUris(soundViewModel!!!!.currentSoundPlayingUris!!)
        soundMediaPlayerService.setVolumes(soundViewModel!!!!.soundSliderVolumes!!)
        val intent = Intent()
        intent.action = "PLAY"
        soundMediaPlayerService.onStartCommand(intent, 0, 0)
        soundMediaPlayerService.loopMediaPlayers()

        soundViewModel!!!!.soundMeditationBellInterval = 0
        resetGlobalControlButtons()
        soundViewModel!!!!.soundTimerTime = 0
        startCountDownTimer(
            context,
            soundViewModel!!!!.soundTimerTime,
            soundMediaPlayerService
        )

        createMeditationBellMediaPlayer(context)

        afterPlayingSound()
    }
}

private fun pauseSoundScreenSounds(
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    if(soundMediaPlayerService.areMediaPlayersInitialized()) {
        if(soundMediaPlayerService.areMediaPlayersPlaying()) {
            globalViewModel!!.soundPlaytimeTimer.pause()
            soundMediaPlayerService.pauseMediaPlayers()
            activateGlobalControlButton(3)
            soundViewModel!!.isCurrentSoundPlaying = false
        }
    }
}

private fun increaseSliderLevels(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    if(soundViewModel!!.currentSoundPlaying != null) {
        if (soundViewModel!!.currentSoundPlaying!!.id == soundData.id) {
            soundViewModel!!.currentSoundPlayingSliderPositions.forEachIndexed{ index, sliderPosition ->
                if(sliderPosition!!.value < 10) {
                    sliderPosition.value++
                    soundViewModel!!.soundSliderVolumes!![index] = soundViewModel!!!!.soundSliderVolumes!![index] + 1
                    soundMediaPlayerService.setVolumes(soundViewModel!!!!.soundSliderVolumes!!)
                    soundMediaPlayerService.adjustMediaPlayerVolumes()
                }
            }
        }
    }
}

private fun decreaseSliderLevels(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    if(soundViewModel!!!!.currentSoundPlaying != null) {
        if (soundViewModel!!!!.currentSoundPlaying!!.id == soundData.id) {
            soundViewModel!!!!.currentSoundPlayingSliderPositions.forEachIndexed{ index, sliderPosition ->
                if(sliderPosition!!.value > 0) {
                    sliderPosition.value--
                    soundViewModel!!!!.soundSliderVolumes!![index] = soundViewModel!!!!.soundSliderVolumes!![index]!! - 1
                    soundMediaPlayerService.setVolumes(soundViewModel!!!!.soundSliderVolumes!!)
                    soundMediaPlayerService.adjustMediaPlayerVolumes()
                }
            }
        }
    }
}

fun ringMeditationBell(
    context: Context,
    index: Int,
    soundData: SoundData
) {
    if (soundViewModel!!!!.currentSoundPlaying != null) {
        if (soundViewModel!!!!.currentSoundPlaying!!.id == soundData.id) {
            soundViewModel!!!!.soundMeditationBellInterval++
            fixedRateTimer(
                "Meditation Bell Timer",
                false,
                0L,
                soundViewModel!!!!.soundMeditationBellInterval * 60000L
            ) {
                Log.i(TAG, "${soundViewModel!!!!.soundMeditationBellInterval * 60000L}")
                if (soundViewModel!!!!.soundMeditationBellInterval == 0) {
                    deActivateGlobalControlButton(index)
                    cancel()
                    Log.i(TAG, "Cancelled meditation bell timer")
                } else {
                    if (soundViewModel!!!!.soundMeditationBellInterval <= 5) {
                        activateGlobalControlButton(index)
                        soundViewModel!!!!.soundMeditationBellMediaPlayer?.start()
                    } else {
                        deActivateGlobalControlButton(index)
                        soundViewModel!!!!.soundMeditationBellInterval = 0
                    }
                }
            }
            if (soundViewModel!!!!.soundMeditationBellInterval in 1..5) {
                val minute =
                    if (soundViewModel!!!!.soundMeditationBellInterval == 1) "minute" else "minutes"
                Toast.makeText(
                    context,
                    "Sound: meditation bell every ${soundViewModel!!!!.soundMeditationBellInterval} $minute",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

fun resetGlobalControlButtons(){
    activateGlobalControlButton(0)
    deActivateGlobalControlButton(1)
    deActivateGlobalControlButton(2)
    activateGlobalControlButton(3)
    deActivateGlobalControlButton(4)
    deActivateGlobalControlButton(5)
    deActivateGlobalControlButton(6)
}

fun activateGlobalControlButton(index: Int){
    soundViewModel!!!!.soundScreenBorderControlColors[index].value = Black
    soundViewModel!!!!.soundScreenBackgroundControlColor1[index].value = SoftPeach
    soundViewModel!!!!.soundScreenBackgroundControlColor2[index].value = Solitude
    if(index == 3){
        soundViewModel!!!!.soundScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivateGlobalControlButton(index: Int){
    soundViewModel!!!!.soundScreenBorderControlColors[index].value = Bizarre
    soundViewModel!!!!.soundScreenBackgroundControlColor1[index].value = White
    soundViewModel!!!!.soundScreenBackgroundControlColor2[index].value = White
    if(index == 3){
        soundViewModel!!!!.soundScreenIcons[index].value = R.drawable.pause_icon
    }
}