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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amplifyframework.datastore.generated.model.BedtimeStoryAudioSource
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.AnImageWithColor
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PurpleBackgroundControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    lambda: () -> Unit
){
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
                /*Controls(
                    globalViewModel.currentSoundPlaying!!,
                    globalViewModel.currentSoundPlayingPreset!!,
                    globalViewModel.currentSoundPlayingContext!!,
                    false,
                    scope,
                    state
                )*/
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetBedtimeStoryControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    applicationContext: Context,
    generalMediaPlayerService: GeneralMediaPlayerService,
    showAddIcon: Boolean,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
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
                        applicationContext,
                        generalMediaPlayerService,
                    )
                }
            }
        }
        if(showAddIcon) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(bedtimeStoryScreenBorderControlColors[7].value),
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
                    bedtimeStoryScreenBorderControlColors[5].value = Black
                    globalViewModel_!!.currentBedtimeStoryToBeAdded = bedtimeStoryInfoData
                    globalViewModel_!!.bottomSheetOpenFor = "addToSoundListOrRoutine"
                    openBottomSheet(scope, state)
                }
            }
        }
    }
}

private fun activateControls(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    index: Int,
    context: Context,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    when(index){
        /*0 -> resetBedtimeStory(
            generalMediaPlayerService,
            bedtimeStoryInfoData,
            context
        )
        1 -> seekBack15(
            index,
            bedtimeStoryInfoData,
            context,
            generalMediaPlayerService,
        )
        2 -> {
            playOrPauseAccordingly(
                bedtimeStoryInfoData,
                generalMediaPlayerService,
                generalMediaPlayerService,
                context
            )
        }
        3 -> seekForward15(
            index,
            bedtimeStoryInfoData,
            context,
            generalMediaPlayerService,
        )*/
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
            bedtimeStoryTimer.pause()
            activateLocalBedtimeStoryControlButton(2)
            activateGlobalBedtimeStoryControlButton(2)
            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
        }
    }
}

private fun startBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
) {
    if(bedtimeStoryUri != null){
        if(generalMediaPlayerService.isMediaPlayerInitialized()){
            if(globalViewModel_!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                generalMediaPlayerService.startMediaPlayer()
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
        bedtimeStoryTimer.start()
        deActivateLocalBedtimeStoryControlButton(2)
        deActivateLocalBedtimeStoryControlButton(0)
        setGlobalPropertiesAfterPlayingBedtimeStory(bedtimeStoryInfoData)
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData
){
    generalMediaPlayerService.onDestroy()
    generalMediaPlayerService.setAudioUri(bedtimeStoryUri!!)
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    bedtimeStoryTimer.setMaxDuration(bedtimeStoryInfoData.fullPlayTime.toLong())
    bedtimeStoryTimer.setDuration(0L)
    resetBothLocalAndGlobalControlButtons()
    //resetAll(context, soundMediaPlayerService)
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

private fun retrieveBedtimeStoryAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
) {
    if (
        bedtimeStoryInfoData.audioSource == BedtimeStoryAudioSource.UPLOADED
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
    } else {
        //if recorded
        //get chapter, get pages, get recordings from s3, play them one after the order
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