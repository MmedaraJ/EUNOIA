package com.example.eunoia.create.createPrayer

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
fun SwipeToResetUploadedPrayerUI(
    context: Context,
    takeAction: () -> Unit
){
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmStateChange = {
            Log.i(TAG, "Confirm state change ==>> $it")
            if(it == DismissValue.DismissedToStart){
                resetPrayerUploadUI()
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
                text = uploadedFilePrayer.value.name,
                height = 55,
                fontSize = 16,
                textColor = Black,
                backgroundColor = uploadedFileColorPrayer.value,
                corner = 10,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "light",
                maxWidthFraction = 1F
            ) {
                if (uploadedFilePrayer.value.name == "Choose a file") {
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
    if (uploadedFileMediaPlayerPrayer.value.isPlaying) {
        uploadedFileMediaPlayerPrayer.value.stop()
        uploadedFileMediaPlayerPrayer.value.reset()
    } else {
        uploadedFileMediaPlayerPrayer.value.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, uploadedFileUriPrayer.value)
            setVolume(1f, 1f)
            prepare()
            start()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToResetRecordedPrayerUI(
    context: Context,
    takeAction: () -> Unit
){
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmStateChange = {
            Log.i(TAG, "Confirm state change ==>> $it")
            if(it == DismissValue.DismissedToStart){
                resetPrayerRecordUI(context)
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
                text = recordedFilePrayer.value.name,
                height = 55,
                fontSize = 16,
                textColor = Black,
                backgroundColor = recordedFileColorPrayer.value,
                corner = 10,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "light",
                maxWidthFraction = 1F
            ) {
                if (recordedPrayerAbsolutePath.value == "") {
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
    if (recordedFileMediaPlayerPrayer.value.isPlaying) {
        recordedFileMediaPlayerPrayer.value.stop()
        recordedFileMediaPlayerPrayer.value.reset()
    } else {
        recordedFileMediaPlayerPrayer.value.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, recordedFileUriPrayer.value)
            setVolume(1f, 1f)
            prepare()
            start()
        }
    }
}

fun resetPrayerUploadUI() {
    uploadedFilePrayer.value = File("Choose a file")
    uploadedFileColorPrayer.value = WePeep
    resetUploadPrayerMediaPlayers()
    uploadedFileMediaPlayerPrayer.value = MediaPlayer()
    uploadedFileUriPrayer.value = "".toUri()
    uploadedAudioFileLengthMilliSecondsPrayer.value = 0L
}

fun resetPrayerRecordUI(context: Context) {
    recordedPrayerAbsolutePath.value = ""
    recordedFilePrayer.value = File("Record Prayer")
    recordedFileColorPrayer.value = WePeep
    resetRecordingFileAndMediaPrayer(context)
    recordedFileMediaPlayerPrayer.value = MediaPlayer()
    recordedFileUriPrayer.value = "".toUri()
    recordedAudioFileLengthMilliSecondsPrayer.value = 0L
    deleteRecordingFile(context)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PrayerRecordingBlock(
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    takeAction: (index: Int) -> Unit
){
    val context = LocalContext.current
    if(index < prayerRecordingFileNames.size) {
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
                        if(prayerRecordingFileNames[index].value != "empty recording") {
                            stopPlayingThisPrayer(generalMediaPlayerService)
                            resetPrayerRecordUI(index)
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
                    text = prayerRecordingFileNames[index].value,
                    height = 55,
                    fontSize = 16,
                    textColor = Black,
                    backgroundColor = prayerRecordingFileColors[index].value,
                    corner = 10,
                    borderStroke = 0.0,
                    borderColor = Black.copy(alpha = 0F),
                    textType = "light",
                    maxWidthFraction = 1F
                ) {
                    if (prayerRecordingFileNames[index].value == "empty recording") {
                        resetAllPrayerRecordUIMediaPlayers()
                        generalMediaPlayerService.onDestroy()
                        resetPrayerRecordingCDT()
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

fun resetPrayerRecordUI(index: Int) {
    if(index < prayerRecordingFileNames.size) {
        if (prayerRecordingFileNames[index].value != "empty recording") {
            if(prayerRecordingNames[index].value == prayerRecordingFileNames[index].value) {
                SoundBackend.deleteAudio(prayerRecordingS3Keys[index].value)
                prayerRecordingS3Keys[index].value = ""
            }

            recordedPrayerRecordingAbsolutePath[index].value = ""
            prayerRecordingFileColors[index].value = SoftPeach
            prayerRecordingFileNames[index].value = "empty recording"
            prayerRecordingFileUris[index].value = "".toUri()
            resetRecordPrayerMediaPlayers(index)
            val mediaPlayer = MediaPlayer()
            prayerRecordingFileMediaPlayers[index].value = mediaPlayer
            audioPrayerRecordingFileLengthMilliSeconds[index].value = 0L
        }
    }
}

fun resetRecordPrayerMediaPlayers(index: Int){
    if(index < prayerRecordingFileMediaPlayers.size) {
        if (prayerRecordingFileMediaPlayers[index].value.isPlaying) {
            prayerRecordingFileMediaPlayers[index].value.stop()
        }
        prayerRecordingFileMediaPlayers[index].value.reset()
        prayerRecordingFileMediaPlayers[index].value.release()
    }
}

/**
 * reset all the media players for this bedtime story page
 */
fun resetAllPrayerRecordUIMediaPlayers() {
    prayerRecordingFileMediaPlayers.forEachIndexed { index, _ ->
        resetRecordPrayerMediaPlayers(index)
        val mp = MediaPlayer()
        prayerRecordingFileMediaPlayers[index].value = mp
    }
}

/**
 * Get the first uri for this prayer.
 * This kick starts the audio playing process for this prayer
 *
 * @param generalMediaPlayerService The media player service
 */
fun getInitialUri(generalMediaPlayerService: GeneralMediaPlayerService){
    if(playingIndex < prayerRecordingFileNames.size) {
        deActivatePrayerRecordingControls(2)
        deActivatePrayerRecordingControls(3)
        playIt(generalMediaPlayerService)
    }
}

/**
 * Start playing the audio uri
 *
 * @param generalMediaPlayerService The media player service
 */
fun playIt(generalMediaPlayerService: GeneralMediaPlayerService){
    if(playingIndex < prayerRecordingFileUris.size) {
        if(prayerRecordingFileUris[playingIndex].value.toString().isEmpty()){
            SoundBackend.retrieveAudio(
                prayerRecordingS3Keys[playingIndex].value,
                globalViewModel!!.currentUser!!.amplifyAuthUserId
            ) {
                prayerRecordingFileUris[playingIndex].value = it!!
                startPlaying(
                    prayerRecordingFileUris[playingIndex].value,
                    generalMediaPlayerService
                )
            }
        }else {
            startPlaying(
                prayerRecordingFileUris[playingIndex].value,
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
    if(playingIndex < prayerRecordingFileUris.size) {
        if(prayerRecordingFileUris[playingIndex].value != "".toUri()){
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(uri)

            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.startMediaPlayer()
            } else {
                val intent = Intent()
                intent.action = "PLAY"
                generalMediaPlayerService.onStartCommand(intent, 0, 0)
            }

            for(i in prayerRecordingFileColors.indices) {
                prayerRecordingFileColors[i].value = Peach
            }
            if(playingIndex < prayerRecordingFileColors.size){
                prayerRecordingFileColors[playingIndex].value = Color.Green
            }

            startTimer(generalMediaPlayerService)
        }else{
            playingIndex += 1
            if(playingIndex >= prayerRecordingFileUris.size){
                resetPrayerRecordingCDT()
                playingIndex = -1
                generalMediaPlayerService.onDestroy()
                activatePrayerRecordingControls(2)
                deActivatePrayerRecordingControls(3)
            }else {
                playIt(
                    generalMediaPlayerService
                )
            }
        }
    }
}

fun resetPrayerRecordingCDT(){
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
        playingIndex < prayerRecordingFileUris.size &&
        playingIndex < audioPrayerRecordingFileLengthMilliSeconds.size &&
        playingIndex < prayerRecordingFileColors.size
    ){
        startPrayerRecordingCountDownTimer(
            audioPrayerRecordingFileLengthMilliSeconds[playingIndex].value,
            generalMediaPlayerService
        ) {
            for(i in prayerRecordingFileColors.indices) {
                prayerRecordingFileColors[i].value = Peach
            }

            //after a uri is done playing, play the next one
            playingIndex += 1

            resetPrayerRecordingCDT()

            //if all uris for this page have been played, stop media player
            if(
                playingIndex >= prayerRecordingFileUris.size ||
                prayerRecordingFileUris[playingIndex].value == "".toUri()
            ){
                playingIndex = -1
                generalMediaPlayerService.onDestroy()
                activatePrayerRecordingControls(2)
                deActivatePrayerRecordingControls(3)
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
fun startPrayerRecordingCountDownTimer(
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
                        activatePrayerRecordingControls(2)
                        deActivatePrayerRecordingControls(3)
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
 * reset all the media players for this prayer
 */
fun resetAllPrayerRecordBedtimeStoryUIMediaPlayers() {
    prayerRecordingFileMediaPlayers.forEachIndexed { index, _ ->
        resetRecordPrayerMediaPlayers(index)
        val mp = MediaPlayer()
        prayerRecordingFileMediaPlayers[index].value = mp
    }
}