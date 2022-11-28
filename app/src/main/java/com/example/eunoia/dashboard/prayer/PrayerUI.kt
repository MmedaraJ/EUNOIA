package com.example.eunoia.dashboard.prayer

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
import com.amplifyframework.datastore.generated.model.PrayerData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.home.PrayerForRoutine.updateRecentlyPlayedUserPrayerRelationshipWithPrayer
import com.example.eunoia.dashboard.home.updatePreviousUserRoutineRelationship
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.prayer.resetGlobalControlButtons
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.formatMilliSecond
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

@Composable
fun DisplayUsersPrayers(
    prayerData: PrayerData,
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    startClicked: (index: Int) -> Unit,
    clicked: (index: Int) -> Unit
){
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { clicked(index) }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = Neptune,
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
                    text = "[${prayerData.displayName}]",
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
                if(prayerData.shortDescription != null){
                    shortDescription = prayerData.shortDescription
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
                val playTimeString = formatMilliSecond(prayerData.fullPlayTime.toLong())
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
                    text = prayerActivityPlayButtonTexts[index]!!.value,
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
                val icon = if(prayerData.icon == null) R.drawable.danger_of_sleeping_pills_icon
                else prayerData.icon
                AnImage(
                    icon,
                    "${prayerData.displayName} icon",
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
fun PurpleBackgroundPrayerControls(
    prayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    context: Context
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
                    prayerData,
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    true,
                    scope,
                    state,
                    context
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetPrayerControls(
    prayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    showAddIcon: Boolean,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    context: Context
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        prayerScreenIcons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(
                        listOf(
                            prayerScreenBackgroundControlColor1[index].value,
                            prayerScreenBackgroundControlColor2[index].value
                        ),
                        angle = 45f
                    )
                    .border(
                        BorderStroke(
                            0.5.dp,
                            prayerScreenBorderControlColors[index].value
                        ),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    icon.value,
                    "icon",
                    prayerScreenBorderControlColors[index].value,
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {
                    activateControls(
                        prayerData,
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
                    .background(prayerScreenBorderControlColors[4].value),
                contentAlignment = Alignment.Center
            ) {
                AnImageWithColor(
                    soundViewModel!!.addIcon.value,
                    "save prayer icon",
                    White,
                    10.dp,
                    10.dp,
                    0,
                    0
                ) {
                    prayerScreenBorderControlColors[4].value = Black
                    prayerViewModel!!.currentPrayerToBeAdded = prayerData
                    globalViewModel!!.bottomSheetOpenFor = "addToPrayerListOrRoutine"
                    openBottomSheet(scope, state)
                }
            }
        }
    }
}

private fun activateControls(
    prayerData: PrayerData,
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    context: Context
){
    when(index){
        0 -> resetPrayer(
            generalMediaPlayerService,
            prayerData
        )
        1 -> seekBack15(
            prayerData,
            generalMediaPlayerService,
        )
        2 -> {
            resetCurrentlyPlayingRoutineIfNecessaryPrayerUI(
                soundMediaPlayerService,
                generalMediaPlayerService,
                context,
                prayerData
            )
        }
        3 -> seekForward15(
            prayerData,
            generalMediaPlayerService,
        )
    }
}

fun resetPrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    prayerData: PrayerData
){
    if(prayerViewModel!!.currentPrayerPlaying != null) {
        if (prayerViewModel!!.currentPrayerPlaying!!.id == prayerData.id) {
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
                selfLoveViewModel!!.currentSelfLovePlaying == null
            ) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                prayerClicked.value = false
                prayerAngle.value = 0f
                prayerTimer.stop()
                prayerViewModel!!.prayerTimer = prayerTimer
                prayerTimeDisplay = timerFormatMS(prayerData.fullPlayTime.toLong())
                prayerViewModel!!.prayerTimeDisplay = prayerTimeDisplay
                prayerViewModel!!.isCurrentPrayerPlaying = false
                generalMediaPlayerService.onDestroy()
            }
        }
    }
}

fun seekBack15(
    prayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(prayerViewModel!!.currentPrayerPlaying != null) {
        if (prayerViewModel!!.currentPrayerPlaying!!.id == prayerData.id) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
                selfLoveViewModel!!.currentSelfLovePlaying == null
            ) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition - 15000
                if(newSeekTo < 0){
                    newSeekTo = 0
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                prayerClicked.value = false
                prayerAngle.value = (
                        (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                                (prayerData.fullPlayTime).toFloat()
                        ) * 360f
                prayerTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                prayerViewModel!!.prayerTimer = prayerTimer
                if(prayerViewModel!!.isCurrentPrayerPlaying) {
                    prayerTimer.start()
                    prayerViewModel!!.prayerTimer = prayerTimer
                }
            }
        }
    }
}

