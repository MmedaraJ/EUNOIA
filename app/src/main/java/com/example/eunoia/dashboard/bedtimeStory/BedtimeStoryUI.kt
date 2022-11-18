package com.example.eunoia.dashboard.bedtimeStory

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amplifyframework.datastore.generated.model.BedtimeStoryAudioSource
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.BedtimeStoryForRoutine.updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo
import com.example.eunoia.dashboard.home.updatePreviousUserRoutineRelationship
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.bedtimeStory.resetGlobalControlButtons
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.openRoutineIsCurrentlyPlayingDialogBox
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PurpleBackgroundControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(115.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 2.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PeriwinkleGray.copy(alpha = 0.3F),
                            Color(0xFFCBCBE8).copy(alpha = 0.4F),
                            Mischka.copy(alpha = 0.6F),
                        ),
                        center = Offset.Unspecified,
                        radius = 300f,
                        tileMode = TileMode.Clamp
                    ),
                ),
        ) {
            val (
                title,
                by,
                controls
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                NormalText(
                    text = bedtimeStoryInfoData.displayName,
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(by) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                LightText(
                    text = "By: @${bedtimeStoryInfoData.bedtimeStoryOwner.username}",
                    color = Black,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(controls) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        end.linkTo(parent.end, margin = 32.dp)
                        start.linkTo(parent.start, margin = 32.dp)
                    }
            ) {
                BottomSheetBedtimeStoryControls(
                    bedtimeStoryInfoData,
                    generalMediaPlayerService,
                    true,
                    scope,
                    state,
                    soundMediaPlayerService,
                    context
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetBedtimeStoryControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    showAddIcon: Boolean,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    context: Context,
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        bedtimeStoryScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            bedtimeStoryScreenBackgroundControlColor1[index].value,
                            bedtimeStoryScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            bedtimeStoryScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    bedtimeStoryScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        bedtimeStoryInfoData,
                        index,
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        context
                    )
                }
            }
        }
        if(showAddIcon) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(bedtimeStoryScreenBorderControlColors[4].value),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    globalViewModel_!!.addIcon.value,
                    "save bedtime story icon",
                    White,
                    10.dp,
                    10.dp,
                    0,
                    0
                ) {
                    bedtimeStoryScreenBorderControlColors[4].value = Black
                    globalViewModel_!!.currentBedtimeStoryToBeAdded = bedtimeStoryInfoData
                    globalViewModel_!!.bottomSheetOpenFor = "addToBedtimeStoryListOrRoutine"
                    openBottomSheet(scope, state)
                }
            }
        }
    }
}

private fun activateControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    context: Context
){
    when(index){
        0 -> resetBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData
        )
        1 -> seekBack15(
            bedtimeStoryInfoData,
            generalMediaPlayerService,
        )
        2 -> {
            resetCurrentlyPlayingRoutineIfNecessaryBedtimeStoryUI(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
                bedtimeStoryInfoData
            )
        }
        3 -> seekForward15(
            bedtimeStoryInfoData,
            generalMediaPlayerService,
        )
    }
}

fun resetBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData
){
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentSelfLovePlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                clicked.value = false
                angle.value = 0f
                bedtimeStoryTimer.stop()
                globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
                bedtimeStoryTimeDisplay = timerFormatMS(bedtimeStoryInfoData.fullPlayTime.toLong())
                globalViewModel_!!.bedtimeStoryTimeDisplay = bedtimeStoryTimeDisplay
                globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
                generalMediaPlayerService.onDestroy()
            }
        }
    }
}

fun seekBack15(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentSelfLovePlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                clicked.value = false
                angle.value = (
                    (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                    (bedtimeStoryInfoData.fullPlayTime).toFloat()
                ) * 360f
                bedtimeStoryTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
                if(globalViewModel_!!.isCurrentBedtimeStoryPlaying) {
                    bedtimeStoryTimer.start()
                    globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
                }
            }
        }
    }
}

