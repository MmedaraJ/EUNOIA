package com.example.eunoia.dashboard.routine.userRoutineRelationshipScreen

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.*
import com.example.eunoia.dashboard.routine.RoutineElements
import com.example.eunoia.dashboard.sound.navigateBack
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.WrappedPurpleBackgroundStart
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.utils.formatMilliSecond
import kotlinx.coroutines.CoroutineScope

var prayerUri = mutableMapOf<String, Uri?>()
var bedtimeStoryUri = mutableMapOf<String, Uri?>()
var selfLoveUri = mutableMapOf<String, Uri?>()
var presetUris = mutableMapOf<String, MutableList<Uri?>>()
var sliderVolumes = mutableMapOf<String, MutableList<Int>>()

var thisUserRoutineRelationship: UserRoutineRelationship? = null

var playingOrderIndex = 0
var playingOrder: MutableList<String?>? = null
var presetsIndex = 0
var presets: MutableList<UserRoutineRelationshipSoundPreset?>? = null
var prayersIndex = 0
var prayers: MutableList<UserRoutineRelationshipPrayer?>? = null
var bedtimeStoriesIndex = 0
var bedtimeStories: MutableList<UserRoutineRelationshipBedtimeStoryInfo?>? = null
var selfLovesIndex = 0
var selfLoves: MutableList<UserRoutineRelationshipSelfLove?>? = null

var soundCountDownTimer: CountDownTimer? = null
var prayerCountDownTimer: CountDownTimer? = null
var nextPrayerCountDownTimer: CountDownTimer? = null
var selfLoveCountDownTimer: CountDownTimer? = null
var nextSelfLoveCountDownTimer: CountDownTimer? = null
var bedtimeStoryCountDownTimer: CountDownTimer? = null
var nextBedtimeStoryCountDownTimer: CountDownTimer? = null

const val START_ROUTINE = "start"
const val PAUSE_ROUTINE = "pause"
const val WAIT_FOR_ROUTINE = "wait"

var playButtonText by mutableStateOf(START_ROUTINE)

