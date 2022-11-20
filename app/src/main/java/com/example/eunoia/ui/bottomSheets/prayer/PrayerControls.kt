package com.example.eunoia.ui.bottomSheets.prayer

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
import com.amplifyframework.datastore.generated.model.PrayerAudioSource
import com.amplifyframework.datastore.generated.model.PrayerData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.bedtimeStory.getCurrentlyPlayingTime
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.home.PrayerForRoutine.updateRecentlyPlayedUserPrayerRelationshipWithPrayer
import com.example.eunoia.dashboard.prayer.*
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

private const val TAG = "bottomSheetPrayerControl"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetPrayerControlPanel(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
): Boolean{
    var showing = false
    if(globalViewModel.currentPrayerPlaying != null) {
        showing = true
        BottomSheetPrayerControlPanelUI(
            prayerData = globalViewModel.currentPrayerPlaying!!,
            scope = scope,
            state = state,
            generalMediaPlayerService = generalMediaPlayerService
        )
    }
    return showing
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetPrayerControlPanelUI(
    prayerData: PrayerData,
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
                    navigateToPrayerScreen(
                        globalViewModel_!!.navController!!,
                        prayerData
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
                    text = prayerData.displayName,
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
                    text = "By: @${prayerData.prayerOwner.username}",
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
                BottomSheetPrayerControls(
                    globalViewModel_!!.currentPrayerPlaying!!,
                    generalMediaPlayerService
                )
            }
        }
    }
}

@Composable
fun BottomSheetPrayerControls(
    prayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        globalViewModel_!!.prayerScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                           globalViewModel_!!.prayerScreenBackgroundControlColor1[index].value,
                            globalViewModel_!!.prayerScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            globalViewModel_!!.prayerScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    globalViewModel_!!.prayerScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        prayerData,
                        index,
                        generalMediaPlayerService,
                    )
                }
            }
        }
    }
}

private fun activateControls(
    PrayerData: PrayerData,
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    when(index){
        0 -> resetPrayer(
            generalMediaPlayerService,
            PrayerData,
        )
        1 -> seekBack15(
            PrayerData,
            generalMediaPlayerService,
        )
        2 -> {
            pauseOrPlayPrayerAccordingly(
                PrayerData,
                generalMediaPlayerService,
            )
        }
        3 -> seekForward15(
            PrayerData,
            generalMediaPlayerService,
        )
    }
}

fun resetPrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    PrayerData: PrayerData
){
    if(globalViewModel_!!.currentPrayerPlaying != null) {
        if (globalViewModel_!!.currentPrayerPlaying!!.id == PrayerData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                globalViewModel_!!.prayerCircularSliderClicked = false
                globalViewModel_!!.prayerCircularSliderAngle = 0f
                globalViewModel_!!.prayerTimer.stop()
                globalViewModel_!!.prayerTimeDisplay =
                    timerFormatMS(PrayerData.fullPlayTime.toLong())
                globalViewModel_!!.isCurrentPrayerPlaying = false
                generalMediaPlayerService.onDestroy()
            }
        }
    }
}

fun seekBack15(
    PrayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(globalViewModel_!!.currentPrayerPlaying != null) {
        if (globalViewModel_!!.currentPrayerPlaying!!.id == PrayerData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                globalViewModel_!!.prayerCircularSliderClicked = false
                globalViewModel_!!.prayerCircularSliderAngle = (
                        (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                                (PrayerData.fullPlayTime).toFloat()
                        ) * 360f
                globalViewModel_!!.prayerTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(globalViewModel_!!.isCurrentPrayerPlaying) {
                    globalViewModel_!!.prayerTimer.start()
                }
            }
        }
    }
}

fun seekForward15(
    PrayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(globalViewModel_!!.currentPrayerPlaying != null) {
        if (globalViewModel_!!.currentPrayerPlaying!!.id == PrayerData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activatePrayerGlobalControlButton(2)
                    deActivatePrayerGlobalControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                globalViewModel_!!.prayerCircularSliderClicked = false
                globalViewModel_!!.prayerCircularSliderAngle = (
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                                PrayerData.fullPlayTime.toFloat()
                        ) * 360f
                globalViewModel_!!.prayerTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(globalViewModel_!!.isCurrentPrayerPlaying) {
                    globalViewModel_!!.prayerTimer.start()
                }
            }
        }
    }
}