fun seekForward15(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentSelfLovePlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateGlobalBedtimeStoryControlButton(2)
                    deActivateGlobalBedtimeStoryControlButton(0)
                    activateLocalBedtimeStoryControlButton(2)
                    deActivateLocalBedtimeStoryControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                clicked.value = false
                angle.value = (
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                    bedtimeStoryInfoData.fullPlayTime.toFloat()
                ) * 360f
                bedtimeStoryTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
                if(globalViewModel_!!.isCurrentBedtimeStoryPlaying) {
                    bedtimeStoryTimer.start()
                    globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
                }
            }
        }
    }
}

@Composable
fun SetUpRoutineCurrentlyPlayingAlertDialogBedtimeStoryUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    bedtimeStoryData: BedtimeStoryInfoData
){
    if(openRoutineIsCurrentlyPlayingDialogBox){
        ConfirmAlertDialog(
            "Are you sure you want to stop your routine?",
            {
                updatePreviousUserRoutineRelationship {
                    resetEverything(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context
                    ){
                        pauseOrPlayBedtimeStoryAccordingly(
                            bedtimeStoryData,
                            generalMediaPlayerService,
                        )
                    }
                }
            },
            {
                openRoutineIsCurrentlyPlayingDialogBox = false
            }
        )
    }
}

fun resetCurrentlyPlayingRoutineIfNecessaryBedtimeStoryUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    bedtimeStoryData: BedtimeStoryInfoData
) {
    if(globalViewModel_!!.currentRoutinePlaying != null){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        pauseOrPlayBedtimeStoryAccordingly(
            bedtimeStoryData,
            generalMediaPlayerService,
        )
    }
}

fun pauseOrPlayBedtimeStoryAccordingly(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentSelfLovePlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
                if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                    pauseBedtimeStory(generalMediaPlayerService)
                }else{
                    startBedtimeStory(
                        generalMediaPlayerService,
                        bedtimeStoryInfoData,
                    )
                }
            }else{
                startBedtimeStory(
                    generalMediaPlayerService,
                    bedtimeStoryInfoData,
                )
            }
        }else{
            retrieveBedtimeStoryAudio(
                generalMediaPlayerService,
                bedtimeStoryInfoData,
            )
        }
    }else{
        retrieveBedtimeStoryAudio(
            generalMediaPlayerService,
            bedtimeStoryInfoData,
        )
    }
}

private fun pauseBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        globalViewModel_!!.currentSelfLovePlaying == null &&
        globalViewModel_!!.currentPrayerPlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            bedtimeStoryTimer.pause()
            globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
            activateLocalBedtimeStoryControlButton(2)
            activateGlobalBedtimeStoryControlButton(2)
            globalViewModel_!!.generalPlaytimeTimer.pause()
            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
        }
    }
}

private fun startBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
) {
    if(bedtimeStoryUri != null){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentSelfLovePlaying == null &&
            globalViewModel_!!.currentPrayerPlaying == null
        ){
            if(globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingBedtimeStory(bedtimeStoryInfoData)
            }else{
                initializeMediaPlayer(
                    generalMediaPlayerService,
                    bedtimeStoryInfoData
                )
            }
        }else{
            initializeMediaPlayer(
                generalMediaPlayerService,
                bedtimeStoryInfoData
            )
        }
    }
}

private fun afterPlayingBedtimeStory(
    bedtimeStoryInfoData: BedtimeStoryInfoData
){
    bedtimeStoryTimer.start()
    globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
    deActivateLocalBedtimeStoryControlButton(2)
    deActivateLocalBedtimeStoryControlButton(0)
    globalViewModel_!!.generalPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingBedtimeStory(bedtimeStoryInfoData)
}

private fun updatePreviousAndCurrentBedtimeStoryRelationship(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    completed: () -> Unit
){
    updatePreviousUserBedtimeStoryRelationship {
        updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(bedtimeStoryInfoData) {
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship {
                    completed()
                }
            }
        }
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
){
    updatePreviousAndCurrentBedtimeStoryRelationship(bedtimeStoryInfoData) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(bedtimeStoryUri!!)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        bedtimeStoryTimer.setMaxDuration(bedtimeStoryInfoData.fullPlayTime.toLong())
        globalViewModel_!!.bedtimeStoryTimer = bedtimeStoryTimer
        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()
    }
    resetBothLocalAndGlobalControlButtons()
    afterPlayingBedtimeStory(bedtimeStoryInfoData)
}

