package com.example.eunoia.create.createPrayer

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
import com.example.eunoia.viewModels.PrayerRecordingViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File

var recordedFilePrayer = mutableStateOf(File("Record Prayer"))
var recordedFileColorPrayer = mutableStateOf(WePeep)
var recordedFileMediaPlayerPrayer = mutableStateOf(MediaPlayer())
var recordedFileUriPrayer = mutableStateOf("".toUri())
var recordedAudioFileLengthMilliSecondsPrayer = mutableStateOf(0L)
var recordedPrayerAbsolutePath = mutableStateOf("")

var prayerRecordingNames = mutableListOf<MutableState<String>>()
var prayerRecordingS3Keys = mutableListOf<MutableState<String>>()

var recordedPrayerRecordingAbsolutePath = mutableListOf<MutableState<String>>()
var prayerRecordingFileColors = mutableListOf<MutableState<Color>>()
var prayerRecordingFileUris = mutableListOf<MutableState<Uri>>()
var prayerRecordingFileMediaPlayers = mutableListOf<MutableState<MediaPlayer>>()
var prayerRecordingFileNames = mutableListOf<MutableState<String>>()
var audioPrayerRecordingFileLengthMilliSeconds = mutableListOf<MutableState<Long>>()
var selectedPrayerRecordingIndex = 0
var thisPrayerData: PrayerData? = null
var recordingCDT: CountDownTimer? = null
var individualCDT: CountDownTimer? = null

val audioNames = mutableListOf<String>()
val audioKeysS3 = mutableListOf<String>()
val audioLength = mutableListOf<Int>()

var playingIndex by mutableStateOf(-1)

