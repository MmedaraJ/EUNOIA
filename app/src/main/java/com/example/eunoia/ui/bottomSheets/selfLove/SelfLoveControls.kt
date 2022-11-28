package com.example.eunoia.ui.bottomSheets.selfLove

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.home.SelfLoveForRoutine
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.selfLove.navigateToSelfLoveScreen
import com.example.eunoia.dashboard.selfLove.resetBothLocalAndGlobalControlButtonsAfterReset
import com.example.eunoia.dashboard.selfLove.resetOtherGeneralMediaPlayerUsersExceptSelfLove
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.components.AnImageWithColor
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.navigation.selfLoveViewModel
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

private const val TAG = "bottomSheetSelfLoveControl"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetSelfLoveControlPanel(
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
): Boolean{
    var showing = false
    if(selfLoveViewModel!!.currentSelfLovePlaying != null) {
        showing = true
        BottomSheetSelfLoveControlPanelUI(
            selfLoveData = selfLoveViewModel!!.currentSelfLovePlaying!!,
            scope = scope,
            state = state,
            generalMediaPlayerService = generalMediaPlayerService
        )
    }
    return showing
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetSelfLoveControlPanelUI(
    selfLoveData: SelfLoveData,
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
                if(globalViewModel!!.navController != null){
                    closeBottomSheet(scope, state)
                    navigateToSelfLoveScreen(
                        globalViewModel!!.navController!!,
                        selfLoveData
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
                    text = selfLoveData.displayName,
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
                    text = "By: @${selfLoveData.selfLoveOwner.username}",
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
                BottomSheetSelfLoveControls(
                    selfLoveViewModel!!.currentSelfLovePlaying!!,
                    generalMediaPlayerService
                )
            }
        }
    }
}

@Composable
fun BottomSheetSelfLoveControls(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        selfLoveViewModel!!.selfLoveScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            selfLoveViewModel!!.selfLoveScreenBackgroundControlColor1[index].value,
                            selfLoveViewModel!!.selfLoveScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            selfLoveViewModel!!.selfLoveScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    selfLoveViewModel!!.selfLoveScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        selfLoveData,
                        index,
                        generalMediaPlayerService,
                    )
                }
            }
        }
    }
}

private fun activateControls(
    selfLoveData: SelfLoveData,
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    when(index){
        0 -> resetSelfLove(
            generalMediaPlayerService,
            selfLoveData,
        )
        1 -> seekBack15(
            selfLoveData,
            generalMediaPlayerService,
        )
        2 -> {
            pauseOrPlayselfLoveAccordingly(
                selfLoveData,
                generalMediaPlayerService,
            )
        }
        3 -> seekForward15(
            selfLoveData,
            generalMediaPlayerService,
        )
    }
}

fun resetSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData
){
    if(selfLoveViewModel!!.currentSelfLovePlaying != null) {
        if (selfLoveViewModel!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                selfLoveViewModel!!.selfLoveCircularSliderClicked = false
                selfLoveViewModel!!.selfLoveCircularSliderAngle = 0f
                selfLoveViewModel!!.selfLoveTimer.stop()
                selfLoveViewModel!!.selfLoveTimeDisplay =
                    timerFormatMS(selfLoveData.fullPlayTime.toLong())
                selfLoveViewModel!!.isCurrentSelfLovePlaying = false
                generalMediaPlayerService.onDestroy()
            }
        }
    }
}

fun seekBack15(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(selfLoveViewModel!!.currentSelfLovePlaying != null) {
        if (selfLoveViewModel!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                selfLoveViewModel!!.selfLoveCircularSliderClicked = false
                selfLoveViewModel!!.selfLoveCircularSliderAngle = (
                        (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                                (selfLoveData.fullPlayTime).toFloat()
                        ) * 360f
                selfLoveViewModel!!.selfLoveTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(selfLoveViewModel!!.isCurrentSelfLovePlaying) {
                    selfLoveViewModel!!.selfLoveTimer.start()
                }
            }
        }
    }
}

fun seekForward15(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(selfLoveViewModel!!.currentSelfLovePlaying != null) {
        if (selfLoveViewModel!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateSelfLoveGlobalControlButton(2)
                    deActivateSelfLoveGlobalControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                selfLoveViewModel!!.selfLoveCircularSliderClicked = false
                selfLoveViewModel!!.selfLoveCircularSliderAngle = (
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                                selfLoveData.fullPlayTime.toFloat()
                        ) * 360f
                selfLoveViewModel!!.selfLoveTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(selfLoveViewModel!!.isCurrentSelfLovePlaying) {
                    selfLoveViewModel!!.selfLoveTimer.start()
                }
            }
        }
    }
}

