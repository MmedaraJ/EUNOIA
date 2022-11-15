package com.example.eunoia.ui.bottomSheets.recordAudio

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.R
import com.example.eunoia.backend.BedtimeStoryChapterBackend
import com.example.eunoia.backend.PageBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.create.createPrayer.*
import com.example.eunoia.create.createSelfLove.*
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.recordAudioViewModel
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.Timer
import com.example.eunoia.utils.getRandomString
import com.example.eunoia.utils.retrieveUriDuration
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.IOException

private var TAG = "Record Audio"
private var isRecording = mutableStateOf(false)
var timer = Timer(UserDashboardActivity.getInstanceActivity())
var recorder: MediaRecorder? = null
private var vibrator: Vibrator = UserDashboardActivity.getInstanceActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
var recordingTimeDisplay = mutableStateOf("00:00.00")
var showRecordIcon = mutableStateOf(true)
var showPause = mutableStateOf(false)
var currentMaxAmplitude = mutableStateOf(0)
var recordingFile: File? = null
var noOfPlaybackClicks = mutableStateOf(0)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordAudio(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .wrapContentHeight()
            //.height(370.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp
    ) {
        BoxWithConstraints(
            modifier = Modifier
                //.wrapContentHeight()
                //.height(370.dp)
                .fillMaxWidth()
        ){
            val boxWithConstraintsScope = this
            ConstraintLayout(
                modifier = Modifier
                    //.height(370.dp)
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
                        processClosingRecordAudio(
                            context,
                            scope,
                            state,
                            generalMediaPlayerService
                        )
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
                    RecordAudioControls(
                        scope,
                        state,
                        generalMediaPlayerService
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun processClosingRecordAudio(
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(!isRecording.value) {
        when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
            is PageData -> {
                closeRecordAudioAccordingly(
                    "special",
                    context,
                    scope,
                    state,
                    generalMediaPlayerService
                )
            }
            is String ->{
                when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
                    "prayer" ->{
                        closeRecordAudioAccordingly(
                            "special",
                            context,
                            scope,
                            state,
                            generalMediaPlayerService
                        )
                    }
                    "selfLove" ->{
                        closeRecordAudioAccordingly(
                            "special",
                            context,
                            scope,
                            state,
                            generalMediaPlayerService
                        )
                    }
                    "page" ->{
                        closeRecordAudioAccordingly(
                            "special",
                            context,
                            scope,
                            state,
                            generalMediaPlayerService
                        )
                    }
                }
            }
        }
        //closeRecordAudioAccordingly("special", context, scope, state)
    }
}

fun resetRecordingFileWithoutDeleting(
    context: Context
){
    Log.i(TAG, "resetRecordingFileWithoutDeleting")
    recordingFile = File(context.externalCacheDir!!.absolutePath + "/${getRandomString(5)}_audio.aac")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordAudioControls(
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    val context = LocalContext.current
    //recordingFile = File(context.externalCacheDir!!.absolutePath + "/${globalViewModel_!!.currentUser!!.username}_audio.aac")
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
                                if(!generalMediaPlayerService.isMediaPlayerInitialized()){
                                    initializeAudioRecordedMediaPlayer(
                                        generalMediaPlayerService
                                    )
                                }else {
                                    if (!generalMediaPlayerService.isMediaPlayerPlaying()) {
                                        startAudioRecordedMediaPlayer(generalMediaPlayerService)
                                    } else {
                                        pauseAudioRecordedMediaPlayer(generalMediaPlayerService)
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
                        if(!isRecording.value && !generalMediaPlayerService.isMediaPlayerPlaying()) {
                            clearRecordingForSomeElements(generalMediaPlayerService)
                            saveRecordedAudio(
                                context,
                                scope,
                                state,
                                generalMediaPlayerService
                            )
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
                                        if (!generalMediaPlayerService.isMediaPlayerPlaying()) {
                                            showPause.value = !showPause.value
                                            resumeRecorder(generalMediaPlayerService)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clickable {
                                            if(!generalMediaPlayerService.isMediaPlayerPlaying()) {
                                                showPause.value = !showPause.value
                                                resumeRecorder(generalMediaPlayerService)
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
                            clearRecording(context, generalMediaPlayerService)
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
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    when(action){
        "special" -> {
            closeRecordAudioForSomeElements(
                context,
                scope,
                state,
                generalMediaPlayerService
            )
        }
        else -> {
            closeRecordAudio(
                context,
                scope,
                state,
                generalMediaPlayerService
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun closeRecordAudioForSomeElements(
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
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
    if(generalMediaPlayerService.isMediaPlayerInitialized()) {
        stopAudioRecordedMediaPlayer(generalMediaPlayerService)
    }
    closeBottomSheet(scope, state)
}

@OptIn(ExperimentalMaterialApi::class)
fun closeRecordAudio(
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
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
    if(generalMediaPlayerService.isMediaPlayerInitialized()) {
        stopAudioRecordedMediaPlayer(generalMediaPlayerService)
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
        resetRecordingFileWithoutDeleting(context)
        Log.i(TAG, "Recording file length is: ${recordingFile!!.length()}")
        Log.i(TAG, "Recording file absolute is: ${recordingFile!!.absolutePath}")
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
        Log.i(TAG, "recording file length before recording is ${recordingFile!!.length()}")
        startRecorder(recordingFile!!)
    } else {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            3
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun saveRecordedAudio(
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
        is PageData -> {
            if(selectedPageRecordingIndex < recordedPageRecordingAbsolutePath.size) {
                val pageData =
                    recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording as PageData
                setPageRecordingsData(context)
                //storeToS3IfChapterPage(pageData)
            }
        }

        is String ->{
            when(recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording){
                "prayer" -> {
                    setPrayerRecordingsData()
                }

                "selfLove" -> {
                    setSelfLoveRecordingsData()
                }
            }
        }
    }

    processClosingRecordAudio(
        context,
        scope,
        state,
        generalMediaPlayerService
    )
}

fun setPageRecordingsData(
    context: Context
) {
    if(selectedPageRecordingIndex < pageRecordingFileUris.size) {
        Log.i(TAG, "selectedPageRecordingIndex =-- $selectedPageRecordingIndex")
        Log.i(TAG, "recordingFile =-- ${recordingFile}")
        Log.i(TAG, "recordingFile.length() =-- ${recordingFile!!.length()}")
        Log.i(TAG, "recordingFile.toString() =-- ${recordingFile.toString()}")
        val recordedNames = mutableListOf<File>()
        recordedNames.add(recordingFile!!)
        Log.i(TAG, "recordedNames.last() =-- ${recordedNames.last()}")
        recordedPageRecordingAbsolutePath[selectedPageRecordingIndex].value =
            recordedNames.last().absolutePath
        pageRecordingFileColors[selectedPageRecordingIndex].value = Peach
        pageRecordingFileUris[selectedPageRecordingIndex].value =
            recordedNames.last().absolutePath.toUri()
        pageRecordingFileNames[selectedPageRecordingIndex].value =
            pageRecordingNames[selectedPageRecordingIndex].value
        audioPageRecordingFileLengthMilliSeconds[selectedPageRecordingIndex].value = retrieveUriDuration(
            recordedNames.last().path,
            context
        ).toLong()
        pageRecordingS3Keys[selectedPageRecordingIndex].value = "open"
        resetRecordingFileWithoutDeleting(context)
        Log.i(TAG, "Saving page recording 0")
        Log.i(TAG, "99 pageRecordingFileNames.seize => ${pageRecordingFileNames.size}")
        Log.i(TAG, "99 pageRecordingFileNames--seize => ${pageRecordingFileNames}")
    }
}

fun setPrayerRecordingsData() {
    recordedPrayerAbsolutePath.value = recordingFile!!.absolutePath
    recordedFilePrayer.value = recordingFile!!
    recordedFileUriPrayer.value = recordingFile!!.absolutePath.toUri()
    recordedAudioFileLengthMilliSecondsPrayer.value = recordingFile!!.length()
    recordedFileColorPrayer.value = Peach
}

fun setSelfLoveRecordingsData() {
    recordedSelfLoveAbsolutePath.value = recordingFile!!.absolutePath
    recordedFileSelfLove.value = recordingFile!!
    recordedFileUriSelfLove.value = recordingFile!!.absolutePath.toUri()
    recordedAudioFileLengthMilliSecondsSelfLove.value = recordingFile!!.length()
    recordedFileColorSelfLove.value = Peach
}

fun clearRecordingForSomeElements(generalMediaPlayerService: GeneralMediaPlayerService) {
    if(recordingFile!!.length() > 0) {
        stopRecorder()
    }
    timer.stop()
    showRecordIcon.value = true
    showPause.value = false
    recordingTimeDisplay.value = "00:00.00"
    noOfPlaybackClicks.value = 0
    stopAudioRecordedMediaPlayer(generalMediaPlayerService)
    clearAmplitudes()
}

fun clearRecording(context: Context, generalMediaPlayerService: GeneralMediaPlayerService){
    if(recordingFile!!.length() > 0) {
        stopRecorder()
    }
    timer.stop()
    showRecordIcon.value = true
    showPause.value = false
    recordingTimeDisplay.value = "00:00.00"
    noOfPlaybackClicks.value = 0
    stopAudioRecordedMediaPlayer(generalMediaPlayerService)
    clearAmplitudes()
    resetRecordingFile(context)
}

fun initializeAudioRecordedMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService
){
    generalMediaPlayerService.setAudioUri(recordingFile!!.absolutePath.toUri())
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    recordingTimeDisplay.value = timer.setDuration(0)
    timer.startTicking()
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

fun resumeRecorder(generalMediaPlayerService: GeneralMediaPlayerService){
    recorder!!.resume()
    isRecording.value = true
    recordingTimeDisplay.value = timer.setDuration(getLastAmplitude().milliSeconds)
    timer.start()
    stopAudioRecordedMediaPlayer(generalMediaPlayerService)
}

fun stopRecorder(){
    Log.i(TAG, "Stopping recorder")
    timer.stop()
    recorder!!.stop()
    recorder!!.reset()
    recorder!!.release()
    isRecording.value = false
}

fun startAudioRecordedMediaPlayer(generalMediaPlayerService: GeneralMediaPlayerService){
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        generalMediaPlayerService.getMediaPlayer()!!.currentPosition == generalMediaPlayerService.getMediaPlayer()!!.duration
    ){
        recordingTimeDisplay.value = timer.setDuration(0)
    }
    generalMediaPlayerService.startMediaPlayer()
    timer.startTicking()
}

fun pauseAudioRecordedMediaPlayer(generalMediaPlayerService: GeneralMediaPlayerService){
    generalMediaPlayerService.pauseMediaPlayer()
    timer.stopTicking()
}

fun stopAudioRecordedMediaPlayer(generalMediaPlayerService: GeneralMediaPlayerService){
    generalMediaPlayerService.onDestroy()
    //generalMediaPlayerService.stopService(Intent(UserDashboardActivity.getInstanceActivity(), GeneralMediaPlayerService::class.java))
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
            ),
            GeneralMediaPlayerService()
        )
    }
}