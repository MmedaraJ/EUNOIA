package com.example.eunoia.create.createSelfLove

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
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
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.eunoia.create.createBedtimeStory.uploadedFileBedtimeStory
import com.example.eunoia.create.createSound.TAG
import com.example.eunoia.ui.bottomSheets.deleteRecordingFile
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.ui.theme.White
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
    uploadedFileMediaPlayerSelfLove.value = MediaPlayer()
    uploadedFileUriSelfLove.value = "".toUri()
    uploadedAudioFileLengthMilliSecondsSelfLove.value = 0L
}

fun resetSelfLoveRecordUI(context: Context) {
    recordedSelfLoveAbsolutePath.value = ""
    recordedFileSelfLove.value = File("Record Self Love")
    recordedFileColorSelfLove.value = WePeep
    recordedFileMediaPlayerSelfLove.value = MediaPlayer()
    recordedFileUriSelfLove.value = "".toUri()
    recordedAudioFileLengthMilliSecondsSelfLove.value = 0L
    deleteRecordingFile(context)
}