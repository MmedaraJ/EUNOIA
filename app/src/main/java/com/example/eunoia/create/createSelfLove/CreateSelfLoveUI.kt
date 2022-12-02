package com.example.eunoia.create.createSelfLove

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ResetTv
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.createSound.TAG
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.recordAudio.deleteRecordingFile
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.theme.*
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToResetUploadedSelfLoveUI(
    context: Context,
    takeAction: () -> Unit
){
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmStateChange = {
            Log.i(TAG, "Confirm state change ==>> $it")
            if(it == DismissValue.DismissedToStart){
                resetSelfLoveUploadUI()
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
                text = uploadedFileSelfLove.value.name,
                height = 55,
                fontSize = 16,
                textColor = Black,
                backgroundColor = uploadedFileColorSelfLove.value,
                corner = 10,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "light",
                maxWidthFraction = 1F
            ) {
                if (uploadedFileSelfLove.value.name == "Choose a file") {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        takeAction()
                    } else {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            3
                        )
                    }
                } else {
                    startUploadedMediaPlayer(context)
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

fun startUploadedMediaPlayer(context: Context){
    if (uploadedFileMediaPlayerSelfLove.value.isPlaying) {
        uploadedFileMediaPlayerSelfLove.value.stop()
        uploadedFileMediaPlayerSelfLove.value.reset()
    } else {
        uploadedFileMediaPlayerSelfLove.value.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, uploadedFileUriSelfLove.value)
            setVolume(1f, 1f)
            prepare()
            start()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToResetRecordedSelfLoveUI(
    context: Context,
    takeAction: () -> Unit
){
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmStateChange = {
            Log.i(TAG, "Confirm state change ==>> $it")
            if(it == DismissValue.DismissedToStart){
                resetSelfLoveRecordUI(context)
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
                text = recordedFileSelfLove.value.name,
                height = 55,
                fontSize = 16,
                textColor = Black,
                backgroundColor = recordedFileColorSelfLove.value,
                corner = 10,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "light",
                maxWidthFraction = 1F
            ) {
                if (recordedSelfLoveAbsolutePath.value == "") {
                    takeAction()
                } else {
                    startRecordedMediaPlayer(context)
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

fun startRecordedMediaPlayer(context: Context) {
    if (recordedFileMediaPlayerSelfLove.value.isPlaying) {
        recordedFileMediaPlayerSelfLove.value.stop()
        recordedFileMediaPlayerSelfLove.value.reset()
    } else {
        recordedFileMediaPlayerSelfLove.value.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, recordedFileUriSelfLove.value)
            setVolume(1f, 1f)
            prepare()
            start()
        }
    }
}

fun resetSelfLoveUploadUI() {
    uploadedFileSelfLove.value = File("Choose a file")
    uploadedFileColorSelfLove.value = WePeep
    resetUploadSelfLoveMediaPlayers()
    uploadedFileMediaPlayerSelfLove.value = MediaPlayer()
    uploadedFileUriSelfLove.value = "".toUri()
    uploadedAudioFileLengthMilliSecondsSelfLove.value = 0L
}

fun resetSelfLoveRecordUI(context: Context) {
    recordedSelfLoveAbsolutePath.value = ""
    recordedFileSelfLove.value = File("Record Self Love")
    recordedFileColorSelfLove.value = WePeep
    resetRecordingFileAndMediaSelfLove(context)
    recordedFileMediaPlayerSelfLove.value = MediaPlayer()
    recordedFileUriSelfLove.value = "".toUri()
    recordedAudioFileLengthMilliSecondsSelfLove.value = 0L
    deleteRecordingFile(context)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelfLoveRecordingBlock(
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    takeAction: (index: Int) -> Unit
){
    val context = LocalContext.current
    if(index < selfLoveRecordingFileNames.size) {
        val dismissState = rememberDismissState(
            initialValue = DismissValue.Default,
            confirmStateChange = {
                if (it == DismissValue.DismissedToStart) {
                    if (
                        generalMediaPlayerService.isMediaPlayerInitialized() &&
                        generalMediaPlayerService.isMediaPlayerPlaying()
                    ) {
                        //Toast
                    }else {
                        if(selfLoveRecordingFileNames[index].value != "empty recording") {
                            stopPlayingThisSelfLove(generalMediaPlayerService)
                            resetSelfLoveRecordUI(index)
                        }
                    }
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
                    targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f
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
                    text = selfLoveRecordingFileNames[index].value,
                    height = 55,
                    fontSize = 16,
                    textColor = Black,
                    backgroundColor = selfLoveRecordingFileColors[index].value,
                    corner = 10,
                    borderStroke = 0.0,
                    borderColor = Black.copy(alpha = 0F),
                    textType = "light",
                    maxWidthFraction = 1F
                ) {
                    if (selfLoveRecordingFileNames[index].value == "empty recording") {
                        resetAllSelfLoveRecordUIMediaPlayers()
                        generalMediaPlayerService.onDestroy()
                        resetSelfLoveRecordingCDT()
                        takeAction(index)
                    } else {
                        playingIndex = index
                        getInitialUri(generalMediaPlayerService)
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
}

fun resetSelfLoveRecordUI(index: Int) {
    if(index < selfLoveRecordingFileNames.size) {
        if (selfLoveRecordingFileNames[index].value != "empty recording") {
            if(selfLoveRecordingNames[index].value == selfLoveRecordingFileNames[index].value) {
                SoundBackend.deleteAudio(selfLoveRecordingS3Keys[index].value)
                selfLoveRecordingS3Keys[index].value = ""
            }

            recordedSelfLoveRecordingAbsolutePath[index].value = ""
            selfLoveRecordingFileColors[index].value = SoftPeach
            selfLoveRecordingFileNames[index].value = "empty recording"
            selfLoveRecordingFileUris[index].value = "".toUri()
            resetRecordSelfLoveMediaPlayers(index)
            val mediaPlayer = MediaPlayer()
            selfLoveRecordingFileMediaPlayers[index].value = mediaPlayer
            audioSelfLoveRecordingFileLengthMilliSeconds[index].value = 0L
        }
    }
}

fun resetRecordSelfLoveMediaPlayers(index: Int){
    if(index < selfLoveRecordingFileMediaPlayers.size) {
        if (selfLoveRecordingFileMediaPlayers[index].value.isPlaying) {
            selfLoveRecordingFileMediaPlayers[index].value.stop()
        }
        selfLoveRecordingFileMediaPlayers[index].value.reset()
        selfLoveRecordingFileMediaPlayers[index].value.release()
    }
}

/**
 * reset all the media players for this bedtime story page
 */
fun resetAllSelfLoveRecordUIMediaPlayers() {
    selfLoveRecordingFileMediaPlayers.forEachIndexed { index, _ ->
        resetRecordSelfLoveMediaPlayers(index)
        val mp = MediaPlayer()
        selfLoveRecordingFileMediaPlayers[index].value = mp
    }
}

/**
 * Get the first uri for this self love.
 * This kick starts the audio playing process for this self love
 *
 * @param generalMediaPlayerService The media player service
 */
fun getInitialUri(generalMediaPlayerService: GeneralMediaPlayerService){
    if(playingIndex < selfLoveRecordingFileNames.size) {
        deActivateSelfLoveRecordingControls(2)
        deActivateSelfLoveRecordingControls(3)
        playIt(generalMediaPlayerService)
    }
}

/**
 * Start playing the audio uri
 *
 * @param generalMediaPlayerService The media player service
 */
fun playIt(generalMediaPlayerService: GeneralMediaPlayerService){
    if(playingIndex < selfLoveRecordingFileUris.size) {
        if(selfLoveRecordingFileUris[playingIndex].value.toString().isEmpty()){
            SoundBackend.retrieveAudio(
                selfLoveRecordingS3Keys[playingIndex].value,
                globalViewModel!!.currentUser!!.amplifyAuthUserId
            ) {
                selfLoveRecordingFileUris[playingIndex].value = it!!
                startPlaying(
                    selfLoveRecordingFileUris[playingIndex].value,
                    generalMediaPlayerService
                )
            }
        }else {
            startPlaying(
                selfLoveRecordingFileUris[playingIndex].value,
                generalMediaPlayerService
            )
        }
    }
}

/**
 * Start playing the audio uri using generalMediaPlayerService.
 * Then start timer and get next uri
 *
 * @param uri is the audio uri to be played
 * @param generalMediaPlayerService The media player service
 */
private fun startPlaying(
    uri: Uri,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    if(playingIndex < selfLoveRecordingFileUris.size) {
        if(selfLoveRecordingFileUris[playingIndex].value != "".toUri()){
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(uri)

            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.startMediaPlayer()
            } else {
                val intent = Intent()
                intent.action = "PLAY"
                generalMediaPlayerService.onStartCommand(intent, 0, 0)
            }

            for(i in selfLoveRecordingFileColors.indices) {
                selfLoveRecordingFileColors[i].value = Peach
            }
            if(playingIndex < selfLoveRecordingFileColors.size){
                selfLoveRecordingFileColors[playingIndex].value = Color.Green
            }

            startTimer(generalMediaPlayerService)
        }else{
            playingIndex += 1
            if(playingIndex >= selfLoveRecordingFileUris.size){
                resetSelfLoveRecordingCDT()
                playingIndex = -1
                generalMediaPlayerService.onDestroy()
                activateSelfLoveRecordingControls(2)
                deActivateSelfLoveRecordingControls(3)
            }else {
                playIt(
                    generalMediaPlayerService
                )
            }
        }
    }
}

fun resetSelfLoveRecordingCDT(){
    if (recordingCDT != null) {
        recordingCDT!!.cancel()
        recordingCDT = null
    }
}

/**
 * Start the count down timer for the duration of the currently playing uri.
 * Play the next uri when count down timer is done
 *
 * @param generalMediaPlayerService is the media player service that plays the audio
 */
private fun startTimer(
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(
        playingIndex < selfLoveRecordingFileUris.size &&
        playingIndex < audioSelfLoveRecordingFileLengthMilliSeconds.size &&
        playingIndex < selfLoveRecordingFileColors.size
    ){
        startSelfLoveRecordingCountDownTimer(
            audioSelfLoveRecordingFileLengthMilliSeconds[playingIndex].value,
            generalMediaPlayerService
        ) {
            for(i in selfLoveRecordingFileColors.indices) {
                selfLoveRecordingFileColors[i].value = Peach
            }

            //after a uri is done playing, play the next one
            playingIndex += 1

            resetSelfLoveRecordingCDT()

            //if all uris for this page have been played, stop media player
            if(
                playingIndex >= selfLoveRecordingFileUris.size ||
                selfLoveRecordingFileUris[playingIndex].value == "".toUri()
            ){
                playingIndex = -1
                generalMediaPlayerService.onDestroy()
                activateSelfLoveRecordingControls(2)
                deActivateSelfLoveRecordingControls(3)
            }else {
                playIt(
                    generalMediaPlayerService
                )
            }
        }
    }
}

/**
 * Start count down timer
 *
 * @param time duration of the count down timer
 * @param completed runs when count down timer is finished
 */
fun startSelfLoveRecordingCountDownTimer(
    time: Long,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
) {
    if (recordingCDT == null) {
        recordingCDT = object : CountDownTimer(time, 1) {
            override fun onTick(millisUntilFinished: Long) {
                if(generalMediaPlayerService.isMediaPlayerInitialized()){
                    if(
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition >=
                        generalMediaPlayerService.getMediaPlayer()!!.duration
                    ){
                        Log.i(TAG, "Timer stopped")
                        generalMediaPlayerService.onDestroy()
                        activateSelfLoveRecordingControls(2)
                        deActivateSelfLoveRecordingControls(3)
                    }
                }
                if(millisUntilFinished % 10000 == 0L) {
                    Log.i(TAG, "Page recording timer: $millisUntilFinished")
                }
            }

            override fun onFinish() {
                completed()
                Log.i(TAG, "Timer stopped")
            }
        }
    }
    recordingCDT!!.start()
}

/**
 * reset all the media players for this self love
 */
fun resetAllSelfLoveRecordBedtimeStoryUIMediaPlayers() {
    selfLoveRecordingFileMediaPlayers.forEachIndexed { index, _ ->
        resetRecordSelfLoveMediaPlayers(index)
        val mp = MediaPlayer()
        selfLoveRecordingFileMediaPlayers[index].value = mp
    }
}