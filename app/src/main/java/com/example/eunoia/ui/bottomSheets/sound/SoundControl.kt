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
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.models.SoundObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.components.AnImageWithColor
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.formatMilliSecond
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.concurrent.fixedRateTimer

private const val TAG = "bottomSheetSoundControl"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetSoundControlPanel(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
): Boolean{
    var showing = false
    if(globalViewModel.currentSoundPlaying != null &&
        globalViewModel.currentSoundPlayingPreset != null &&
        globalViewModel.currentSoundPlayingContext != null) {
        showing = true
        Card(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(115.dp)
                .fillMaxWidth()
                .clickable {
                    if(globalViewModel_!!.navController != null){
                        closeBottomSheet(scope, state)
                        globalViewModel_!!.navController!!.navigate(
                            "${Screen.SoundScreen.screen_route}/sound=${SoundObject.Sound.from(globalViewModel.currentSoundPlaying!!)}"
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
                        text = globalViewModel.currentSoundPlaying!!.displayName,
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
                        globalViewModel.currentSoundPlaying!!,
                        globalViewModel.currentSoundPlayingPreset!!,
                        globalViewModel.currentSoundPlayingContext!!,
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
        globalViewModel_!!.soundScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            globalViewModel_!!.soundScreenBackgroundControlColor1[index].value,
                            globalViewModel_!!.soundScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            globalViewModel_!!.soundScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    globalViewModel_!!.soundScreenBorderControlColors[index].value,
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
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
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
    if(globalViewModel_!!.currentSoundPlaying != null){
        if(globalViewModel_!!.currentSoundPlaying!!.id == soundData.id){
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                resetSliders()
                globalViewModel_!!.soundMeditationBellInterval = 0
                resetBothLocalAndGlobalControlButtonsAfterReset()
                globalViewModel_!!.soundTimerTime = 0
                startCountDownTimer(context, globalViewModel_!!.soundTimerTime, soundMediaPlayerService)
                globalViewModel_!!.isCurrentSoundPlaying = false
                soundMediaPlayerService.onDestroy()
            }
        }
    }
}

private fun resetSliders(){
    globalViewModel_!!.currentSoundPlayingSliderPositions.forEachIndexed { index, sliderPosition ->
        sliderPosition!!.value = globalViewModel_!!.currentSoundPlayingPreset!!.volumes[index].toFloat()
    }
}

private fun startCountDownTimer(
    context: Context,
    time: Long,
    soundMediaPlayerService: SoundMediaPlayerService
){
    if(globalViewModel_!!.soundCountDownTimer != null){
        globalViewModel_!!.soundCountDownTimer!!.cancel()
    }

    globalViewModel_!!.soundCountDownTimer = object : CountDownTimer(time, 100) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i(TAG, "Timer has been going for $millisUntilFinished")
        }
        override fun onFinish() {
            soundMediaPlayerService.onDestroy()
            globalViewModel_!!.soundMeditationBellInterval = 0
            resetBothLocalAndGlobalControlButtons()
            globalViewModel_!!.isCurrentSoundPlaying = false
            Toast.makeText(context, "Sound: timer stopped", Toast.LENGTH_SHORT).show()
            globalViewModel_!!.soundTimerTime = 0
        }
    }

    globalViewModel_!!.soundCountDownTimer!!.start()
}

private fun changeTimerTime(
    index: Int,
    soundData: SoundData,
    context: Context,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    if (globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                globalViewModel_!!.soundTimerTime += 60000L
                Log.i(TAG, "Timer time set to ${formatMilliSecond(globalViewModel_!!.soundTimerTime)} minutes")
                if (globalViewModel_!!.soundTimerTime in 60000L..300000L) {
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
                        globalViewModel_!!.soundTimerTime,
                        soundMediaPlayerService
                    )
                } else {
                    globalViewModel_!!.soundTimerTime = 0L
                    startCountDownTimer(
                        context,
                        globalViewModel_!!.soundTimerTime,
                        soundMediaPlayerService
                    )
                    deActivateGlobalControlButton(index)
                }
                Toast.makeText(
                    context,
                    "Sound: timer set to ${formatMilliSecond(globalViewModel_!!.soundTimerTime)} minutes",
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
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if(globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
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
    globalViewModel_!!.currentSoundPlayingUris!!.clear()
    SoundBackend.listS3Sounds(
        soundData.audioKeyS3,
        soundData.soundOwner.amplifyAuthUserId
    ){ s3List ->
        s3List.items.forEachIndexed { i, item ->
            SoundBackend.retrieveAudio(
                item.key,
                soundData.soundOwner.amplifyAuthUserId
            ) { uri ->
                globalViewModel_!!.currentSoundPlayingUris!!.add(uri)
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
    if(globalViewModel_!!.currentSoundPlayingUris!!.isNotEmpty()){
        if(soundMediaPlayerService.areMediaPlayersInitialized()){
            if(globalViewModel_!!.currentSoundPlaying!!.id == soundData.id){
                soundMediaPlayerService.startMediaPlayers()
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
        deActivateGlobalControlButton(3)
        deActivateGlobalControlButton(1)
        globalViewModel_!!.generalPlaytimeTimer.start()
        globalViewModel_!!.isCurrentSoundPlaying = true
    }
}

private fun initializeMediaPlayers(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    soundData: SoundData
){
    generalMediaPlayerService.onDestroy()
    soundMediaPlayerService.onDestroy()
    soundMediaPlayerService.setAudioUris(globalViewModel_!!.currentSoundPlayingUris!!)
    soundMediaPlayerService.setVolumes(globalViewModel_!!.soundSliderVolumes!!)
    val intent = Intent()
    intent.action = "PLAY"
    soundMediaPlayerService.onStartCommand(intent, 0, 0)

    globalViewModel_!!.soundMeditationBellInterval = 0
    resetGlobalControlButtons()
    globalViewModel_!!.soundTimerTime = 0
    startCountDownTimer(
        context,
        globalViewModel_!!.soundTimerTime,
        soundMediaPlayerService
    )

    createMeditationBellMediaPlayer(context)
}

private fun pauseSoundScreenSounds(
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    if(soundMediaPlayerService.areMediaPlayersInitialized()) {
        if(soundMediaPlayerService.areMediaPlayersPlaying()) {
            globalViewModel_!!.generalPlaytimeTimer.pause()
            soundMediaPlayerService.pauseMediaPlayers()
            activateGlobalControlButton(3)
            globalViewModel_!!.isCurrentSoundPlaying = false
        }
    }
}

private fun increaseSliderLevels(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            globalViewModel_!!.currentSoundPlayingSliderPositions.forEachIndexed{ index, sliderPosition ->
                if(sliderPosition!!.value < 10) {
                    sliderPosition.value++
                    globalViewModel_!!.soundSliderVolumes!![index] = globalViewModel_!!.soundSliderVolumes!![index] + 1
                    soundMediaPlayerService.setVolumes(globalViewModel_!!.soundSliderVolumes!!)
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
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            globalViewModel_!!.currentSoundPlayingSliderPositions.forEachIndexed{ index, sliderPosition ->
                if(sliderPosition!!.value > 0) {
                    sliderPosition.value--
                    globalViewModel_!!.soundSliderVolumes!![index] = globalViewModel_!!.soundSliderVolumes!![index]!! - 1
                    soundMediaPlayerService.setVolumes(globalViewModel_!!.soundSliderVolumes!!)
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
    if (globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            globalViewModel_!!.soundMeditationBellInterval++
            fixedRateTimer(
                "Meditation Bell Timer",
                false,
                0L,
                globalViewModel_!!.soundMeditationBellInterval * 60000L
            ) {
                Log.i(TAG, "${globalViewModel_!!.soundMeditationBellInterval * 60000L}")
                if (globalViewModel_!!.soundMeditationBellInterval == 0) {
                    deActivateGlobalControlButton(index)
                    cancel()
                    Log.i(TAG, "Cancelled meditation bell timer")
                } else {
                    if (globalViewModel_!!.soundMeditationBellInterval <= 5) {
                        activateGlobalControlButton(index)
                        globalViewModel_!!.soundMeditationBellMediaPlayer?.start()
                    } else {
                        deActivateGlobalControlButton(index)
                        globalViewModel_!!.soundMeditationBellInterval = 0
                    }
                }
            }
            if (globalViewModel_!!.soundMeditationBellInterval in 1..5) {
                val minute =
                    if (globalViewModel_!!.soundMeditationBellInterval == 1) "minute" else "minutes"
                Toast.makeText(
                    context,
                    "Sound: meditation bell every ${globalViewModel_!!.soundMeditationBellInterval} $minute",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

fun resetGlobalControlButtons(){
    deActivateGlobalControlButton(0)
    deActivateGlobalControlButton(1)
    deActivateGlobalControlButton(2)
    activateGlobalControlButton(3)
    deActivateGlobalControlButton(4)
    deActivateGlobalControlButton(5)
    deActivateGlobalControlButton(6)
}

fun activateGlobalControlButton(index: Int){
    globalViewModel_!!.soundScreenBorderControlColors[index].value = Black
    globalViewModel_!!.soundScreenBackgroundControlColor1[index].value = SoftPeach
    globalViewModel_!!.soundScreenBackgroundControlColor2[index].value = Solitude
    if(index == 3){
        globalViewModel_!!.soundScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivateGlobalControlButton(index: Int){
    globalViewModel_!!.soundScreenBorderControlColors[index].value = Bizarre
    globalViewModel_!!.soundScreenBackgroundControlColor1[index].value = White
    globalViewModel_!!.soundScreenBackgroundControlColor2[index].value = White
    if(index == 3){
        globalViewModel_!!.soundScreenIcons[index].value = R.drawable.pause_icon
    }
}