private fun setGlobalPropertiesAfterPlayingBedtimeStory(
    bedtimeStoryInfoData: BedtimeStoryInfoData
){
    globalViewModel_!!.currentBedtimeStoryPlaying = bedtimeStoryInfoData
    globalViewModel_!!.currentBedtimeStoryPlayingUri = bedtimeStoryUri
    globalViewModel_!!.isCurrentBedtimeStoryPlaying = true
    deActivateGlobalBedtimeStoryControlButton(2)
    deActivateGlobalBedtimeStoryControlButton(0)
}

fun resetBedtimeStoryGlobalProperties(){
    globalViewModel_!!.currentBedtimeStoryPlaying = null
    globalViewModel_!!.currentBedtimeStoryPlayingUri = null
    globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
    globalViewModel_!!.bedtimeStoryTimer.stop()
    resetGlobalControlButtons()
}

private fun retrieveBedtimeStoryAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
) {
    SoundBackend.retrieveAudio(
        bedtimeStoryInfoData.audioKeyS3,
        bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
    ) {
        bedtimeStoryUri = it
        startBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData,
        )
    }
}

fun resetBothLocalAndGlobalControlButtons(){
    deActivateLocalBedtimeStoryControlButton(0)
    deActivateLocalBedtimeStoryControlButton(1)
    activateLocalBedtimeStoryControlButton(2)
    deActivateLocalBedtimeStoryControlButton(3)

    deActivateGlobalBedtimeStoryControlButton(0)
    deActivateGlobalBedtimeStoryControlButton(1)
    activateGlobalBedtimeStoryControlButton(2)
    deActivateGlobalBedtimeStoryControlButton(3)
}

fun resetBothLocalAndGlobalControlButtonsAfterReset(){
    activateLocalBedtimeStoryControlButton(0)
    deActivateLocalBedtimeStoryControlButton(1)
    activateLocalBedtimeStoryControlButton(2)
    deActivateLocalBedtimeStoryControlButton(3)

    activateGlobalBedtimeStoryControlButton(0)
    deActivateGlobalBedtimeStoryControlButton(1)
    activateGlobalBedtimeStoryControlButton(2)
    deActivateGlobalBedtimeStoryControlButton(3)
}

private fun activateLocalBedtimeStoryControlButton(index: Int){
    bedtimeStoryScreenBorderControlColors[index].value = Black
    bedtimeStoryScreenBackgroundControlColor1[index].value = SoftPeach
    bedtimeStoryScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        bedtimeStoryScreenIcons[index].value = R.drawable.play_icon
    }
}

private fun deActivateLocalBedtimeStoryControlButton(index: Int){
    bedtimeStoryScreenBorderControlColors[index].value = Bizarre
    bedtimeStoryScreenBackgroundControlColor1[index].value = White
    bedtimeStoryScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        bedtimeStoryScreenIcons[index].value = R.drawable.pause_icon
    }
}

private fun activateGlobalBedtimeStoryControlButton(index: Int){
    globalViewModel_!!.bedtimeStoryScreenBorderControlColors[index].value = bedtimeStoryScreenBorderControlColors[index].value
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor1[index].value = bedtimeStoryScreenBackgroundControlColor1[index].value
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor2[index].value = bedtimeStoryScreenBackgroundControlColor2[index].value
    if(index == 2){
        globalViewModel_!!.bedtimeStoryScreenIcons[index].value = bedtimeStoryScreenIcons[index].value
    }
}

private fun deActivateGlobalBedtimeStoryControlButton(index: Int){
    globalViewModel_!!.bedtimeStoryScreenBorderControlColors[index].value = bedtimeStoryScreenBorderControlColors[index].value
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor1[index].value = bedtimeStoryScreenBackgroundControlColor1[index].value
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor2[index].value = bedtimeStoryScreenBackgroundControlColor2[index].value
    if(index == 2){
        globalViewModel_!!.bedtimeStoryScreenIcons[index].value = bedtimeStoryScreenIcons[index].value
    }
}