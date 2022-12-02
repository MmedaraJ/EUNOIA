package com.example.eunoia.create.createSelfLove

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.create.createBedtimeStory.InputAndConcat
import com.example.eunoia.create.createBedtimeStory.concatenateAllUris
import com.example.eunoia.create.createBedtimeStory.userBedtimeStories
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.lifecycle.CustomLifecycleEventListenerProcessor
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.recordAudio.deleteRecordingFile
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingFile
import com.example.eunoia.ui.components.AnImageWithColor
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.getRandomString
import com.example.eunoia.utils.retrieveUriDuration
import com.example.eunoia.viewModels.SelfLoveRecordingViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File

var recordedFileSelfLove = mutableStateOf(File("Record Self Love"))
var recordedFileColorSelfLove = mutableStateOf(WePeep)
var recordedFileMediaPlayerSelfLove = mutableStateOf(MediaPlayer())
var recordedFileUriSelfLove = mutableStateOf("".toUri())
var recordedAudioFileLengthMilliSecondsSelfLove = mutableStateOf(0L)
var recordedSelfLoveAbsolutePath = mutableStateOf("")

var selfLoveRecordingNames = mutableListOf<MutableState<String>>()
var selfLoveRecordingS3Keys = mutableListOf<MutableState<String>>()

var recordedSelfLoveRecordingAbsolutePath = mutableListOf<MutableState<String>>()
var selfLoveRecordingFileColors = mutableListOf<MutableState<Color>>()
var selfLoveRecordingFileUris = mutableListOf<MutableState<Uri>>()
var selfLoveRecordingFileMediaPlayers = mutableListOf<MutableState<MediaPlayer>>()
var selfLoveRecordingFileNames = mutableListOf<MutableState<String>>()
var audioSelfLoveRecordingFileLengthMilliSeconds = mutableListOf<MutableState<Long>>()
var selectedSelfLoveRecordingIndex = 0
var thisSelfLoveData: SelfLoveData? = null
var recordingCDT: CountDownTimer? = null
var individualCDT: CountDownTimer? = null

val audioNames = mutableListOf<String>()
val audioKeysS3 = mutableListOf<String>()
val audioLength = mutableListOf<Int>()

var playingIndex by mutableStateOf(-1)

private var TAG = "Self Love Recording"

