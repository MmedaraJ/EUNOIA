package com.example.eunoia.create.createSound

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ResetTv
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.example.eunoia.R
import com.example.eunoia.create.createSoundViewModel
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.dashboard.sound.mediaPlayers
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import java.io.File
import java.lang.IllegalStateException
import kotlin.concurrent.fixedRateTimer

private val isPlaying = mutableStateOf(false)
private val isLooping = mutableStateOf(false)
private val meditationBellInterval = mutableStateOf(0)
private val timerTime = mutableStateOf(0L)
private val meditationBellMediaPlayer = MutableLiveData<MediaPlayer>()
private var numCounters = 0
var displayName = ""

@Composable
fun SoundUploader(
    index: Int,
){
    val context = LocalContext.current
    var fileName by rememberSaveable{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth(1F)
            .wrapContentHeight()
    ){
        fileName = customizedOutlinedTextInput(
            width = 0,
            height = 55,
            color = SoftPeach,
            focusedBorderColor = BeautyBush,
            inputFontSize = 16,
            placeholder = "Name of this audio",
            placeholderFontSize = 16,
            offset = 0
        )
        fileNames[index]!!.value = fileName
        Spacer(modifier = Modifier.height(6.dp))
        SwipeToResetSoundUI(index, context){
            selectedIndex = it
            UserDashboardActivity.getInstanceActivity().selectAudio()
        }
    }
}

@Composable
fun SavePreset(){
    val context = LocalContext.current
    var fileName by rememberSaveable{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth(1F)
            .wrapContentHeight()
    ){
        fileName = customizedOutlinedTextInput(
            width = 0,
            height = 55,
            color = SoftPeach,
            focusedBorderColor = BeautyBush,
            inputFontSize = 16,
            placeholder = "Name of this audio",
            placeholderFontSize = 16,
            offset = 0
        )
        presetName = fileName
        nameErrorMessage = if(presetName.isEmpty()) {
            "Name this preset"
        }else{
            ""
        }
        if(!nameDoesNotAlreadyExist()){
            nameErrorMessage = "Name already exists"
        }
    }
}

fun nameDoesNotAlreadyExist(): Boolean{
    for(map in soundPresetNameAndVolumesMap){
        if(map!!.value.key == presetName){
            return false
        }
    }
    return true
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToResetSoundUI(
    index: Int,
    context: Context,
    fileUploaded: (index: Int) -> Unit
){
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmStateChange = {
            Log.i(TAG, "Confirm state change ==>> $it")
            if(it == DismissValue.DismissedToStart){
                resetSoundUploadUI(index)
            }
            true
        }
    )
    if (dismissState.currentValue != DismissValue.Default) {
        LaunchedEffect(Unit) {
            dismissState.reset()
        }
    }
    SwipeToDismiss(
        state = dismissState,
        /***  create dismiss alert Background */
        background = {
            val color by animateColorAsState(
                targetValue = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> Color.Transparent
                    DismissDirection.EndToStart -> Color.Red
                    null -> Color.Transparent
                }
            )
            val scale by animateFloatAsState(
                targetValue = if(dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f
            )
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            if (direction == DismissDirection.EndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ResetTv,
                            contentDescription = "reset",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .scale(scale)
                        )
                        NormalText(
                            text = "Reset",
                            color = White,
                            fontSize = 13,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
        },
        /**** Dismiss Content */
        dismissContent = {
            CustomizableButton(
                text = uploadedFiles[index]!!.value.name,
                height = 55,
                fontSize = 16,
                textColor = Black,
                backgroundColor = fileColors[index]!!.value,
                corner = 10,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "light",
                maxWidthFraction = 1F
            ) {
                if(uploadedFiles[index]!!.value.name == "Choose a file"){
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fileUploaded(index)
                    } else {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            3
                        )
                    }
                }else{
                    if(fileMediaPlayers[index]!!.value.isPlaying){
                        fileMediaPlayers[index]!!.value.stop()
                        fileMediaPlayers[index]!!.value.reset()
                    }else {
                        clearMainMediaPlayers()
                        fileMediaPlayers[index]!!.value.apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            setDataSource(context, fileUris[index]!!.value)
                            setVolume(1f, 1f)
                            prepare()
                            start()
                        }
                    }
                }
            }
        },
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
        },
        /*** Set Direction to dismiss */
        directions = setOf(DismissDirection.EndToStart),
    )
}

