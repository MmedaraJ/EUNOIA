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
import com.example.eunoia.dashboard.selfLove.navigateToSelfLoveScreen
import com.example.eunoia.dashboard.selfLove.resetBothLocalAndGlobalControlButtonsAfterReset
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

private const val TAG = "bottomSheetSelfLoveControl"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetSelfLoveControlPanel(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
): Boolean{
    var showing = false
    if(globalViewModel.currentSelfLovePlaying != null) {
        showing = true
        BottomSheetSelfLoveControlPanelUI(
            selfLoveData = globalViewModel.currentSelfLovePlaying!!,
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
                if(globalViewModel_!!.navController != null){
                    closeBottomSheet(scope, state)
                    navigateToSelfLoveScreen(
                        globalViewModel_!!.navController!!,
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
                    globalViewModel_!!.currentSelfLovePlaying!!,
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
        globalViewModel_!!.selfLoveScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            globalViewModel_!!.selfLoveScreenBackgroundControlColor1[index].value,
                            globalViewModel_!!.selfLoveScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            globalViewModel_!!.selfLoveScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    globalViewModel_!!.selfLoveScreenBorderControlColors[index].value,
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
    if(globalViewModel_!!.currentSelfLovePlaying != null) {
        if (globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if (generalMediaPlayerService.isMediaPlayerInitialized()) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                globalViewModel_!!.selfLoveCircularSliderClicked = false
                globalViewModel_!!.selfLoveCircularSliderAngle = 0f
                globalViewModel_!!.selfLoveTimer.stop()
                globalViewModel_!!.selfLoveTimeDisplay =
                    timerFormatMS(selfLoveData.fullPlayTime.toLong())
                globalViewModel_!!.isCurrentSelfLovePlaying = false
                generalMediaPlayerService.onDestroy()
            }
        }
    }
}

fun seekBack15(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(globalViewModel_!!.currentSelfLovePlaying != null) {
        if (globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                globalViewModel_!!.selfLoveCircularSliderClicked = false
                globalViewModel_!!.selfLoveCircularSliderAngle = (
                        (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                                (selfLoveData.fullPlayTime).toFloat()
                        ) * 360f
                globalViewModel_!!.selfLoveTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(globalViewModel_!!.isCurrentSelfLovePlaying) {
                    globalViewModel_!!.selfLoveTimer.start()
                }
            }
        }
    }
}

fun seekForward15(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(globalViewModel_!!.currentSelfLovePlaying != null) {
        if (globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if(generalMediaPlayerService.isMediaPlayerInitialized()) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateSelfLoveGlobalControlButton(2)
                    deActivateSelfLoveGlobalControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                globalViewModel_!!.selfLoveCircularSliderClicked = false
                globalViewModel_!!.selfLoveCircularSliderAngle = (
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                                selfLoveData.fullPlayTime.toFloat()
                        ) * 360f
                globalViewModel_!!.selfLoveTimer.setDuration(generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong())
                if(globalViewModel_!!.isCurrentSelfLovePlaying) {
                    globalViewModel_!!.selfLoveTimer.start()
                }
            }
        }
    }
}

fun pauseOrPlayselfLoveAccordingly(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(globalViewModel_!!.currentSelfLovePlaying != null) {
        if (globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
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
            globalViewModel_!!.selfLoveTimer.pause()
            activateSelfLoveGlobalControlButton(2)
            activateSelfLoveGlobalControlButton(2)
            globalViewModel_!!.isCurrentSelfLovePlaying = false
        }
    }
}

private fun startSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData,
) {
    if(globalViewModel_!!.currentSelfLovePlayingUri != null){
        if(generalMediaPlayerService.isMediaPlayerInitialized()){
            if(globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id){
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
        globalViewModel_!!.selfLoveTimer.start()
        globalViewModel_!!.isCurrentSelfLovePlaying = true
        deActivateSelfLoveGlobalControlButton(2)
        deActivateSelfLoveGlobalControlButton(0)
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData
){
    generalMediaPlayerService.onDestroy()
    generalMediaPlayerService.setAudioUri(globalViewModel_!!.currentSelfLovePlayingUri!!)
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    globalViewModel_!!.selfLoveTimer.setMaxDuration(selfLoveData.fullPlayTime.toLong())
    globalViewModel_!!.selfLoveTimer.setDuration(0L)
    resetGlobalControlButtons()
    //resetAll(context, soundMediaPlayerService)
}

private fun retrieveSelfLoveAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData,
) {
    SoundBackend.retrieveAudio(
        selfLoveData.audioKeyS3,
        selfLoveData.selfLoveOwner.amplifyAuthUserId
    ) {
        globalViewModel_!!.currentSelfLovePlayingUri = it
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
    globalViewModel_!!.selfLoveScreenBorderControlColors[index].value = Black
    globalViewModel_!!.selfLoveScreenBackgroundControlColor1[index].value = SoftPeach
    globalViewModel_!!.selfLoveScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        globalViewModel_!!.selfLoveScreenIcons[index].value = R.drawable.play_icon
    }
}

fun deActivateSelfLoveGlobalControlButton(index: Int){
    globalViewModel_!!.selfLoveScreenBorderControlColors[index].value = Bizarre
    globalViewModel_!!.selfLoveScreenBackgroundControlColor1[index].value = White
    globalViewModel_!!.selfLoveScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        globalViewModel_!!.selfLoveScreenIcons[index].value = R.drawable.pause_icon
    }
}