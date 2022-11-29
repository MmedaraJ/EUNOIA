package com.example.eunoia.ui.bottomSheets.bedtimeStory

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.bedtimeStory.*
import com.example.eunoia.dashboard.home.BedtimeStoryForRoutine
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.dashboard.sound.soundActivityUris
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.components.AnImageWithColor
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.bedtimeStoryViewModel
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.navigation.routineViewModel
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

private const val TAG = "bottomSheetBedtimeStoryControl"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetBedtimeStoryControlPanel(
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
): Boolean{
    var showing = false
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        showing = true
        BottomSheetBedtimeStoryControlPanelUI(
            bedtimeStoryInfoData = bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!,
            scope = scope,
            state = state,
            generalMediaPlayerService = generalMediaPlayerService,
            soundMediaPlayerService = soundMediaPlayerService
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
    soundMediaPlayerService: SoundMediaPlayerService,
){
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(115.dp)
            .fillMaxWidth()
            .clickable {
                if(globalViewModel!!.navController != null){
                    closeBottomSheet(scope, state)
                    navigateToBedtimeStoryScreen(
                        globalViewModel!!.navController!!,
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
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!,
                    context,
                )
            }
        }
    }
}

@Composable
fun BottomSheetBedtimeStoryControls(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        bedtimeStoryViewModel!!.bedtimeStoryScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                           bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor1[index].value,
                            bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            bedtimeStoryViewModel!!.bedtimeStoryScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    bedtimeStoryViewModel!!.bedtimeStoryScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        bedtimeStoryInfoData,
                        context,
                        index,
                    )
                }
            }
        }
    }
}

private fun activateControls(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context,
    index: Int,
){
    when(index){
        0 -> resetBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData,
        )
        1 -> seekBack15(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
        )
        2 -> {
            pauseOrPlayBedtimeStoryAccordingly(
                generalMediaPlayerService,
                soundMediaPlayerService,
                bedtimeStoryInfoData,
                context
            )
        }
        3 -> seekForward15(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
        )
    }
}

fun resetBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData
){
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        if (bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderClicked = false
                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderAngle = 0f
                bedtimeStoryViewModel!!.bedtimeStoryTimer.stop()
                bedtimeStoryViewModel!!.bedtimeStoryTimeDisplay =
                    timerFormatMS(bedtimeStoryInfoData.fullPlayTime.toLong())
                globalViewModel!!.resetCDT()
                bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
                generalMediaPlayerService.onDestroy()
            }
        }
    }
}

fun seekBack15(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
) {
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        if (bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                globalViewModel!!.resetCDT()
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)

                globalViewModel!!.remainingPlayTime =
                    bedtimeStoryInfoData.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition

                startCDT(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    bedtimeStoryInfoData,
                    context
                )

                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderClicked = false
                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderAngle = (
                        (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                                (bedtimeStoryInfoData.fullPlayTime).toFloat()
                        ) * 360f

                bedtimeStoryViewModel!!.bedtimeStoryTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying) {
                    bedtimeStoryViewModel!!.bedtimeStoryTimer.start()
                }
            }
        }
    }
}

fun seekForward15(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
) {
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        if (bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                globalViewModel!!.resetCDT()

                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateBedtimeStoryGlobalControlButton(2)
                    deActivateBedtimeStoryGlobalControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)

                globalViewModel!!.remainingPlayTime =
                    bedtimeStoryInfoData.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition

                startCDT(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    bedtimeStoryInfoData,
                    context
                )

                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderClicked = false
                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderAngle = (
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                                bedtimeStoryInfoData.fullPlayTime.toFloat()
                        ) * 360f
                bedtimeStoryViewModel!!.bedtimeStoryTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying) {
                    bedtimeStoryViewModel!!.bedtimeStoryTimer.start()
                }
            }
        }
    }
}

private fun startCDT(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
) {
    globalViewModel!!.startTheCDT(
        globalViewModel!!.remainingPlayTime.toLong(),
        generalMediaPlayerService
    ){
        resetBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData
        )
        deActivateResetButton()

        if(routineViewModel!!.currentRoutinePlaying != null){
            if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null){
                if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                    BedtimeStoryForRoutine.individualCDTDone(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        routineViewModel!!.routineIndex,
                        context,
                    )
                }
            }
        }
    }
}