fun resetSoundUploadUI(index: Int) {
    Log.i(TAG, "About to reset UI $index")
    uploadedFiles[index]!!.value = File("Choose a file")
    fileColors[index]!!.value = WePeep
    fileUris[index]!!.value = "".toUri()
    if(fileMediaPlayers[index]!!.value.isPlaying) {
        fileMediaPlayers[index]!!.value.stop()
    }
    fileMediaPlayers[index]!!.value.reset()
    fileMediaPlayers[index]!!.value.release()
    val mediaPlayer = MediaPlayer()
    fileMediaPlayers[index]!!.value = mediaPlayer
}

@Composable
fun CreatePresetMixer(context: Context, soundMediaPlayerService: SoundMediaPlayerService){
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
                        standardCentralizedOutlinedTextInput(soundName, SoftPeach, true)
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
                Sliders()
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
                Controls(context, soundMediaPlayerService)
            }
        }
    }
}

@Composable
fun Sliders(){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        sliderVolumes.forEachIndexed { index, volume ->
            Log.i(TAG, "Uri index ==>>> $index")
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
                        color = createSoundViewModel!!.mixerColors[index]
                    )
                    var tapped by remember { mutableStateOf(false) }
                    val interactionSource = remember { MutableInteractionSource() }
                    if (volume != null) {
                        Slider(
                            value = volume.value.toFloat(),
                            valueRange = 0f..10f,
                            onValueChange = {
                                volume.value = it.toInt()
                                adjustMediaPlayerVolumes(index, fileMediaPlayers[index]!!)
                            },
                            steps = 10,
                            onValueChangeFinished = { Log.i(TAG, "Value Changed") },
                            colors = SliderDefaults.colors(
                                thumbColor = createSoundViewModel!!.mixerColors[index],
                                activeTrackColor = createSoundViewModel!!.mixerColors[index],
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent,
                                inactiveTrackColor = createSoundViewModel!!.mixerColors[index],
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
                    val name = if(fileNames[index]!!.value.length > 4)
                                    fileNames[index]!!.value.substring(0, 4)
                                else
                                    fileNames[index]!!.value
                    NormalText(
                        text = name,
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
fun Controls(applicationContext: Context, soundMediaPlayerService: SoundMediaPlayerService){
    createMeditationBellMediaPlayer(applicationContext)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        createSoundViewModel!!.icons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            createSoundViewModel!!.backgroundControlColor1[index].value,
                            createSoundViewModel!!.backgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            createSoundViewModel!!.borderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    createSoundViewModel!!.borderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        index,
                        applicationContext,
                        soundMediaPlayerService
                    )
                }
            }
        }
    }
}

fun createMeditationBellMediaPlayer(context: Context){
    var resID = context.resources?.getIdentifier("bell", "raw", context.packageName)

    resID?.let {
        if (it == 0) {
            val errorString = "Error occured."
            return
        }
        meditationBellMediaPlayer.value = MediaPlayer.create(context, it)
        return
    }
}

