package com.example.eunoia.dashboard.sound

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.home.SoundForRoutine.updateRecentlyPlayedUserSoundRelationshipWithSound
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.formatMilliSecond
import kotlinx.coroutines.CoroutineScope
import java.lang.Math.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "Mixer"
private val meditationBellMediaPlayer = MutableLiveData<MediaPlayer>()
var displayName = ""

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Mixer(
    sound: SoundData,
    context: Context,
    preset: SoundPresetData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    navController: NavController
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Snuff, SoftPeach),
                        center = Offset.Unspecified,
                        radius = Float.POSITIVE_INFINITY,
                        tileMode = TileMode.Clamp
                    ),
                ),
        ) {
            val (
                title,
                sliders,
                controls
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                ConstraintLayout {
                    val (
                        leftBracket,
                        text,
                        rightBracket
                    ) = createRefs()
                    Column(
                        modifier = Modifier
                            .constrainAs(leftBracket){
                                top.linkTo(text.top, margin = 0.dp)
                                end.linkTo(text.start, margin = 0.dp)
                                start.linkTo(parent.start, margin = 32.dp)
                                bottom.linkTo(text.bottom, margin = 0.dp)
                            }
                    ) {
                        NormalText(
                            text = "[",
                            color = Black,
                            fontSize = 18,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                    Column(
                        modifier = Modifier
                            .constrainAs(text){
                                top.linkTo(parent.top, margin = 0.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                                bottom.linkTo(parent.bottom, margin = 0.dp)
                                end.linkTo(parent.end, margin = 16.dp)
                            }
                    ) {
                        NormalText(
                            text = sound.displayName,
                            color = Black,
                            fontSize = 18,
                            xOffset = 0,
                            yOffset = 0
                        )

                        //displayName = standardCentralizedOutlinedTextInput(sound.displayName, SoftPeach, true)
                    }
                    Column(
                        modifier = Modifier
                            .constrainAs(rightBracket){
                                top.linkTo(text.top, margin = 0.dp)
                                start.linkTo(text.end, margin = 0.dp)
                                end.linkTo(parent.end, margin = 32.dp)
                                bottom.linkTo(text.bottom, margin = 0.dp)
                            }
                    ) {
                        NormalText(
                            text = "]",
                            color = Black,
                            fontSize = 18,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(sliders) {
                        top.linkTo(title.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                Sliders(
                    sound,
                    soundMediaPlayerService,
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(controls) {
                        top.linkTo(sliders.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
            ) {
                Controls(
                    sound,
                    preset,
                    context,
                    true,
                    scope,
                    state,
                    soundMediaPlayerService,
                    navController
                )
            }
        }
    }
}

@Composable
fun Sliders(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        val colors = mutableListOf<Color>()
        val labels = mutableListOf<String>()
        for(i in soundData.audioNames.indices){
            labels.add(soundData.audioNames[i])
            colors.add(globalViewModel_!!.mixerColors[i])
        }

        sliderPositions!!.forEachIndexed {index, sliderPosition ->
            if(index < sliderVolumes!!.size) {
                ConstraintLayout(
                    modifier = Modifier.size(32.dp, 200.dp)
                ) {
                    val (
                        mixer,
                        label
                    ) = createRefs()
                    Column(
                        modifier = Modifier
                            .constrainAs(mixer) {
                                top.linkTo(parent.top, margin = 0.dp)
                            }
                            .fillMaxSize()
                            .size(32.dp, 190.dp)
                    ) {
                        val ripple = rememberRipple(
                            bounded = true,
                            color = colors[index]
                        )
                        var tapped by remember { mutableStateOf(false) }
                        val interactionSource = remember { MutableInteractionSource() }
                        if (sliderPosition != null) {
                            Slider(
                                value = sliderPosition.value,
                                valueRange = 0f..10f,
                                onValueChange = {
                                    sliderPositions!![index]!!.value = it
                                    sliderVolumes!![index] = it.toInt()
                                    if (globalViewModel_!!.currentSoundPlaying != null) {
                                        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
                                            globalViewModel_!!.soundSliderVolumes!![index] = it.toInt()
                                            soundMediaPlayerService.setVolumes(sliderVolumes!!)
                                            soundMediaPlayerService.adjustMediaPlayerVolumes()
                                        }
                                    }
                                },
                                steps = 10,
                                onValueChangeFinished = {
                                    if (globalViewModel_!!.currentSoundPlaying != null) {
                                        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
                                            if(
                                                otherPresetsThatOriginatedFromThisSound!!.size > 0 &&
                                                otherPresetsThatOriginatedFromThisSound!!.size == itPresetsSize
                                            ) {
                                                getAssociatedPresetWithSameVolume(
                                                    otherPresetsThatOriginatedFromThisSound!!,
                                                )
                                            }
                                        }
                                    }
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = colors[index],
                                    activeTrackColor = colors[index],
                                    activeTickColor = Color.Transparent,
                                    inactiveTickColor = Color.Transparent,
                                    inactiveTrackColor = colors[index],
                                ),
                                modifier = Modifier
                                    .size(32.dp, 190.dp)
                                    .graphicsLayer {
                                        rotationZ = 270f
                                        transformOrigin = TransformOrigin(0f, 0f)
                                    }
                                    .layout { measurable, constraints ->
                                        val placeable = measurable.measure(
                                            Constraints(
                                                minWidth = constraints.minHeight,
                                                maxWidth = constraints.maxHeight,
                                                minHeight = constraints.minWidth,
                                                maxHeight = constraints.maxHeight,
                                            )
                                        )
                                        layout(placeable.height, placeable.width) {
                                            placeable.place(-placeable.width, 0)
                                        }
                                    }
                                    .indication(interactionSource, LocalIndication.current)
                                    .pointerInput(Unit) {
                                        detectTapGestures(onPress = { offset ->
                                            tapped = true
                                            val press = PressInteraction.Press(offset)
                                            interactionSource.emit(press)
                                            tryAwaitRelease()
                                            interactionSource.emit(PressInteraction.Release(press))
                                            tapped = false
                                        })
                                    }
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .constrainAs(label) {
                                top.linkTo(mixer.bottom, margin = 10.dp)
                                bottom.linkTo(parent.bottom, margin = 0.dp)
                                start.linkTo(parent.start, margin = 0.dp)
                                end.linkTo(parent.end, margin = 0.dp)
                            }
                    ) {
                        NormalText(
                            text = labels[index],
                            color = Black,
                            fontSize = 5,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Controls(
    sound: SoundData,
    preset: SoundPresetData,
    applicationContext: Context,
    showAddIcon: Boolean,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    navController: NavController
){
    createMeditationBellMediaPlayer(applicationContext)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        soundScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            soundScreenBackgroundControlColor1[index].value,
                            soundScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            soundScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    soundScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        sound,
                        index,
                        applicationContext,
                        soundMediaPlayerService,
                        navController
                    )
                }
            }
        }
        if(showAddIcon) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(soundScreenBorderControlColors[7].value),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    globalViewModel_!!.addIcon.value,
                    "save sound icon",
                    White,
                    10.dp,
                    10.dp,
                    0,
                    0
                ) {
                    soundScreenBorderControlColors[7].value = Black
                    globalViewModel_!!.currentSoundToBeAdded = sound
                    globalViewModel_!!.currentPresetToBeAdded = associatedPreset
                    globalViewModel_!!.bottomSheetOpenFor = "addToSoundListOrRoutine"
                    openBottomSheet(scope, state)
                }
            }
        }
    }
}

fun createMeditationBellMediaPlayer(context: Context){
    val resID = context.resources?.getIdentifier("bell", "raw", context.packageName)
    resID?.let {
        if (it == 0) {
            return
        }
        meditationBellMediaPlayer.value = MediaPlayer.create(context, it)
        globalViewModel_!!.soundMeditationBellMediaPlayer = meditationBellMediaPlayer.value
        return
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
                    activateLocalControlButton(0)
                    activateGlobalControlButton(0)
                } else {
                    deActivateLocalControlButton(0)
                    deActivateGlobalControlButton(0)
                }
            }
        }
    }
}

fun resetSounds(
    soundMediaPlayerService: SoundMediaPlayerService,
    soundData: SoundData,
    context: Context
){
    if(globalViewModel_!!.currentSoundPlaying != null){
        if(globalViewModel_!!.currentSoundPlaying!!.id == soundData.id){
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                resetSliders()
                meditationBellInterval.value = 0
                globalViewModel_!!.soundMeditationBellInterval = 0
                resetBothLocalAndGlobalControlButtonsAfterReset()
                timerTime.value = 0
                globalViewModel_!!.soundTimerTime = 0
                startCountDownTimer(context, timerTime.value, soundMediaPlayerService)
                globalViewModel_!!.isCurrentSoundPlaying = false
                soundMediaPlayerService.onDestroy()
            }
        }
    }
}

fun resetAll(applicationContext: Context, soundMediaPlayerService: SoundMediaPlayerService){
    meditationBellInterval.value = 0
    globalViewModel_!!.soundMeditationBellInterval = 0
    resetBothLocalAndGlobalControlButtons()
    globalViewModel_!!.currentSoundPlaying = null
    globalViewModel_!!.isCurrentSoundPlaying = false
    globalViewModel_!!.currentSoundPlayingUris = null
    globalViewModel_!!.currentSoundPlayingPreset = null
    globalViewModel_!!.currentSoundPlayingContext = null
    timerTime.value = 0
    globalViewModel_!!.soundTimerTime = 0
    startCountDownTimer(applicationContext, timerTime.value, soundMediaPlayerService)
}

fun resetSliders(){
    sliderPositions!!.forEachIndexed { index, sliderPosition ->
        sliderPosition!!.value = soundPreset!!.volumes[index].toFloat()
        globalViewModel_!!.currentSoundPlayingSliderPositions[index]!!.value = soundPreset!!.volumes[index].toFloat()
    }
}

fun increaseSliderLevels(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
    navController: NavController
){
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            sliderPositions!!.forEachIndexed{ index, sliderPosition ->
                if(sliderPosition!!.value < 10) {
                    sliderPosition.value++
                    sliderVolumes!![index] = sliderVolumes!![index] + 1
                    globalViewModel_!!.soundSliderVolumes!![index] = sliderVolumes!![index]
                    soundMediaPlayerService.setVolumes(sliderVolumes!!)
                    soundMediaPlayerService.adjustMediaPlayerVolumes()
                }
            }
            if(
                otherPresetsThatOriginatedFromThisSound!!.size > 0 &&
                otherPresetsThatOriginatedFromThisSound!!.size == itPresetsSize
            ) {
                getAssociatedPresetWithSameVolume(
                    otherPresetsThatOriginatedFromThisSound!!,
                )
            }
        }
    }
}

fun decreaseSliderLevels(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
    navController: NavController
){
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            sliderPositions!!.forEachIndexed{ index, sliderPosition ->
                if(sliderPosition!!.value > 0) {
                    sliderPosition.value--
                    sliderVolumes!![index] = sliderVolumes!![index] - 1
                    globalViewModel_!!.soundSliderVolumes!![index] = sliderVolumes!![index]
                    soundMediaPlayerService.setVolumes(sliderVolumes!!)
                    soundMediaPlayerService.adjustMediaPlayerVolumes()
                }
            }
            if(
                otherPresetsThatOriginatedFromThisSound!!.size > 0 &&
                otherPresetsThatOriginatedFromThisSound!!.size == itPresetsSize
            ) {
                getAssociatedPresetWithSameVolume(
                    otherPresetsThatOriginatedFromThisSound!!,
                )
            }
        }
    }
}

