package com.example.eunoia.create.createBedtimeStory

import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.R
import com.example.eunoia.backend.BedtimeStoryChapterBackend
import com.example.eunoia.backend.PageBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.bedtimeStory.bedtimeStoryActivityUris
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.lifecycle.CustomLifecycleEventListener
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingFile
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.getRandomString
import com.example.eunoia.utils.retrieveUriDuration
import com.example.eunoia.viewModels.PageViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File

var pageRecordingNames = mutableListOf<MutableState<String>>()
var pageRecordingS3Keys = mutableListOf<MutableState<String>>()

var recordedPageRecordingAbsolutePath = mutableListOf<MutableState<String>>()
var pageRecordingFileColors = mutableListOf<MutableState<Color>>()
var pageRecordingFileUris = mutableListOf<MutableState<Uri>>()
var pageRecordingFileMediaPlayers = mutableListOf<MutableState<MediaPlayer>>()
var pageRecordingFileNames = mutableListOf<MutableState<String>>()
var audioPageRecordingFileLengthMilliSeconds = mutableListOf<MutableState<Long>>()
var selectedPageRecordingIndex = 0
var thisChapterIndex = -1
var thisPageData: PageData? = null
var thisChapterData: BedtimeStoryInfoChapterData? = null
var recordingCDT: CountDownTimer? = null
var individualCDT: CountDownTimer? = null

val audioNames = mutableListOf<String>()
val audioKeysS3 = mutableListOf<String>()
val audioLength = mutableListOf<Int>()

private var icons = arrayOf(
    mutableStateOf(R.drawable.ic_baseline_delete),
    mutableStateOf(R.drawable.back_arrow),
    mutableStateOf(R.drawable.play_icon),
    mutableStateOf(R.drawable.ic_baseline_stop),
    mutableStateOf(R.drawable.ic_baseline_arrow_right_alt),
    mutableStateOf(R.drawable.ic_baseline_mic),
)
private var borderControlColors = arrayOf(
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
private var backgroundControlColor1 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)
private var backgroundControlColor2 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)