fun pauseOrPlayPrayerAccordingly(
    PrayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(globalViewModel_!!.currentPrayerPlaying != null) {
        if (globalViewModel_!!.currentPrayerPlaying!!.id == PrayerData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                    pausePrayer(generalMediaPlayerService)
                }else{
                    startPrayer(
                        generalMediaPlayerService,
                        PrayerData,
                    )
                }
            }else{
                startPrayer(
                    generalMediaPlayerService,
                    PrayerData,
                )
            }
        }else{
            retrievePrayerAudio(
                generalMediaPlayerService,
                PrayerData,
            )
        }
    }else{
        retrievePrayerAudio(
            generalMediaPlayerService,
            PrayerData,
        )
    }
}

private fun pausePrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(generalMediaPlayerService.isMediaPlayerInitialized()) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            globalViewModel_!!.prayerTimer.pause()
            globalViewModel_!!.generalPlaytimeTimer.pause()
            activatePrayerGlobalControlButton(2)
            activatePrayerGlobalControlButton(2)
            globalViewModel_!!.isCurrentPrayerPlaying = false
        }
    }
}

private fun startPrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    prayerData: PrayerData,
) {
    if(globalViewModel_!!.currentPrayerPlayingUri != null){
        if(generalMediaPlayerService.isMediaPlayerInitialized()){
            if(globalViewModel_!!.currentPrayerPlaying!!.id == prayerData.id){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingPrayer()
            }else{
                initializeMediaPlayer(
                    generalMediaPlayerService,
                    prayerData
                )
            }
        }else{
            initializeMediaPlayer(
                generalMediaPlayerService,
                prayerData
            )
        }
    }
}

private fun afterPlayingPrayer(){
    globalViewModel_!!.prayerTimer.start()
    globalViewModel_!!.generalPlaytimeTimer.start()
    globalViewModel_!!.isCurrentPrayerPlaying = true
    deActivatePrayerGlobalControlButton(2)
    deActivatePrayerGlobalControlButton(0)
}

private fun updatePreviousAndCurrentPrayerRelationship(
    prayerData: PrayerData,
    conntinuePlayingTime: Int,
    completed: () -> Unit
){
    updatePreviousUserPrayerRelationship {
        updateRecentlyPlayedUserPrayerRelationshipWithPrayer(
            prayerData
        ) {
            updatePreviousUserBedtimeStoryRelationship(conntinuePlayingTime) {
                updatePreviousUserSelfLoveRelationship(conntinuePlayingTime) {
                    completed()
                }
            }
        }
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    prayerData: PrayerData,
){
    val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)
    updatePreviousAndCurrentPrayerRelationship(
        prayerData,
        continuePlayingTime
    ) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(globalViewModel_!!.currentPrayerPlayingUri!!)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        globalViewModel_!!.prayerTimer.setMaxDuration(prayerData.fullPlayTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptPrayer()
    }
    resetGlobalControlButtons()
    afterPlayingPrayer()
}

private fun retrievePrayerAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    prayerData: PrayerData,
) {
    if (
        prayerData.audioSource == PrayerAudioSource.UPLOADED
    ) {
        SoundBackend.retrieveAudio(
            prayerData.audioKeyS3,
            prayerData.prayerOwner.amplifyAuthUserId
        ) {
            globalViewModel_!!.currentPrayerPlayingUri = it
            startPrayer(
                generalMediaPlayerService,
                prayerData,
            )
        }
    } else {
        //if recorded
        //get chapter, get pages, get recordings from s3, play them one after the order
    }
}

fun resetGlobalControlButtons(){
    deActivatePrayerGlobalControlButton(0)
    deActivatePrayerGlobalControlButton(1)
    activatePrayerGlobalControlButton(2)
    deActivatePrayerGlobalControlButton(3)
    deActivatePrayerGlobalControlButton(4)
}

fun activatePrayerGlobalControlButton(index: Int){
    globalViewModel_!!.prayerScreenBorderControlColors[index].value = Black
    globalViewModel_!!.prayerScreenBackgroundControlColor1[index].value = SoftPeach
    globalViewModel_!!.prayerScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        globalViewModel_!!.prayerScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivatePrayerGlobalControlButton(index: Int){
    globalViewModel_!!.prayerScreenBorderControlColors[index].value = Bizarre
    globalViewModel_!!.prayerScreenBackgroundControlColor1[index].value = White
    globalViewModel_!!.prayerScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        globalViewModel_!!.prayerScreenIcons[index].value = R.drawable.pause_icon
    }
}