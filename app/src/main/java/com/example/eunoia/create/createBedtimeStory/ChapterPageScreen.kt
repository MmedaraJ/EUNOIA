package com.example.eunoia.create.createBedtimeStory

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
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.R
import com.example.eunoia.backend.BedtimeStoryChapterBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.createSound.activateControls
import com.example.eunoia.create.createSoundViewModel
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var pageRecordings = mutableListOf<MutableMap<String, String>>()

var recordedPageRecordingAbsolutePath = mutableListOf<MutableState<String>?>()
var pageRecordingFileColors = mutableListOf<MutableState<Color>?>()
var pageRecordingFileUris = mutableListOf<MutableState<Uri>?>()
var pageRecordingFileMediaPlayers = mutableListOf<MutableState<MediaPlayer>?>()
var pageRecordingFileNames = mutableListOf<MutableState<String>?>()
var audioPageRecordingFileLengthMilliSeconds = mutableListOf<MutableState<Long>?>()
var selectedPageRecordingIndex by mutableStateOf(0)
var thisPageIndex by mutableStateOf(-1)
var thisPageData by mutableStateOf<PageData?>(null)
var recordingCDT: CountDownTimer? = null

private var icons = arrayOf(
    mutableStateOf(R.drawable.ic_baseline_delete),
    mutableStateOf(R.drawable.back_arrow),
    mutableStateOf(R.drawable.play_icon),
    mutableStateOf(R.drawable.ic_baseline_arrow_right_alt),
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

private var TAG = "Page Screen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageScreenUI(
    navController: NavController,
    pageData: PageData,
    pageIndex: Int,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    val context = LocalContext.current
    thisPageData = pageData
    thisPageIndex = pageIndex

    resetEverything(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    ){}

    Log.i(TAG, "page index is $pageIndex")


    //if(pageRecordings.isNotEmpty()) {
        if (pageIndex >= pageRecordings.size) {
            for (i in pageRecordings.size..pageIndex) {
                pageRecordings.add(mutableMapOf())
            }
        }
    //}

    pageRecordings[pageIndex].clear()

    var numberOfRecordings by rememberSaveable { mutableStateOf(pageRecordings[pageIndex].size) }
    for (i in pageData.audioKeysS3.indices) {
        pageRecordings[pageIndex][pageData.audioNames[i]] = pageData.audioKeysS3[i]
    }
    numberOfRecordings = pageRecordings[pageIndex].size
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxHeight(),
    ) {
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
                    resetAllPageRecordBedtimeStoryUIMediaPlayers()
                    generalMediaPlayerService.onDestroy()

                    if(recordingCDT != null) {
                        recordingCDT!!.cancel()
                        recordingCDT = null
                    }
                    navController.popBackStack()
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
                    .size(24.dp)
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
                            0.5.dp,
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
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    //delete page
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(0.4f)
            ){
                icons.forEachIndexed { index, icon ->
                    if(index == 1 || index == 2 || index == 3){
                        Box(
                            modifier = Modifier
                                .size(24.dp)
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
                                        0.5.dp,
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
                                12.dp,
                                12.dp,
                                0,
                                0
                            ) {
                                //activate controls
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            backgroundControlColor1[4].value,
                            backgroundControlColor2[4].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            borderControlColors[4].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icons[4].value,
                    "delete page icon",
                    borderControlColors[4].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    //delete page
                }
            }
        }
        /*Column(
            modifier = Modifier
                .constrainAs(delete) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            CustomizableButton(
                text = "delete",
                height = 35,
                fontSize = 15,
                textColor = White,
                backgroundColor = Black,
                corner = 50,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "morge",
                maxWidthFraction = 1F
            ) {
                resetAllPageRecordBedtimeStoryUIMediaPlayers()
                generalMediaPlayerService.onDestroy()

                if(recordingCDT != null) {
                    recordingCDT!!.cancel()
                    recordingCDT = null
                }
                getFirstUri(generalMediaPlayerService)
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(save1) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.3F)
        ) {
            CustomizableButton(
                text = "new recording",
                height = 35,
                fontSize = 15,
                textColor = Black,
                backgroundColor = SwansDown,
                corner = 50,
                borderStroke = 0.0,
                borderColor = SwansDown.copy(alpha = 0F),
                textType = "morge",
                maxWidthFraction = 1F
            ) {
                Log.i(TAG, "We arreived clear1")
                val newRecordingName = "recording ${pageRecordings[pageIndex].size + 1}"
                val newRecordingKeyS3 = ""
                pageRecordings[pageIndex][newRecordingName] = newRecordingKeyS3
                numberOfRecordings++
            }
        }*/
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
                    recordedPageRecordingAbsolutePath.clear()
                    pageRecordingFileColors.clear()
                    pageRecordingFileUris.clear()
                    pageRecordingFileMediaPlayers.clear()
                    pageRecordingFileNames.clear()
                    audioPageRecordingFileLengthMilliSeconds.clear()

                    if(numberOfRecordings > 0 && pageRecordings.isNotEmpty() && pageRecordings[pageIndex].isNotEmpty()){
                        for(i in pageRecordings[pageIndex].values.indices){
                            var done by rememberSaveable { mutableStateOf(false) }

                            if(pageRecordings[pageIndex].values.isNotEmpty()){
                                Log.i(TAG, "Were in values")
                                if(i < pageRecordings[pageIndex].values.size){
                                    Log.i(TAG, "Were in values1")
                                    if(pageRecordings[pageIndex].values.elementAt(i) == ""){
                                        Log.i(TAG, "Were in values2")
                                        pageRecordingFileNames.add(remember { mutableStateOf("empty recording")})
                                        recordedPageRecordingAbsolutePath.add(remember { mutableStateOf("")})
                                        pageRecordingFileColors.add(remember{ mutableStateOf(WePeep) })
                                        pageRecordingFileUris.add(remember{ mutableStateOf("".toUri()) })
                                        val mediaPlayer = MediaPlayer()
                                        pageRecordingFileMediaPlayers.add(remember{ mutableStateOf(mediaPlayer) })
                                        audioPageRecordingFileLengthMilliSeconds.add(remember{ mutableStateOf(0L) })
                                        done = true
                                    }else{
                                        var length = 0L
                                        if(thisPageData!!.audioLength != null){
                                            Log.i(TAG, "Were in lengths")
                                            if(thisPageData!!.audioLength.isNotEmpty()){
                                                Log.i(TAG, "Were in lengths1")
                                                if(i < thisPageData!!.audioLength.size) {
                                                    Log.i(TAG, "Were in length2s")
                                                    length = thisPageData!!.audioLength[i].toLong()
                                                    Log.i(TAG, "Were in lengths3")
                                                }
                                            }
                                        }
                                        pageRecordingFileNames.add(remember{ mutableStateOf(thisPageData!!.audioNames[i]) })
                                        recordedPageRecordingAbsolutePath.add(remember{ mutableStateOf("") })
                                        pageRecordingFileColors.add(remember{ mutableStateOf(Peach) })
                                        pageRecordingFileUris.add(remember{ mutableStateOf("".toUri()) })
                                        val mediaPlayer = MediaPlayer()
                                        pageRecordingFileMediaPlayers.add(remember{ mutableStateOf(mediaPlayer) })
                                        audioPageRecordingFileLengthMilliSeconds.add(remember{ mutableStateOf(length) })
                                        done = true
                                    }
                                }
                            }

                            if(done) {
                                Log.i(TAG, "DOner $i")
                                Spacer(modifier = Modifier.height(16.dp))
                                RecordingBlock(
                                    thisPageData!!,
                                    i,
                                    context,
                                    generalMediaPlayerService
                                ) {
                                    selectedPageRecordingIndex = it
                                    thisPageIndex = pageIndex
                                    thisPageData = pageData
                                    globalViewModel_!!.bottomSheetOpenFor = "recordAudio"
                                    recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording =
                                        thisPageData
                                    openBottomSheet(scope, state)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

fun retrieveThisUri(
    pageData: PageData,
    index: Int,
    completed: (uri: Uri) -> Unit
){
    BedtimeStoryChapterBackend.queryBedtimeStoryChapterBasedOnId(pageData.bedtimeStoryInfoChapterId){
        if(it.isNotEmpty()) {
            val key = "Routine/" +
                    "BedtimeStories/" +
                    "${globalViewModel_!!.currentUser!!.username}/" +
                    "recorded/" +
                    "${it[0].bedtimeStoryInfo.displayName}/" +
                    "${it[0].displayName}/" +
                    "${pageData.displayName}/" +
                    "recording_${index + 1}.aac"

            SoundBackend.retrieveAudio(
                key,
                globalViewModel_!!.currentUser!!.amplifyAuthUserId
            ){ uri ->
                completed(uri)
            }
        }
    }
}

fun clearPageRecordingsList(){
    pageRecordings.clear()
}