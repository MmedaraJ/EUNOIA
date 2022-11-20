package com.example.eunoia.dashboard.bedtimeStory

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
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
import com.example.eunoia.backend.UserBedtimeStoryInfoRelationshipBackend
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.BedtimeStoryTimer
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
var bedtimeStoryTimer = BedtimeStoryTimer(UserDashboardActivity.getInstanceActivity())
var bedtimeStoryTimeDisplay by mutableStateOf("00.00")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BedtimeStoryScreen(
    navController: NavController,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    val context = LocalContext.current

    SetUpRoutineCurrentlyPlayingAlertDialogBedtimeStoryUI(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context,
        bedtimeStoryInfoData
    )

    globalViewModel_!!.navController = navController
    var retrievedUris by rememberSaveable{ mutableStateOf(false) }

    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            setParametersFromGlobalVariables{
                retrievedUris = true
            }
        }else{
            setUpForNewPlay(bedtimeStoryInfoData){
                retrievedUris = true
            }
        }
    }else{
        setUpForNewPlay(bedtimeStoryInfoData){
            retrievedUris = true
        }
    }

    if(retrievedUris) {
        val scrollState = rememberScrollState()
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            val (
                header,
                title,
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
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(header.bottom, margin = 40.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                NormalText(
                    text = "Bedtime Story",
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(playBox) {
                        top.linkTo(title.bottom, margin = 20.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .fillMaxWidth()
            ) {
                CircularSlider(
                    thumbColor = Snuff,
                    progressColor = Black,
                    backgroundColor = PeriwinkleGray.copy(alpha = 0.5F),
                    modifier = Modifier.size(320.dp),
                    actionDown = { a ->
                        Log.i(TAG, "actionDown = true")
                        if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                            if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                                setCircularSliderClicked(true)
                                setCircularSliderAngle(a + 0)
                            }
                        }
                    },
                    actionMove = { a ->
                        if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                            if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                                setCircularSliderClicked(true)
                                setCircularSliderAngle(a + 0)
                            }
                        }
                        Log.i(TAG, "actionMove = true")
                    }
                ){ appliedAngle ->
                    if(globalViewModel_!!.currentBedtimeStoryPlaying != null){
                        if(globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                            if(getCircularSliderClicked()) {
                                globalViewModel_!!.resetCDT()

                                val newSeek = (
                                        appliedAngle /
                                                360f
                                        ) * generalMediaPlayerService.getMediaPlayer()!!.duration
                                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeek.toInt())

                                globalViewModel_!!.remainingPlayTime =
                                    bedtimeStoryInfoData.fullPlayTime -
                                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                                startCDT(
                                    generalMediaPlayerService,
                                    bedtimeStoryInfoData
                                )

                                bedtimeStoryTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                                bedtimeStoryTimer.start()
                                globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
                                if(!globalViewModel_!!.isCurrentBedtimeStoryPlaying) {
                                    bedtimeStoryTimer.pause()
                                    globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
                                }
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
                                resetCurrentlyPlayingRoutineIfNecessaryBedtimeStoryUI(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    context,
                                    bedtimeStoryInfoData
                                )
                            }
                    ) {
                        LightText(
                            text = bedtimeStoryTimeDisplay,
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
                    generalMediaPlayerService = generalMediaPlayerService,
                    scope = scope,
                    state = state,
                    soundMediaPlayerService = soundMediaPlayerService
                )
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
                var longDescription = "Long description"
                if(bedtimeStoryInfoData.longDescription != null){
                    longDescription = bedtimeStoryInfoData.longDescription
                }
                PurpleBackgroundInfo(
                    "Description",
                    longDescription
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

fun setUpForNewPlay(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    completed: () -> Unit
) {
    resetBedtimeStoryControlsUI()
    setCircularSliderClicked(false)
    UserBedtimeStoryInfoRelationshipBackend.queryUserBedtimeStoryInfoRelationshipBasedOnUserAndBedtimeStoryInfo(
        globalViewModel_!!.currentUser!!,
        bedtimeStoryInfoData
    ){
        if(it.isNotEmpty()){
            val angle = (
                    it[0]!!.continuePlayingTime.toFloat() /
                        bedtimeStoryInfoData.fullPlayTime
                    ) * 360f
            setCircularSliderAngle(angle)
            bedtimeStoryTimeDisplay = timerFormatMS(it[0]!!.continuePlayingTime.toLong())
            completed()
        }else{
            setCircularSliderAngle(0f)
            bedtimeStoryTimeDisplay = timerFormatMS(bedtimeStoryInfoData.fullPlayTime.toLong())
            completed()
        }
    }
}

private fun setParametersFromGlobalVariables(
    completed: () -> Unit
) {
    setUpParameters{
        completed()
    }
}

private const val TAG = "BedtimeStoryScreen"

fun setUpParameters(
    completed: () -> Unit
) {
    Log.d(TAG, "setup params")
    if(bedtimeStoryUri == null) {
        bedtimeStoryUri = globalViewModel_!!.currentBedtimeStoryPlayingUri!!
    }

    for(i in bedtimeStoryScreenIcons.indices){
        bedtimeStoryScreenIcons[i].value = globalViewModel_!!.bedtimeStoryScreenIcons[i].value
    }
    for(i in bedtimeStoryScreenBorderControlColors.indices){
        bedtimeStoryScreenBorderControlColors[i].value = globalViewModel_!!.bedtimeStoryScreenBorderControlColors[i].value
    }
    for(i in bedtimeStoryScreenBackgroundControlColor1.indices){
        bedtimeStoryScreenBackgroundControlColor1[i].value = globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor1[i].value
    }
    for(i in bedtimeStoryScreenBackgroundControlColor2.indices){
        bedtimeStoryScreenBackgroundControlColor2[i].value = globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor2[i].value
    }

    bedtimeStoryTimeDisplay = globalViewModel_!!.bedtimeStoryTimeDisplay
    bedtimeStoryTimer = globalViewModel_!!.bedtimeStoryTimer
    setCircularSliderClicked(globalViewModel_!!.bedtimeStoryCircularSliderClicked)
    setCircularSliderAngle(globalViewModel_!!.bedtimeStoryCircularSliderAngle)
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