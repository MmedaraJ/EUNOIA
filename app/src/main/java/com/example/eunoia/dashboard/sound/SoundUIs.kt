package com.example.eunoia.dashboard.sound

import android.content.Context
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.CommentData
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.R
import com.example.eunoia.backend.CommentBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import java.lang.Math.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "Mixer"
private var mediaPlayers = mutableListOf<MediaPlayer>()
private val isPlaying = mutableStateOf(false)
private val isLooping = mutableStateOf(false)
private val meditationBellInterval = mutableStateOf(0)
private val timerTime = mutableStateOf(0L)
private val meditationBellMediaPlayer = MutableLiveData<MediaPlayer>()
private var numCounters = 0
var displayName = ""

@Composable
fun Mixer(
    sound: SoundData,
    context: Context,
    preset: PresetData
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
                        displayName = standardCentralizedOutlinedTextInput(sound.displayName,
                            SoftPeach
                        )
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
                Sliders(sound)
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
                Controls(sound, preset, context, true)
            }
        }
    }
}

@Composable
fun Sliders(sound: SoundData){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        val colors = arrayOf(
            GoldSand,
            JungleMist,
            PeriwinkleGray,
            BeautyBush,
            WaikawaGray,
            Madang,
            SeaPink,
            Twine,
            DoveGray,
            Neptune,
        )
        val labels = arrayOf(
            sound.audioNames[0],
            sound.audioNames[1],
            sound.audioNames[2],
            sound.audioNames[3],
            sound.audioNames[4],
            sound.audioNames[5],
            sound.audioNames[6],
            sound.audioNames[7],
            sound.audioNames[8],
            sound.audioNames[9],
        )
        globalViewModel_!!.currentSoundPlayingSliderPositions.forEachIndexed {index, sliderPosition ->
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
                                //sound.currentVolumes[index] = it.toInt()
                                globalViewModel_!!.currentSoundPlayingSliderPositions[index]!!.value = it
                                adjustMediaPlayerVolumes(mediaPlayers, index)
                            },
                            steps = 10,
                            onValueChangeFinished = { Log.i(TAG, "Value Changed") },
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

@Composable
fun Controls(
    sound: SoundData,
    preset: PresetData,
    applicationContext: Context,
    showAddIcon: Boolean
){
    val uris = remember{mutableListOf<Uri>()}
    createMeditationBellMediaPlayer(applicationContext)
    retrieveAudioUris(uris, sound)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        globalViewModel_!!.icons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            globalViewModel_!!.backgroundControlColor1[index].value,
                            globalViewModel_!!.backgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            globalViewModel_!!.borderControlColors[index].value),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    globalViewModel_!!.borderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    if(uris.size == 10) {
                        activateControls(
                            sound,
                            preset,
                            index,
                            uris.subList(0, 10),
                            applicationContext,
                            mediaPlayers
                        )
                    }
                }
            }
        }
        if(showAddIcon) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(globalViewModel_!!.borderControlColors[7].value),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    globalViewModel_!!.addIcon.value,
                    "icon",
                    White,
                    10.dp,
                    10.dp,
                    0,
                    0
                ) {}
            }
        }
    }
}

fun createMeditationBellMediaPlayer(context: Context){
    meditationBellMediaPlayer.value = MediaPlayer.create(
        context,
        R.raw.bell
    )
}

fun retrieveAudioUris(
    uris: MutableList<Uri>,
    sound: SoundData
){
    if(uris.size == 0) {
        SoundBackend.listEunoiaSounds(sound.audioKeyS3) { result ->
            result.items.forEach { item ->
                SoundBackend.retrieveAudio(item.key) { audioUri ->
                    uris.add(audioUri)
                }
            }
            globalViewModel_!!.currentSoundPlayingUris = uris
        }
    }
}

fun playTenSounds(
    tenSounds: MutableList<Uri>,
    applicationContext: Context,
    mediaPlayers: MutableList<MediaPlayer>,
    index: Int
){
    if(mediaPlayers.size == 0 && !globalViewModel_!!.isCurrentSoundPlaying) {
        tenSounds.forEachIndexed { i, audioUri ->
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(applicationContext, audioUri)
                setVolume(
                    globalViewModel_!!.currentSoundPlayingSliderPositions[i]!!.value/10,
                    globalViewModel_!!.currentSoundPlayingSliderPositions[i]!!.value/10
                )
                Log.i(TAG, "New volume for player $i = ${globalViewModel_!!.currentSoundPlayingSliderPositions[i]!!.value/10}")
                prepare()
                start()
            }
            mediaPlayers.add(mediaPlayer)
        }
    }else{
        mediaPlayers.forEach { mediaPlayer ->
            mediaPlayer.start()
        }
    }
    isPlaying.value = true
    deActivateControlButton(index)
    deActivateControlButton(1)
    globalViewModel_!!.isCurrentSoundPlaying = true
    Toast.makeText(applicationContext, "Sound: playing", Toast.LENGTH_SHORT).show()
}