fun clearMainMediaPlayers(){
    if(mediaPlayers.isNotEmpty()) {
        mediaPlayers.forEach { mediaPlayer ->
            if(mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            mediaPlayer.release()
        }
        mediaPlayers.clear()
    }
}

fun playSoundsPreset(
    applicationContext: Context,
    index: Int,
    soundMediaPlayerService: SoundMediaPlayerService
){
    globalViewModel_!!.bottomSheetOpenFor = ""
    clearMainMediaPlayers()
    com.example.eunoia.dashboard.sound.resetAll(applicationContext, soundMediaPlayerService)
    fileMediaPlayers.forEachIndexed { i,  media ->
        try {
            media!!.value.start()
        }catch(e: IllegalStateException){
            Log.i(TAG, "$e")
        }
    }
    isPlaying.value = true
    deActivateControlButton(index)
    deActivateControlButton(1)
    runOnUiThread {
        Toast.makeText(applicationContext, "Sound: playing", Toast.LENGTH_SHORT).show()
    }
}

fun pauseSounds(applicationContext: Context, index: Int){
    fileMediaPlayers.forEachIndexed { i, media ->
        if(media!!.value.isPlaying) {
            try {
                media.value.pause()
            }catch(e: IllegalStateException){
                Log.i(TAG, "$e")
            }
        }
    }
    isPlaying.value = false
    activateControlButton(index)
    runOnUiThread {
        Toast.makeText(applicationContext, "Sound: paused", Toast.LENGTH_SHORT).show()
    }
}

fun loopSounds(
    applicationContext: Context,
    index: Int
){
    isLooping.value = !isLooping.value
    fileMediaPlayers.forEach { mediaPlayer ->
        mediaPlayer!!.value.isLooping = isLooping.value
        if(
            mediaPlayer.value.currentPosition == mediaPlayer.value.duration ||
            mediaPlayer.value.currentPosition == 0
        ){
            mediaPlayer.value.seekTo(0)
            mediaPlayer.value.start()
        }
    }
    if(isLooping.value){
        activateControlButton(index)
    }else{
        deActivateControlButton(index)
    }
    val loopingInfo = if(isLooping.value) "Sound: looped" else "Sound: not looped"
    Toast.makeText(applicationContext, loopingInfo, Toast.LENGTH_SHORT).show()
}

fun resetSounds(){
    fileMediaPlayers.forEachIndexed { i, media ->
        media!!.value.pause()
    }
    isPlaying.value = false
    activateControlButton(1)
    activateControlButton(3)
    resetSliders()
}

fun resetAll(applicationContext: Context){
    fileMediaPlayers.forEach { media ->
        media!!.value.stop()
        media.value.reset()
        media.value.release()
    }
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
    timerTime.value = 0
    startCountDownTimer(applicationContext, timerTime.value)
}

fun resetSliders(){
    sliderVolumes.forEach { volume ->
        volume!!.value = 5
    }
}

fun increaseSliderLevelsPreset(){
    Log.i(TAG, "Slider volumes size ==>> ${sliderVolumes.size}")
    sliderVolumes.forEachIndexed{ index, volume ->
        if(volume!!.value < 10) {
            volume.value++
            adjustMediaPlayerVolumes(index, fileMediaPlayers[index]!!)
        }
    }
}

fun decreaseSliderLevelsPreset(){
    sliderVolumes.forEachIndexed{ index, volume ->
        if(volume!!.value > 0) {
            volume.value--
            adjustMediaPlayerVolumes(index, fileMediaPlayers[index]!!)
        }
    }
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
                    fileMediaPlayers.forEach { mediaPlayer ->
                        mediaPlayer!!.value.pause()
                    }
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
    applicationContext: Context,
    index: Int,
    soundMediaPlayerService: SoundMediaPlayerService
){
    timerTime.value += 60000L
    Log.i(TAG, "Timer time set to ${timerTime.value}")
    if(timerTime.value in 60000L..300000L){
        numCounters++
        activateControlButton(index)
        if(!isPlaying.value) {
            playSoundsPreset(
                applicationContext,
                3,
                soundMediaPlayerService
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
    index: Int,
    applicationContext: Context,
    soundMediaPlayerService: SoundMediaPlayerService
){
    when(index){
        0 -> loopSounds(
            applicationContext,
            index
        )
        1 -> resetSounds()
        2 -> changeTimerTime(
            applicationContext,
            index,
            soundMediaPlayerService
        )
        3 -> {
            if(isPlaying.value){
                pauseSounds(
                    applicationContext,
                    index
                )
            } else {
                playSoundsPreset(
                    applicationContext,
                    index,
                    soundMediaPlayerService
                )
            }
        }
        4 -> increaseSliderLevelsPreset()
        5 -> decreaseSliderLevelsPreset()
        6 -> ringMeditationBell(
            applicationContext,
            index
        )
    }
}

fun activateControlButton(index: Int){
    createSoundViewModel!!.borderControlColors[index].value = Black
    createSoundViewModel!!.backgroundControlColor1[index].value = SoftPeach
    createSoundViewModel!!.backgroundControlColor2[index].value = Solitude
    if(index == 3){
        createSoundViewModel!!.icons[index].value = R.drawable.play_icon
    }
}

fun deActivateControlButton(index: Int){
    createSoundViewModel!!.borderControlColors[index].value = Bizarre
    createSoundViewModel!!.backgroundControlColor1[index].value = White
    createSoundViewModel!!.backgroundControlColor2[index].value = White
    if(index == 3){
        createSoundViewModel!!.icons[index].value = R.drawable.pause_icon
    }
}

fun adjustMediaPlayerVolumes(index: Int, volume: MutableState<MediaPlayer>){
    volume.value.setVolume(
        sliderVolumes[index]!!.value.toFloat() / 10,
        sliderVolumes[index]!!.value.toFloat() / 10
    )
    Log.i(TAG, "Volume for media player ==>> ${sliderVolumes[index]!!.value.toFloat()}")
}

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
fun SoundUploaderPreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        //SoundUploader(0){}
    }
}