fun ringMeditationBell(
    context: Context,
    index: Int,
    soundData: SoundData
){
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            meditationBellInterval.value++
            globalViewModel_!!.soundMeditationBellInterval++
            fixedRateTimer(
                "Meditation Bell Timer",
                false,
                0L,
                meditationBellInterval.value * 60000L
            ) {
                Log.i(TAG, "${meditationBellInterval.value * 60000L}")
                if (meditationBellInterval.value == 0) {
                    deActivateLocalControlButton(index)
                    cancel()
                    Log.i(TAG, "Cancelled meditation bell timer")
                } else {
                    if (meditationBellInterval.value <= 5) {
                        activateLocalControlButton(index)
                        meditationBellMediaPlayer.value?.start()
                        globalViewModel_!!.soundMeditationBellMediaPlayer?.start()
                    } else {
                        deActivateLocalControlButton(index)
                        meditationBellInterval.value = 0
                        globalViewModel_!!.soundMeditationBellInterval = 0
                    }
                }
            }
            if (meditationBellInterval.value in 1..5) {
                val minute = if (meditationBellInterval.value == 1) "minute" else "minutes"
                Toast.makeText(
                    context,
                    "Sound: meditation bell every ${meditationBellInterval.value} $minute",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

fun resetBothLocalAndGlobalControlButtonsAfterReset(){
    activateLocalControlButton(0)
    activateLocalControlButton(1)
    deActivateLocalControlButton(2)
    activateLocalControlButton(3)
    deActivateLocalControlButton(4)
    deActivateLocalControlButton(5)
    deActivateLocalControlButton(6)

    activateGlobalControlButton(0)
    activateGlobalControlButton(1)
    deActivateGlobalControlButton(2)
    activateGlobalControlButton(3)
    deActivateGlobalControlButton(4)
    deActivateGlobalControlButton(5)
    deActivateGlobalControlButton(6)
}

fun resetBothLocalAndGlobalControlButtons(){
    activateLocalControlButton(0)
    deActivateLocalControlButton(1)
    deActivateLocalControlButton(2)
    activateLocalControlButton(3)
    deActivateLocalControlButton(4)
    deActivateLocalControlButton(5)
    deActivateLocalControlButton(6)

    activateGlobalControlButton(0)
    deActivateGlobalControlButton(1)
    deActivateGlobalControlButton(2)
    activateGlobalControlButton(3)
    deActivateGlobalControlButton(4)
    deActivateGlobalControlButton(5)
    deActivateGlobalControlButton(6)
}

fun startCountDownTimer(
    context: Context,
    time: Long,
    soundMediaPlayerService: SoundMediaPlayerService
){
    if(countDownTimer != null){
        countDownTimer!!.cancel()
    }

    if(globalViewModel_!!.soundCountDownTimer != null){
        globalViewModel_!!.soundCountDownTimer!!.cancel()
    }

    countDownTimer = object : CountDownTimer(time, 100) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i(TAG, "Timer has been going for $millisUntilFinished")
        }
        override fun onFinish() {
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                soundMediaPlayerService.onDestroy()
            }
            meditationBellInterval.value = 0
            globalViewModel_!!.soundMeditationBellInterval = 0
            resetBothLocalAndGlobalControlButtons()
            globalViewModel_!!.isCurrentSoundPlaying = false
            Toast.makeText(context, "Sound: timer stopped", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Timer stopped")
            timerTime.value = 0
            globalViewModel_!!.soundTimerTime = 0
        }
    }

    globalViewModel_!!.soundCountDownTimer = countDownTimer

    countDownTimer!!.start()
    globalViewModel_!!.soundCountDownTimer!!.start()
}

fun changeTimerTime(
    index: Int,
    soundData: SoundData,
    context: Context,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    if (globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                timerTime.value += 60000L
                globalViewModel_!!.soundTimerTime += 60000L
                Log.i(TAG, "Timer time set to ${formatMilliSecond(timerTime.value)} minutes")
                if (timerTime.value in 60000L..300000L) {
                    activateLocalControlButton(index)
                    activateGlobalControlButton(index)
                    if (!soundMediaPlayerService.areMediaPlayersPlaying()) {
                        playOrPauseAccordingly(
                            soundData,
                            soundMediaPlayerService,
                            context
                        )
                    }
                    startCountDownTimer(context, timerTime.value, soundMediaPlayerService)
                } else {
                    timerTime.value = 0L
                    globalViewModel_!!.soundTimerTime = 0L
                    startCountDownTimer(context, timerTime.value, soundMediaPlayerService)
                    deActivateLocalControlButton(index)
                    deActivateGlobalControlButton(index)
                }
                Toast.makeText(
                    context,
                    "Sound: timer set to ${timerTime.value}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

private fun activateControls(
    soundData: SoundData,
    index: Int,
    context: Context,
    soundMediaPlayerService: SoundMediaPlayerService,
    navController: NavController
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
                soundMediaPlayerService,
            )
        3 -> {
            playOrPauseAccordingly(
                soundData,
                soundMediaPlayerService,
                context
            )
        }
        4 -> increaseSliderLevels(
                soundData,
                soundMediaPlayerService,
                navController
            )
        5 -> decreaseSliderLevels(
                soundData,
                soundMediaPlayerService,
                navController
            )
        6 -> ringMeditationBell(
                context,
                index,
                soundData
            )
    }
}

fun playOrPauseAccordingly(
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService,
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
                        context,
                        soundData
                    )
                }
            }else {
                startSoundScreenSounds(
                    soundMediaPlayerService,
                    context,
                    soundData
                )
            }
        }else{
            retrieveSoundAudio(
                soundMediaPlayerService,
                context,
                soundData
            )
        }
    }else{
        retrieveSoundAudio(
            soundMediaPlayerService,
            context,
            soundData
        )
    }
}