fun pauseTenSounds(
    applicationContext: Context,
    mediaPlayers: MutableList<MediaPlayer>,
    index: Int
){
    if(isPlaying.value && globalViewModel_!!.isCurrentSoundPlaying) {
        mediaPlayers.forEach { mediaPlayer ->
            mediaPlayer.pause()
        }
        isPlaying.value = false
        activateControlButton(index)
        globalViewModel_!!.isCurrentSoundPlaying = false
        Toast.makeText(applicationContext, "Sound: paused", Toast.LENGTH_SHORT).show()
    }
}

fun loopTenSounds(
    applicationContext: Context,
    mediaPlayers: MutableList<MediaPlayer>,
    index: Int
){
    if(mediaPlayers.size == 10) {
        isLooping.value = !isLooping.value
        mediaPlayers.forEach { mediaPlayer ->
            mediaPlayer.isLooping = isLooping.value
        }
        if(isLooping.value){
            activateControlButton(index)
        }else{
            deActivateControlButton(index)
        }
        val loopingInfo = if(isLooping.value) "Sound: looped" else "Sound: not looped"
        Toast.makeText(applicationContext, loopingInfo, Toast.LENGTH_SHORT).show()
    }
}

fun resetTenSounds(
    applicationContext: Context,
    mediaPlayers: MutableList<MediaPlayer>,
    sound: SoundData,
    preset: PresetData,
    index: Int
){
    if(mediaPlayers.size == 10) {
        mediaPlayers.forEach { mediaPlayer ->
            mediaPlayer.reset()
        }
        mediaPlayers.clear()
        isPlaying.value = false
        globalViewModel_!!.isCurrentSoundPlaying = false
        activateControlButton(index)
        activateControlButton(3)
        resetSliders(sound, preset)
    }
}

fun resetAll(applicationContext: Context){
    mediaPlayers.forEach { mediaPlayer ->
        mediaPlayer.reset()
    }
    mediaPlayers.clear()
    isPlaying.value = false
    isLooping.value = false
    meditationBellInterval.value = 0
    deActivateControlButton(0)
    deActivateControlButton(1)
    deActivateControlButton(2)
    activateControlButton(3)
    deActivateControlButton(4)
    deActivateControlButton(5)
    deActivateControlButton(6)
    globalViewModel_!!.isCurrentSoundPlaying = false
    timerTime.value = 0
    startCountDownTimer(applicationContext, timerTime.value)
}

fun resetSliders(
    sound: SoundData,
    preset: PresetData
){
    globalViewModel_!!.currentSoundPlayingSliderPositions.forEachIndexed { index, mutableState ->
        mutableState!!.value = preset.presets[1].volumes[index].toFloat()
    }
}

fun increaseSliderLevels(
    applicationContext: Context,
    index: Int
){
    globalViewModel_!!.currentSoundPlayingSliderPositions.forEachIndexed{ index, mutableState ->
        if(mutableState!!.value < 10) {
            mutableState.value++
            adjustMediaPlayerVolumes(mediaPlayers, index)
        }
    }
    Toast.makeText(applicationContext, "Sound: levels increased", Toast.LENGTH_SHORT).show()
}

fun decreaseSliderLevels(
    applicationContext: Context,
    index: Int
){
    globalViewModel_!!.currentSoundPlayingSliderPositions.forEachIndexed{ index, mutableState ->
        if(mutableState!!.value > 0) {
            mutableState.value--
            adjustMediaPlayerVolumes(mediaPlayers, index)
        }
    }
    Toast.makeText(applicationContext, "Sound: levels decreased", Toast.LENGTH_SHORT).show()
}

fun ringMeditationBell(
    context: Context,
    index: Int
){
    meditationBellInterval.value++
    fixedRateTimer(
        "Meditation Bell Timer",
        false,
        0L,
        meditationBellInterval.value * 60000L
    ){
        Log.i(TAG, "${meditationBellInterval.value * 60000L}")
        if(meditationBellInterval.value == 0){
            deActivateControlButton(index)
            cancel()
            Log.i(TAG, "Cancelled meditation bell timer")
        }else{
            if(meditationBellInterval.value <= 5) {
                activateControlButton(index)
                meditationBellMediaPlayer.value?.start()
            }else{
                deActivateControlButton(index)
                meditationBellInterval.value = 0
            }
        }
    }
    if(meditationBellInterval.value in 1..5){
        val minute = if(meditationBellInterval.value == 1) "minute" else "minutes"
        Toast.makeText(context, "Sound: meditation bell every ${meditationBellInterval.value} $minute", Toast.LENGTH_SHORT).show()
    }
}

fun startCountDownTimer(
    context: Context,
    time: Long
){
    object : CountDownTimer(time, 10000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i(TAG, "Timer has been going for $millisUntilFinished")
        }
        override fun onFinish() {
            if(numCounters == 1){
                if(timerTime.value > 0) {
                    mediaPlayers.forEach { mediaPlayer ->
                        mediaPlayer.pause()
                    }
                    //mediaPlayers.clear()
                    isPlaying.value = false
                    isLooping.value = false
                    meditationBellInterval.value = 0
                    deActivateControlButton(0)
                    deActivateControlButton(1)
                    deActivateControlButton(2)
                    activateControlButton(3)
                    deActivateControlButton(4)
                    deActivateControlButton(5)
                    deActivateControlButton(6)
                    globalViewModel_!!.isCurrentSoundPlaying = false
                    Toast.makeText(context, "Sound: timer stopped", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Timer stopped")
                    timerTime.value = 0
                }
            }
            numCounters--
        }
    }.start()
}

