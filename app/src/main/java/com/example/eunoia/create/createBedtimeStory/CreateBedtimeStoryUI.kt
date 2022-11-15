package com.example.eunoia.create.createBedtimeStory

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ResetTv
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.PageData
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.chaquo.python.Python
import com.example.eunoia.R
import com.example.eunoia.backend.PageBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.CustomizableLRButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.components.WrappedPurpleBackgroundStart
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.retrieveUriDuration
import java.io.File


private var TAG = "Page Screen UIs"

@Composable
fun ChapterBlock(
    navController: NavController,
    chapterData: BedtimeStoryInfoChapterData,
    chapterIndex: Int
){
    var numberOfPages by rememberSaveable { mutableStateOf(-1) }
    PageBackend.queryPageBasedOnChapter(chapterData){
        numberOfPages = it.size
    }

    if(numberOfPages > -1) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    navigateToBedtimeStoryChapterScreen(navController, chapterData, chapterIndex)
                }
        ) {
            val pagesText = if (numberOfPages == 1) "page" else "pages"
            WrappedPurpleBackgroundStart(
                chapterData.displayName,
                "$numberOfPages $pagesText",
                "start"
            ) {
            }
        }
    }
}

@Composable
fun PageBlock(
    navController: NavController,
    pageData: PageData,
    pageIndex: Int,
    chapterData: BedtimeStoryInfoChapterData,
    chapterIndex: Int
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {

            }
    ) {
        CustomizableLRButton(
            text = pageData.displayName,
            height = 55,
            fontSize = 16,
            textColor = Black,
            backgroundColor = WePeep,
            corner = 10,
            borderStroke = 0.0,
            borderColor = Black.copy(alpha = 0F),
            textType = "light",
            maxWidthFraction = 1F,
            R.drawable.little_right_arrow,
            8,
            14,
            BeautyBush
        ) {
            navigateToPageScreen(
                navController,
                pageData,
                chapterData,
                chapterIndex
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordingBlock(
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    takeAction: (index: Int) -> Unit
){
    val context = LocalContext.current
    if(index < pageRecordingFileNames.size) {
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
                        if(pageRecordingFileNames[index].value != "empty recording") {
                            stopPlayingThisPage(generalMediaPlayerService)
                            resetBedtimeStoryRecordUI(index)
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
                    text = pageRecordingFileNames[index].value,
                    height = 55,
                    fontSize = 16,
                    textColor = Black,
                    backgroundColor = pageRecordingFileColors[index].value,
                    corner = 10,
                    borderStroke = 0.0,
                    borderColor = Black.copy(alpha = 0F),
                    textType = "light",
                    maxWidthFraction = 1F
                ) {
                    if (pageRecordingFileNames[index].value == "empty recording") {
                        resetAllPageRecordBedtimeStoryUIMediaPlayers()
                        generalMediaPlayerService.onDestroy()
                        resetRecordingCDT()
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

fun resetBedtimeStoryRecordUI(index: Int) {
    if(index < pageRecordingFileNames.size) {
        if (pageRecordingFileNames[index].value != "empty recording") {
            if(pageRecordingNames[index].value == pageRecordingFileNames[index].value) {
                SoundBackend.deleteAudio(pageRecordingS3Keys[index].value)
                pageRecordingS3Keys[index].value = ""
            }

            recordedPageRecordingAbsolutePath[index].value = ""
            pageRecordingFileColors[index].value = SoftPeach
            pageRecordingFileNames[index].value = "empty recording"
            pageRecordingFileUris[index].value = "".toUri()
            resetRecordBedtimeStoryMediaPlayers(index)
            val mediaPlayer = MediaPlayer()
            pageRecordingFileMediaPlayers[index].value = mediaPlayer
            audioPageRecordingFileLengthMilliSeconds[index].value = 0L

            Log.i(TAG, "ff pageRecordingFileNames[index] ==> ${pageRecordingFileNames[index].value}")
        }
    }
}

fun resetRecordBedtimeStoryMediaPlayers(index: Int){
    if(index < pageRecordingFileMediaPlayers.size) {
        if (pageRecordingFileMediaPlayers[index].value.isPlaying) {
            pageRecordingFileMediaPlayers[index].value.stop()
        }
        pageRecordingFileMediaPlayers[index].value.reset()
        pageRecordingFileMediaPlayers[index].value.release()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToResetBedtimeStoryUI(
    context: Context,
    fileUploaded: () -> Unit
){
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmStateChange = {
            Log.i(TAG, "Confirm state change ==>> $it")
            if(it == DismissValue.DismissedToStart){
                resetBedtimeStoryUploadUI()
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
                text = uploadedFileBedtimeStory.value.name,
                height = 55,
                fontSize = 16,
                textColor = Black,
                backgroundColor = fileColorBedtimeStory.value,
                corner = 10,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "light",
                maxWidthFraction = 1F
            ) {
                if(uploadedFileBedtimeStory.value.name == "Choose a file"){
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fileUploaded()
                    } else {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            3
                        )
                    }
                }else{
                    if(fileMediaPlayerBedtimeStory.value.isPlaying){
                        fileMediaPlayerBedtimeStory.value.stop()
                        fileMediaPlayerBedtimeStory.value.reset()
                    }else {
                        fileMediaPlayerBedtimeStory.value.apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            setDataSource(context, fileUriBedtimeStory.value)
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

fun resetBedtimeStoryUploadUI() {
    uploadedFileBedtimeStory.value = File("Choose a file")
    fileColorBedtimeStory.value = WePeep
    resetUploadBedtimeStoryMediaPlayers()
    fileMediaPlayerBedtimeStory.value = MediaPlayer()
    fileUriBedtimeStory.value = "".toUri()
    audioFileLengthMilliSecondsBedtimeStory.value = 0L
}

var playingIndex by mutableStateOf(-1)

/**
 * Get the first uri for this page.
 * This kick starts the audio playing process for this page
 *
 * @param generalMediaPlayerService is the media player service that plays the audio
 */
fun getInitialUri(generalMediaPlayerService: GeneralMediaPlayerService){
    if(playingIndex < pageRecordingFileNames.size) {
        deActivatePageControls(2)
        deActivatePageControls(3)
        playIt(generalMediaPlayerService)
    }
}

/**
 * Downloads and stores the next audio uri.
 * This method does not play the uri.
 * It just stores it so that it is available when its time to play it
 *
 * @param generalMediaPlayerService is the media player service that plays the audio
 */
private fun getNextUri(generalMediaPlayerService: GeneralMediaPlayerService){
    if(playingIndex < pageRecordingFileUris.size){
        var nxtInd = playingIndex
        nxtInd += 1

        //if all uris have not been played yet
        if(nxtInd < thisPageData!!.audioKeysS3.size) {
            SoundBackend.retrieveAudio(
                thisPageData!!.audioKeysS3[nxtInd],
                globalViewModel_!!.currentUser!!.amplifyAuthUserId
            ) {
                if(nxtInd < pageRecordingFileUris.size) {
                    pageRecordingFileUris[nxtInd].value = it!!
                }else{
                    pageRecordingFileUris.add(mutableStateOf(it!!))
                }
            }
        }
    }
}

/**
 * Start playing the audio uri
 *
 * @param generalMediaPlayerService is the media player service that plays the audio
 */
fun playIt(generalMediaPlayerService: GeneralMediaPlayerService){
    if(playingIndex < pageRecordingFileUris.size) {
        if(pageRecordingFileUris[playingIndex].value.toString().isEmpty()){
            SoundBackend.retrieveAudio(
                pageRecordingS3Keys[playingIndex].value,
                globalViewModel_!!.currentUser!!.amplifyAuthUserId
            ) {
                pageRecordingFileUris[playingIndex].value = it!!
                startPlaying(
                    pageRecordingFileUris[playingIndex].value,
                    generalMediaPlayerService
                )
            }
        }else {
            startPlaying(
                pageRecordingFileUris[playingIndex].value,
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
 * @param generalMediaPlayerService is the media player service that plays the audio
 */
private fun startPlaying(
    uri: Uri,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    if(playingIndex < pageRecordingFileUris.size) {
        if(pageRecordingFileUris[playingIndex].value != "".toUri()){
            generalMediaPlayerService.onDestroy()
            generalMediaPlayerService.setAudioUri(uri)

            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                generalMediaPlayerService.startMediaPlayer()
            } else {
                val intent = Intent()
                intent.action = "PLAY"
                generalMediaPlayerService.onStartCommand(intent, 0, 0)
            }

            for(i in pageRecordingFileColors.indices) {
                Log.i(TAG, "Peaching all")
                pageRecordingFileColors[i].value = Peach
            }
            if(playingIndex < pageRecordingFileColors.size){
                Log.i(TAG, "GHreening this $playingIndex")
                pageRecordingFileColors[playingIndex].value = Color.Green
            }

            startTimer(generalMediaPlayerService)
        }else{
            playingIndex += 1
            if(playingIndex >= pageRecordingFileUris.size){
                resetRecordingCDT()
                playingIndex = -1
                generalMediaPlayerService.onDestroy()
                activatePageControls(2)
                deActivatePageControls(3)
            }else {
                playIt(
                    generalMediaPlayerService
                )
            }
        }
    }
}

fun resetRecordingCDT(){
    if (recordingCDT != null) {
        recordingCDT!!.cancel()
        recordingCDT = null
    }
}

fun resetIndividualCDT(){
    if (individualCDT != null) {
        individualCDT!!.cancel()
        individualCDT = null
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
        playingIndex < pageRecordingFileUris.size &&
        playingIndex < audioPageRecordingFileLengthMilliSeconds.size &&
        playingIndex < pageRecordingFileColors.size
    ){
        startBedtimeStoryRecordingCountDownTimer(
            audioPageRecordingFileLengthMilliSeconds[playingIndex].value,
            generalMediaPlayerService
        ) {
            for(i in pageRecordingFileColors.indices) {
                Log.i(TAG, "Peaching all")
                pageRecordingFileColors[i].value = Peach
            }

            //after a uri is done playing, play the next one
            playingIndex += 1

            resetRecordingCDT()

            //if all uris for this page have been played, stop media player
            if(
                playingIndex >= pageRecordingFileUris.size ||
                pageRecordingFileUris[playingIndex].value == "".toUri()
            ){
                playingIndex = -1
                generalMediaPlayerService.onDestroy()
                activatePageControls(2)
                deActivatePageControls(3)
            }else {
                playIt(
                    generalMediaPlayerService
                )
            }
        }
    }
}

/**
 * reset all the media players for this bedtime story page
 */
fun resetAllPageRecordBedtimeStoryUIMediaPlayers() {
    pageRecordingFileMediaPlayers.forEachIndexed { index, mediaPlayer ->
        resetRecordBedtimeStoryMediaPlayers(index)
        val mp = MediaPlayer()
        pageRecordingFileMediaPlayers[index].value = mp
    }
}

/**
 * Start count down timer
 *
 * @param time duration of the count down timer
 * @param completed runs when count down timer is finished
 */
fun startBedtimeStoryRecordingCountDownTimer(
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
                        Log.i(TAG, "Done with gmp3")
                        generalMediaPlayerService.onDestroy()
                        activatePageControls(2)
                        deActivatePageControls(3)
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

fun setColorToGreen(index: Int){
    pageRecordingFileColors[index].value = Color.Green
}

fun startBedtimeStoryIndividualCountDownTimer(
    time: Long,
    index: Int,
    completed: (index: Int) -> Unit
) {
    if (individualCDT == null) {
        individualCDT = object : CountDownTimer(time, 1) {
            override fun onTick(millisUntilFinished: Long) {
                if(millisUntilFinished % 10000 == 0L) {
                    Log.i(TAG, "individual recording timer: $millisUntilFinished")
                }
            }

            override fun onFinish() {
                Log.i(TAG, "individual stopped")
                completed(index)
            }
        }
    }
    individualCDT!!.start()
}

var individualPlayingIndex = 0

/*fun startIndividualCDT(
    index: Int
){
    resetColors()

    setColorToGreen(index)
    startBedtimeStoryIndividualCountDownTimer(
        remainingIndividualCDTTime.toLong(),
        individualPlayingIndex
    ){
        resetColors()
        if(index + 1 < pageRecordingFileColors.size) {
            resetIndividualCDT()
            individualPlayingIndex = index + 1
            Log.i(TAG, "individualPlayingIndex = $individualPlayingIndex")
            startIndividualCDT(individualPlayingIndex)
        }
    }
}*/

fun combineAudioFiles(
    context: Context
){
    val py = Python.getInstance()
    val pyObj = py.getModule("myscript")
    val clipPaths = mutableListOf<String>()

    pageRecordingFileUris.forEach { uri ->
        clipPaths.add(uri.value.path!!)
    }

    val outFile = File(context.externalCacheDir!!.absolutePath + "/${globalViewModel_!!.currentUser!!.username}_audio.aac")
    Log.i(TAG, "Output file before: 0. ${outFile.length()}")

    pyObj.callAttr(
        "concatenate_audio_pydub",
        clipPaths.toTypedArray(),
        outFile.absolutePath
    )

    Log.i(TAG, "Output file done: 1. ${outFile.length()}")
    startMediaPlayerHere(
        context,
        outFile.toUri()
    )
}

fun startMediaPlayerHere(
    context: Context,
    uri: Uri
){
    MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        setDataSource(context, uri)
        setVolume(1f, 1f)
        prepare()
        start()
    }
}

/*
fun resetColors(){
    pageRecordingFileColors.forEach {
        it.value = Peach
    }
}

fun processIt(
    uriStartIndex: Int,
    context: Context,
    completed: () -> Unit
) {
    var inputStr = ""
    var concatStr = ""
    var j = -1

    val range = uriStartIndex until pageRecordingFileUris.size
    for(i in range){
        inputStr += "-i ${pageRecordingFileUris[i].value.path} "
        j += 1
        concatStr += "[$j:a]"
    }

    Log.i(TAG, "Output file before: 0. ${outFile!!.length()}")

    val rc = FFmpeg.execute("$inputStr-filter_complex \"${concatStr}concat=n=${range.toList().size}:v=0:a=1\" -y ${outFile!!.absolutePath}")

    when (rc) {
        RETURN_CODE_SUCCESS -> {
            Log.i(Config.TAG, "Command execution completed successfully.")
            val len = retrieveUriDuration(
                outFile!!.path,
                context
            )
            Log.i(TAG, "Output file done: 1. $len")
            completed()
        }
        RETURN_CODE_CANCEL -> {
            Log.i(Config.TAG, "Command execution cancelled by user.")
        }
        else -> {
            Log.i(
                Config.TAG,
                String.format("Command execution failed with rc=$rc and the output below.", rc)
            )
            Config.printLastCommandOutput(Log.INFO)
        }
    }
}*/
