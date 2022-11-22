package com.example.eunoia.dashboard.prayer

import android.content.res.Configuration
import android.net.Uri
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
import com.amplifyframework.datastore.generated.model.PrayerData
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.dashboard.sound.navigateBack
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.components.PurpleBackgroundInfo
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.navigation.prayerViewModel
import com.example.eunoia.ui.navigation.prayerViewModel
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.PrayerTimer
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

var prayerUri: Uri? = null
var prayerScreenIcons = arrayOf(
    mutableStateOf(R.drawable.reset_sliders_icon),
    mutableStateOf(R.drawable.seek_back_15),
    mutableStateOf(R.drawable.play_icon),
    mutableStateOf(R.drawable.seek_forward_15),
)
var prayerScreenBorderControlColors = arrayOf(
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
var prayerScreenBackgroundControlColor1 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
)
var prayerScreenBackgroundControlColor2 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
)
var prayerAngle = mutableStateOf(0f)
var prayerClicked = mutableStateOf(false)
var prayerTimer = PrayerTimer(UserDashboardActivity.getInstanceActivity())
var prayerTimeDisplay by mutableStateOf("00.00")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PrayerScreen(
    navController: NavController,
    prayerData: PrayerData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    val context = LocalContext.current

    SetUpRoutineCurrentlyPlayingAlertDialogPrayerUI(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context,
        prayerData
    )

    globalViewModel!!.navController = navController
    var retrievedUris by rememberSaveable{ mutableStateOf(false) }

    if(prayerViewModel!!.currentPrayerPlaying != null) {
        if (prayerViewModel!!.currentPrayerPlaying!!.id == prayerData.id) {
            setParametersFromGlobalVariables{
                retrievedUris = true
            }
        }else{
            resetPrayerControlsUI()
            prayerAngle.value = 0f
            prayerClicked.value = false
            prayerTimeDisplay = timerFormatMS(prayerData.fullPlayTime.toLong())
            retrievedUris = true
        }
    }else{
        resetPrayerControlsUI()
        prayerAngle.value = 0f
        prayerClicked.value = false
        prayerTimeDisplay = timerFormatMS(prayerData.fullPlayTime.toLong())
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
                        globalViewModel!!.bottomSheetOpenFor = "controls"
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
                    text = "Prayer",
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
                CircularSliderPrayer(
                    thumbColor = Snuff,
                    progressColor = Black,
                    backgroundColor = PeriwinkleGray.copy(alpha = 0.5F),
                    modifier = Modifier.size(320.dp),
                ){ appliedAngle ->
                    if(prayerViewModel!!.currentPrayerPlaying != null){
                        if(prayerViewModel!!.currentPrayerPlaying!!.id == prayerData.id){
                            if(prayerClicked.value) {
                                val newSeek = (
                                        appliedAngle /
                                                360f
                                        ) * generalMediaPlayerService.getMediaPlayer()!!.duration
                                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeek.toInt())
                                prayerTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                                //prayerViewModel_!!.prayerTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                                prayerTimer.start()
                                prayerViewModel!!.prayerTimer = prayerTimer
                                if(!prayerViewModel!!.isCurrentPrayerPlaying) {
                                    prayerTimer.pause()
                                    prayerViewModel!!.prayerTimer = prayerTimer
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
                                resetCurrentlyPlayingRoutineIfNecessaryPrayerUI(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    context,
                                    prayerData
                                )
                            }
                    ) {
                        LightText(
                            text = prayerTimeDisplay,
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
                PurpleBackgroundPrayerControls(
                    prayerData = prayerData,
                    generalMediaPlayerService = generalMediaPlayerService,
                    soundMediaPlayerService = soundMediaPlayerService,
                    scope = scope,
                    state = state,
                    context = context,
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
                if(prayerData.longDescription != null){
                    longDescription = prayerData.longDescription
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

private const val TAG = "prayerScreen"

fun setUpParameters(
    completed: () -> Unit
) {
    if(prayerViewModel!!.currentPrayerPlayingUri != null) {
        prayerUri = prayerViewModel!!.currentPrayerPlayingUri!!

        for (i in prayerScreenIcons.indices) {
            prayerScreenIcons[i].value = prayerViewModel!!.prayerScreenIcons[i].value
        }
        for (i in prayerScreenBorderControlColors.indices) {
            prayerScreenBorderControlColors[i].value =
                prayerViewModel!!.prayerScreenBorderControlColors[i].value
        }
        for (i in prayerScreenBackgroundControlColor1.indices) {
            prayerScreenBackgroundControlColor1[i].value =
                prayerViewModel!!.prayerScreenBackgroundControlColor1[i].value
        }
        for (i in prayerScreenBackgroundControlColor2.indices) {
            prayerScreenBackgroundControlColor2[i].value =
                prayerViewModel!!.prayerScreenBackgroundControlColor2[i].value
        }

        prayerTimeDisplay = prayerViewModel!!.prayerTimeDisplay
        prayerTimer = prayerViewModel!!.prayerTimer
        prayerAngle.value = prayerViewModel!!.prayerCircularSliderAngle
        prayerClicked.value = prayerViewModel!!.prayerCircularSliderClicked
        completed()
    }
}

fun resetPrayerControlsUI(){
    prayerScreenIcons[2].value = R.drawable.play_icon

    for(i in prayerScreenBorderControlColors.indices){
        if(i == 2){
            prayerScreenBorderControlColors[i].value = Black
        }else{
            prayerScreenBorderControlColors[i].value = Bizarre
        }
    }

    for(i in prayerScreenBackgroundControlColor1.indices){
        if(i == 2){
            prayerScreenBackgroundControlColor1[i].value = SoftPeach
        }else{
            prayerScreenBackgroundControlColor1[i].value = White
        }
    }

    for(i in prayerScreenBackgroundControlColor2.indices){
        if(i == 2){
            prayerScreenBackgroundControlColor2[i].value = Solitude
        }else{
            prayerScreenBackgroundControlColor2[i].value = White
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
        prayerScreen(
            rememberNavController(),
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }*/
}