fun changeTimerTime(
    mediaPlayers: MutableList<MediaPlayer>,
    tenSoundsURIs: MutableList<Uri>,
    applicationContext: Context,
    index: Int
){
    timerTime.value += 60000L
    Log.i(TAG, "Timer time set to ${timerTime.value}")
    if(timerTime.value in 60000L..300000L){
        numCounters++
        activateControlButton(index)
        if(!isPlaying.value) {
            playTenSounds(
                tenSoundsURIs,
                applicationContext,
                mediaPlayers,
                3
            )
        }
    }else{
        timerTime.value = 0L
        deActivateControlButton(index)
    }
    startCountDownTimer(applicationContext, timerTime.value)
    Toast.makeText(applicationContext, "Sound: timer set to ${timerTime.value}", Toast.LENGTH_SHORT).show()
}

fun activateControls(
    sound: SoundData,
    preset: PresetData,
    index: Int,
    tenSoundsURIs: MutableList<Uri>,
    applicationContext: Context,
    mediaPlayers: MutableList<MediaPlayer>){
    when(index){
        0 -> loopTenSounds(
                applicationContext,
                mediaPlayers,
                index
            )
        1 -> resetTenSounds(
                applicationContext,
                mediaPlayers,
                sound,
                preset,
                index
            )
        2 -> changeTimerTime(
                mediaPlayers,
                tenSoundsURIs,
                applicationContext,
                index
            )
        3 -> {
            if(isPlaying.value){
                pauseTenSounds(
                    applicationContext,
                    mediaPlayers,
                    index
                )
            } else {
                playTenSounds(
                    tenSoundsURIs,
                    applicationContext,
                    mediaPlayers,
                    index
                )
                globalViewModel_!!.currentSoundPlaying = sound
                globalViewModel_!!.currentSoundPlayingPreset = preset
                globalViewModel_!!.currentSoundPlayingContext = applicationContext
            }
        }
        4 -> increaseSliderLevels(
                applicationContext,
                index
            )
        5 -> decreaseSliderLevels(
                applicationContext,
                index
            )
        6 -> ringMeditationBell(
                applicationContext,
                index
            )
        7 -> ""
    }
}

fun activateControlButton(index: Int){
    globalViewModel_!!.borderControlColors[index].value = Black
    globalViewModel_!!.backgroundControlColor1[index].value = SoftPeach
    globalViewModel_!!.backgroundControlColor2[index].value = Solitude
    if(index == 3){
        globalViewModel_!!.icons[index].value = R.drawable.play_icon
    }
}

fun deActivateControlButton(index: Int){
    globalViewModel_!!.borderControlColors[index].value = Bizarre
    globalViewModel_!!.backgroundControlColor1[index].value = White
    globalViewModel_!!.backgroundControlColor2[index].value = White
    if(index == 3){
        globalViewModel_!!.icons[index].value = R.drawable.pause_icon
    }
}

fun adjustMediaPlayerVolumes(mediaPlayers: MutableList<MediaPlayer>, index: Int){
    if(mediaPlayers.size>0) {
        Log.i(TAG, "Adjusting volumes")
        mediaPlayers[index].setVolume(
            globalViewModel_!!.currentSoundPlayingSliderPositions[index]!!.value / 10,
            globalViewModel_!!.currentSoundPlayingSliderPositions[index]!!.value / 10
        )
        Log.i(TAG, "New volume for player $index = ${globalViewModel_!!.currentSoundPlayingSliderPositions[index]!!.value / 10}")
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
                                7.dp,
                                12.dp,
                                0,
                                0
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
                    9.dp,
                    11.dp,
                    0,
                    0
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
fun CommentsForSound(
    sounds: MutableList<SoundData>,
    currentSound: SoundData,
    navController: NavController,
    context: Context
){
    for(sound in sounds){
        var soundComment by remember{ mutableStateOf<CommentData?>(null) }
        Log.i(TAG, "Comments for sound -> $sound")
        getSoundComment(sound){
            soundComment = it
        }
        if(
            soundComment != null &&
            sound != currentSound /*&&
            sound.currentOwner != UserObject.signedInUser().value!!.data*/) {
            OtherUsersCommentsUI(sound = sound, soundComment!!, navController, context)
        }
    }
}

fun getSoundComment(sound: SoundData, completed: (commentData: CommentData) -> Unit){
    CommentBackend.queryCommentBasedOnSound(sound){
        completed(it)
    }
}

@Composable
fun OtherUsersCommentsUI(
    sound: SoundData,
    comment: CommentData,
    navController: NavController,
    context: Context
){
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .clickable {
            clicked = !clicked
            resetAll(context)
            Log.i(TAG, "Navigate to ${Screen.SoundScreen.screen_route}/${sound.displayName}")
            navController.navigate("${Screen.SoundScreen.screen_route}/${sound.displayName}")
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