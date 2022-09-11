package com.example.eunoia.ui.bottomSheets

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amplifyframework.datastore.generated.model.ChapterPageData
import com.example.eunoia.R
import com.example.eunoia.backend.ChapterPageBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.createBedtimeStory.AudioWaves
import com.example.eunoia.create.createBedtimeStory.clearAmplitudes
import com.example.eunoia.create.createBedtimeStory.getLastAmplitude
import com.example.eunoia.create.createPrayer.*
import com.example.eunoia.create.createSelfLove.*
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.services.MediaPlayerService
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.recordAudioViewModel
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.Timer
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.IOException

private var TAG = "Record Audio"
private var isRecording = mutableStateOf(false)
var timer = Timer(UserDashboardActivity.getInstanceActivity())
var recorder: MediaRecorder? = null
var audioRecordedMediaPlayer: MediaPlayer? = null
private var vibrator: Vibrator = UserDashboardActivity.getInstanceActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
var recordingTimeDisplay = mutableStateOf("00:00.00")
var showRecordIcon = mutableStateOf(true)
var showPause = mutableStateOf(false)
var currentMaxAmplitude = mutableStateOf(0)
var recordingFile: File? = null
var audioMediaPlayerIsPlaying = mutableStateOf(false)
var audioMediaPlayerInitialized = mutableStateOf(false)
var noOfPlaybackClicks = mutableStateOf(0)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordAudio(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .height(370.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .height(370.dp)
                .fillMaxWidth()
        ){
            val boxWithConstraintsScope = this
            ConstraintLayout(
                modifier = Modifier
                    .height(370.dp)
                    .background(SoftPeach),
            ) {
                val (
                    timeStamp,
                    waves,
                    close,
                    controls
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(timeStamp) {
                            top.linkTo(parent.top, margin = 32.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                        }
                ) {
                    LightText(
                        text = recordingTimeDisplay.value,
                        color = Black,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(close) {
                            top.linkTo(parent.top, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                ) {
                    ClickableNormalText(
                        text = "Close",
                        color = Black,
                        fontSize = 9,
                        xOffset = 0,
                        yOffset = 0
                    ){
                        if(!isRecording.value) {
                            when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
                                is ChapterPageData -> {
                                    //closeRecordAudioAccordingly("special", context, scope, state)
                                }
                                is String ->{
                                    when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
                                        "prayer" ->{
                                            closeRecordAudioAccordingly("special", context, scope, state)
                                        }
                                        "selfLove" ->{
                                            closeRecordAudioAccordingly("special", context, scope, state)
                                        }
                                    }
                                }
                            }
                            //closeRecordAudioAccordingly("special", context, scope, state)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(OldLace)
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 8.dp)
                        .constrainAs(waves) {
                            top.linkTo(timeStamp.bottom, margin = 4.dp)
                            bottom.linkTo(controls.top, margin = 12.dp)
                            end.linkTo(parent.end, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AudioWaves()
                }
                Column(
                    modifier = Modifier
                        .constrainAs(controls) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        }
                ) {
                    RecordAudioControls()
                }
            }
        }
    }
}

@Composable
fun RecordAudioControls(){
    val context = LocalContext.current
    recordingFile = File(context.externalCacheDir!!.absolutePath + "/${globalViewModel_!!.currentUser!!.username}_audio.aac")
    Log.i(TAG, "Recording file length is: ${recordingFile!!.length()}")
    Log.i(TAG, "Recording file absolute is: ${recordingFile!!.absolutePath}")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Column{
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(BeautyBush),
                    contentAlignment = Alignment.Center,
                ) {
                    AnImageWithColor(
                        R.drawable.tick_mark,
                        "Play Audio",
                        White,
                        10.dp,
                        6.84.dp,
                        0,
                        0
                    ) {
                        if(!isRecording.value) {
                            if(recordingFile!!.length() > 0) {
                                if(!audioMediaPlayerInitialized.value){
                                    initializeAudioRecordedMediaPlayer(context)
                                }else {
                                    if (!audioMediaPlayerIsPlaying.value) {
                                        startAudioRecordedMediaPlayer()
                                    } else {
                                        pauseAudioRecordedMediaPlayer()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.Bottom
        ){
            Column{
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(BeautyBush),
                    contentAlignment = Alignment.Center
                ) {
                    AnImageWithColor(
                        R.drawable.tick_mark,
                        "save audio",
                        White,
                        10.dp,
                        6.84.dp,
                        0,
                        0
                    ) {
                        if(!isRecording.value && !audioMediaPlayerIsPlaying.value) {
                            saveRecordedAudio(context)
                        }
                    }
                }
            }
            Column{
                if(showRecordIcon.value) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, ClassicRose, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(BeautyBush)
                                .clickable {
                                    currentMaxAmplitude.value = 0
                                    setUpMediaRecorder(context)
                                    vibrator.vibrate(
                                        VibrationEffect.createOneShot(
                                            50,
                                            VibrationEffect.DEFAULT_AMPLITUDE
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                        }
                    }
                }else{
                    if(!showPause.value){
                        Box(
                            modifier = Modifier
                                .size(72.92.dp, 35.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .border(2.dp, ClassicRose, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(62.71.dp, 24.79.dp)
                                    .clip(RoundedCornerShape(14.91.dp))
                                    .background(SwansDown)
                                    .clickable {
                                        showPause.value = !showPause.value
                                        pauseRecorder()
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                AnImage(
                                    id = R.drawable.pause_icon,
                                    contentDescription = "pause recording",
                                    width = 5.0,
                                    height = 9.48,
                                    xOffset = 0,
                                    yOffset = 0,
                                    context = context
                                ) {
                                    showPause.value = !showPause.value
                                    pauseRecorder()
                                }
                            }
                        }
                    }else{
                        Box(
                            modifier = Modifier
                                .size(72.92.dp, 35.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .border(2.dp, ClassicRose, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(62.71.dp, 24.79.dp)
                                    .clip(RoundedCornerShape(14.91.dp))
                                    .background(SwansDown)
                                    .clickable {
                                        if(!audioMediaPlayerIsPlaying.value) {
                                            showPause.value = !showPause.value
                                            resumeRecorder(recordingFile!!)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clickable {
                                            if(!audioMediaPlayerIsPlaying.value) {
                                                showPause.value = !showPause.value
                                                resumeRecorder(recordingFile!!)
                                            }
                                        }
                                ) {
                                    MorgeNormalText(
                                        text = "resume",
                                        color = Black,
                                        fontSize = 15,
                                        xOffset = 0,
                                        yOffset = 0
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column{
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Black),
                    contentAlignment = Alignment.Center
                ) {
                    AnImageWithColor(
                        R.drawable.x_mark,
                        "delete audio",
                        White,
                        10.dp,
                        10.dp,
                        0,
                        0
                    ) {
                        if(!isRecording.value) {
                            clearRecording(context)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun closeRecordAudioAccordingly(
    action: String,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    when(action){
        "special" -> {
            closeRecordAudioForSomeElements(
                context,
                scope,
                state
            )
        }
        else -> {
            closeRecordAudio(
                context,
                scope,
                state
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun closeRecordAudioForSomeElements(
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if(isRecording.value) {
        stopRecorder()
    }
    timer.stop()
    showRecordIcon.value = true
    showPause.value = false
    recordingTimeDisplay.value = "00:00.00"
    clearAmplitudes()
    noOfPlaybackClicks.value = 0
    if(audioMediaPlayerInitialized.value) {
        stopAudioRecordedMediaPlayer()
    }
    closeBottomSheet(scope, state)
}

@OptIn(ExperimentalMaterialApi::class)
fun closeRecordAudio(
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if(isRecording.value) {
        stopRecorder()
    }
    timer.stop()
    deleteRecordingFile(context)
    showRecordIcon.value = true
    showPause.value = false
    recordingTimeDisplay.value = "00:00.00"
    clearAmplitudes()
    noOfPlaybackClicks.value = 0
    if(audioMediaPlayerInitialized.value) {
        stopAudioRecordedMediaPlayer()
    }
    closeBottomSheet(scope, state)
}

fun resetRecordingFile(context: Context) {
    val fileIsDeleted = recordingFile!!.delete()
    if(fileIsDeleted) {
        recordingFile =
            File(context.externalCacheDir!!.absolutePath + "/${globalViewModel_!!.currentUser!!.username}_audio.aac")
    }
}

fun deleteRecordingFile(context: Context): Boolean {
    if(recordingFile != null) {
        return recordingFile!!.delete()
    }
    return false
}

fun setUpMediaRecorder(context: Context) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        Log.i(TAG, "About to prep recorder")
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(recordingFile!!.absolutePath)
            try {
                prepare()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        showRecordIcon.value = false
        startRecorder(recordingFile!!)
    } else {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            3
        )
    }
}

fun saveRecordedAudio(context: Context) {
    var key = ""
    when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
        is ChapterPageData -> {
            val chapterPageData = recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording as ChapterPageData
            storeToS3IfChapterPage(chapterPageData)
        }

        is String ->{
            when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
                "prayer" ->{
                    recordedPrayerAbsolutePath.value = recordingFile!!.absolutePath
                    recordedFilePrayer.value = recordingFile!!
                    recordedFileUriPrayer.value = recordingFile!!.absolutePath.toUri()
                    recordedAudioFileLengthMilliSecondsPrayer.value = recordingFile!!.length()
                    recordedFileColorPrayer.value = Peach
                    clearRecordingForSomeElements()
                }

                "selfLove" ->{
                    recordedSelfLoveAbsolutePath.value = recordingFile!!.absolutePath
                    recordedFileSelfLove.value = recordingFile!!
                    recordedFileUriSelfLove.value = recordingFile!!.absolutePath.toUri()
                    recordedAudioFileLengthMilliSecondsSelfLove.value = recordingFile!!.length()
                    recordedFileColorSelfLove.value = Peach
                    clearRecordingForSomeElements()
                }
            }
        }
    }

    /*key = "Routine/BedtimeStories/${globalViewModel_!!.currentUser!!.username}/recorded/bedtimeStoryName/chapter/page/file1.aac"
    SoundBackend.storeAudio(recordingFile!!.absolutePath, key){
        Log.i(TAG, "Chapter Page's audio keys ==>> $it")
        clearRecording(context)
    }*/
}

fun storeToS3IfChapterPage(chapterPageData: ChapterPageData){
    val key = "Routine/" +
            "BedtimeStories/" +
            "${globalViewModel_!!.currentUser!!.username}/" +
            "recorded/" +
            "${chapterPageData.bedtimeStoryInfoChapter.bedtimeStoryInfo.displayName}/" +
            "${chapterPageData.bedtimeStoryInfoChapter.displayName}/" +
            "${chapterPageData.displayName}/" +
            "recording_${chapterPageData.audioKeysS3.size + 1}.aac"

    SoundBackend.storeAudio(recordingFile!!.absolutePath, key){ s3key ->
        updateChapterPageData(
            s3key,
            chapterPageData
        )
    }
}

fun updateChapterPageData(s3Key: String, chapterPageData: ChapterPageData) {
    chapterPageData.audioKeysS3.add(s3Key)
    chapterPageData.audioNames.add("${chapterPageData.audioNames.size + 1}")
    ChapterPageBackend.updateChapterPage(chapterPageData){
        Log.i(TAG, "${chapterPageData.displayName}'s audio names ==>> ${it.audioNames}")
        Log.i(TAG, "${chapterPageData.displayName}'s audio keys ==>> ${it.audioKeysS3}")
    }
}

fun clearRecordingForSomeElements() {
    if(recordingFile!!.length() > 0) {
        stopRecorder()
    }
    timer.stop()
    showRecordIcon.value = true
    showPause.value = false
    recordingTimeDisplay.value = "00:00.00"
    noOfPlaybackClicks.value = 0
    stopAudioRecordedMediaPlayer()
    clearAmplitudes()
}

fun clearRecording(context: Context){
    if(recordingFile!!.length() > 0) {
        stopRecorder()
    }
    timer.stop()
    showRecordIcon.value = true
    showPause.value = false
    recordingTimeDisplay.value = "00:00.00"
    noOfPlaybackClicks.value = 0
    stopAudioRecordedMediaPlayer()
    clearAmplitudes()
    resetRecordingFile(context)
}

fun initializeAudioRecordedMediaPlayer(context: Context){
    val service = MediaPlayerService()
    val intent = Intent()
    intent.action = "PLAY"
    service.onStartCommand(intent, 0, 0)
}

fun startRecorder(recordingFile: File){
    recorder!!.start()
    timer.start()
    isRecording.value = true
}

fun pauseRecorder(){
    recorder!!.pause()
    isRecording.value = false
    timer.pause()
}

fun resumeRecorder(recordingFile: File){
    recorder!!.resume()
    isRecording.value = true
    recordingTimeDisplay.value = timer.setDuration(getLastAmplitude().milliSeconds)
    timer.start()
    stopAudioRecordedMediaPlayer()
}

fun stopRecorder(){
    Log.i(TAG, "Stopping recorder")
    timer.stop()
    recorder!!.stop()
    recorder!!.reset()
    recorder!!.release()
    isRecording.value = false
}

fun startAudioRecordedMediaPlayer(){
    if(audioMediaPlayerInitialized.value && audioRecordedMediaPlayer!!.currentPosition == audioRecordedMediaPlayer!!.duration){
        recordingTimeDisplay.value = timer.setDuration(0)
    }
    audioRecordedMediaPlayer!!.start()
    timer.startTicking()
    audioMediaPlayerIsPlaying.value = true
}

fun pauseAudioRecordedMediaPlayer(){
    timer.stopTicking()
    audioRecordedMediaPlayer!!.pause()
    audioMediaPlayerIsPlaying.value = false
}

fun stopAudioRecordedMediaPlayer(){
    if(audioMediaPlayerInitialized.value) {
        if (audioMediaPlayerIsPlaying.value) {
            audioRecordedMediaPlayer!!.stop()
        }
        audioRecordedMediaPlayer!!.reset()
        audioRecordedMediaPlayer!!.release()
        audioMediaPlayerIsPlaying.value = false
        audioMediaPlayerInitialized.value = false
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
fun RecordAudioPreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        RecordAudio(
            globalViewModel,
            rememberCoroutineScope(),
            rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                confirmStateChange = {false}
            )
        )
    }
}