private var TAG = "Prayer Recording"

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
fun RecordPrayerUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    prayerData: PrayerData,
    viewModel: PrayerRecordingViewModel,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    val context = LocalContext.current
    var numberOfRecordings by rememberSaveable { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    thisPrayerData = prayerData
    prayerRecordingNames.clear()
    prayerRecordingS3Keys.clear()
    recordedPrayerRecordingAbsolutePath.clear()
    prayerRecordingFileColors.clear()
    prayerRecordingFileUris.clear()
    prayerRecordingFileMediaPlayers.clear()
    prayerRecordingFileNames.clear()
    audioPrayerRecordingFileLengthMilliSeconds.clear()

    for (i in prayerData.audioKeysS3.indices) {
        prayerRecordingNames.add(remember{mutableStateOf(prayerData.audioNames[i])})
        prayerRecordingS3Keys.add(remember{mutableStateOf(prayerData.audioKeysS3[i])})
    }

    numberOfRecordings = prayerRecordingNames.size

    SetUpConfirmDeletePrayerDialogBoxUI(
        prayerData,
        navController
    )

    SetUpSavedPrayerDialogBoxUI()

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
                savePrayerRecordingToS3AndDB(thisPrayerData!!) {
                    playingIndex = -1
                    viewModel.onStop(generalMediaPlayerService)
                }
            },
            onDestroy = {
                savePrayerRecordingToS3AndDB(thisPrayerData!!) {
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
                    savePrayerRecordingToS3AndDB(prayerData) {
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
                                    openConfirmDeletePrayerDialogBox = true
                                }
                                1 -> {
                                    savePrayerRecordingToS3AndDB(prayerData) {
                                        playingIndex = -1
                                        processCompletePrayer(
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
                                    numberOfRecordings += addNewRecordingToPrayerRecording(
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
                text = prayerData.displayName,
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
                        DisplayPrayerRecordingBlocks(
                            prayerData,
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
            resetPrayerRecordingCDT()
            activatePrayerRecordingControls(index)
        }else{
            if(prayerRecordingNames.isNotEmpty()) {
                playIt(generalMediaPlayerService)
                deActivatePrayerRecordingControls(index)
                deActivatePrayerRecordingControls(index + 1)
            }
        }
    }else {
        if(prayerRecordingNames.isNotEmpty()) {
            resetAllPrayerRecordUIMediaPlayers()
            generalMediaPlayerService.onDestroy()
            resetPrayerRecordingCDT()
            playingIndex = 0
            getInitialUri(generalMediaPlayerService)
            deActivatePrayerRecordingControls(index)
            deActivatePrayerRecordingControls(index + 1)
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
        resetAllPrayerRecordUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        if (recordingCDT != null) {
            recordingCDT!!.cancel()
            recordingCDT = null
        }
        playingIndex = -1
        activatePrayerRecordingControls(3)
        activatePrayerRecordingControls(2)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DisplayPrayerRecordingBlocks(
    prayerData: PrayerData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(prayerRecordingS3Keys.isNotEmpty()){
        for(i in prayerRecordingS3Keys.indices){
            var done by rememberSaveable { mutableStateOf(false) }
            if(prayerRecordingS3Keys[i].value == ""){
                //if recording block has not been recorded into yet
                if(i < prayerRecordingFileNames.size){
                    DisplayPrayerEmptyRecordingBlockIfItAlreadyExist(i)
                }else{
                    DisplayPrayerEmptyRecordingBlockIfItDoesNotExist(i)
                }
                done = true
            }else{
                //if recording block has been recorded into
                if(prayerRecordingS3Keys[i].value != "open"){
                    //if recording is already saved
                    val length = getPrayerRecordingLength(
                        prayerData,
                        i
                    )

                    if(i < prayerRecordingFileNames.size) {
                        DisplayPrayerRecordingBlockIfItAlreadyExist(
                            prayerData,
                            i,
                            length
                        )

                        SoundBackend.retrieveAudio(
                            prayerRecordingS3Keys[i].value,
                            globalViewModel!!.currentUser!!.amplifyAuthUserId
                        ) {
                            updatePrayerRecordingFileUris(
                                it,
                                i
                            )
                            done = true
                        }
                    }else{
                        DisplayPrayerRecordingBlockIfItDoesNotExist(
                            prayerData,
                            i,
                            length
                        )

                        SoundBackend.retrieveAudio(
                            prayerRecordingS3Keys[i].value,
                            globalViewModel!!.currentUser!!.amplifyAuthUserId
                        ) {
                            updatePrayerRecordingFileUris(
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
                PrayerRecordingBlock(
                    i,
                    generalMediaPlayerService
                ) {
                    if(it < recordedPrayerRecordingAbsolutePath.size) {
                        selectedPrayerRecordingIndex = it
                        activatePrayerRecordingControls(2)
                        globalViewModel!!.bottomSheetOpenFor = "recordAudio"
                        recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording =
                            prayerData
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
    recordedPrayerRecordingAbsolutePath.clear()
    prayerRecordingFileColors.clear()
    prayerRecordingFileUris.clear()
    prayerRecordingFileMediaPlayers.clear()
    prayerRecordingFileNames.clear()
    audioPrayerRecordingFileLengthMilliSeconds.clear()
}

@Composable
fun DisplayPrayerRecordingBlockIfItDoesNotExist(
    prayerData: PrayerData,
    i: Int,
    length: Long
) {
    prayerRecordingFileNames.add(remember{mutableStateOf(prayerData.audioNames[i])})
    recordedPrayerRecordingAbsolutePath.add(remember{mutableStateOf("")})

    if(playingIndex == i) {
        prayerRecordingFileColors.add(remember {
            mutableStateOf(
                Color.Green
            )
        })
    }else{
        prayerRecordingFileColors.add(remember {
            mutableStateOf(
                Peach
            )
        })
    }

    prayerRecordingFileMediaPlayers.add(remember{mutableStateOf(MediaPlayer())})
    audioPrayerRecordingFileLengthMilliSeconds.add(remember{mutableStateOf(length)})
}

fun updatePrayerRecordingFileUris(uri: Uri?, i: Int) {
    if(uri != null){
        if(i < prayerRecordingFileUris.size) {
            prayerRecordingFileUris[i].value = uri
        }else{
            prayerRecordingFileUris.add(mutableStateOf(uri))
        }
    }else{
        if(i < prayerRecordingFileUris.size) {
            prayerRecordingFileUris[i].value = "".toUri()
        }else{
            prayerRecordingFileUris.add(mutableStateOf("".toUri()))
        }
    }
}

@Composable
fun DisplayPrayerRecordingBlockIfItAlreadyExist(
    prayerData: PrayerData,
    i: Int,
    length: Long
) {
    prayerRecordingFileNames[i].value = prayerData.audioNames[i]
    recordedPrayerRecordingAbsolutePath[i].value = ""

    if(playingIndex == i) {
        prayerRecordingFileColors[i].value = Color.Green
    }else{
        prayerRecordingFileColors[i].value = Peach
    }

    prayerRecordingFileMediaPlayers[i].value = MediaPlayer()
    audioPrayerRecordingFileLengthMilliSeconds[i].value = length
}

/**
 * Returns the length of a particular recording in a page
 *
 * @param prayerData The self love
 * @param i The index of the particular recording
 * @return The length of the recording
 */
private fun getPrayerRecordingLength(
    prayerData: PrayerData,
    i: Int
): Long {
    var length = 0L
    if(prayerData.audioLengths != null){
        if(prayerData.audioLengths.isNotEmpty()){
            if(i < prayerData.audioLengths.size) {
                length = prayerData.audioLengths[i].toLong()
            }
        }
    }
    return length
}

@Composable
fun DisplayPrayerEmptyRecordingBlockIfItAlreadyExist(i: Int) {
    prayerRecordingFileNames[i].value = "empty recording"
    recordedPrayerRecordingAbsolutePath[i].value = ""

    if(playingIndex == i) {
        prayerRecordingFileColors[i].value = Color.Green
    }else{
        prayerRecordingFileColors[i].value = WePeep
    }
    prayerRecordingFileUris[i].value = "".toUri()
    prayerRecordingFileMediaPlayers[i].value = MediaPlayer()
    audioPrayerRecordingFileLengthMilliSeconds[i].value = 0L
}

@Composable
fun DisplayPrayerEmptyRecordingBlockIfItDoesNotExist(i: Int) {
    prayerRecordingFileNames.add(remember{mutableStateOf("empty recording")})
    recordedPrayerRecordingAbsolutePath.add(remember{mutableStateOf("")})

    if(playingIndex == i) {
        prayerRecordingFileColors.add(remember {
            mutableStateOf(
                Color.Green
            )
        })
    }else{
        prayerRecordingFileColors.add(remember {
            mutableStateOf(
                WePeep
            )
        })
    }

    prayerRecordingFileUris.add(remember{mutableStateOf("".toUri())})
    prayerRecordingFileMediaPlayers.add(remember{mutableStateOf(MediaPlayer())})
    audioPrayerRecordingFileLengthMilliSeconds.add(remember{mutableStateOf(0L)})
}

/**
 * Adds a new recording block to the self love recording page
 *
 * @param generalMediaPlayerService The media player service
 * @return 1 if recording block was added or 0 if otherwise
 */
private fun addNewRecordingToPrayerRecording(
    generalMediaPlayerService: GeneralMediaPlayerService,
): Int{
    if(!generalMediaPlayerService.isMediaPlayerPlaying()) {
        //recording name must be bigger than others and must not exist
        var newRecordingName = "recording 1"

        if(prayerRecordingNames.isNotEmpty()) {
            //names end with a number (recording 4)
            //sort this list by this number
            val sortedNames = prayerRecordingNames.sortedBy {
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
        prayerRecordingNames.add(mutableStateOf(newRecordingName))
        prayerRecordingS3Keys.add(mutableStateOf(newRecordingKeyS3))
        deActivatePrayerRecordingControls(4)
        return 1
    }
    return 0
}

/**
 * Triggers the completePrayer() method
 *
 * @param prayerData The self love object
 * @param context The context
 * @param navController The navController
 */
private fun processCompletePrayer(
    prayerData: PrayerData,
    context: Context,
    navController: NavController
) {
    completePrayer(
        prayerData,
        context
    ) { updatedPrayerData ->
        //Remove this completed self love from list of incomplete self loves
        userPrayers.removeIf {
            it!!.id == updatedPrayerData.id
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
 * @param prayerData The self love object
 * @param context The context
 * @param completed Function that runs when this function is completed
 */
private fun completePrayer(
    prayerData: PrayerData,
    context: Context,
    completed: (prayerData: PrayerData) -> Unit
) {
    val outFile = File(context.externalCacheDir!!.absolutePath + "/${getRandomString(5)}_audio.aac")
    getAllUris(prayerData){
        concatenateAllUris(
            outFile = outFile,
            context = context,
            size = allUriDoubles.size,
            getInputAndConcat = {
                getInputAndConcatForRecordPrayerAndPrayer()
            }
        ) {
            val fullPlayTime = retrieveUriDuration(
                outFile.path,
                context
            )
            Log.i(TAG, "full play ties = $fullPlayTime")

            SoundBackend.storeAudio(
                outFile.absolutePath,
                prayerData.audioKeyS3
            ) { _ ->
                val newPrayer = prayerData.copyOfBuilder()
                    .fullPlayTime(fullPlayTime)
                    .creationStatus(PrayerCreationStatus.COMPLETED)
                    .build()

                PrayerBackend.updatePrayer(newPrayer) {
                    Log.i(TAG, "newPrayer = $it")
                    completed(it)
                }
            }
        }
    }
}

private val allUriDoubles = mutableListOf<AllUriDoubles>()

private fun getAllUris(
    prayerData: PrayerData,
    completed: () -> Unit
) {
    allUriDoubles.clear()
    prayerData.audioKeysS3.forEachIndexed { i, s3Key ->
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
private fun getInputAndConcatForRecordPrayerAndPrayer(): InputAndConcat {
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
fun SetUpSavedPrayerDialogBoxUI(){
    if(openSavedElementDialogBox){
        AlertDialogBox(text = "Prayer saved. We will send you an email when it is approved.")
    }
}

@Composable
fun SetUpConfirmDeletePrayerDialogBoxUI(
    prayerData: PrayerData,
    navController: NavController
) {
    if(openConfirmDeletePrayerDialogBox){
        ConfirmAlertDialog(
            "Are you sure you want to delete this self love?",
            {
                processPrayerDeletion(
                    prayerData,
                    navController
                )
            },
            {
                openConfirmDeletePrayerDialogBox = false
            }
        )
    }
}

/**
 * Triggers the deletePrayer() method
 *
 * @param prayerData The self love object
 * @param navController The nav controller
 */
fun processPrayerDeletion(
    prayerData: PrayerData,
    navController: NavController
) {
    deletePrayer(prayerData){
        //Display the delete confirmation box
        openConfirmDeletePrayerDialogBox = false
        navController.popBackStack()
    }
}

/**
 * Delete self love audio file from s3 and delete self love object from database
 *
 * @param prayerData The self love object
 * @param completed The function that runs after deletion is complete
 */
fun deletePrayer(
    prayerData: PrayerData,
    completed: () -> Unit
) {
    prayerData.audioKeysS3.forEach { s3Key ->
        SoundBackend.deleteAudio(s3Key)
    }
    PrayerBackend.deletePrayer(prayerData){
        completed()
    }
}

fun resetRecordingFileAndMediaPrayer(context: Context) {
    if(recordingFile != null){
        deleteRecordingFile(context)
    }
    if(recordedFileMediaPlayerPrayer.value.isPlaying){
        recordedFileMediaPlayerPrayer.value.stop()
    }
    recordedFileMediaPlayerPrayer.value.reset()
    recordedFileMediaPlayerPrayer.value.release()
}

/**
 * stop playing the uris for this self love
 *
 * @param generalMediaPlayerService The media player service
 */
fun stopPlayingThisPrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    if(generalMediaPlayerService.isMediaPlayerInitialized()){
        resetAllPrayerRecordBedtimeStoryUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        if (recordingCDT != null) {
            recordingCDT!!.cancel()
            recordingCDT = null
        }
        playingIndex = -1
        activatePrayerRecordingControls(3)
        activatePrayerRecordingControls(2)
    }
}

/**
 * Change the appearance of the control button to look activated
 *
 * @param index is the index of the button that was clicked
 */
fun activatePrayerRecordingControls(index: Int){
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
fun deActivatePrayerRecordingControls(index: Int){
    borderControlColors[index].value = Bizarre
    backgroundControlColor1[index].value = White
    backgroundControlColor2[index].value = White
    if(index == 2){
        icons[index].value = R.drawable.pause_icon
    }
}

fun clearPrayerRecordingsList(){
    prayerRecordingNames.clear()
    prayerRecordingS3Keys.clear()
}

private fun storeToS3IfPrayer(
    index: Int,
    prayerData: PrayerData,
    totalNonEmptyBlocks: Int,
    completed: (didUpdate: Boolean) -> Unit
){
    if(index < prayerRecordingFileNames.size) {
        val key = "${globalViewModel!!.currentUser!!.username.lowercase()}/" +
                "routine/" +
                "self-love/" +
                "recorded/" +
                "${prayerData.displayName.lowercase()}/" +
                "recordings/" +
                "${prayerRecordingFileNames[index].value.lowercase()}.aac"

        if (recordedPrayerRecordingAbsolutePath[index].value != "") {
            SoundBackend.storeAudio(
                recordedPrayerRecordingAbsolutePath[index].value,
                key
            ) { s3key ->
                updatePrayerData(
                    s3key,
                    index,
                    totalNonEmptyBlocks,
                    prayerData
                ) { didUpdate ->
                    completed(didUpdate)
                }
            }
        } else {
            updatePrayerData(
                "",
                index,
                totalNonEmptyBlocks,
                prayerData
            ) { didUpdate ->
                completed(didUpdate)
            }
        }
    } else {
        completed(false)
    }
}

private fun updatePrayerData(
    s3Key: String,
    index: Int,
    totalNonEmptyBlocks: Int,
    prayerData: PrayerData,
    completed: (didUpdate: Boolean) -> Unit
) {
    if(index < prayerRecordingFileNames.size) {
        audioNames[index] = prayerRecordingFileNames[index].value
        if(s3Key == ""){
            if(index < prayerData.audioKeysS3.size) {
                audioKeysS3[index] = prayerData.audioKeysS3[index]
            }
        }else {
            audioKeysS3[index] = s3Key
        }
        audioLength[index] = audioPrayerRecordingFileLengthMilliSeconds[index].value.toInt()

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

fun savePrayerRecordingToS3AndDB(
    prayerData: PrayerData,
    completed: (prayerData: PrayerData) -> Unit
){
    audioNames.clear()
    audioLength.clear()
    audioKeysS3.clear()

    var totalNonEmptyBlocks = 0
    var updated = false
    var index = -1

    for(i in prayerRecordingFileNames.indices){
        if(prayerRecordingFileNames[i].value != "empty recording"){
            totalNonEmptyBlocks += 1
        }
    }

    if(prayerRecordingFileNames.size > 0) {
        for(name in prayerRecordingFileNames){
            audioNames.add("")
            audioKeysS3.add("")
            audioLength.add(0)
        }
        for (i in prayerRecordingFileNames.indices) {
            if (prayerRecordingFileNames[i].value != "empty recording") {
                storeToS3IfPrayer(
                    i,
                    prayerData,
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

                        val newPrayer = prayerData.copyOfBuilder()
                            .audioNames(audioNames)
                            .audioKeysS3(audioKeysS3)
                            .audioLengths(audioLength)
                            .build()

                        PrayerBackend.updatePrayer(newPrayer) { updatedPrayer ->
                            updateUserPrayersList(updatedPrayer) {
                                completed(updatedPrayer)
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
        completed(prayerData)
    }

    if(!updated && index == prayerRecordingFileNames.size - 1) {
        if (prayerRecordingFileNames.isNotEmpty()) {
            val newPrayer = prayerData.copyOfBuilder()
                .audioNames(audioNames)
                .audioKeysS3(audioKeysS3)
                .audioLengths(audioLength)
                .build()

            PrayerBackend.updatePrayer(newPrayer) { updatedPrayer ->
                updateUserPrayersList(updatedPrayer){
                    completed(updatedPrayer)
                }
            }
        }
    }
}

fun updateUserPrayersList(updatedPrayer: PrayerData, completed: () -> Unit) {
    var updated = false
    userPrayers.forEachIndexed { index, prayer ->
        if(prayer!!.id == updatedPrayer.id){
            userPrayers[index] = updatedPrayer
            updated = true
            completed()
        }
    }
    if(!updated){
        completed()
    }
}