fun pauseOrPlayBedtimeStoryAccordingly(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
) {
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        if (bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                    pauseBedtimeStory(
                        generalMediaPlayerService,
                        bedtimeStoryInfoData
                    )
                }else{
                    startBedtimeStory(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        bedtimeStoryInfoData,
                        context
                    )
                }
            }else{
                startBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    bedtimeStoryInfoData,
                    context
                )
            }
        }else{
            retrieveBedtimeStoryAudio(
                generalMediaPlayerService,
                soundMediaPlayerService,
                bedtimeStoryInfoData,
                context
            )
        }
    }else{
        retrieveBedtimeStoryAudio(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
        )
    }
}

private fun pauseBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData
) {
    if(generalMediaPlayerService.isMediaPlayerInitialized()) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            bedtimeStoryViewModel!!.bedtimeStoryTimer.pause()
            globalViewModel!!.generalPlaytimeTimer.pause()
            globalViewModel!!.resetCDT()

            if(routineViewModel!!.currentRoutinePlaying != null){
                if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null){
                    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                        BedtimeStoryForRoutine.resetBtsCDT()
                    }
                }
            }

            activateBedtimeStoryGlobalControlButton(2)
            activateBedtimeStoryGlobalControlButton(2)
            bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
        }
    }
}

private fun startBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
) {
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri != null){
        if(generalMediaPlayerService.isMediaPlayerInitialized()){
            if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                globalViewModel!!.remainingPlayTime =
                    bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.fullPlayTime -
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition

                bedtimeStoryViewModel!!.remainingPlayTime = bedtimeStoryViewModel!!.playTimeSoFar.toInt()

                generalMediaPlayerService.startMediaPlayer()
                afterPlayingBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    bedtimeStoryInfoData,
                    context
                )
            }else{
                initializeMediaPlayer(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    bedtimeStoryInfoData,
                    context
                )
            }
        }else{
            if(bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri != null) {
                initializeMediaPlayer(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    bedtimeStoryInfoData,
                    context
                )
            }
        }
    }
}

private fun afterPlayingBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
){
    bedtimeStoryViewModel!!.bedtimeStoryTimer.start()
    globalViewModel!!.generalPlaytimeTimer.start()

    startCDT(
        generalMediaPlayerService,
        soundMediaPlayerService,
        bedtimeStoryInfoData,
        context
    )

    if(routineViewModel!!.currentRoutinePlaying != null){
        if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null){
            if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                BedtimeStoryForRoutine.generalBedtimeStoryTimer(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    routineViewModel!!.routineIndex,
                    context
                )
            }
        }
    }

    bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = true
    deActivateBedtimeStoryGlobalControlButton(2)
    deActivateBedtimeStoryGlobalControlButton(0)
}

private fun updatePreviousAndCurrentBedtimeStoryRelationship(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
){
    updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
        BedtimeStoryForRoutine.updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(
            bedtimeStoryInfoData
        ) {
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
                    completed()
                }
            }
        }
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
){
    updatePreviousAndCurrentBedtimeStoryRelationship(
        bedtimeStoryInfoData,
        generalMediaPlayerService
    ) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri!!)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        bedtimeStoryViewModel!!.bedtimeStoryTimer.setMaxDuration(bedtimeStoryInfoData.fullPlayTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

        resetGlobalControlButtons()
        afterPlayingBedtimeStory(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
        )
    }
}

private fun retrieveBedtimeStoryAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
) {
    SoundBackend.retrieveAudio(
        bedtimeStoryInfoData.audioKeyS3,
        bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
    ) {
        bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = it
        startBedtimeStory(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
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
    bedtimeStoryViewModel!!.bedtimeStoryScreenBorderControlColors[index].value = Black
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor1[index].value = SoftPeach
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        bedtimeStoryViewModel!!.bedtimeStoryScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivateBedtimeStoryGlobalControlButton(index: Int){
    bedtimeStoryViewModel!!.bedtimeStoryScreenBorderControlColors[index].value = Bizarre
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor1[index].value = White
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        bedtimeStoryViewModel!!.bedtimeStoryScreenIcons[index].value = R.drawable.pause_icon
    }
}