private var icons = arrayOf(
    mutableStateOf(R.drawable.ic_baseline_delete),
    mutableStateOf(R.drawable.ic_baseline_check),
    mutableStateOf(R.drawable.play_icon),
    mutableStateOf(R.drawable.ic_baseline_stop),
    mutableStateOf(R.drawable.ic_baseline_mic),
)
private var borderControlColors = arrayOf(
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
private var backgroundControlColor1 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
)
private var backgroundControlColor2 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordSelfLoveUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    selfLoveData: SelfLoveData,
    viewModel: SelfLoveRecordingViewModel,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    val context = LocalContext.current
    var numberOfRecordings by rememberSaveable { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    thisSelfLoveData = selfLoveData
    selfLoveRecordingNames.clear()
    selfLoveRecordingS3Keys.clear()
    recordedSelfLoveRecordingAbsolutePath.clear()
    selfLoveRecordingFileColors.clear()
    selfLoveRecordingFileUris.clear()
    selfLoveRecordingFileMediaPlayers.clear()
    selfLoveRecordingFileNames.clear()
    audioSelfLoveRecordingFileLengthMilliSeconds.clear()

    for (i in selfLoveData.audioKeysS3.indices) {
        selfLoveRecordingNames.add(remember{mutableStateOf(selfLoveData.audioNames[i])})
        selfLoveRecordingS3Keys.add(remember{mutableStateOf(selfLoveData.audioKeysS3[i])})
    }

    numberOfRecordings = selfLoveRecordingNames.size

    SetUpConfirmDeleteSelfLoveDialogBoxUI(
        selfLoveData,
        navController
    )

    SetUpSavedSelfLoveDialogBoxUI()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        CustomLifecycleEventListenerProcessor(
            onResume = {
                viewModel.onStart(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
                playingIndex = -1
            },
            onStop = {
                saveSelfLoveRecordingToS3AndDB(thisSelfLoveData!!) {
                    playingIndex = -1
                    viewModel.onStop(generalMediaPlayerService)
                }
            },
            onDestroy = {
                saveSelfLoveRecordingToS3AndDB(thisSelfLoveData!!) {
                    playingIndex = -1
                    viewModel.onStop(generalMediaPlayerService)
                }
            }
        )

        val (
            header,
            title,
            controls,
            record_column,
            record,
            next
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
            BackArrowHeader(
                {
                    saveSelfLoveRecordingToS3AndDB(selfLoveData) {
                        playingIndex = -1
                        navController.popBackStack()
                    }
                },
                {
                    globalViewModel!!.bottomSheetOpenFor = "controls"
                    openBottomSheet(scope, state)
                },
                {
                    //navController.navigate(Screen.Settings.screen_route)
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(controls) {
                    top.linkTo(header.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ){
            icons.forEachIndexed { index, icon ->
                if(
                    index == 0 ||
                    (index == 1 && numberOfRecordings > 0) ||
                    (index == 2 && numberOfRecordings > 0) ||
                    (index == 3 && numberOfRecordings > 0) ||
                    index == 4
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .gradientBackground(
                                listOf(
                                    backgroundControlColor1[index].value,
                                    backgroundControlColor2[index].value
                                ),
                                angle = 45f
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    borderControlColors[index].value
                                ),
                                RoundedCornerShape(50.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnImageWithColor(
                            icon.value,
                            "icon",
                            borderControlColors[index].value,
                            16.dp,
                            16.dp,
                            0,
                            0
                        ) {
                            when (index) {
                                0 -> {
                                    openConfirmDeleteSelfLoveDialogBox = true
                                }
                                1 -> {
                                    saveSelfLoveRecordingToS3AndDB(selfLoveData) {
                                        playingIndex = -1
                                        processCompleteSelfLove(
                                            it,
                                            context,
                                            navController
                                        )
                                    }
                                }
                                2 -> {
                                    startPlayingThisPage(
                                        generalMediaPlayerService,
                                        index,
                                    )
                                }
                                3 -> {
                                    stopPlayingThisPage(
                                        generalMediaPlayerService,
                                    )
                                }
                                4 -> {
                                    numberOfRecordings += addNewRecordingToSelfLoveRecording(
                                        generalMediaPlayerService,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(controls.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = selfLoveData.displayName,
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(record_column) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .verticalScroll(scrollState),
            ) {
                val (
                    recordings,
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(recordings) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                        .fillMaxWidth()
                ) {
                    if(numberOfRecordings > -1){
                        DisplaySelfLoveRecordingBlocks(
                            selfLoveData,
                            scope,
                            state,
                            generalMediaPlayerService
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

/**
 * start or pause playing the uris for this self love
 *
 * @param index is the index of the play button
 * @param generalMediaPlayerService is the media player service that will play uri
 */
fun startPlayingThisPage(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
){
    if(generalMediaPlayerService.isMediaPlayerInitialized()) {
        if (generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.stopMediaPlayer()
            resetSelfLoveRecordingCDT()
            activateSelfLoveRecordingControls(index)
        }else{
            if(selfLoveRecordingNames.isNotEmpty()) {
                playIt(generalMediaPlayerService)
                deActivateSelfLoveRecordingControls(index)
                deActivateSelfLoveRecordingControls(index + 1)
            }
        }
    }else {
        if(selfLoveRecordingNames.isNotEmpty()) {
            resetAllSelfLoveRecordUIMediaPlayers()
            generalMediaPlayerService.onDestroy()
            resetSelfLoveRecordingCDT()
            playingIndex = 0
            getInitialUri(generalMediaPlayerService)
            deActivateSelfLoveRecordingControls(index)
            deActivateSelfLoveRecordingControls(index + 1)
        }
    }
}

/**
 * stop playing the uris for this self love
 *
 * @param generalMediaPlayerService is the media player service that will play uri
 */
fun stopPlayingThisPage(
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    if(generalMediaPlayerService.isMediaPlayerInitialized()){
        resetAllSelfLoveRecordUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        if (recordingCDT != null) {
            recordingCDT!!.cancel()
            recordingCDT = null
        }
        playingIndex = -1
        activateSelfLoveRecordingControls(3)
        activateSelfLoveRecordingControls(2)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DisplaySelfLoveRecordingBlocks(
    selfLoveData: SelfLoveData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(selfLoveRecordingS3Keys.isNotEmpty()){
        for(i in selfLoveRecordingS3Keys.indices){
            var done by rememberSaveable { mutableStateOf(false) }
            if(selfLoveRecordingS3Keys[i].value == ""){
                //if recording block has not been recorded into yet
                if(i < selfLoveRecordingFileNames.size){
                    DisplaySelfLoveEmptyRecordingBlockIfItAlreadyExist(i)
                }else{
                    DisplaySelfLoveEmptyRecordingBlockIfItDoesNotExist(i)
                }
                done = true
            }else{
                //if recording block has been recorded into
                if(selfLoveRecordingS3Keys[i].value != "open"){
                    //if recording is already saved
                    val length = getSelfLoveRecordingLength(
                        selfLoveData,
                        i
                    )

                    if(i < selfLoveRecordingFileNames.size) {
                        DisplaySelfLoveRecordingBlockIfItAlreadyExist(
                            selfLoveData,
                            i,
                            length
                        )

                        SoundBackend.retrieveAudio(
                            selfLoveRecordingS3Keys[i].value,
                            globalViewModel!!.currentUser!!.amplifyAuthUserId
                        ) {
                            updateSelfLoveRecordingFileUris(
                                it,
                                i
                            )
                            done = true
                        }
                    }else{
                        DisplaySelfLoveRecordingBlockIfItDoesNotExist(
                            selfLoveData,
                            i,
                            length
                        )

                        SoundBackend.retrieveAudio(
                            selfLoveRecordingS3Keys[i].value,
                            globalViewModel!!.currentUser!!.amplifyAuthUserId
                        ) {
                            updateSelfLoveRecordingFileUris(
                                it,
                                i
                            )
                            done = true
                        }
                    }
                }
            }

            if(done) {
                Spacer(modifier = Modifier.height(16.dp))
                SelfLoveRecordingBlock(
                    i,
                    generalMediaPlayerService
                ) {
                    if(it < recordedSelfLoveRecordingAbsolutePath.size) {
                        selectedSelfLoveRecordingIndex = it
                        activateSelfLoveRecordingControls(2)
                        globalViewModel!!.bottomSheetOpenFor = "recordAudio"
                        recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording =
                            selfLoveData
                        openBottomSheet(scope, state)
                    }
                }
            }
        }
    }else{
        clearAllPageRecordingLists()
    }
}

fun clearAllPageRecordingLists() {
    recordedSelfLoveRecordingAbsolutePath.clear()
    selfLoveRecordingFileColors.clear()
    selfLoveRecordingFileUris.clear()
    selfLoveRecordingFileMediaPlayers.clear()
    selfLoveRecordingFileNames.clear()
    audioSelfLoveRecordingFileLengthMilliSeconds.clear()
}

@Composable
fun DisplaySelfLoveRecordingBlockIfItDoesNotExist(
    selfLoveData: SelfLoveData,
    i: Int,
    length: Long
) {
    selfLoveRecordingFileNames.add(remember{mutableStateOf(selfLoveData.audioNames[i])})
    recordedSelfLoveRecordingAbsolutePath.add(remember{mutableStateOf("")})

    if(playingIndex == i) {
        selfLoveRecordingFileColors.add(remember {
            mutableStateOf(
                Color.Green
            )
        })
    }else{
        selfLoveRecordingFileColors.add(remember {
            mutableStateOf(
                Peach
            )
        })
    }

    selfLoveRecordingFileMediaPlayers.add(remember{mutableStateOf(MediaPlayer())})
    audioSelfLoveRecordingFileLengthMilliSeconds.add(remember{mutableStateOf(length)})
}

fun updateSelfLoveRecordingFileUris(uri: Uri?, i: Int) {
    if(uri != null){
        if(i < selfLoveRecordingFileUris.size) {
            selfLoveRecordingFileUris[i].value = uri
        }else{
            selfLoveRecordingFileUris.add(mutableStateOf(uri))
        }
    }else{
        if(i < selfLoveRecordingFileUris.size) {
            selfLoveRecordingFileUris[i].value = "".toUri()
        }else{
            selfLoveRecordingFileUris.add(mutableStateOf("".toUri()))
        }
    }
}

@Composable
fun DisplaySelfLoveRecordingBlockIfItAlreadyExist(
    selfLoveData: SelfLoveData,
    i: Int,
    length: Long
) {
    selfLoveRecordingFileNames[i].value = selfLoveData.audioNames[i]
    recordedSelfLoveRecordingAbsolutePath[i].value = ""

    if(playingIndex == i) {
        selfLoveRecordingFileColors[i].value = Color.Green
    }else{
        selfLoveRecordingFileColors[i].value = Peach
    }

    selfLoveRecordingFileMediaPlayers[i].value = MediaPlayer()
    audioSelfLoveRecordingFileLengthMilliSeconds[i].value = length
}

/**
 * Returns the length of a particular recording in a page
 *
 * @param selfLoveData The self love
 * @param i The index of the particular recording
 * @return The length of the recording
 */
private fun getSelfLoveRecordingLength(
    selfLoveData: SelfLoveData, 
    i: Int
): Long {
    var length = 0L
    if(selfLoveData.audioLengths != null){
        if(selfLoveData.audioLengths.isNotEmpty()){
            if(i < selfLoveData.audioLengths.size) {
                length = selfLoveData.audioLengths[i].toLong()
            }
        }
    }
    return length
}

@Composable
fun DisplaySelfLoveEmptyRecordingBlockIfItAlreadyExist(i: Int) {
    selfLoveRecordingFileNames[i].value = "empty recording"
    recordedSelfLoveRecordingAbsolutePath[i].value = ""

    if(playingIndex == i) {
        selfLoveRecordingFileColors[i].value = Color.Green
    }else{
        selfLoveRecordingFileColors[i].value = WePeep
    }
    selfLoveRecordingFileUris[i].value = "".toUri()
    selfLoveRecordingFileMediaPlayers[i].value = MediaPlayer()
    audioSelfLoveRecordingFileLengthMilliSeconds[i].value = 0L
}

@Composable
fun DisplaySelfLoveEmptyRecordingBlockIfItDoesNotExist(i: Int) {
    selfLoveRecordingFileNames.add(remember{mutableStateOf("empty recording")})
    recordedSelfLoveRecordingAbsolutePath.add(remember{mutableStateOf("")})

    if(playingIndex == i) {
        selfLoveRecordingFileColors.add(remember {
            mutableStateOf(
                Color.Green
            )
        })
    }else{
        selfLoveRecordingFileColors.add(remember {
            mutableStateOf(
                WePeep
            )
        })
    }

    selfLoveRecordingFileUris.add(remember{mutableStateOf("".toUri())})
    selfLoveRecordingFileMediaPlayers.add(remember{mutableStateOf(MediaPlayer())})
    audioSelfLoveRecordingFileLengthMilliSeconds.add(remember{mutableStateOf(0L)})
}

/**
 * Adds a new recording block to the self love recording page
 *
 * @param generalMediaPlayerService The media player service
 * @return 1 if recording block was added or 0 if otherwise
 */
private fun addNewRecordingToSelfLoveRecording(
    generalMediaPlayerService: GeneralMediaPlayerService,
): Int{
    if(!generalMediaPlayerService.isMediaPlayerPlaying()) {
        //recording name must be bigger than others and must not exist
        var newRecordingName = "recording 1"

        if(selfLoveRecordingNames.isNotEmpty()) {
            //names end with a number (recording 4)
            //sort this list by this number
            val sortedNames = selfLoveRecordingNames.sortedBy {
                //use split() to get the number associated with a recording
                it.value.split(" ")[1].toInt()
            }
            //Make the new recording name have a number that is one higher than the biggest number 
            //in the list
            val lastName = sortedNames.last().value
            val lastNumber = lastName.split(" ")[1].toInt()
            newRecordingName = "recording ${lastNumber + 1}"
        }

        val newRecordingKeyS3 = ""
        //add the new recording name and empty s3Key to the lists
        selfLoveRecordingNames.add(mutableStateOf(newRecordingName))
        selfLoveRecordingS3Keys.add(mutableStateOf(newRecordingKeyS3))
        deActivateSelfLoveRecordingControls(4)
        return 1
    }
    return 0
}

/**
 * Triggers the completeSelfLove() method
 * 
 * @param selfLoveData The self love object
 * @param context The context
 * @param navController The navController
 */
private fun processCompleteSelfLove(
    selfLoveData: SelfLoveData,
    context: Context,
    navController: NavController
) {
    completeSelfLove(
        selfLoveData,
        context
    ) { updatedSelfLoveData ->
        //Remove this completed self love from list of incomplete self loves
        userSelfLoves.removeIf {
            it!!.id == updatedSelfLoveData.id
        }
        openSavedElementDialogBox = true
        Thread.sleep(10_000)
        navController.popBackStack()
    }
}

/**
 * 1. Get all uris from the self love recording page.
 * 2. Use Ffmpeg to concatenate all recordings into the output file
 * 3. Store the output file in s3
 * 4. Update the self love object
 * 
 * @param selfLoveData The self love object
 * @param context The context
 * @param completed Function that runs when this function is completed
 */
private fun completeSelfLove(
    selfLoveData: SelfLoveData,
    context: Context,
    completed: (selfLoveData: SelfLoveData) -> Unit
) {
    val outFile = File(context.externalCacheDir!!.absolutePath + "/${getRandomString(5)}_audio.aac")
    getAllUris(selfLoveData){
        concatenateAllUris(
            outFile = outFile, 
            context = context,
            size = allUriDoubles.size,
            getInputAndConcat = {
                getInputAndConcatForRecordSelfLoveAndPrayer()
            }
        ) {
            val fullPlayTime = retrieveUriDuration(
                outFile.path,
                context
            )
            Log.i(TAG, "full play ties = $fullPlayTime")

            SoundBackend.storeAudio(
                outFile.absolutePath,
                selfLoveData.audioKeyS3
            ) { _ ->
                val newSelfLove = selfLoveData.copyOfBuilder()
                    .fullPlayTime(fullPlayTime)
                    .creationStatus(SelfLoveCreationStatus.COMPLETED)
                    .build()

                SelfLoveBackend.updateSelfLove(newSelfLove) {
                    Log.i(TAG, "newSelfLove = $it")
                    completed(it)
                }
            }
        }
    }
}

private val allUriDoubles = mutableListOf<AllUriDoubles>()

private fun getAllUris(
    selfLoveData: SelfLoveData,
    completed: () -> Unit
) {
    allUriDoubles.clear()
    selfLoveData.audioKeysS3.forEachIndexed { i, s3Key ->
        val double = AllUriDoubles(
            i,
            "".toUri()
        )
        allUriDoubles.add(double)
        retrieveCorrespondingUri(
            s3Key,
            allUriDoubles.size - 1,
        ){
            completed()
        }
    }
}

private fun retrieveCorrespondingUri(
    s3Key: String?,
    i: Int,
    completed: () -> Unit
) {
    SoundBackend.retrieveAudio(
        s3Key!!,
        globalViewModel!!.currentUser!!.amplifyAuthUserId
    ) {
        if (it != null) {
            allUriDoubles[i].uri = it

            if(
                allUriDoubles.none{ triple ->
                    triple.uri == "".toUri()
                }
            ){
                allUriDoubles.sortWith(
                    compareBy { double ->
                        double.index
                    },
                )
                completed()
            }
        }
    }
}

data class AllUriDoubles(
    var index: Int,
    var uri: Uri
)

/**
 * Create the input and concat strings used to concatenate recording audios
 * @return InputAndConcat(inputStr, concatStr) object
 */
private fun getInputAndConcatForRecordSelfLoveAndPrayer(): InputAndConcat {
    var inputStr = ""
    var concatStr = ""

    //input string format is "-i URI_PATH " for all the uris in list
    //concat string format is "[INDEX:a]" for all the uris in list
    for(i in allUriDoubles.indices){
        inputStr += "-i ${allUriDoubles[i].uri.path} "
        concatStr += "[$i:a]"
    }

    return InputAndConcat(inputStr, concatStr)
}

@Composable
fun SetUpSavedSelfLoveDialogBoxUI(){
    if(openSavedElementDialogBox){
        AlertDialogBox(text = "Self Love saved. We will send you an email when it is approved.")
    }
}

@Composable
fun SetUpConfirmDeleteSelfLoveDialogBoxUI(
    selfLoveData: SelfLoveData,
    navController: NavController
) {
    if(openConfirmDeleteSelfLoveDialogBox){
        ConfirmAlertDialog(
            "Are you sure you want to delete this self love?",
            {
                processSelfLoveDeletion(
                    selfLoveData,
                    navController
                )
            },
            {
                openConfirmDeleteSelfLoveDialogBox = false
            }
        )
    }
}

/**
 * Triggers the deleteSelfLove() method
 * 
 * @param selfLoveData The self love object
 * @param navController The nav controller
 */
fun processSelfLoveDeletion(
    selfLoveData: SelfLoveData,
    navController: NavController
) {
    deleteSelfLove(selfLoveData){
        //Display the delete confirmation box
        openConfirmDeleteSelfLoveDialogBox = false
        navController.popBackStack()
    }
}

/**
 * Delete self love audio file from s3 and delete self love object from database
 * 
 * @param selfLoveData The self love object
 * @param completed The function that runs after deletion is complete
 */
fun deleteSelfLove(
    selfLoveData: SelfLoveData,
    completed: () -> Unit
) {
    selfLoveData.audioKeysS3.forEach { s3Key ->
        SoundBackend.deleteAudio(s3Key)
    }
    SelfLoveBackend.deleteSelfLove(selfLoveData){
        completed()
    }
}

fun resetRecordingFileAndMediaSelfLove(context: Context) {
    if(recordingFile != null){
        deleteRecordingFile(context)
    }
    if(recordedFileMediaPlayerSelfLove.value.isPlaying){
        recordedFileMediaPlayerSelfLove.value.stop()
    }
    recordedFileMediaPlayerSelfLove.value.reset()
    recordedFileMediaPlayerSelfLove.value.release()
}

/**
 * stop playing the uris for this self love
 *
 * @param generalMediaPlayerService The media player service
 */
fun stopPlayingThisSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    if(generalMediaPlayerService.isMediaPlayerInitialized()){
        resetAllSelfLoveRecordBedtimeStoryUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        if (recordingCDT != null) {
            recordingCDT!!.cancel()
            recordingCDT = null
        }
        playingIndex = -1
        activateSelfLoveRecordingControls(3)
        activateSelfLoveRecordingControls(2)
    }
}

/**
 * Change the appearance of the control button to look activated
 *
 * @param index is the index of the button that was clicked
 */
fun activateSelfLoveRecordingControls(index: Int){
    borderControlColors[index].value = Black
    backgroundControlColor1[index].value = SoftPeach
    backgroundControlColor2[index].value = Solitude
    if(index == 2){
        icons[index].value = R.drawable.play_icon
    }
}

/**
 * Change the appearance of the control button to look deactivated
 *
 * @param index is the index of the button that was clicked
 */
fun deActivateSelfLoveRecordingControls(index: Int){
    borderControlColors[index].value = Bizarre
    backgroundControlColor1[index].value = White
    backgroundControlColor2[index].value = White
    if(index == 2){
        icons[index].value = R.drawable.pause_icon
    }
}

fun clearSelfLoveRecordingsList(){
    selfLoveRecordingNames.clear()
    selfLoveRecordingS3Keys.clear()
}

private fun storeToS3IfSelfLove(
    index: Int,
    selfLoveData: SelfLoveData,
    totalNonEmptyBlocks: Int,
    completed: (didUpdate: Boolean) -> Unit
){
    if(index < selfLoveRecordingFileNames.size) {
        val key = "${globalViewModel!!.currentUser!!.username.lowercase()}/" +
                "routine/" +
                "self-love/" +
                "recorded/" +
                "${selfLoveData.displayName.lowercase()}/" +
                "recordings/" +
                "${selfLoveRecordingFileNames[index].value.lowercase()}.aac"

        if (recordedSelfLoveRecordingAbsolutePath[index].value != "") {
            SoundBackend.storeAudio(
                recordedSelfLoveRecordingAbsolutePath[index].value,
                key
            ) { s3key ->
                updateSelfLoveData(
                    s3key,
                    index,
                    totalNonEmptyBlocks,
                    selfLoveData
                ) { didUpdate ->
                    completed(didUpdate)
                }
            }
        } else {
            updateSelfLoveData(
                "",
                index,
                totalNonEmptyBlocks,
                selfLoveData
            ) { didUpdate ->
                completed(didUpdate)
            }
        }
    } else {
        completed(false)
    }
}

private fun updateSelfLoveData(
    s3Key: String,
    index: Int,
    totalNonEmptyBlocks: Int,
    selfLoveData: SelfLoveData,
    completed: (didUpdate: Boolean) -> Unit
) {
    if(index < selfLoveRecordingFileNames.size) {
        audioNames[index] = selfLoveRecordingFileNames[index].value
        if(s3Key == ""){
            if(index < selfLoveData.audioKeysS3.size) {
                audioKeysS3[index] = selfLoveData.audioKeysS3[index]
            }
        }else {
            audioKeysS3[index] = s3Key
        }
        audioLength[index] = audioSelfLoveRecordingFileLengthMilliSeconds[index].value.toInt()

        val audioNamesCount = audioNames.count {
            it != ""
        }
        if(audioNamesCount == totalNonEmptyBlocks){
            completed(true)
        }
    }else{
        completed(false)
    }
}

fun saveSelfLoveRecordingToS3AndDB(
    selfLoveData: SelfLoveData,
    completed: (selfLoveData: SelfLoveData) -> Unit
){
    audioNames.clear()
    audioLength.clear()
    audioKeysS3.clear()

    var totalNonEmptyBlocks = 0
    var updated = false
    var index = -1

    for(i in selfLoveRecordingFileNames.indices){
        if(selfLoveRecordingFileNames[i].value != "empty recording"){
            totalNonEmptyBlocks += 1
        }
    }

    if(selfLoveRecordingFileNames.size > 0) {
        for(name in selfLoveRecordingFileNames){
            audioNames.add("")
            audioKeysS3.add("")
            audioLength.add(0)
        }
        for (i in selfLoveRecordingFileNames.indices) {
            if (selfLoveRecordingFileNames[i].value != "empty recording") {
                storeToS3IfSelfLove(
                    i,
                    selfLoveData,
                    totalNonEmptyBlocks,
                ) {
                    if (it) {
                        updated = true

                        val removed = audioNames.removeIf { name ->
                            name == ""
                        }
                        if(removed) {
                            audioKeysS3.removeIf { key ->
                                key == ""
                            }
                            audioLength.removeIf { length ->
                                length == 0
                            }
                        }

                        val newSelfLove = selfLoveData.copyOfBuilder()
                            .audioNames(audioNames)
                            .audioKeysS3(audioKeysS3)
                            .audioLengths(audioLength)
                            .build()

                        SelfLoveBackend.updateSelfLove(newSelfLove) { updatedSelfLove ->
                            updateUserSelfLovesList(updatedSelfLove) {
                                completed(updatedSelfLove)
                            }
                        }
                    }else{
                        index = i
                    }
                }
            }else{
                index = i
            }
        }
    }else{
        completed(selfLoveData)
    }

    if(!updated && index == selfLoveRecordingFileNames.size - 1) {
        if (selfLoveRecordingFileNames.isNotEmpty()) {
            val newSelfLove = selfLoveData.copyOfBuilder()
                .audioNames(audioNames)
                .audioKeysS3(audioKeysS3)
                .audioLengths(audioLength)
                .build()

            SelfLoveBackend.updateSelfLove(newSelfLove) { updatedSelfLove ->
                updateUserSelfLovesList(updatedSelfLove){
                    completed(updatedSelfLove)
                }
            }
        }
    }
}

fun updateUserSelfLovesList(updatedSelfLove: SelfLoveData, completed: () -> Unit) {
    var updated = false
    userSelfLoves.forEachIndexed { index, selfLove ->
        if(selfLove!!.id == updatedSelfLove.id){
            userSelfLoves[index] = updatedSelfLove
            updated = true
            completed()
        }
    }
    if(!updated){
        completed()
    }
}
