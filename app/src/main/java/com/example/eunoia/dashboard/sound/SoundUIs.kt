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
import com.example.eunoia.backend.CommentBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import java.lang.Math.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "Mixer"
var mediaPlayers = mutableListOf<MediaPlayer>()
private val isPlaying = mutableStateOf(false)
private val isLooping = mutableStateOf(false)
private val meditationBellMediaPlayer = MutableLiveData<MediaPlayer>()
var displayName = ""
var openUserAlreadyHasSoundDialogBox by mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Mixer(
    sound: SoundData,
    context: Context,
    preset: PresetData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
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
                        top.linkTo(parent.top, margin = 0.dp)
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
                        displayName = standardCentralizedOutlinedTextInput(sound.displayName, SoftPeach, false)
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
                        top.linkTo(title.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                Sliders(
                    sound,
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    navController
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
                    generalMediaPlayerService,
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
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    navController: NavController
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
                                                childSounds.value.size > 0 &&
                                                childSounds.value.size == itSize
                                            ) {
                                                getAssociatedSoundWithSameVolume(
                                                    childSounds.value,
                                                    navController
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
    preset: PresetData,
    applicationContext: Context,
    showAddIcon: Boolean,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
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
                        generalMediaPlayerService,
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
                    globalViewModel_!!.currentSoundToBeAdded = sound
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
        return
    }
}

fun loopSounds(
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
                soundMediaPlayerService.onDestroy()
                Log.i(TAG, "preset name map size -0 ${soundPresetNameAndVolumesMapData.value!!.volumes.size}")
                resetSliders()
                meditationBellInterval.value = 0
                globalViewModel_!!.soundMeditationBellInterval = 0
                resetBothLocalAndGlobalControlButtonsAfterReset()
                timerTime.value = 0
                globalViewModel_!!.soundTimerTime = 0
                startCountDownTimer(context, timerTime.value, soundMediaPlayerService)
                globalViewModel_!!.isCurrentSoundPlaying = false
            }
        }
    }
}