private fun retrieveSoundAudio(
    soundMediaPlayerService: SoundMediaPlayerService,
    context: Context,
    soundData: SoundData
) {
    soundUris.clear()
    SoundBackend.listS3Sounds(
        soundData.audioKeyS3,
        soundData.soundOwner.amplifyAuthUserId
    ){ s3List ->
        s3List.items.forEachIndexed { i, item ->
            SoundBackend.retrieveAudio(
                item.key,
                soundData.soundOwner.amplifyAuthUserId
            ) { uri ->
                soundUris.add(uri)
                if(soundUris.size == s3List.items.size){
                    Log.i(TAG, "Sound list size is ${soundUris.size}")
                    startSoundScreenSounds(
                        soundMediaPlayerService,
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
    context: Context,
    soundData: SoundData
) {
    if(soundUris.isNotEmpty()){
        if(soundMediaPlayerService.areMediaPlayersInitialized()){
            if(globalViewModel_!!.currentSoundPlaying!!.id == soundData.id){
                soundMediaPlayerService.startMediaPlayers()
            }else{
                initializeMediaPlayers(
                    soundMediaPlayerService,
                    context,
                    soundData
                )
            }
        }else{
            initializeMediaPlayers(
                soundMediaPlayerService,
                context,
                soundData
            )
        }
        deActivateLocalControlButton(3)
        deActivateLocalControlButton(1)
        globalViewModel_!!.soundPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingSound(soundData, context)
    }
}

private fun initializeMediaPlayers(
    soundMediaPlayerService: SoundMediaPlayerService,
    context: Context,
    soundData: SoundData
){
    updatePreviousUserSoundRelationship {}
    updateRecentlyPlayedUserSoundRelationshipWithSound(soundData) {}

    soundMediaPlayerService.onDestroy()
    soundMediaPlayerService.setAudioUris(soundUris)
    soundMediaPlayerService.setVolumes(sliderVolumes!!)
    val intent = Intent()
    intent.action = "PLAY"
    soundMediaPlayerService.onStartCommand(intent, 0, 0)
    soundMediaPlayerService.loopMediaPlayers()

    meditationBellInterval.value = 0
    globalViewModel_!!.soundMeditationBellInterval = 0
    resetBothLocalAndGlobalControlButtons()
    timerTime.value = 0
    globalViewModel_!!.soundTimerTime = 0
    startCountDownTimer(context, timerTime.value, soundMediaPlayerService)

    globalViewModel_!!.currentSoundPlayingPreset = soundPreset
    globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
    for (volume in globalViewModel_!!.currentSoundPlayingPreset!!.volumes) {
        globalViewModel_!!.currentSoundPlayingSliderPositions.add(
            mutableStateOf(volume.toFloat())
        )
    }

    createMeditationBellMediaPlayer(context)
}

private fun setGlobalPropertiesAfterPlayingSound(soundData: SoundData, context: Context) {
    globalViewModel_!!.currentSoundPlaying = soundData
    globalViewModel_!!.currentSoundPlayingPreset = soundPreset
    /*globalViewModel_!!.currentAllOriginalSoundPreset = allOriginalSoundPresets
    globalViewModel_!!.currentAllUserSoundPreset = allUserSoundPresets*/
    globalViewModel_!!.soundSliderVolumes = sliderVolumes
    globalViewModel_!!.currentSoundPlayingUris = soundUris
    globalViewModel_!!.currentSoundPlayingContext = context
    globalViewModel_!!.isCurrentSoundPlaying = true
    deActivateGlobalControlButton(3)
    deActivateGlobalControlButton(1)
}

private fun pauseSoundScreenSounds(
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    if(soundMediaPlayerService.areMediaPlayersInitialized()) {
        if(soundMediaPlayerService.areMediaPlayersPlaying()) {
            globalViewModel_!!.soundPlaytimeTimer.pause()
            soundMediaPlayerService.pauseMediaPlayers()
            activateLocalControlButton(3)
            activateGlobalControlButton(3)
            globalViewModel_!!.isCurrentSoundPlaying = false
        }
    }
}

private fun activateLocalControlButton(index: Int){
    soundScreenBorderControlColors[index].value = Black
    soundScreenBackgroundControlColor1[index].value = SoftPeach
    soundScreenBackgroundControlColor2[index].value = Solitude
    if(index == 3){
        soundScreenIcons[index].value = R.drawable.play_icon
    }
}

private fun deActivateLocalControlButton(index: Int){
    soundScreenBorderControlColors[index].value = Bizarre
    soundScreenBackgroundControlColor1[index].value = White
    soundScreenBackgroundControlColor2[index].value = White
    if(index == 3){
        soundScreenIcons[index].value = R.drawable.pause_icon
    }
}

private fun activateGlobalControlButton(index: Int){
    globalViewModel_!!.soundScreenBorderControlColors[index] = soundScreenBorderControlColors[index]
    globalViewModel_!!.soundScreenBackgroundControlColor1[index] = soundScreenBackgroundControlColor1[index]
    globalViewModel_!!.soundScreenBackgroundControlColor2[index] = soundScreenBackgroundControlColor2[index]
    if(index == 3){
        globalViewModel_!!.soundScreenIcons[index] = soundScreenIcons[index]
    }
}

private fun deActivateGlobalControlButton(index: Int){
    globalViewModel_!!.soundScreenBorderControlColors[index] = soundScreenBorderControlColors[index]
    globalViewModel_!!.soundScreenBackgroundControlColor1[index] = soundScreenBackgroundControlColor1[index]
    globalViewModel_!!.soundScreenBackgroundControlColor2[index] = soundScreenBackgroundControlColor2[index]
    if(index == 3){
        globalViewModel_!!.soundScreenIcons[index] = soundScreenIcons[index]
    }
}

fun Modifier.gradientBackground(colors: List<Color>, angle: Float) = this.then(
    Modifier.drawBehind {
        val angleRad = angle / 180f * PI
        val x = cos(angleRad).toFloat() //Fractional x
        val y = sin(angleRad).toFloat() //Fractional y

        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = min(offset.x.coerceAtLeast(0f), size.width),
            y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
        )

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(size.width, size.height),
                end = exactOffset
            ),
            size = size
        )
    }
)

@Composable
fun ControlPanelManual(showTap: Boolean, lambda: () -> Unit){
    var showTapColumn by rememberSaveable{ mutableStateOf(showTap) }

    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .clickable {
                showTapColumn = !showTapColumn
                lambda()
            }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = BeautyBush,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                short_desc,
                instruction1,
                instruction2,
                tap
            ) = createRefs()

            if(showTapColumn){
                Column(
                    modifier = Modifier
                        .constrainAs(tap) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                        }
                ) {
                    ConstraintLayout(
                    ) {
                        val (
                            tap_text,
                            up_icon
                        ) = createRefs()
                        Column(
                            modifier = Modifier
                                .constrainAs(tap_text) {
                                    top.linkTo(parent.top, margin = 16.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    bottom.linkTo(parent.bottom, margin = 0.dp)
                                }
                        ) {
                            AlignedLightText(
                                text = "Tap to read the manual",
                                color = Black,
                                fontSize = 13,
                                xOffset = 0,
                                yOffset = 0
                            )
                        }
                        Column(
                            modifier = Modifier
                                .constrainAs(up_icon) {
                                    top.linkTo(tap_text.top, margin = 0.dp)
                                    start.linkTo(tap_text.end, margin = 4.dp)
                                    bottom.linkTo(tap_text.bottom, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                        ) {
                            AnImage(
                                R.drawable.increase_levels_icon,
                                "noise control manual",
                                7.0,
                                12.0,
                                0,
                                0,
                                LocalContext.current
                            ) {
                                showTapColumn = !showTapColumn
                                lambda()
                            }
                        }
                    }
                }
            } else{
                Column(
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                        }
                ) {
                    NormalText(
                        text = "[how to generate Noise Control panel]",
                        color = Black,
                        fontSize = 16,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(short_desc) {
                            top.linkTo(title.bottom, margin = 2.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                        }
                ) {
                    ExtraLightText(
                        text = "Noise Control panel allows you to create your own white noise and adjust " +
                                "selected white noise to your taste.",
                        color = Black,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(instruction1) {
                            top.linkTo(short_desc.bottom, margin = 8.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                ) {
                    AlignedLightText(
                        text = "Each slider controls a particular frequency band, from the lowest to the" +
                                " highest frequency. Adjust sliders to taste.",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(instruction2) {
                            top.linkTo(instruction1.bottom, margin = 16.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                ) {
                    AlignedLightText(
                        text = "To mask undesirable noises, focus on bands sharing the same tone as the " +
                                "noise you want to cover. Doing so achieves a higher efficiency, and quieter " +
                                "masking noise levels.",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
        }
    }
}

@Composable
fun Tip(){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = Peach,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                icon,
                info
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "[tip]",
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(title.top, margin = 0.dp)
                        bottom.linkTo(title.bottom, margin = 0.dp)
                        start.linkTo(title.end, margin = 4.dp)
                    }
            ) {
                AnImage(
                    R.drawable.tip_icon,
                    "tip",
                    9.0,
                    11.0,
                    0,
                    0,
                    LocalContext.current
                ) {}
            }
            Column(
                modifier = Modifier
                    .constrainAs(info) {
                        top.linkTo(title.bottom, margin = 2.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Click a comment card to load the user's sound on Noise Control panel",
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}

@Composable
fun CommentsUI(
    allComments: MutableList<CommentData>,
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService
){
    val borders = mutableListOf<MutableState<Boolean>>()
    for(i in allComments.indices){
        borders.add(remember { mutableStateOf(false) })
    }

    allComments.forEachIndexed { index, commentData ->
        var cardModifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .clickable {
                if (globalViewModel_!!.currentSoundPlaying != null) {
                    if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
                        borders.forEach { border ->
                            border.value = false
                        }
                        borders[index].value = !borders[index].value

                        changePreset(
                            commentData.preset,
                            soundMediaPlayerService
                        ) {

                        }
                    }
                }
            }
            .fillMaxWidth()

        if (borders[index].value) {
            cardModifier = cardModifier.then(
                Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Card(
                modifier = cardModifier,
                shape = MaterialTheme.shapes.small,
                backgroundColor = Snuff,
                elevation = 8.dp,
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val (user_feedback) = createRefs()
                    Column(
                        modifier = Modifier
                            .constrainAs(user_feedback) {
                                top.linkTo(parent.top, margin = 0.dp)
                                start.linkTo(parent.start, margin = 0.dp)
                                bottom.linkTo(parent.bottom, margin = 0.dp)
                            }
                    ) {
                        LightText(
                            text = commentData.comment,
                            color = Black,
                            fontSize = 13,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PresetsUI(
    allPresets: MutableSet<SoundPresetData>,
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService
){
    val borders = mutableListOf<MutableState<Boolean>>()
    for(i in allPresets.indices){
        if(i == 0){
            borders.add(remember{ mutableStateOf(true) })
        }else {
            borders.add(remember { mutableStateOf(false) })
        }
    }

    allPresets.forEachIndexed{ index, presetData ->
        var changePreset by rememberSaveable { mutableStateOf(false) }
        var cardModifier = Modifier
            .padding(bottom = 8.dp)
            .wrapContentHeight()
            .clickable {
                if(globalViewModel_!!.currentSoundPlaying != null) {
                    if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
                        borders.forEach { border ->
                            border.value = false
                        }
                        borders[index].value = !borders[index].value
                        changePreset = true
                    }
                }
            }
            .wrapContentWidth()

        if (changePreset) {
            changePreset(
                presetData,
                soundMediaPlayerService
            ) {
                changePreset = false
            }
        }

        if (borders[index].value) {
            cardModifier = cardModifier.then(
                Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
            )
        }

        Card(
            modifier = cardModifier,
            shape = MaterialTheme.shapes.small,
            elevation = 2.dp,
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .background(BeautyBush.copy(alpha = 0.5F))
            ) {
                val (preset_name) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(preset_name) {
                            top.linkTo(parent.top, margin = 8.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                        }
                ) {
                    LightText(
                        text = presetData.key,
                        color = Black,
                        fontSize = 16,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
        }
    }
}

fun changePreset(
    preset: SoundPresetData,
    soundMediaPlayerService: SoundMediaPlayerService,
    completed: () -> Unit
){
    sliderVolumes = preset.volumes
    globalViewModel_!!.soundSliderVolumes = preset.volumes
    defaultVolumes = preset.volumes
    soundPreset = preset
    globalViewModel_!!.currentSoundPlayingPreset = preset
    /*globalViewModel_!!.currentAllOriginalSoundPreset = allOriginalSoundPresets
    globalViewModel_!!.currentAllUserSoundPreset = allUserSoundPresets*/

    soundMediaPlayerService.setVolumes(sliderVolumes!!)
    soundMediaPlayerService.adjustMediaPlayerVolumes()
    resetSliders()

    completed()
}

fun getAssociatedPresetWithSameVolume(
    otherPresetsThatOriginatedFromThisSound: MutableList<SoundPresetData>,
){
    var foundSimilarVolumes by mutableStateOf(false)
    var presetData:  SoundPresetData? = null
    for(preset in otherPresetsThatOriginatedFromThisSound){
        var sameVolume = 0
        for (i in sliderVolumes!!.indices){
            if(i < sliderVolumes!!.size) {
                if (sliderVolumes!![i] == preset.volumes[i]) {
                    sameVolume++
                }
                if (sameVolume == sliderVolumes!!.size) {
                    foundSimilarVolumes = true
                    presetData = preset
                    break
                }
            }
            if(foundSimilarVolumes){
                associatedPreset = presetData
                if(associatedPreset != null){
                    showCommentBox = false
                    showAssociatedSoundWithSameVolume.value = true
                }
                break
            }
        }
    }

    if(!foundSimilarVolumes){
        showAssociatedSoundWithSameVolume.value = false
        associatedPreset = null
    }
}

@Composable
fun AssociatedPresetWithSameVolume(
    soundData: SoundData,
    presetData: SoundPresetData,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    val clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .clickable {}
        .wrapContentWidth()

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }

    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        elevation = 2.dp,
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(BeautyBush.copy(alpha = 0.5F))
        ) {
            val (
                title,
                soundName
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
            ) {
                LightText(
                    text = "This preset already has this volume",
                    color = Black,
                    fontSize = 16,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(soundName) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
            ) {
                NormalText(
                    text = presetData.key,
                    color = Black,
                    fontSize = 16,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Light mode"
)
@Preview(
    showBackground = true,
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PreviewUI() {
    EUNOIATheme {
        //OtherUsersFeedback("geujhfkejkfekfehf"){}
    }
}