package com.example.eunoia.ui.bottomSheets.bedtimeStory

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amplifyframework.datastore.generated.model.BedtimeStoryAudioSource
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.bedtimeStory.*
import com.example.eunoia.dashboard.home.BedtimeStoryForRoutine
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.components.AnImageWithColor
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.timerFormatMS
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

private const val TAG = "bottomSheetBedtimeStoryControl"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetBedtimeStoryControlPanel(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
): Boolean{
    var showing = false
    if(globalViewModel.currentBedtimeStoryPlaying != null) {
        showing = true
        BottomSheetBedtimeStoryControlPanelUI(
            bedtimeStoryInfoData = globalViewModel.currentBedtimeStoryPlaying!!,
            scope = scope,
            state = state,
            generalMediaPlayerService = generalMediaPlayerService
        )
    }
    return showing
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetBedtimeStoryControlPanelUI(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(115.dp)
            .fillMaxWidth()
            .clickable {
                if(globalViewModel_!!.navController != null){
                    closeBottomSheet(scope, state)
                    navigateToBedtimeStoryScreen(
                        globalViewModel_!!.navController!!,
                        bedtimeStoryInfoData
                    )
                }
            },
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
                    globalViewModel_!!.currentBedtimeStoryPlaying!!,
                    generalMediaPlayerService
                )
            }
        }
    }
}

@Composable
fun BottomSheetBedtimeStoryControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        globalViewModel_!!.bedtimeStoryScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                           globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor1[index].value,
                            globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            globalViewModel_!!.bedtimeStoryScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    globalViewModel_!!.bedtimeStoryScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        bedtimeStoryInfoData,
                        index,
                        generalMediaPlayerService,
                    )
                }
            }
        }
    }
}

private fun activateControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    when(index){
        0 -> resetBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData,
        )
        1 -> seekBack15(
            bedtimeStoryInfoData,
            generalMediaPlayerService,
        )
        2 -> {
            pauseOrPlayBedtimeStoryAccordingly(
                bedtimeStoryInfoData,
                generalMediaPlayerService,
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
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                globalViewModel_!!.bedtimeStoryCircularSliderClicked = false
                globalViewModel_!!.bedtimeStoryCircularSliderAngle = 0f
                globalViewModel_!!.bedtimeStoryTimer.stop()
                globalViewModel_!!.bedtimeStoryTimeDisplay =
                    timerFormatMS(bedtimeStoryInfoData.fullPlayTime.toLong())
                globalViewModel_!!.resetCDT()
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
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                globalViewModel_!!.resetCDT()
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)

                globalViewModel_!!.remainingPlayTime =
                    bedtimeStoryInfoData.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                startCDT(
                    generalMediaPlayerService,
                    bedtimeStoryInfoData
                )

                globalViewModel_!!.bedtimeStoryCircularSliderClicked = false
                globalViewModel_!!.bedtimeStoryCircularSliderAngle = (
                        (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                                (bedtimeStoryInfoData.fullPlayTime).toFloat()
                        ) * 360f

                globalViewModel_!!.bedtimeStoryTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(globalViewModel_!!.isCurrentBedtimeStoryPlaying) {
                    globalViewModel_!!.bedtimeStoryTimer.start()
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
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                globalViewModel_!!.resetCDT()

                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateBedtimeStoryGlobalControlButton(2)
                    deActivateBedtimeStoryGlobalControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)

                globalViewModel_!!.remainingPlayTime =
                    bedtimeStoryInfoData.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                startCDT(
                    generalMediaPlayerService,
                    bedtimeStoryInfoData
                )

                globalViewModel_!!.bedtimeStoryCircularSliderClicked = false
                globalViewModel_!!.bedtimeStoryCircularSliderAngle = (
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                                bedtimeStoryInfoData.fullPlayTime.toFloat()
                        ) * 360f
                globalViewModel_!!.bedtimeStoryTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(globalViewModel_!!.isCurrentBedtimeStoryPlaying) {
                    globalViewModel_!!.bedtimeStoryTimer.start()
                }
            }
        }
    }
}