fun resetAll(applicationContext: Context, soundMediaPlayerService: SoundMediaPlayerService){
    mediaPlayers.forEach { mediaPlayer ->
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
    }
    mediaPlayers.clear()
    isPlaying.value = false
    isLooping.value = false
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
    Log.i(TAG, "0th vol size ${soundPresets!!.presets[0].volumes[0]}")
    sliderPositions!!.forEachIndexed { index, sliderPosition ->
        sliderPosition!!.value = soundPresets!!.presets[0].volumes[index].toFloat()
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
                    sliderVolumes!![index] = sliderVolumes!![index]!! + 1
                    soundMediaPlayerService.setVolumes(sliderVolumes!!)
                    soundMediaPlayerService.adjustMediaPlayerVolumes()
                }
            }
            if(
                childSounds.value.size > 0 &&
                childSounds.value.size == itSize
            ) {
                getAssociatedSoundWithSameVolume(
                    childSounds.value,
                    navController
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
                    sliderVolumes!![index] = sliderVolumes!![index]!! - 1
                    soundMediaPlayerService.setVolumes(sliderVolumes!!)
                    soundMediaPlayerService.adjustMediaPlayerVolumes()
                }
            }
            if(
                childSounds.value.size > 0 &&
                childSounds.value.size == itSize
            ) {
                getAssociatedSoundWithSameVolume(
                    childSounds.value,
                    navController
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
    deActivateLocalControlButton(0)
    activateLocalControlButton(1)
    deActivateLocalControlButton(2)
    activateLocalControlButton(3)
    deActivateLocalControlButton(4)
    deActivateLocalControlButton(5)
    deActivateLocalControlButton(6)

    deActivateGlobalControlButton(0)
    activateGlobalControlButton(1)
    deActivateGlobalControlButton(2)
    activateGlobalControlButton(3)
    deActivateGlobalControlButton(4)
    deActivateGlobalControlButton(5)
    deActivateGlobalControlButton(6)
}

fun resetBothLocalAndGlobalControlButtons(){
    deActivateLocalControlButton(0)
    deActivateLocalControlButton(1)
    deActivateLocalControlButton(2)
    activateLocalControlButton(3)
    deActivateLocalControlButton(4)
    deActivateLocalControlButton(5)
    deActivateLocalControlButton(6)

    deActivateGlobalControlButton(0)
    deActivateGlobalControlButton(1)
    deActivateGlobalControlButton(2)
    activateGlobalControlButton(3)
    deActivateGlobalControlButton(4)
    deActivateGlobalControlButton(5)
    deActivateGlobalControlButton(6)
}

var countDownTimer: CountDownTimer? = null
fun startCountDownTimer(
    context: Context,
    time: Long,
    soundMediaPlayerService: SoundMediaPlayerService
){
    if(countDownTimer != null){
        countDownTimer!!.cancel()
    }

    countDownTimer = object : CountDownTimer(time, 100) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i(TAG, "Timer has been going for $millisUntilFinished")
        }
        override fun onFinish() {
            soundMediaPlayerService.onDestroy()
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

    countDownTimer!!.start()
}

fun changeTimerTime(
    index: Int,
    soundData: SoundData,
    context: Context,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    if (globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                timerTime.value += 60000L
                globalViewModel_!!.soundTimerTime += 60000L
                Log.i(TAG, "Timer time set to ${timerTime.value}")
                if (timerTime.value in 60000L..300000L) {
                    activateLocalControlButton(index)
                    activateGlobalControlButton(index)
                    if (!soundMediaPlayerService.areMediaPlayersPlaying()) {
                        playOrPauseAccordingly(
                            soundData,
                            soundMediaPlayerService,
                            generalMediaPlayerService,
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
    generalMediaPlayerService: GeneralMediaPlayerService,
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
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context
) {
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if(globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            Log.i(TAG, "Testing play after reset 0")
            if(soundMediaPlayerService.areMediaPlayersInitialized()) {
                Log.i(TAG, "Testing play after reset 1")
                if (soundMediaPlayerService.areMediaPlayersPlaying()) {
                    Log.i(TAG, "Testing play after reset 2")
                    pauseSoundScreenSounds(
                        soundMediaPlayerService
                    )
                } else {
                    Log.i(TAG, "Testing play after reset 3")
                    startSoundScreenSounds(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context,
                        soundData
                    )
                }
            }else {
                Log.i(TAG, "Testing play after reset 4")
                startSoundScreenSounds(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context,
                    soundData
                )
            }
        }else{
            Log.i(TAG, "I must receive audio /.")
            retrieveSoundAudio(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
                soundData
            )
        }
    }else{
        Log.i(TAG, "Testing play after reset 5")
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
                if(i == s3List.items.size - 1){
                    Log.i(TAG, "Uri SIZEEE 01 is ${soundUris.size} /.")
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
    Log.i(TAG, "Testing play after reset 6")
    if(soundUris.isNotEmpty()){
        if(soundMediaPlayerService.areMediaPlayersInitialized()){
            if(globalViewModel_!!.currentSoundPlaying!!.id == soundData.id){
                soundMediaPlayerService.startMediaPlayers()
                Log.i(TAG, "Same sounds /.")
            }else{
                Log.i(TAG, "Different sounds /.")
                initializeMediaPlayers(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context,
                    soundData
                )
            }
        }else{
            Log.i(TAG, "Uri SIZEEE 02 is ${soundUris.size} /.")
            initializeMediaPlayers(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
                soundData
            )
        }
        deActivateLocalControlButton(3)
        deActivateLocalControlButton(1)
        setGlobalPropertiesAfterPlayingSound(soundData, context)
    }else {
        Log.i(TAG, "Testing play after reset 7")
    }
}

private fun initializeMediaPlayers(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    soundData: SoundData
){
    Log.i(TAG, "Sound uri size 03 = ${soundUris.size}")
    generalMediaPlayerService.onDestroy()
    soundMediaPlayerService.onDestroy()
    Log.i(TAG, "Sound uri size 04 = ${soundUris.size}")
    soundMediaPlayerService.setAudioUris(soundUris)
    soundMediaPlayerService.setVolumes(sliderVolumes!!)
    val intent = Intent()
    intent.action = "PLAY"
    soundMediaPlayerService.onStartCommand(intent, 0, 0)

    meditationBellInterval.value = 0
    globalViewModel_!!.soundMeditationBellInterval = 0
    resetBothLocalAndGlobalControlButtons()
    timerTime.value = 0
    globalViewModel_!!.soundTimerTime = 0
    startCountDownTimer(context, timerTime.value, soundMediaPlayerService)

    createMeditationBellMediaPlayer(context)
}

private fun setGlobalPropertiesAfterPlayingSound(soundData: SoundData, context: Context) {
    globalViewModel_!!.currentSoundPlaying = soundData
    globalViewModel_!!.currentSoundPlayingPreset = soundPresets
    globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap = soundPresetNameAndVolumesMapData.value
    Log.i(TAG, "0th size 3 ${soundPresetNameAndVolumesMapData.value!!.volumes[0]}")
    globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
    for (volume in globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap!!.volumes) {
        globalViewModel_!!.currentSoundPlayingSliderPositions.add(
            mutableStateOf(volume.toFloat())
        )
    }
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
            soundMediaPlayerService.pauseMediaPlayers()
            activateLocalControlButton(3)
            activateGlobalControlButton(3)
            globalViewModel_!!.isCurrentSoundPlaying = false
        }
    }
}

fun activateLocalControlButton(index: Int){
    soundScreenBorderControlColors[index].value = Black
    soundScreenBackgroundControlColor1[index].value = SoftPeach
    soundScreenBackgroundControlColor2[index].value = Solitude
    if(index == 3){
        soundScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivateLocalControlButton(index: Int){
    soundScreenBorderControlColors[index].value = Bizarre
    soundScreenBackgroundControlColor1[index].value = White
    soundScreenBackgroundControlColor2[index].value = White
    if(index == 3){
        soundScreenIcons[index].value = R.drawable.pause_icon
    }
}

fun activateGlobalControlButton(index: Int){
    globalViewModel_!!.soundScreenBorderControlColors[index].value = soundScreenBorderControlColors[index].value
    globalViewModel_!!.soundScreenBackgroundControlColor1[index].value = soundScreenBackgroundControlColor1[index].value
    globalViewModel_!!.soundScreenBackgroundControlColor2[index].value = soundScreenBackgroundControlColor2[index].value
    if(index == 3){
        globalViewModel_!!.soundScreenIcons[index].value = soundScreenIcons[index].value
    }
}

fun deActivateGlobalControlButton(index: Int){
    globalViewModel_!!.soundScreenBorderControlColors[index].value = soundScreenBorderControlColors[index].value
    globalViewModel_!!.soundScreenBackgroundControlColor1[index].value = soundScreenBackgroundControlColor1[index].value
    globalViewModel_!!.soundScreenBackgroundControlColor2[index].value = soundScreenBackgroundControlColor2[index].value
    if(index == 3){
        globalViewModel_!!.soundScreenIcons[index].value = soundScreenIcons[index].value
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
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    NormalText(
                        text = "[how to generate Noise Control panel]",
                        color = Black,
                        fontSize = 16,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    NormalText(
                        text = "[how to generate Noise Control panel]",
                        color = BeautyBush,
                        fontSize = 16,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(short_desc) {
                        top.linkTo(title.bottom, margin = 2.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    ExtraLightText(
                        text = "Noise Control panel allows you to create your own white noise and adjust " +
                                "selected white noise to your taste.",
                        color = Black,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    ExtraLightText(
                        text = "Noise Control panel allows you to create your own white noise and adjust " +
                                "selected white noise to your taste.",
                        color = BeautyBush,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(instruction1) {
                        top.linkTo(short_desc.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    AlignedLightText(
                        text = "Each slider controls a particular frequency band, from the lowest to the" +
                                " highest frequency. Adjust sliders to taste.",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    AlignedLightText(
                        text = "Each slider controls a particular frequency band, from the lowest to the" +
                                " highest frequency. Adjust sliders to taste.",
                        color = BeautyBush,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(instruction2) {
                        top.linkTo(instruction1.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    AlignedLightText(
                        text = "To mask undesirable noises, focus on bands sharing the same tone as the " +
                                "noise you want to cover. Doing so achieves a higher efficiency, and quieter " +
                                "masking noise levels.",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    AlignedLightText(
                        text = "To mask undesirable noises, focus on bands sharing the same tone as the " +
                                "noise you want to cover. Doing so achieves a higher efficiency, and quieter " +
                                "masking noise levels.",
                        color = BeautyBush,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            if(showTapColumn){
                Column(
                    modifier = Modifier
                        .constrainAs(tap) {
                            top.linkTo(instruction2.bottom, margin = 0.dp)
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
fun GetTheCommentsAssociatedWithTheseSounds(
    sounds: MutableList<SoundData>,
    currentSound: SoundData,
    navController: NavController,
    context: Context,
    soundMediaPlayerService: SoundMediaPlayerService
){
    for(sound in sounds){
        var commentData by remember{ mutableStateOf<CommentData?>(null) }
        getCommentForThisSound(sound){
            commentData = it
        }

        //check if user already has this sound saved and do not display it
        var userAlreadyHasSound = false
        for (userSound in globalViewModel_!!.currentUser!!.sounds) {
            if (
                userSound.soundData.id == sound.id &&
                userSound.userData.id == globalViewModel_!!.currentUser!!.id
            ) {
                userAlreadyHasSound = true
                break
            }
        }

        if(
            !userAlreadyHasSound &&
            commentData != null &&
            sound.id != currentSound.id &&
            commentData!!.commentOwner.id != globalViewModel_!!.currentUser!!.id &&
            sound.approvalStatus.equals(SoundApprovalStatus.APPROVED)
        ) {
            OtherUsersCommentsUI(
                sound,
                commentData!!,
                navController,
                context,
                soundMediaPlayerService
            )
        }
    }
}

fun getCommentForThisSound(sound: SoundData, completed: (commentData: CommentData) -> Unit){
    CommentBackend.queryCommentBasedOnSound(sound){
        completed(it)
    }
}

@Composable
fun OtherUsersCommentsUI(
    sound: SoundData,
    comment: CommentData,
    navController: NavController,
    context: Context,
    soundMediaPlayerService: SoundMediaPlayerService
){
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .clickable {
            clicked = !clicked
            /*globalViewModel_!!.currentSoundPlayingPreset = null
            resetAll(context, soundMediaPlayerService)*/
            navigateToSoundScreen(navController, sound)
            clicked = !clicked
        }
        .fillMaxWidth()

    if(clicked){
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
                        text = comment.comment,
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
fun PresetsUI(
    allPresetNameAndVolumeMapData: List<PresetNameAndVolumesMapData>,
    soundData: SoundData,
    soundMediaPlayerService: SoundMediaPlayerService
){
    val borders = mutableListOf<MutableState<Boolean>>()
    for(i in allPresetNameAndVolumeMapData.indices){
        if(i == 0){
            borders.add(remember{ mutableStateOf(true) })
        }else {
            borders.add(remember { mutableStateOf(false) })
        }
    }

    allPresetNameAndVolumeMapData.forEachIndexed{ index, presetNameAndVolumeMapData ->
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
                presetNameAndVolumeMapData,
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
                        text = presetNameAndVolumeMapData.key,
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
    presetNameAndVolumeMapData: PresetNameAndVolumesMapData,
    soundMediaPlayerService: SoundMediaPlayerService,
    completed: () -> Unit
){
    sliderVolumes = presetNameAndVolumeMapData.volumes
    defaultVolumes = presetNameAndVolumeMapData.volumes
    sliderPositions = mutableListOf()
    globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
    for(volume in sliderVolumes!!){
        if(sliderPositions!!.size < sliderVolumes!!.size) {
            sliderPositions!!.add(mutableStateOf(volume!!.toFloat()))
            globalViewModel_!!.currentSoundPlayingSliderPositions.add(mutableStateOf(volume.toFloat()))
        }
    }
    soundPresetNameAndVolumesMapData.value = presetNameAndVolumeMapData
    globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap = soundPresetNameAndVolumesMapData.value

    soundMediaPlayerService.setVolumes(sliderVolumes!!)
    soundMediaPlayerService.adjustMediaPlayerVolumes()
    resetSliders()

    completed()
}

fun getAssociatedSoundWithSameVolume(
    otherSoundsThatOriginatedFromThisSound: MutableList<SoundData>,
    navController: NavController
){
    var foundSimilarVolumes by mutableStateOf(false)
    var presetMapData:  PresetNameAndVolumesMapData? = null
    for(sound in otherSoundsThatOriginatedFromThisSound){
        //var presets: PresetData? = null
        getSoundPresets(sound){
            for(presetMap in it.presets) {
                var sameVolume = 0
                for (i in sliderVolumes!!.indices){
                    if(i < sliderVolumes!!.size) {
                        if (sliderVolumes!![i] == presetMap.volumes[i]) {
                            sameVolume++
                        }
                        if (sameVolume == sliderVolumes!!.size) {
                            foundSimilarVolumes = true
                            presetMapData = presetMap
                            break
                        }
                    }
                }
                if(foundSimilarVolumes){
                    associatedSound = sound
                    associatedPresetNameAndVolumesMapData = presetMapData
                    if(associatedSound != null && associatedPresetNameAndVolumesMapData != null){
                        showAssociatedSoundWithSameVolume.value = true
                    }
                    break
                }
            }
        }
    }

    if(!foundSimilarVolumes){
        showAssociatedSoundWithSameVolume.value = false
        associatedSound = null
        associatedPresetNameAndVolumesMapData = null
    }
}

@Composable
fun AssociatedSoundWithSameVolume(
    soundData: SoundData,
    presetName: String,
    navController: NavController
){
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .clickable {
            clicked = !clicked
            associatedSound = null
            associatedPresetNameAndVolumesMapData = null
            showAssociatedSoundWithSameVolume.value = false
            navigateToSoundScreen(navController, soundData)
            clicked = !clicked
        }
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
                    text = "This sound has this volume",
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
                    text = "${soundData.displayName} - '$presetName' preset",
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