private var TAG = "Page Screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageScreenUI(
    navController: NavController,
    pageData: PageData,
    chapterData: BedtimeStoryInfoChapterData,
    chapterIndex: Int,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    viewModel: PageViewModel,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    val context = LocalContext.current
    var numberOfRecordings by rememberSaveable { mutableStateOf(0) }

    val scrollState = rememberScrollState()

    thisPageData = pageData
    thisChapterData = chapterData
    thisChapterIndex = chapterIndex

    pageRecordingNames.clear()
    pageRecordingS3Keys.clear()
    recordedPageRecordingAbsolutePath.clear()
    pageRecordingFileColors.clear()
    pageRecordingFileUris.clear()
    pageRecordingFileMediaPlayers.clear()
    pageRecordingFileNames.clear()
    audioPageRecordingFileLengthMilliSeconds.clear()

    Log.i(TAG, "pageData.audioNames for order = ${pageData.audioNames}")
    Log.i(TAG, "pageData.audioKeysS3 for order = ${pageData.audioKeysS3}")

    for (i in pageData.audioKeysS3.indices) {
        pageRecordingNames.add(remember{mutableStateOf(pageData.audioNames[i])})
        pageRecordingS3Keys.add(remember{mutableStateOf(pageData.audioKeysS3[i])})
    }

    Log.i(TAG, "pageRecordingNames for order = $pageRecordingNames")
    numberOfRecordings = pageRecordingNames.size

    SetUpConfirmDeletePageDialogBoxUI(
        pageData,
        navController
    )

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxHeight(),
    ) {
        CustomLifecycleEventListener { event ->
            when(event){
                Lifecycle.Event.ON_CREATE -> {
                    Log.i(TAG, "ON_CREATE here")
                }
                Lifecycle.Event.ON_START -> {
                    Log.i(TAG, "ON_START here")
                }
                Lifecycle.Event.ON_RESUME -> {
                    Log.i(TAG, "ON_RESUME here")
                    viewModel.onStart(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context
                    )
                    playingIndex = -1
                }
                Lifecycle.Event.ON_PAUSE -> {
                    Log.i(TAG, "ON_PAUSE here")
                }
                Lifecycle.Event.ON_STOP -> {
                    Log.i(TAG, "ON_STOP here $pageData")
                    Log.i(TAG, "ON_STOP here thisPageData $thisPageData")
                    saveRecordingToS3AndDB(thisPageData!!) {
                        playingIndex = -1
                        viewModel.onStop(generalMediaPlayerService)
                    }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    Log.i(TAG, "ON_DESTROY here")
                    saveRecordingToS3AndDB(thisPageData!!) {
                        playingIndex = -1
                        viewModel.onStop(generalMediaPlayerService)
                    }
                }
                Lifecycle.Event.ON_ANY -> {
                    Log.i(TAG, "ON_ANY here")
                }
            }
        }

        val (
            header,
            title,
            controls,
            save1,
            page_column,
            all_recordings,
            delete,
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                //.fillMaxWidth()
        ) {
            BackArrowHeader(
                {
                    saveRecordingToS3AndDB(thisPageData!!) {
                        playingIndex = -1
                        navController.popBackStack()
                    }
                },
                {
                    globalViewModel_!!.bottomSheetOpenFor = "controls"
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
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            backgroundControlColor1[0].value,
                            backgroundControlColor2[0].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            borderControlColors[0].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icons[0].value,
                    "delete page icon",
                    borderControlColors[0].value,
                    16.dp,
                    16.dp,
                    0,
                    0
                ) {
                    //delete page
                    openConfirmDeletePageDialogBox = true
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(0.4f)
            ){
                icons.forEachIndexed { index, icon ->
                    if(
                        /*(index == 1 && pageIndex > 0) ||*/
                        index == 2 ||
                        index == 3
                        /*(index == 4 && pageIndex < getChapterPageSize(chapterIndex) - 1)*/
                    ){
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
                                when(index){
                                    1 -> {
                                    }
                                    //if play/pause button is clicked
                                    2 -> {
                                        startPlayingThisPage(
                                            generalMediaPlayerService,
                                            index,
                                            //context
                                        )
                                    }
                                    //if next page button is clicked
                                    3 -> {
                                        stopPlayingThisPage(
                                            generalMediaPlayerService,
                                        )
                                    }
                                    4 -> {
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            backgroundControlColor1[5].value,
                            backgroundControlColor2[5].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            borderControlColors[5].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icons[5].value,
                    "new recording icon",
                    borderControlColors[5].value,
                    16.dp,
                    16.dp,
                    0,
                    0
                ) {
                    if(!generalMediaPlayerService.isMediaPlayerPlaying()) {
                        //recording name must be bigger than others and must not exist
                        var newRecordingName = "recording 1"

                        if(pageRecordingNames.isNotEmpty()) {
                            val sortedNames = pageRecordingNames.sortedBy {
                                it.value.split(" ")[1].toInt()
                            }
                            Log.i(TAG, "sortedNames = $sortedNames")
                            val lastName = sortedNames.last().value
                            Log.i(TAG, "lastName = $lastName")
                            val lastNumber = lastName.split(" ")[1].toInt()
                            Log.i(TAG, "lastNumber = $lastNumber")
                            newRecordingName = "recording ${lastNumber + 1}"
                        }

                        val newRecordingKeyS3 = ""
                        pageRecordingNames.add(mutableStateOf(newRecordingName))
                        pageRecordingS3Keys.add(mutableStateOf(newRecordingKeyS3))
                        numberOfRecordings += 1
                        deActivatePageControls(5)
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
                text = pageData.displayName,
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(page_column) {
                    top.linkTo(title.bottom, margin = 12.dp)
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
                    spacer,
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
                    Log.i(TAG, "We arreived clear")

                    if(numberOfRecordings > -1/* && pageRecordingrs.isNotEmpty() && pageRecordingrs.isNotEmpty()*/){
                        if(pageRecordingS3Keys.isNotEmpty()){
                            for(i in pageRecordingS3Keys.indices){
                                var done by rememberSaveable { mutableStateOf(false) }
                                Log.i(TAG, "Were in values1")
                                if(pageRecordingS3Keys[i].value == ""){
                                    Log.i(TAG, "Were in values2")
                                    if(i < pageRecordingFileNames.size){
                                        pageRecordingFileNames[i].value = "empty recording"
                                        recordedPageRecordingAbsolutePath[i].value = ""

                                        if(playingIndex == i) {
                                            pageRecordingFileColors[i].value = Color.Green
                                        }else{
                                            pageRecordingFileColors[i].value = WePeep
                                        }
                                        pageRecordingFileUris[i].value = "".toUri()
                                        pageRecordingFileMediaPlayers[i].value = MediaPlayer()
                                        audioPageRecordingFileLengthMilliSeconds[i].value = 0L
                                    }else{
                                        pageRecordingFileNames.add(remember{mutableStateOf("empty recording")})
                                        recordedPageRecordingAbsolutePath.add(remember{mutableStateOf("")})

                                        if(playingIndex == i) {
                                            pageRecordingFileColors.add(remember {
                                                mutableStateOf(
                                                    Color.Green
                                                )
                                            })
                                        }else{
                                            pageRecordingFileColors.add(remember {
                                                mutableStateOf(
                                                    WePeep
                                                )
                                            })
                                        }

                                        pageRecordingFileUris.add(remember{mutableStateOf("".toUri())})
                                        pageRecordingFileMediaPlayers.add(remember{mutableStateOf(MediaPlayer())})
                                        audioPageRecordingFileLengthMilliSeconds.add(remember{mutableStateOf(0L)})
                                    }
                                    Log.i(TAG, "ff22 pageRecordingFileNames ==> $pageRecordingFileNames")
                                    done = true
                                }else{
                                    if(pageRecordingS3Keys[i].value != "open"){
                                        var length = 0L
                                        if(pageData.audioLength != null){
                                            Log.i(TAG, "Were in lengths")
                                            if(pageData.audioLength.isNotEmpty()){
                                                Log.i(TAG, "Were in lengths1")
                                                if(i < pageData.audioLength.size) {
                                                    Log.i(TAG, "Were in length2s")
                                                    length = pageData.audioLength[i].toLong()
                                                    Log.i(TAG, "Were in lengths3")
                                                }
                                            }
                                        }
                                        if(i < pageRecordingFileNames.size) {
                                            pageRecordingFileNames[i].value = pageData.audioNames[i]
                                            Log.i(TAG, "ff221 pageRecordingFileNames ==> ${pageRecordingFileNames}")
                                            recordedPageRecordingAbsolutePath[i].value = ""

                                            if(playingIndex == i) {
                                                pageRecordingFileColors[i].value = Color.Green
                                            }else{
                                                pageRecordingFileColors[i].value = Peach
                                            }

                                            pageRecordingFileMediaPlayers[i].value = MediaPlayer()
                                            audioPageRecordingFileLengthMilliSeconds[i].value = length
                                            //pageRecordingFileUris[i].value = "".toUri()

                                            SoundBackend.retrieveAudio(
                                                pageRecordingS3Keys[i].value,
                                                globalViewModel_!!.currentUser!!.amplifyAuthUserId
                                            ) {
                                                if(it != null){
                                                    if(i < pageRecordingFileUris.size) {
                                                        pageRecordingFileUris[i].value = it
                                                    }else{
                                                        pageRecordingFileUris.add(mutableStateOf(it))
                                                    }
                                                }else{
                                                    if(i < pageRecordingFileUris.size) {
                                                        pageRecordingFileUris[i].value = "".toUri()
                                                    }else{
                                                        pageRecordingFileUris.add(mutableStateOf("".toUri()))
                                                    }
                                                }
                                                done = true
                                            }
                                        }else{
                                            pageRecordingFileNames.add(remember{mutableStateOf(pageData.audioNames[i])})
                                            Log.i(TAG, "ff221 pageRecordingFileNames ==> $pageRecordingFileNames")
                                            recordedPageRecordingAbsolutePath.add(remember{mutableStateOf("")})

                                            if(playingIndex == i) {
                                                pageRecordingFileColors.add(remember {
                                                    mutableStateOf(
                                                        Color.Green
                                                    )
                                                })
                                            }else{
                                                pageRecordingFileColors.add(remember {
                                                    mutableStateOf(
                                                        Peach
                                                    )
                                                })
                                            }

                                            pageRecordingFileMediaPlayers.add(remember{mutableStateOf(MediaPlayer())})
                                            audioPageRecordingFileLengthMilliSeconds.add(remember{mutableStateOf(length)})

                                            SoundBackend.retrieveAudio(
                                                pageRecordingS3Keys[i].value,
                                                globalViewModel_!!.currentUser!!.amplifyAuthUserId
                                            ) {
                                                if(it != null){
                                                    if(i < pageRecordingFileUris.size) {
                                                        pageRecordingFileUris[i].value = it
                                                    }else{
                                                        pageRecordingFileUris.add(mutableStateOf(it))
                                                    }
                                                }else{
                                                    if(i < pageRecordingFileUris.size) {
                                                        pageRecordingFileUris[i].value = "".toUri()
                                                    }else{
                                                        pageRecordingFileUris.add(mutableStateOf("".toUri()))
                                                    }
                                                }
                                                done = true
                                            }
                                        }
                                    }
                                }

                                if(done) {
                                    Log.i(TAG, "DOner $i")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    RecordingBlock(
                                        i,
                                        generalMediaPlayerService
                                    ) {
                                        Log.i(TAG, "recordedPageRecordingAbsolutePath iz ${recordedPageRecordingAbsolutePath.size}")
                                        if(it < recordedPageRecordingAbsolutePath.size) {
                                            Log.i(TAG, "selectedPageRecordingIndex iz $it")
                                            selectedPageRecordingIndex = it
                                            activatePageControls(2)
                                            globalViewModel_!!.bottomSheetOpenFor = "recordAudio"
                                            recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording =
                                                pageData
                                            openBottomSheet(scope, state)
                                        }
                                    }
                                }
                            }
                        }else{
                            recordedPageRecordingAbsolutePath.clear()
                            pageRecordingFileColors.clear()
                            pageRecordingFileUris.clear()
                            pageRecordingFileMediaPlayers.clear()
                            pageRecordingFileNames.clear()
                            audioPageRecordingFileLengthMilliSeconds.clear()
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun SetUpConfirmDeletePageDialogBoxUI(
    pageData: PageData,
    navController: NavController
) {
    if(openConfirmDeletePageDialogBox){
        ConfirmAlertDialog(
            "Are you sure you want to delete this page?",
            {
                processPageDeletion(
                    pageData,
                    navController
                )
            },
            {
                openConfirmDeletePageDialogBox = false
            }
        )
    }
}

fun processPageDeletion(
    pageData: PageData,
    navController: NavController
) {
    deletePage(pageData){
        openConfirmDeletePageDialogBox = false
        chapterPages.removeIf {
            it!!.id == pageData.id
        }

        //update chapter names. chapter 2 becomes chapter 1
        for(i in pageData.pageNumber..chapterPages.size){
            val newPage = chapterPages[i - 1]!!.copyOfBuilder()
                .displayName("Page $i")
                .pageNumber(i)
                .build()

            PageBackend.updatePage(newPage){
                chapterPages[i - 1] = it
                if(i == chapterPages.size){
                    navController.popBackStack()
                }
            }
        }
    }
}

fun deletePage(
    pageData: PageData,
    completed: () -> Unit
) {
    pageData.audioKeysS3.forEach { s3Key ->
        SoundBackend.deleteAudio(s3Key)
    }
    PageBackend.deletePage(pageData){
        completed()
    }
}

/**
 * start or pause playing the uris for this page
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
            resetRecordingCDT()
            activatePageControls(index)
        }else{
            if(pageRecordingNames.isNotEmpty()) {
                playIt(generalMediaPlayerService)
                deActivatePageControls(index)
                deActivatePageControls(index + 1)
            }
        }
    }else {
        if(pageRecordingNames.isNotEmpty()) {
            resetAllPageRecordBedtimeStoryUIMediaPlayers()
            generalMediaPlayerService.onDestroy()
            resetRecordingCDT()
            playingIndex = 0
            getInitialUri(generalMediaPlayerService)
            deActivatePageControls(index)
            deActivatePageControls(index + 1)
        }
    }
}

/**
 * stop playing the uris for this page
 *
 * @param generalMediaPlayerService is the media player service that will play uri
 */
fun stopPlayingThisPage(
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    if(generalMediaPlayerService.isMediaPlayerInitialized()){
        resetAllPageRecordBedtimeStoryUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        if (recordingCDT != null) {
            recordingCDT!!.cancel()
            recordingCDT = null
        }
        playingIndex = -1
        activatePageControls(3)
        activatePageControls(2)
    }
}

/**
 * Change the appearance of the control button to look activated
 *
 * @param index is the index of the button that was clicked
 */
fun activatePageControls(index: Int){
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
fun deActivatePageControls(index: Int){
    borderControlColors[index].value = Bizarre
    backgroundControlColor1[index].value = White
    backgroundControlColor2[index].value = White
    if(index == 2){
        icons[index].value = R.drawable.pause_icon
    }
}

fun clearPageRecordingsList(){
    pageRecordingNames.clear()
    pageRecordingS3Keys.clear()
    Log.i(TAG, "Clearing page recordingd ee")
}

private fun storeToS3IfChapterPage(
    index: Int,
    thisPageData: PageData,
    totalNonEmptyBlocks: Int,
    completed: (didUpdate: Boolean) -> Unit
){
    Log.i(TAG, "storeToS3IfChapterPage 990 $index")
    if(index < pageRecordingFileNames.size) {
        val key = "Routine/" +
                "BedtimeStories/" +
                "${globalViewModel_!!.currentUser!!.username}/" +
                "recorded/" +
                "${thisChapterData!!.bedtimeStoryInfo.displayName}/" +
                "${thisChapterData!!.displayName}/" +
                "${thisPageData.displayName}/" +
                "${pageRecordingFileNames[index].value}.aac"

        if (recordedPageRecordingAbsolutePath[index].value != "") {
            Log.i(TAG, "updated: true 3")
            SoundBackend.storeAudio(
                recordedPageRecordingAbsolutePath[index].value,
                key
            ) { s3key ->
                Log.i(TAG, "Saving page recording 1")
                updateChapterPageData(
                    s3key,
                    index,
                    totalNonEmptyBlocks,
                    thisPageData
                ) { didUpdate ->
                    Log.i(TAG, "updated: true 3")
                    completed(didUpdate)
                }
            }
        } else {
            Log.i(TAG, "updated: true 2")
            updateChapterPageData(
                "",
                index,
                totalNonEmptyBlocks,
                thisPageData
            ) { didUpdate ->
                Log.i(TAG, "updated: true 2")
                completed(didUpdate)
            }
        }
    } else {
        Log.i(TAG, "updated: false 9")
        completed(false)
    }
}

private fun updateChapterPageData(
    s3Key: String,
    index: Int,
    totalNonEmptyBlocks: Int,
    thisPageData: PageData,
    completed: (didUpdate: Boolean) -> Unit
) {
    Log.i(TAG, "Werieed audio $pageRecordingFileNames")
    if(index < pageRecordingFileNames.size) {
        Log.i(TAG, "index audio $index")
        Log.i(TAG, "pageRecordingFileNames[index].value audio ${pageRecordingFileNames[index].value}")
        audioNames[index] = pageRecordingFileNames[index].value
        Log.i(TAG, "vv pageRecordingFileNames[index].value ==> ${pageRecordingFileNames[index].value}")
        Log.i(TAG, "vv the audioNames ==> $audioNames")
        if(s3Key == ""){
            if(index < thisPageData.audioKeysS3.size) {
                Log.i(TAG, "vv pageData.audioKeysS3[index] ==> ${thisPageData.audioKeysS3[index]}")
                audioKeysS3[index] = thisPageData.audioKeysS3[index]
            }else{
                Log.i(TAG, "Weried audio s3")
            }
        }else {
            Log.i(TAG, "vv s3Key ==> $s3Key")
            audioKeysS3[index] = s3Key
        }
        audioLength[index] = audioPageRecordingFileLengthMilliSeconds[index].value.toInt()
        Log.i(TAG, "vv audioPageRecordingFileLengthMilliSeconds[index].value.toInt() ==> ${audioPageRecordingFileLengthMilliSeconds[index].value.toInt()}")

        Log.i(TAG, "audioNames.size = ${audioNames.size}")
        Log.i(TAG, "totalNonEmptyBlocks = ${totalNonEmptyBlocks}")
        val audioNamesCount = audioNames.count {
            it != ""
        }
        if(audioNamesCount == totalNonEmptyBlocks){
            Log.i(TAG, "Saving page recording 3")
            completed(true)
        }
    }else{
        Log.i(TAG, "updated: false 2")
        completed(false)
    }
}

fun saveRecordingToS3AndDB(
    thisPageData: PageData,
    completed: () -> Unit
){
    audioNames.clear()
    audioLength.clear()
    audioKeysS3.clear()

    var totalNonEmptyBlocks = 0
    Log.i(TAG, "pageRecordingFileNames.seize => ${pageRecordingFileNames}")
    Log.i(TAG, "pageRecordingFileNames--seize => ${pageRecordingFileNames}")
    for(i in pageRecordingFileNames.indices){
        if(pageRecordingFileNames[i].value != "empty recording"){
            totalNonEmptyBlocks += 1
        }
    }
    Log.i(TAG, "totalNonEmptyBlocks = $totalNonEmptyBlocks")
    Log.i(TAG, "pageRecordingFileNames = $pageRecordingFileNames")

    var updated = false

    var index = -1

    if(pageRecordingFileNames.size > 0) {
        for(name in pageRecordingFileNames){
            audioNames.add("")
            audioKeysS3.add("")
            audioLength.add(0)
        }
        for (i in pageRecordingFileNames.indices) {
            Log.i(TAG, "dd pageRecordingFileNames[i].value ==> ${pageRecordingFileNames[i].value}")
            if (pageRecordingFileNames[i].value != "empty recording") {
                Log.i(TAG, "storeToS3IfChapterPage index = $i")
                storeToS3IfChapterPage(
                    i,
                    thisPageData,
                    totalNonEmptyBlocks,
                ) {
                    Log.i(TAG, "updated: true 111")
                    if (it) {
                        updated = true
                        Log.i(TAG, "updated: true 1")

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

                        val newPageData = thisPageData.copyOfBuilder()
                            .audioNames(audioNames)
                            .audioKeysS3(audioKeysS3)
                            .audioLength(audioLength)
                            .build()

                        Log.i(TAG, "audioNames vv == $audioNames")
                        Log.i(TAG, "audioKeysS3 vv == $audioKeysS3")
                        Log.i(TAG, "audioLength vv == $audioLength")

                        PageBackend.updatePage(newPageData) { updatedPage ->
                            Log.i(TAG, "Updated npg66 $updatedPage")
                            updateChapterIndexList(updatedPage, thisChapterIndex)
                            Log.i(TAG, "Updated npg33 $updatedPage")
                            completed()
                        }
                    }else{
                        Log.i(TAG, "it is falssee")
                        index = i
                    }
                }
            }else{
                Log.i(TAG, "pageRecordingFileNames[i].value == empty recording")
                index = i
            }
        }
    }else{
        Log.i(TAG, "udsdssdds")
        completed()
    }

    if(!updated && index == pageRecordingFileNames.size - 1){
        if(pageRecordingFileNames.isNotEmpty()) {
            Log.i(TAG, "updated: false 1")
            val newPageData = thisPageData.copyOfBuilder()
                .audioNames(audioNames)
                .audioKeysS3(audioKeysS3)
                .audioLength(audioLength)
                .build()

            Log.i(TAG, "audioNames vv == $audioNames")
            Log.i(TAG, "audioKeysS3 vv == $audioKeysS3")
            Log.i(TAG, "audioLength vv == $audioLength")

            PageBackend.updatePage(newPageData) {
                Log.i(TAG, "Updated npg66 $it")
                updateChapterIndexList(it, thisChapterIndex)
                Log.i(TAG, "Updated npg33 $it")
                completed()
            }
        }
    }/*else{
        Log.i(TAG, "Nothig - comp")
        completed()
    }*/
}

fun updateThisPageData(
    pageData: PageData
){
    thisPageData = pageData
}