private fun startCDT(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData
) {
    globalViewModel_!!.startTheCDT(
        globalViewModel_!!.remainingPlayTime.toLong(),
        generalMediaPlayerService
    ){
        resetBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData
        )
        deActivateResetButton()
    }
}

fun pauseOrPlayBedtimeStoryAccordingly(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
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
    if(generalMediaPlayerService.isMediaPlayerInitialized()) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            globalViewModel_!!.bedtimeStoryTimer.pause()
            globalViewModel_!!.generalPlaytimeTimer.pause()
            globalViewModel_!!.resetCDT()
            activateBedtimeStoryGlobalControlButton(2)
            activateBedtimeStoryGlobalControlButton(2)
            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
        }
    }
}

private fun startBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
) {
    if(globalViewModel_!!.currentBedtimeStoryPlayingUri != null){
        if(generalMediaPlayerService.isMediaPlayerInitialized()){
            if(globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                globalViewModel_!!.remainingPlayTime =
                    globalViewModel_!!.currentBedtimeStoryPlaying!!.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition

                generalMediaPlayerService.startMediaPlayer()
                afterPlayingBedtimeStory(
                    generalMediaPlayerService,
                    bedtimeStoryInfoData
                )
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
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
){
    globalViewModel_!!.bedtimeStoryTimer.start()
    globalViewModel_!!.generalPlaytimeTimer.start()
    startCDT(
        generalMediaPlayerService,
        bedtimeStoryInfoData
    )
    globalViewModel_!!.isCurrentBedtimeStoryPlaying = true
    deActivateBedtimeStoryGlobalControlButton(2)
    deActivateBedtimeStoryGlobalControlButton(0)
}

private fun updatePreviousAndCurrentBedtimeStoryRelationship(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    continuePlayingTime: Int,
    completed: () -> Unit
){
    updatePreviousUserBedtimeStoryRelationship(continuePlayingTime) {
        BedtimeStoryForRoutine.updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(
            bedtimeStoryInfoData
        ) {
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship(continuePlayingTime) {
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
    val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)
    updatePreviousAndCurrentBedtimeStoryRelationship(
        bedtimeStoryInfoData,
        continuePlayingTime
    ) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(globalViewModel_!!.currentBedtimeStoryPlayingUri!!)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        globalViewModel_!!.bedtimeStoryTimer.setMaxDuration(bedtimeStoryInfoData.fullPlayTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

        resetGlobalControlButtons()
        afterPlayingBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData
        )
    }
}

private fun retrieveBedtimeStoryAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
) {
    SoundBackend.retrieveAudio(
        bedtimeStoryInfoData.audioKeyS3,
        bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
    ) {
        globalViewModel_!!.currentBedtimeStoryPlayingUri = it
        startBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData,
        )
    }
}

fun resetGlobalControlButtons(){
    deActivateBedtimeStoryGlobalControlButton(0)
    deActivateBedtimeStoryGlobalControlButton(1)
    activateBedtimeStoryGlobalControlButton(2)
    deActivateBedtimeStoryGlobalControlButton(3)
    deActivateBedtimeStoryGlobalControlButton(4)
}

fun activateBedtimeStoryGlobalControlButton(index: Int){
    globalViewModel_!!.bedtimeStoryScreenBorderControlColors[index].value = Black
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor1[index].value = SoftPeach
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        globalViewModel_!!.bedtimeStoryScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivateBedtimeStoryGlobalControlButton(index: Int){
    globalViewModel_!!.bedtimeStoryScreenBorderControlColors[index].value = Bizarre
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor1[index].value = White
    globalViewModel_!!.bedtimeStoryScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        globalViewModel_!!.bedtimeStoryScreenIcons[index].value = R.drawable.pause_icon
    }
}