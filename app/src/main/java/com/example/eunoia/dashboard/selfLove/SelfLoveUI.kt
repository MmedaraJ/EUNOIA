package com.example.eunoia.dashboard.selfLove

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.home.SelfLoveForRoutine
import com.example.eunoia.dashboard.home.updatePreviousUserRoutineRelationship
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.selfLove.resetGlobalControlButtons
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.openRoutineIsCurrentlyPlayingDialogBox
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.formatMilliSecond
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

@Composable
fun DisplayUsersSelfLoves(
    selfLoveData: SelfLoveData,
    index: Int,
    startClicked: (index: Int) -> Unit,
    clicked: (index: Int) -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { clicked(index) }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = Madang,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                times_used,
                steps,
                shuffle,
                image
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "[${selfLoveData.displayName}]",
                    color = Black,
                    fontSize = 14,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(times_used) {
                        top.linkTo(title.bottom, margin = 2.dp)
                    }
            ) {
                var shortDescription = "Short description"
                if(selfLoveData.shortDescription != null){
                    shortDescription = selfLoveData.shortDescription
                }

                ExtraLightText(
                    text = shortDescription,
                    color = Black,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(steps) {
                        top.linkTo(times_used.bottom, margin = 2.dp)
                    }
            ) {
                val playTimeString = formatMilliSecond(selfLoveData.fullPlayTime.toLong())
                LightText(
                    text = playTimeString,
                    color = Grey,
                    fontSize = 7,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .constrainAs(shuffle) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
                    .clickable {
                        startClicked(index)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = selfLoveActivityPlayButtonTexts[index]!!.value,
                    color = Color.White,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(image) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        top.linkTo(times_used.bottom, 2.dp)
                    }
            ) {
                val icon = if(selfLoveData.icon == null) R.drawable.danger_of_sleeping_pills_icon
                else selfLoveData.icon
                AnImage(
                    icon,
                    "${selfLoveData.displayName} icon",
                    97.0,
                    104.0,
                    0,
                    0,
                    LocalContext.current
                ){}
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PurpleBackgroundSelfLoveControls(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    context: Context,
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
                    selfLoveData,
                    generalMediaPlayerService,
                    true,
                    scope,
                    state,
                    soundMediaPlayerService,
                    context,
                )
            }
        }
    }
}

