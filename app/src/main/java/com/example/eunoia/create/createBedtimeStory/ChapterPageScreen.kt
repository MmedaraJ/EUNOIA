package com.example.eunoia.create.createBedtimeStory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.create.resetEverything
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.recordAudioViewModel
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.White
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var pageRecordings = mutableListOf<MutableMap<MutableState<String>, MutableState<String>>>()
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

    resetEverything(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    ){}

    if(pageIndex >= pageRecordings.size){
        for(i in 0..pageIndex){
            pageRecordings.add(mutableMapOf())
        }
    }
    var numberOfRecordings by rememberSaveable { mutableStateOf(pageRecordings[pageIndex].size) }
    for (i in pageRecordings[pageIndex].size until pageData.audioKeysS3.size) {
        pageRecordings[pageIndex][remember{mutableStateOf(pageData.audioNames[i])}] =
            remember{mutableStateOf(pageData.audioKeysS3[i])}
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
            save1,
            save2,
            all_recordings,
            delete,
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
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = /*${pageData.bedtimeStoryInfoChapter.displayName} -*/ "${pageData.displayName}",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
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
                //delete page
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(save1) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            CustomizableButton(
                text = "record",
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
                globalViewModel_!!.bottomSheetOpenFor = "recordAudio"
                recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording = "page"
                openBottomSheet(scope, state)
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .constrainAs(all_recordings) {
                    top.linkTo(save1.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
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
                if(numberOfRecordings > 0){
                    for(recording in pageRecordings[pageIndex]){
                        RecordingBlock(recording)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(spacer) {
                        top.linkTo(recordings.bottom, margin = 60.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

fun clearPageRecordingsList(){
    pageRecordings.clear()
}