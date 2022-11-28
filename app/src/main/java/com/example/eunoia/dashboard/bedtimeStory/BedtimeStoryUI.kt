package com.example.eunoia.dashboard.bedtimeStory

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfoRelationship
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.BedtimeStoryForRoutine
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
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.getCurrentlyPlayingTime
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope

private const val TAG = "BedtimeStoryUI"

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
                    soundViewModel!!.addIcon.value,
                    "save bedtime story icon",
                    White,
                    10.dp,
                    10.dp,
                    0,
                    0
                ) {
                    bedtimeStoryScreenBorderControlColors[4].value = Black
                    bedtimeStoryViewModel!!.currentBedtimeStoryToBeAdded = bedtimeStoryInfoData
                    globalViewModel!!.bottomSheetOpenFor = "addToBedtimeStoryListOrRoutine"
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
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
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
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                selfLoveViewModel!!.currentSelfLovePlaying == null &&
                prayerViewModel!!.currentPrayerPlaying == null
            ) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                val angle = 0f
                setCircularSliderClicked(false)
                setCircularSliderAngle(angle)
                bedtimeStoryTimer.stop()
                bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
                bedtimeStoryTimeDisplay = timerFormatMS(bedtimeStoryInfoData.fullPlayTime.toLong())
                bedtimeStoryViewModel!!.bedtimeStoryTimeDisplay = bedtimeStoryTimeDisplay
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
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                selfLoveViewModel!!.currentSelfLovePlaying == null &&
                prayerViewModel!!.currentPrayerPlaying == null
            ) {
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

                val angle = (
                    (generalMediaPlayerService.getMediaPlayer()!!.currentPosition).toFloat() /
                    (bedtimeStoryInfoData.fullPlayTime).toFloat()
                ) * 360f
                setCircularSliderClicked(false)
                setCircularSliderAngle(angle)

                bedtimeStoryTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
                if(bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying) {
                    bedtimeStoryTimer.start()
                    bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
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
            if(
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                selfLoveViewModel!!.currentSelfLovePlaying == null &&
                prayerViewModel!!.currentPrayerPlaying == null
            ) {
                globalViewModel!!.resetCDT()

                var newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.currentPosition + 15000
                if(newSeekTo > generalMediaPlayerService.getMediaPlayer()!!.duration){
                    newSeekTo = generalMediaPlayerService.getMediaPlayer()!!.duration - 2000
                    activateGlobalBedtimeStoryControlButton(2)
                    deActivateGlobalBedtimeStoryControlButton(0)
                    activateLocalBedtimeStoryControlButton(2)
                    deActivateLocalBedtimeStoryControlButton(0)
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

                val angle = (
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toFloat() /
                    bedtimeStoryInfoData.fullPlayTime.toFloat()
                ) * 360f
                setCircularSliderClicked(false)
                setCircularSliderAngle(angle)

                bedtimeStoryTimer.setDuration(
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition.toLong()
                )
                bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
                if(bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying) {
                    bedtimeStoryTimer.start()
                    bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
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
    bedtimeStoryInfoData: BedtimeStoryInfoData
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
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            bedtimeStoryInfoData,
                            context
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
    bedtimeStoryInfoData: BedtimeStoryInfoData
) {
    if(routineViewModel!!.currentRoutinePlaying != null){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        pauseOrPlayBedtimeStoryAccordingly(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
        )
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
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                selfLoveViewModel!!.currentSelfLovePlaying == null &&
                prayerViewModel!!.currentPrayerPlaying == null
            ) {
                if (generalMediaPlayerService.isMediaPlayerPlaying()) {
                    pauseBedtimeStory(generalMediaPlayerService)
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
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        selfLoveViewModel!!.currentSelfLovePlaying == null &&
        prayerViewModel!!.currentPrayerPlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            bedtimeStoryTimer.pause()
            bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
            activateLocalBedtimeStoryControlButton(2)
            activateGlobalBedtimeStoryControlButton(2)
            globalViewModel!!.generalPlaytimeTimer.pause()
            globalViewModel!!.resetCDT()
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
    if(bedtimeStoryUri != null){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            selfLoveViewModel!!.currentSelfLovePlaying == null &&
            prayerViewModel!!.currentPrayerPlaying == null
        ){
            if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id == bedtimeStoryInfoData.id){
                globalViewModel!!.remainingPlayTime =
                    bedtimeStoryInfoData.fullPlayTime -
                        generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                Log.i(TAG, "bedtimeStoryViewModel_!!.remainingPlayTime = ${globalViewModel!!.remainingPlayTime}")

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
            initializeMediaPlayer(
                generalMediaPlayerService,
                soundMediaPlayerService,
                bedtimeStoryInfoData,
                context
            )
        }
    }
}

private fun afterPlayingBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    context: Context
){
    bedtimeStoryTimer.start()
    bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
    deActivateLocalBedtimeStoryControlButton(2)
    deActivateLocalBedtimeStoryControlButton(0)
    globalViewModel!!.generalPlaytimeTimer.start()
    startCDT(
        generalMediaPlayerService,
        soundMediaPlayerService,
        bedtimeStoryInfoData,
        context
    )
    setGlobalPropertiesAfterPlayingBedtimeStory(bedtimeStoryInfoData)
}

fun startCDT(
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
                    Log.i(TAG, "Bts about to be updated from viewing page")
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

private fun updatePreviousAndCurrentBedtimeStoryRelationship(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: (userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
){
    updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
        updateRecentlyPlayedUserBedtimeStoryInfoRelationshipWithBedtimeStoryInfo(bedtimeStoryInfoData) { userBedtimeStoryInfoRelationship ->
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
                    completed(userBedtimeStoryInfoRelationship)
                }
            }
        }
    }
}

private fun getSeekToPos(
    userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship,
): Int{
    var seekToPos = 0
    if(userBedtimeStoryInfoRelationship.continuePlayingTime != null){
        seekToPos = userBedtimeStoryInfoRelationship.continuePlayingTime
    }
    return seekToPos
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
        generalMediaPlayerService.setAudioUri(bedtimeStoryUri!!)
        val seekToPos = getSeekToPos(it)
        Log.i(TAG, "Bts UI seekToPos = $seekToPos")
        generalMediaPlayerService.setSeekToPos(seekToPos)

        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)

        bedtimeStoryTimer.setDuration(
            it.continuePlayingTime.toLong()
        )
        bedtimeStoryTimer.setMaxDuration(bedtimeStoryInfoData.fullPlayTime.toLong())
        bedtimeStoryViewModel!!.bedtimeStoryTimer = bedtimeStoryTimer
        globalViewModel!!.remainingPlayTime = bedtimeStoryInfoData.fullPlayTime - it.continuePlayingTime

        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()
        resetBothLocalAndGlobalControlButtons()
        afterPlayingBedtimeStory(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
        )
    }
}

private fun setGlobalPropertiesAfterPlayingBedtimeStory(
    bedtimeStoryInfoData: BedtimeStoryInfoData
){
    bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = bedtimeStoryInfoData
    bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = bedtimeStoryUri
    bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = true
    deActivateGlobalBedtimeStoryControlButton(2)
    deActivateGlobalBedtimeStoryControlButton(0)
}

fun resetBedtimeStoryGlobalProperties(){
    bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = null
    bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = null
    bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
    bedtimeStoryViewModel!!.bedtimeStoryTimer.stop()
    resetGlobalControlButtons()
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
        bedtimeStoryUri = it
        startBedtimeStory(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryInfoData,
            context
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

fun deActivateResetButton(){
    deActivateLocalBedtimeStoryControlButton(0)
    deActivateGlobalBedtimeStoryControlButton(0)
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
    bedtimeStoryViewModel!!.bedtimeStoryScreenBorderControlColors[index].value = bedtimeStoryScreenBorderControlColors[index].value
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor1[index].value = bedtimeStoryScreenBackgroundControlColor1[index].value
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor2[index].value = bedtimeStoryScreenBackgroundControlColor2[index].value
    if(index == 2){
        bedtimeStoryViewModel!!.bedtimeStoryScreenIcons[index].value = bedtimeStoryScreenIcons[index].value
    }
}

private fun deActivateGlobalBedtimeStoryControlButton(index: Int){
    bedtimeStoryViewModel!!.bedtimeStoryScreenBorderControlColors[index].value = bedtimeStoryScreenBorderControlColors[index].value
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor1[index].value = bedtimeStoryScreenBackgroundControlColor1[index].value
    bedtimeStoryViewModel!!.bedtimeStoryScreenBackgroundControlColor2[index].value = bedtimeStoryScreenBackgroundControlColor2[index].value
    if(index == 2){
        bedtimeStoryViewModel!!.bedtimeStoryScreenIcons[index].value = bedtimeStoryScreenIcons[index].value
    }
}