fun seekForward15(
    prayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    if(prayerViewModel!!.currentPrayerPlaying != null) {
        if (prayerViewModel!!.currentPrayerPlaying!!.id == prayerData.id) {
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
                selfLoveViewModel!!.currentSelfLovePlaying == null
            ) {
                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateGlobalprayerControlButton(2)
                    deActivateGlobalprayerControlButton(0)
                    activateLocalPrayerControlButton(2)
                    deActivateLocalPrayerControlButton(0)
                }
                generalMediaPlayerService.getMediaPlayer()!!.seekTo(newSeekTo)
                prayerClicked.value = false
                prayerAngle.value = (
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                                prayerData.fullPlayTime.toFloat()
                        ) * 360f
                prayerTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                prayerViewModel!!.prayerTimer = prayerTimer
                if(prayerViewModel!!.isCurrentPrayerPlaying) {
                    prayerTimer.start()
                    prayerViewModel!!.prayerTimer = prayerTimer
                }
            }
        }
    }
}

@Composable
fun SetUpRoutineCurrentlyPlayingAlertDialogPrayerUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    prayerData: PrayerData
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
                        pauseOrPlayPrayerAccordingly(
                            prayerData,
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

fun resetCurrentlyPlayingRoutineIfNecessaryPrayerUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    prayerData: PrayerData
) {
    if(routineViewModel!!.currentRoutinePlaying != null){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        pauseOrPlayPrayerAccordingly(
            prayerData,
            generalMediaPlayerService,
        )
    }
}

fun pauseOrPlayPrayerAccordingly(
    prayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(prayerViewModel!!.currentPrayerPlaying != null) {
        if (prayerViewModel!!.currentPrayerPlaying!!.id == prayerData.id) {
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
                selfLoveViewModel!!.currentSelfLovePlaying == null
            ) {
                if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                    pausePrayer(generalMediaPlayerService)
                }else{
                    startPrayer(
                        generalMediaPlayerService,
                        prayerData,
                    )
                }
            }else{
                startPrayer(
                    generalMediaPlayerService,
                    prayerData,
                )
            }
        }else{
            retrievePrayerAudio(
                generalMediaPlayerService,
                prayerData,
            )
        }
    }else{
        retrievePrayerAudio(
            generalMediaPlayerService,
            prayerData,
        )
    }
    openRoutineIsCurrentlyPlayingDialogBox = false
}

private fun pausePrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
        selfLoveViewModel!!.currentSelfLovePlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            prayerTimer.pause()
            prayerViewModel!!.prayerTimer = prayerTimer
            activateLocalPrayerControlButton(2)
            activateGlobalprayerControlButton(2)
            globalViewModel!!.generalPlaytimeTimer.pause()
            prayerViewModel!!.isCurrentPrayerPlaying = false
        }
    }
}