var selfLoveLyricsIcon = R.drawable.comment_icon
var selfLoveLyricsBackgroundColor1 = mutableStateOf(White)
var selfLoveLyricsBackgroundColor2 = mutableStateOf(White)
var selfLoveLyricsBorderColor = mutableStateOf(Bizarre)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetSelfLoveControls(
    selfLoveData: SelfLoveData,
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
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .gradientBackground(
                    listOf(
                        selfLoveLyricsBackgroundColor1.value,
                        selfLoveLyricsBackgroundColor2.value
                    ),
                    angle = 45f
                )
                .border(
                    BorderStroke(
                        0.5.dp,
                        selfLoveLyricsBorderColor.value
                    ),
                    RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            AnImageWithColor(
                selfLoveLyricsIcon,
                "icon",
                selfLoveLyricsBorderColor.value,
                12.dp,
                12.dp,
                0,
                0
            ) {
                showSelfLoveBottomSheet(
                    selfLoveData,
                    scope,
                    state
                )
            }
        }

        selfLoveScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            selfLoveScreenBackgroundControlColor1[index].value,
                            selfLoveScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            selfLoveScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    selfLoveScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        selfLoveData,
                        index,
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        context,
                    )
                }
            }
        }

        if(showAddIcon) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(selfLoveScreenBorderControlColors[4].value),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    globalViewModel_!!.addIcon.value,
                    "save self love icon",
                    White,
                    10.dp,
                    10.dp,
                    0,
                    0
                ) {
                    selfLoveScreenBorderControlColors[4].value = Black
                    globalViewModel_!!.currentSelfLoveToBeAdded = selfLoveData
                    globalViewModel_!!.bottomSheetOpenFor = "addToSelfLoveListOrRoutine"
                    openBottomSheet(scope, state)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun showSelfLoveBottomSheet(
    selfLoveData: SelfLoveData,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    //activateSelfLoveLyricsButton()
    globalViewModel_!!.currentSelfLoveLyricsToBeShown = selfLoveData
    globalViewModel_!!.bottomSheetOpenFor = "showSelfLoveLyrics"
    openBottomSheet(scope, state)
}

private fun activateSelfLoveLyricsButton(){
    selfLoveLyricsBorderColor.value = Black
    selfLoveLyricsBackgroundColor1.value = SoftPeach
    selfLoveLyricsBackgroundColor2.value = Solitude
}

private fun deActivateSelfLoveLyricsButton(){
    selfLoveLyricsBorderColor.value = Bizarre
    selfLoveLyricsBackgroundColor1.value = White
    selfLoveLyricsBackgroundColor2.value = White
}

private fun activateControls(
    selfLoveData: SelfLoveData,
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    context: Context,
){
    when(index){
        0 -> resetSelfLove(
            generalMediaPlayerService,
            selfLoveData
        )
        1 -> seekBack15(
            selfLoveData,
            generalMediaPlayerService,
        )
        2 -> {
            resetCurrentlyPlayingRoutineIfNecessarySelfLoveUI(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
                selfLoveData
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
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                selfLoveClicked.value = false
                selfLoveAngle.value = 0f
                selfLoveTimer.stop()
                globalViewModel_!!.selfLoveTimer = selfLoveTimer
                selfLoveTimeDisplay = timerFormatMS(selfLoveData.fullPlayTime.toLong())
                globalViewModel_!!.selfLoveTimeDisplay = selfLoveTimeDisplay
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
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                selfLoveClicked.value = false
                selfLoveAngle.value = (
                        (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                                (selfLoveData.fullPlayTime).toFloat()
                        ) * 360f
                selfLoveTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                globalViewModel_!!.selfLoveTimer = selfLoveTimer
                if(globalViewModel_!!.isCurrentSelfLovePlaying) {
                    selfLoveTimer.start()
                    globalViewModel_!!.selfLoveTimer = selfLoveTimer
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
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateGlobalSelfLoveControlButton(2)
                    deActivateGlobalSelfLoveControlButton(0)
                    activateLocalSelfLoveControlButton(2)
                    deActivateLocalSelfLoveControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                selfLoveClicked.value = false
                selfLoveAngle.value = (
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                                selfLoveData.fullPlayTime.toFloat()
                        ) * 360f
                selfLoveTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                globalViewModel_!!.selfLoveTimer = selfLoveTimer
                if(globalViewModel_!!.isCurrentSelfLovePlaying) {
                    selfLoveTimer.start()
                    globalViewModel_!!.selfLoveTimer = selfLoveTimer
                }
            }
        }
    }
}

@Composable
fun SetUpRoutineCurrentlyPlayingAlertDialogSelfLoveUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    selfLoveData: SelfLoveData
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
                        pauseOrPlaySelfLoveAccordingly(
                            selfLoveData,
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

fun resetCurrentlyPlayingRoutineIfNecessarySelfLoveUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    selfLoveData: SelfLoveData
) {
    if(globalViewModel_!!.currentRoutinePlaying != null){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        pauseOrPlaySelfLoveAccordingly(
            selfLoveData,
            generalMediaPlayerService,
        )
    }
}

fun pauseOrPlaySelfLoveAccordingly(
    selfLoveData: SelfLoveData,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(globalViewModel_!!.currentSelfLovePlaying != null) {
        if (globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id) {
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                globalViewModel_!!.currentBedtimeStoryPlaying == null &&
                globalViewModel_!!.currentPrayerPlaying == null
            ) {
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
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        globalViewModel_!!.currentBedtimeStoryPlaying == null &&
        globalViewModel_!!.currentPrayerPlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            selfLoveTimer.pause()
            globalViewModel_!!.selfLoveTimer = selfLoveTimer
            activateLocalSelfLoveControlButton(2)
            activateGlobalSelfLoveControlButton(2)
            globalViewModel_!!.generalPlaytimeTimer.pause()
            globalViewModel_!!.isCurrentSelfLovePlaying = false
        }
    }
}

private fun startSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData,
) {
    if(selfLoveUri != null){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentBedtimeStoryPlaying == null &&
            globalViewModel_!!.currentPrayerPlaying == null
        ){
            if(globalViewModel_!!.currentSelfLovePlaying!!.id == selfLoveData.id){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingSelfLove(selfLoveData)
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
    }
}

private fun afterPlayingSelfLove(
    selfLoveData: SelfLoveData
){
    selfLoveTimer.start()
    globalViewModel_!!.selfLoveTimer = selfLoveTimer
    deActivateLocalSelfLoveControlButton(2)
    deActivateLocalSelfLoveControlButton(0)
    globalViewModel_!!.generalPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingSelfLove(selfLoveData)
}

private fun updatePreviousAndCurrentSelfLoveRelationship(
    selfLoveData: SelfLoveData,
    completed: () -> Unit
){
    updatePreviousUserSelfLoveRelationship {
        SelfLoveForRoutine.updateRecentlyPlayedUserSelfLoveRelationshipWithSelfLove(
            selfLoveData
        ) {
            updatePreviousUserPrayerRelationship {
                updatePreviousUserBedtimeStoryRelationship {
                    completed()
                }
            }
        }
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData
){
    updatePreviousAndCurrentSelfLoveRelationship(selfLoveData) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(selfLoveUri!!)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        selfLoveTimer.setMaxDuration(selfLoveData.fullPlayTime.toLong())
        globalViewModel_!!.selfLoveTimer = selfLoveTimer
        resetOtherGeneralMediaPlayerUsersExceptSelfLove()
    }
    resetBothLocalAndGlobalControlButtons()
    afterPlayingSelfLove(selfLoveData)
}

private fun setGlobalPropertiesAfterPlayingSelfLove(
    selfLoveData: SelfLoveData
){
    globalViewModel_!!.currentSelfLovePlaying = selfLoveData
    globalViewModel_!!.currentSelfLovePlayingUri = selfLoveUri
    globalViewModel_!!.isCurrentSelfLovePlaying = true
    deActivateGlobalSelfLoveControlButton(2)
    deActivateGlobalSelfLoveControlButton(0)
}

fun resetSelfLoveGlobalProperties(){
    globalViewModel_!!.currentSelfLovePlaying = null
    globalViewModel_!!.currentSelfLovePlayingUri = null
    globalViewModel_!!.isCurrentSelfLovePlaying = false
    globalViewModel_!!.selfLoveTimer.stop()
    resetGlobalControlButtons()
}

private fun retrieveSelfLoveAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    selfLoveData: SelfLoveData,
) {
    SoundBackend.retrieveAudio(
        selfLoveData.audioKeyS3,
        selfLoveData.selfLoveOwner.amplifyAuthUserId
    ) {
        selfLoveUri = it
        startSelfLove(
            generalMediaPlayerService,
            selfLoveData,
        )
    }
}

fun resetBothLocalAndGlobalControlButtons(){
    deActivateLocalSelfLoveControlButton(0)
    deActivateLocalSelfLoveControlButton(1)
    activateLocalSelfLoveControlButton(2)
    deActivateLocalSelfLoveControlButton(3)

    deActivateGlobalSelfLoveControlButton(0)
    deActivateGlobalSelfLoveControlButton(1)
    activateGlobalSelfLoveControlButton(2)
    deActivateGlobalSelfLoveControlButton(3)
}

fun resetBothLocalAndGlobalControlButtonsAfterReset(){
    activateLocalSelfLoveControlButton(0)
    deActivateLocalSelfLoveControlButton(1)
    activateLocalSelfLoveControlButton(2)
    deActivateLocalSelfLoveControlButton(3)

    activateGlobalSelfLoveControlButton(0)
    deActivateGlobalSelfLoveControlButton(1)
    activateGlobalSelfLoveControlButton(2)
    deActivateGlobalSelfLoveControlButton(3)
}

private fun activateLocalSelfLoveControlButton(index: Int){
    selfLoveScreenBorderControlColors[index].value = Black
    selfLoveScreenBackgroundControlColor1[index].value = SoftPeach
    selfLoveScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        selfLoveScreenIcons[index].value = R.drawable.play_icon
    }
}

private fun deActivateLocalSelfLoveControlButton(index: Int){
    selfLoveScreenBorderControlColors[index].value = Bizarre
    selfLoveScreenBackgroundControlColor1[index].value = White
    selfLoveScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        selfLoveScreenIcons[index].value = R.drawable.pause_icon
    }
}

private fun activateGlobalSelfLoveControlButton(index: Int){
    globalViewModel_!!.selfLoveScreenBorderControlColors[index].value = selfLoveScreenBorderControlColors[index].value
    globalViewModel_!!.selfLoveScreenBackgroundControlColor1[index].value = selfLoveScreenBackgroundControlColor1[index].value
    globalViewModel_!!.selfLoveScreenBackgroundControlColor2[index].value = selfLoveScreenBackgroundControlColor2[index].value
    if(index == 2){
        globalViewModel_!!.selfLoveScreenIcons[index].value = selfLoveScreenIcons[index].value
    }
}

private fun deActivateGlobalSelfLoveControlButton(index: Int){
    globalViewModel_!!.selfLoveScreenBorderControlColors[index].value = selfLoveScreenBorderControlColors[index].value
    globalViewModel_!!.selfLoveScreenBackgroundControlColor1[index].value = selfLoveScreenBackgroundControlColor1[index].value
    globalViewModel_!!.selfLoveScreenBackgroundControlColor2[index].value = selfLoveScreenBackgroundControlColor2[index].value
    if(index == 2){
        globalViewModel_!!.selfLoveScreenIcons[index].value = selfLoveScreenIcons[index].value
    }
}