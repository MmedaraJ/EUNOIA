package com.example.eunoia.dashboard.selfLove

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
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
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.dashboard.sound.navigateBack
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.components.PurpleBackgroundInfo
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.SelfLoveTimer
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

var selfLoveUri: Uri? = null
var selfLoveScreenIcons = arrayOf(
    mutableStateOf(R.drawable.reset_sliders_icon),
    mutableStateOf(R.drawable.seek_back_15),
    mutableStateOf(R.drawable.play_icon),
    mutableStateOf(R.drawable.seek_forward_15),
)
var selfLoveScreenBorderControlColors = arrayOf(
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
var selfLoveScreenBackgroundControlColor1 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
)
var selfLoveScreenBackgroundControlColor2 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
)
var selfLoveAngle = mutableStateOf(0f)
var selfLoveClicked = mutableStateOf(false)
var selfLoveTimer = SelfLoveTimer(UserDashboardActivity.getInstanceActivity())
var selfLoveTimeDisplay by mutableStateOf("00.00")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelfLoveScreen(
    navController: NavController,
    selfLoveData: SelfLoveData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    globalViewModel_!!.navController = navController
    var retrievedUris by rememberSaveable{ mutableStateOf(false) }

    if(globalViewModel_!!.currentSelfLovePlaying != null) {
        if (globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            setParametersFromGlobalVariables{
                retrievedUris = true
            }
        }else{
            resetSelfLoveControlsUI()
            selfLoveAngle.value = 0f
            selfLoveClicked.value = false
            selfLoveTimeDisplay = timerFormatMS(selfLoveData.fullPlayTime.toLong())
            retrievedUris = true
        }
    }else{
        resetSelfLoveControlsUI()
        selfLoveAngle.value = 0f
        selfLoveClicked.value = false
        selfLoveTimeDisplay = timerFormatMS(selfLoveData.fullPlayTime.toLong())
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
                    text = "Self Love",
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
                CircularSliderSelfLove(
                    thumbColor = Snuff,
                    progressColor = Black,
                    backgroundColor = PeriwinkleGray.copy(alpha = 0.5F),
                    modifier = Modifier.size(320.dp),
                ){ appliedAngle ->
                    if(globalViewModel_!!.currentSelfLovePlaying != null){
                        if(globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id){
                            if(selfLoveClicked.value) {
                                val newSeek = (
                                        appliedAngle /
                                                360f
                                        ) * generalMediaPlayerService.getMediaPlayer()!!.duration
                                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeek.toInt())
                                selfLoveTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                                //globalViewModel_!!.selfLoveTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                                selfLoveTimer.start()
                                globalViewModel_!!.selfLoveTimer = selfLoveTimer
                                if(!globalViewModel_!!.isCurrentSelfLovePlaying) {
                                    selfLoveTimer.pause()
                                    globalViewModel_!!.selfLoveTimer = selfLoveTimer
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
                                pauseOrPlaySelfLoveAccordingly(
                                    selfLoveData,
                                    generalMediaPlayerService,
                                )
                            }
                    ) {
                        LightText(
                            text = selfLoveTimeDisplay,
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
                PurpleBackgroundSelfLoveControls(
                    selfLoveData = selfLoveData,
                    generalMediaPlayerService = generalMediaPlayerService,
                    scope = scope,
                    state = state
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
                if(selfLoveData.longDescription != null){
                    longDescription = selfLoveData.longDescription
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

private fun setParametersFromGlobalVariables(
    completed: () -> Unit
) {
    setUpParameters{
        completed()
    }
}

private const val TAG = "selfLoveScreen"

fun setUpParameters(
    completed: () -> Unit
) {
    Log.d(TAG, "setup params")
    selfLoveUri = globalViewModel_!!.currentSelfLovePlayingUri!!

    for(i in selfLoveScreenIcons.indices){
        selfLoveScreenIcons[i].value = globalViewModel_!!.selfLoveScreenIcons[i].value
    }
    for(i in selfLoveScreenBorderControlColors.indices){
        selfLoveScreenBorderControlColors[i].value = globalViewModel_!!.selfLoveScreenBorderControlColors[i].value
    }
    for(i in selfLoveScreenBackgroundControlColor1.indices){
        selfLoveScreenBackgroundControlColor1[i].value = globalViewModel_!!.selfLoveScreenBackgroundControlColor1[i].value
    }
    for(i in selfLoveScreenBackgroundControlColor2.indices){
        selfLoveScreenBackgroundControlColor2[i].value = globalViewModel_!!.selfLoveScreenBackgroundControlColor2[i].value
    }

    selfLoveTimeDisplay = globalViewModel_!!.selfLoveTimeDisplay
    selfLoveTimer = globalViewModel_!!.selfLoveTimer
    selfLoveAngle.value = globalViewModel_!!.selfLoveCircularSliderAngle
    selfLoveClicked.value = globalViewModel_!!.selfLoveCircularSliderClicked
    completed()
}

fun resetSelfLoveControlsUI(){
    selfLoveScreenIcons[2].value = R.drawable.play_icon

    for(i in selfLoveScreenBorderControlColors.indices){
        if(i == 2){
            selfLoveScreenBorderControlColors[i].value = Black
        }else{
            selfLoveScreenBorderControlColors[i].value = Bizarre
        }
    }

    for(i in selfLoveScreenBackgroundControlColor1.indices){
        if(i == 2){
            selfLoveScreenBackgroundControlColor1[i].value = SoftPeach
        }else{
            selfLoveScreenBackgroundControlColor1[i].value = White
        }
    }

    for(i in selfLoveScreenBackgroundControlColor2.indices){
        if(i == 2){
            selfLoveScreenBackgroundControlColor2[i].value = Solitude
        }else{
            selfLoveScreenBackgroundControlColor2[i].value = White
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
        selfLoveScreen(
            rememberNavController(),
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }*/
}