private const val TAG = "RoutineScreen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserRoutineRelationshipScreen(
    navController: NavController,
    userRoutineRelationship: UserRoutineRelationship,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    thisUserRoutineRelationship = userRoutineRelationship

    var retrievedData by rememberSaveable{ mutableStateOf(false) }

    if(routineViewModel!!.currentRoutinePlaying != null) {
        if (routineViewModel!!.currentRoutinePlaying!!.id == userRoutineRelationship.userRoutineRelationshipRoutine.id) {
            setParametersFromGlobalVariables{
                retrievedData = true
            }
        }else{
            setRoutineData {
                retrievedData = true
            }
        }
    }else{
        setRoutineData {
            retrievedData = true
        }
    }

    if(retrievedData) {
        SetUpRoutineCurrentlyPlayingAlertDialogRoutineUI(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context,
        )

        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            val (
                header,
                startBlock,
                elements,
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(header) {
                        top.linkTo(parent.top, margin = 40.dp)
                    }
                    .fillMaxWidth()
            ) {
                BackArrowHeader(
                    {
                        navigateBack(navController)
                    },
                    {
                        globalViewModel!!.bottomSheetOpenFor = "controls"
                        openBottomSheet(scope, state)
                    },
                    {
                    }
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(startBlock) {
                        top.linkTo(header.bottom, margin = 40.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val playTimeString =
                    formatMilliSecond(userRoutineRelationship.fullPlayTime.toLong())
                WrappedPurpleBackgroundStart(
                    "[${userRoutineRelationship.userRoutineRelationshipRoutine.displayName}]",
                    "${userRoutineRelationship.numberOfSteps} steps ~ $playTimeString",
                    playButtonText,
                ) {
                    resetCurrentlyPlayingRoutineIfNecessaryRoutineUI(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        context,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(elements) {
                        top.linkTo(startBlock.bottom, margin = 24.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .wrapContentHeight()
            ) {
                RoutineElements(navController, userRoutineRelationship)
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }else{
        ConstraintLayout{
            val (progressBar) = createRefs()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(progressBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

private fun setParametersFromGlobalVariables(
    completed: () -> Unit
) {
    playButtonText = PAUSE_ROUTINE

    if(routineViewModel!!.currentUserRoutineRelationshipPlaying != null) {
        thisUserRoutineRelationship = routineViewModel!!.currentUserRoutineRelationshipPlaying
    }

    if(routineViewModel!!.currentRoutinePlayingOrder != null) {
        playingOrderIndex = routineViewModel!!.currentRoutinePlayingOrderIndex!!
        playingOrder = routineViewModel!!.currentRoutinePlayingOrder!!
    }

    if (routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets != null) {
        presetsIndex = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex!!
        presets = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets!!
    }else{
        selfLovesIndex = 0
        presets = mutableListOf()
    }

    if (routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers != null) {
        prayersIndex = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!!
        prayers = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!
    }else{
        prayersIndex = thisUserRoutineRelationship!!.currentPrayerPlayingIndex
        prayers = mutableListOf()
    }

    if (routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories != null){
        bedtimeStoriesIndex = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex!!
        bedtimeStories = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories!!
    }else{
        selfLovesIndex = thisUserRoutineRelationship!!.currentSelfLovePlayingIndex
        selfLoves = mutableListOf()
    }

    if(routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves != null){
        selfLovesIndex = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex!!
        selfLoves = routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves!!
    }else{
        selfLovesIndex = thisUserRoutineRelationship!!.currentSelfLovePlayingIndex
        selfLoves = mutableListOf()
    }

    if(prayerViewModel!!.currentPrayerPlayingUri != null) {
        prayerUri[prayers!![prayersIndex]!!.prayerData.id] = prayerViewModel!!.currentPrayerPlayingUri!!
    }else{
        prayerUri = mutableMapOf()
    }

    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri != null) {
        bedtimeStoryUri[bedtimeStories!![bedtimeStoriesIndex]!!.bedtimeStoryInfoData.id] = bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri!!
    }else{
        bedtimeStoryUri = mutableMapOf()
    }

    if(selfLoveViewModel!!.currentSelfLovePlayingUri != null) {
        selfLoveUri[selfLoves!![selfLovesIndex]!!.selfLoveData.id] = selfLoveViewModel!!.currentSelfLovePlayingUri!!
    }else{
        selfLoveUri = mutableMapOf()
    }

    if(soundViewModel!!.currentSoundPlayingUris != null) {
        presetUris[presets!![presetsIndex]!!.soundPresetData.id] = soundViewModel!!.currentSoundPlayingUris!!
    }else{
        presetUris = mutableMapOf()
    }


    soundCountDownTimer = routineViewModel!!.currentRoutinePlayingSoundCountDownTimer
    prayerCountDownTimer = routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer
    nextPrayerCountDownTimer = routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer
    selfLoveCountDownTimer = routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer
    nextSelfLoveCountDownTimer = routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer
    bedtimeStoryCountDownTimer = routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer
    nextBedtimeStoryCountDownTimer = routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer

    completed()
}

fun setRoutineData(
    completed: () -> Unit
){
    playButtonText = START_ROUTINE

    playingOrderIndex = 0
    playingOrder = mutableListOf()
    presetsIndex = 0
    presets = mutableListOf()
    prayersIndex = 0
    prayers = mutableListOf()
    bedtimeStoriesIndex = 0
    bedtimeStories = mutableListOf()
    selfLovesIndex = 0
    selfLoves = mutableListOf()

    prayerUri = mutableMapOf()
    bedtimeStoryUri = mutableMapOf()
    selfLoveUri = mutableMapOf()
    presetUris = mutableMapOf()

    soundCountDownTimer = null
    prayerCountDownTimer = null
    nextPrayerCountDownTimer = null
    selfLoveCountDownTimer = null
    nextSelfLoveCountDownTimer = null
    bedtimeStoryCountDownTimer = null
    nextBedtimeStoryCountDownTimer = null

    completed()
}

@Composable
private fun SetUpRoutineCurrentlyPlayingAlertDialogRoutineUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
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
                        selectNextRoutineElement(
                            soundMediaPlayerService,
                            generalMediaPlayerService,
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

fun resetCurrentlyPlayingRoutineIfNecessaryRoutineUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
) {
    if(
        routineViewModel!!.currentRoutinePlaying != null &&
        routineViewModel!!.currentUserRoutineRelationshipPlaying != null &&
        routineViewModel!!.currentUserRoutineRelationshipPlaying!!.id != thisUserRoutineRelationship!!.id
    ){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        selectNextRoutineElement(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }
}

private fun selectNextRoutineElement(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context
) {
    updateRoutineOncePlayIsClicked(
        playingOrderIndex,
        thisUserRoutineRelationship!!,
    ) {
        if(it != null) {
            thisUserRoutineRelationship = it
        }

        globalViewModel!!.routinePlaytimeTimer.start()
        routineViewModel!!.currentRoutinePlaying = thisUserRoutineRelationship!!.userRoutineRelationshipRoutine
        playingOrder = thisUserRoutineRelationship!!.playingOrder
        routineViewModel!!.currentRoutinePlayingOrder = playingOrder
        routineViewModel!!.currentUserRoutineRelationshipPlaying = thisUserRoutineRelationship

        Log.i(
            TAG,
            "Ouer routine playing orderz is ${playingOrder!![playingOrderIndex]}"
        )
        when (playingOrder!![playingOrderIndex]) {
            "sound" -> {
                incrementPlayingOrderIndex(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
            "sleep" -> {
                incrementPlayingOrderIndex(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
            "prayer" -> {
                PrayerForUserRoutineRelationship.playOrPausePrayerAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
            "bedtimeStory" -> {
                BedtimeStoryForUserRoutineRelationship.playOrPauseBedtimeStoryAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
            "self-love" -> {
                SelfLoveForUserRoutineRelationship.playOrPauseSelfLoveAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
            else -> {
                incrementPlayingOrderIndex(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    context
                )
            }
        }
    }
}

fun incrementPlayingOrderIndex(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context
){
    Log.i(TAG, "plahing ordeer index before increment is $playingOrderIndex")
    playingOrderIndex += 1
    routineViewModel!!.currentRoutinePlayingOrderIndex = routineViewModel!!.currentRoutinePlayingOrderIndex!! + 1
    if(playingOrderIndex > playingOrder!!.indices.last){
        updateUserRoutineRelationshipWhenRoutineIsDonePlaying(
            thisUserRoutineRelationship!!
        ){
            Log.i(TAG, "Routine is endeedz")
            thisUserRoutineRelationship = it
            routineViewModel!!.currentUserRoutineRelationshipPlaying = it
            playingOrderIndex = 0
            routineViewModel!!.currentRoutinePlayingOrderIndex = 0
            playButtonText = START_ROUTINE
        }
    }else{
        selectNextRoutineElement(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }
}

@Preview(
    showBackground = true,
    name = "Light mode"
)
@Composable
fun Preview() {
    EUNOIATheme {
        /*RoutineScreen(
            rememberNavController(),
            LocalContext.current,
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )*/
    }
}