private fun startPrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    prayerData: PrayerData,
) {
    if(prayerUri != null){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
            selfLoveViewModel!!.currentSelfLovePlaying == null
        ){
            if(prayerViewModel!!.currentPrayerPlaying!!.id == prayerData.id){
                generalMediaPlayerService.startMediaPlayer()
                afterPlayingPrayer(prayerData)
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

private fun afterPlayingPrayer(prayerData: PrayerData){
    prayerTimer.start()
    prayerViewModel!!.prayerTimer = prayerTimer
    deActivateLocalPrayerControlButton(2)
    deActivateLocalPrayerControlButton(0)
    globalViewModel!!.generalPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingPrayer(prayerData)
}

private fun updatePreviousAndCurrentPrayerRelationship(
    prayerData: PrayerData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
){
    updatePreviousUserPrayerRelationship {
        updateRecentlyPlayedUserPrayerRelationshipWithPrayer(
            prayerData
        ) {
            updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
                updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
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
    updatePreviousAndCurrentPrayerRelationship(
        prayerData,
        generalMediaPlayerService
    ) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(prayerUri!!)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        prayerTimer.setMaxDuration(prayerData.fullPlayTime.toLong())
        prayerViewModel!!.prayerTimer = prayerTimer
        resetOtherGeneralMediaPlayerUsersExceptPrayer()
    }
    resetBothLocalAndGlobalControlButtons()
    afterPlayingPrayer(prayerData)
}

private fun setGlobalPropertiesAfterPlayingPrayer(
    prayerData: PrayerData
){
    prayerViewModel!!.currentPrayerPlaying = prayerData
    prayerViewModel!!.currentPrayerPlayingUri = prayerUri
    prayerViewModel!!.isCurrentPrayerPlaying = true
    deActivateGlobalprayerControlButton(2)
    deActivateGlobalprayerControlButton(0)
}

fun resetPrayerGlobalProperties(){
    Log.i("Reset praya", "Prayersss will reset now")
    prayerViewModel!!.currentPrayerPlaying = null
    prayerViewModel!!.currentPrayerPlayingUri = null
    prayerViewModel!!.isCurrentPrayerPlaying = false
    prayerViewModel!!.prayerTimer.stop()
    resetGlobalControlButtons()
}

private fun retrievePrayerAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    prayerData: PrayerData,
) {
    SoundBackend.retrieveAudio(
        prayerData.audioKeyS3,
        prayerData.prayerOwner.amplifyAuthUserId
    ) {
        prayerUri = it
        startPrayer(
            generalMediaPlayerService,
            prayerData,
        )
    }
}

fun resetBothLocalAndGlobalControlButtons(){
    deActivateLocalPrayerControlButton(0)
    deActivateLocalPrayerControlButton(1)
    activateLocalPrayerControlButton(2)
    deActivateLocalPrayerControlButton(3)

    deActivateGlobalprayerControlButton(0)
    deActivateGlobalprayerControlButton(1)
    activateGlobalprayerControlButton(2)
    deActivateGlobalprayerControlButton(3)
}

fun resetBothLocalAndGlobalControlButtonsAfterReset(){
    activateLocalPrayerControlButton(0)
    deActivateLocalPrayerControlButton(1)
    activateLocalPrayerControlButton(2)
    deActivateLocalPrayerControlButton(3)

    activateGlobalprayerControlButton(0)
    deActivateGlobalprayerControlButton(1)
    activateGlobalprayerControlButton(2)
    deActivateGlobalprayerControlButton(3)
}

private fun activateLocalPrayerControlButton(index: Int){
    prayerScreenBorderControlColors[index].value = Black
    prayerScreenBackgroundControlColor1[index].value = SoftPeach
    prayerScreenBackgroundControlColor2[index].value = Solitude
    if(index == 2){
        prayerScreenIcons[index].value = R.drawable.play_icon
    }
}

private fun deActivateLocalPrayerControlButton(index: Int){
    prayerScreenBorderControlColors[index].value = Bizarre
    prayerScreenBackgroundControlColor1[index].value = White
    prayerScreenBackgroundControlColor2[index].value = White
    if(index == 2){
        prayerScreenIcons[index].value = R.drawable.pause_icon
    }
}

private fun activateGlobalprayerControlButton(index: Int){
    prayerViewModel!!.prayerScreenBorderControlColors[index].value = prayerScreenBorderControlColors[index].value
    prayerViewModel!!.prayerScreenBackgroundControlColor1[index].value = prayerScreenBackgroundControlColor1[index].value
    prayerViewModel!!.prayerScreenBackgroundControlColor2[index].value = prayerScreenBackgroundControlColor2[index].value
    if(index == 2){
        prayerViewModel!!.prayerScreenIcons[index].value = prayerScreenIcons[index].value
    }
}

private fun deActivateGlobalprayerControlButton(index: Int){
    prayerViewModel!!.prayerScreenBorderControlColors[index].value = prayerScreenBorderControlColors[index].value
    prayerViewModel!!.prayerScreenBackgroundControlColor1[index].value = prayerScreenBackgroundControlColor1[index].value
    prayerViewModel!!.prayerScreenBackgroundControlColor2[index].value = prayerScreenBackgroundControlColor2[index].value
    if(index == 2){
        prayerViewModel!!.prayerScreenIcons[index].value = prayerScreenIcons[index].value
    }
}