fun pauseOrPlayselfLoveAccordingly(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(selfLoveViewModel!!.currentSelfLovePlaying != null) {
        if (selfLoveViewModel!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                    pauseSelfLove(generalMediaPlayerService)
                }else{
                    startSelfLove(
                        generalMediaPlayerService,
                        selfLoveData,
                    )
                }
            }else{
                startSelfLove(
                    generalMediaPlayerService,
                    selfLoveData,
                )
            }
        }else{
            retrieveSelfLoveAudio(
                generalMediaPlayerService,
                selfLoveData,
            )
        }
    }else{
        retrieveSelfLoveAudio(
            generalMediaPlayerService,
            selfLoveData,
        )
    }
}

private fun pauseSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(generalMediaPlayerService.isMediaPlayerInitialized()) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            selfLoveViewModel!!.selfLoveTimer.pause()
            globalViewModel!!.generalPlaytimeTimer.pause()
            activateSelfLoveGlobalControlButton(2)
            activateSelfLoveGlobalControlButton(2)
            selfLoveViewModel!!.isCurrentSelfLovePlaying = false
        }
    }
}

private fun startSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData,
) {
    if(selfLoveViewModel!!.currentSelfLovePlayingUri != null){
        if(generalMediaPlayerService.isMediaPlayerInitialized()){
            if(selfLoveViewModel!!.currentSelfLovePlaying!!.id == selfLoveData.id){
                generalMediaPlayerService.startMediaPlayer()
            }else{
                initializeMediaPlayer(
                    generalMediaPlayerService,
                    selfLoveData
                )
            }
        }else{
            initializeMediaPlayer(
                generalMediaPlayerService,
                selfLoveData
            )
        }

        afterPlayingSelfLove()
    }
}

private fun afterPlayingSelfLove(){
    selfLoveViewModel!!.selfLoveTimer.start()
    globalViewModel!!.generalPlaytimeTimer.start()
    selfLoveViewModel!!.isCurrentSelfLovePlaying = true
    deActivateSelfLoveGlobalControlButton(2)
    deActivateSelfLoveGlobalControlButton(0)
}

private fun updatePreviousAndCurrentSelfLoveRelationship(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
){
    updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
        SelfLoveForRoutine.updateRecentlyPlayedUserSelfLoveRelationshipWithSelfLove(
            selfLoveData
        ) {
            updatePreviousUserPrayerRelationship {
                updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
                    completed()
                }
            }
        }
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData,
){
    updatePreviousAndCurrentSelfLoveRelationship(
        selfLoveData,
        generalMediaPlayerService
    ) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(selfLoveViewModel!!.currentSelfLovePlayingUri!!)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        selfLoveViewModel!!.selfLoveTimer.setMaxDuration(selfLoveData.fullPlayTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptSelfLove()
        resetGlobalControlButtons()
    }
}

private fun retrieveSelfLoveAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData,
) {
    SoundBackend.retrieveAudio(
        selfLoveData.audioKeyS3,
        selfLoveData.selfLoveOwner.amplifyAuthUserId
    ) {
        selfLoveViewModel!!.currentSelfLovePlayingUri = it
        startSelfLove(
            generalMediaPlayerService,
            selfLoveData,
        )
    }
}

fun resetGlobalControlButtons(){
    deActivateSelfLoveGlobalControlButton(0)
    deActivateSelfLoveGlobalControlButton(1)
    activateSelfLoveGlobalControlButton(2)
    deActivateSelfLoveGlobalControlButton(3)
    deActivateSelfLoveGlobalControlButton(4)
}

fun activateSelfLoveGlobalControlButton(index: Int){
    selfLoveViewModel!!.selfLoveScreenBorderControlColors[index].value = Black
    selfLoveViewModel!!.selfLoveScreenBackgroundControlColor1[index].value = SoftPeach
    selfLoveViewModel!!.selfLoveScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        selfLoveViewModel!!.selfLoveScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivateSelfLoveGlobalControlButton(index: Int){
    selfLoveViewModel!!.selfLoveScreenBorderControlColors[index].value = Bizarre
    selfLoveViewModel!!.selfLoveScreenBackgroundControlColor1[index].value = White
    selfLoveViewModel!!.selfLoveScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        selfLoveViewModel!!.selfLoveScreenIcons[index].value = R.drawable.pause_icon
    }
}