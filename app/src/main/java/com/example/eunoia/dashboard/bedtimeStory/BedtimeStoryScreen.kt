package com.example.eunoia.dashboard.bedtimeStory

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.BedtimeStoryTimer
import com.example.eunoia.utils.Timer
import com.example.eunoia.utils.formatMilliSecond
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

var bedtimeStoryUri: Uri? = null
var bedtimeStoryScreenIcons = arrayOf(
    mutableStateOf(R.drawable.reset_sliders_icon),
    mutableStateOf(R.drawable.seek_back_15),
    mutableStateOf(R.drawable.play_icon),
    mutableStateOf(R.drawable.seek_forward_15),
)
var bedtimeStoryScreenBorderControlColors = arrayOf(
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
var bedtimeStoryScreenBackgroundControlColor1 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
)
var bedtimeStoryScreenBackgroundControlColor2 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BedtimeStoryScreen(
    navController: NavController,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    globalViewModel_!!.navController = navController
    var retrievedUris by rememberSaveable{ mutableStateOf(false) }

    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            setParametersFromGlobalVariables(
                bedtimeStoryInfoData,
                generalMediaPlayerService
            ){
                retrievedUris = true
            }
        }else{
            resetBedtimeStoryControlsUI()
            bedtimeStoryTimeDisplay.value = timerFormatMS(bedtimeStoryInfoData.fullPlayTime.toLong())
            retrievedUris = true
        }
    }else{
        resetBedtimeStoryControlsUI()
        bedtimeStoryTimeDisplay.value = timerFormatMS(bedtimeStoryInfoData.fullPlayTime.toLong())
        retrievedUris = true
    }

    if(retrievedUris) {
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            val (
                header,
                playBox,
                controls,
                description
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
                        navigateBack(navController)
                    },
                    {
                        globalViewModel_!!.bottomSheetOpenFor = "controls"
                        openBottomSheet(scope, state)
                    },
                    {
                    }
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(playBox) {
                        top.linkTo(header.bottom, margin = 40.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ) {
                CircularSlider(
                    thumbColor = Snuff,
                    progressColor = Black,
                    backgroundColor = PeriwinkleGray.copy(alpha = 0.5F),
                    modifier = Modifier.size(320.dp),
                    generalMediaPlayerService = generalMediaPlayerService
                ){ appliedAngle ->
                    if(globalViewModel_!!.currentBedtimeStoryPlaying != null){
                        if(globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                            if(clicked.value) {
                                val newSeek = (
                                        appliedAngle /
                                                360f
                                        ) * generalMediaPlayerService.getMediaPlayer()!!.duration
                                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeek.toInt())
                                bedtimeStoryTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                                bedtimeStoryTimer.start()
                            }
                        }
                    }
                }
                Card(
                    elevation = 8.dp,
                    modifier = Modifier.clip(CircleShape)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(223.51.dp)
                            .clip(CircleShape)
                            .gradientBackground(
                                listOf(
                                    SoftPeach,
                                    Snuff,
                                ),
                                angle = 180f
                            )
                            .border(1.dp, Black, CircleShape)
                            .clickable {
                                pauseOrPlayBedtimeStoryAccordingly(
                                    bedtimeStoryInfoData,
                                    generalMediaPlayerService,
                                )
                            }
                    ) {
                        LightText(
                            text = bedtimeStoryTimeDisplay.value,
                            color = Black,
                            fontSize = 10,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(controls) {
                        top.linkTo(playBox.bottom, margin = 40.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .fillMaxWidth()
            ) {
                PurpleBackgroundControls(
                    bedtimeStoryInfoData = bedtimeStoryInfoData,
                    scope = scope,
                    state = state
                ) {

                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(description) {
                        top.linkTo(controls.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .fillMaxWidth()
            ) {
                PurpleBackgroundInfo(
                    "Description",
                    bedtimeStoryInfoData.description
                ) {}
            }
        }
    }else{
        ConstraintLayout{
            val (progressBar) = createRefs()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(progressBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

private fun setParametersFromGlobalVariables(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
) {
    setUpParameters(
        bedtimeStoryInfoData,
        generalMediaPlayerService
    ){
        completed()
    }
}

fun setUpParameters(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
) {
    bedtimeStoryUri = globalViewModel_!!.currentBedtimeStoryPlayingUri!!
    bedtimeStoryScreenIcons = globalViewModel_!!.bedtimeStoryScreenIcons
    bedtimeStoryScreenBorderControlColors = globalViewModel_!!.bedtimeStoryScreenBorderControlColors
    bedtimeStoryScreenBackgroundControlColor1 = globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor1
    bedtimeStoryScreenBackgroundControlColor2 = globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor2
    bedtimeStoryTimeDisplay.value = globalViewModel_!!.bedtimeStoryTimeDisplay.value
    completed()
}

fun resetBedtimeStoryControlsUI(){
    bedtimeStoryScreenIcons[2].value = R.drawable.play_icon

    for(i in bedtimeStoryScreenBorderControlColors.indices){
        if(i == 2){
            bedtimeStoryScreenBorderControlColors[i].value = Black
        }else{
            bedtimeStoryScreenBorderControlColors[i].value = Bizarre
        }
    }

    for(i in bedtimeStoryScreenBackgroundControlColor1.indices){
        if(i == 2){
            bedtimeStoryScreenBackgroundControlColor1[i].value = SoftPeach
        }else{
            bedtimeStoryScreenBackgroundControlColor1[i].value = White
        }
    }

    for(i in bedtimeStoryScreenBackgroundControlColor2.indices){
        if(i == 2){
            bedtimeStoryScreenBackgroundControlColor2[i].value = Solitude
        }else{
            bedtimeStoryScreenBackgroundControlColor2[i].value = White
        }
    }
    Thread.sleep(1_000)
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
fun Preview() {
    /*EUNOIATheme {
        BedtimeStoryScreen(
            rememberNavController(),
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }*/
}