package com.example.eunoia.dashboard.bedtimeStory

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amazonaws.util.DateUtils
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfoRelationship
import com.chaquo.python.Python
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserBedtimeStoryInfoRelationshipBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.*
import com.example.eunoia.dashboard.home.BedtimeStoryForRoutine.updateRecentlyPlayedUserBedtimeStoryRelationshipWithUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.prayer.resetPrayerGlobalProperties
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.selfLove.resetSelfLoveGlobalProperties
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.bedtimeStory.deActivateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.utils.timerFormatMS
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "Bedtime Story Activity"
var bedtimeStoryActivityUris = mutableListOf<MutableState<Uri>?>()
var bedtimeStoryActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()

private const val START_BEDTIME_STORY = "start"
private const val PAUSE_BEDTIME_STORY = "pause"
private const val WAIT_FOR_BEDTIME_STORY = "wait"

private var bedtimeStoryIndex = -1

private val allElements = listOf(
    "pouring\nrain",
    "coffee\nhouse",
    "library",
    "baking",
    "beach\nwaves",
    "next door",
    "keyboard",
    "train\ntrack",
)

private val allIcons = listOf(
    R.drawable.pouring_rain_icon,
    R.drawable.coffee_house_icon,
    R.drawable.library_icon,
    R.drawable.baking_icon,
    R.drawable.beach_waves_icon,
    R.drawable.next_door_icon,
    R.drawable.keyboard_icon,
    R.drawable.train_track_icon,
)

private val allPros = listOf(
    false,
    false,
    true,
    false,
    false,
    true,
    true,
    false,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BedtimeStoryActivityUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val context = LocalContext.current

    SetUpRoutineCurrentlyPlayingAlertDialog(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    )

    resetBedtimeStoryActivityPlayButtonTexts()
    globalViewModel!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedBedtimeStories by rememberSaveable{ mutableStateOf(false) }
    globalViewModel!!.currentUser?.let {
        UserBedtimeStoryInfoRelationshipBackend.queryApprovedUserBedtimeStoryInfoRelationshipBasedOnUser(it) { userBedtimeStoryRelationship ->
            if(bedtimeStoryActivityUris.size < userBedtimeStoryRelationship.size) {
                for (i in userBedtimeStoryRelationship.indices) {
                    bedtimeStoryActivityUris.add(mutableStateOf("".toUri()))
                    bedtimeStoryActivityPlayButtonTexts.add(mutableStateOf(START_BEDTIME_STORY))
                }
            }
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships = userBedtimeStoryRelationship.toMutableList()
            retrievedBedtimeStories = true
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            introTitle,
            options,
            favoriteBedtimeStoryTitle,
            emptyBedtimeStoryList,
            articlesTitle,
            articles,
            endSpace
        ) = createRefs()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ){
            BackArrowHeader(
                {navController.popBackStack()},
                {
                    globalViewModel!!.bottomSheetOpenFor = "controls"
                    openBottomSheet(scope, state)
                },
                {
                    //navController.navigate(Screen.Settings.screen_route)
                }
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(introTitle){
                    top.linkTo(header.bottom, margin = 20.dp)
                }
        ){
            NormalText(
                text = "Bedtime Story",
                color = Color.Black,
                13,
                xOffset = 6,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier.constrainAs(options){
                top.linkTo(introTitle.bottom, margin = 8.dp)
            }
        ) {
            OptionItem(
                allElements,
                allIcons,
                allPros
            ) {
                when (it) {
                    "pouring\nrain" -> {
                        val py = Python.getInstance()
                        val pyObj = py.getModule("myscript")
                        val obj = pyObj.callAttr("main", "GOLIATH")
                        Log.i(TAG, "Dangas python = $obj")
                    }
                    "coffee\nhouse" -> {
                    }
                    "library" -> {
                    }
                    "baking" -> {
                    }
                    "beach\nwaves" -> {
                    }
                    "next door" -> {
                    }
                    "keyboard" -> {
                    }
                    "train\ntrack" -> {
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(favoriteBedtimeStoryTitle) {
                    top.linkTo(options.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Favourite Bedtime Stories")
        }
        Column(
            modifier = Modifier
                .constrainAs(emptyBedtimeStoryList) {
                    top.linkTo(favoriteBedtimeStoryTitle.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            if(
                retrievedBedtimeStories &&
                bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships != null
            ){
                if(bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!!.size > 0){
                    for(i in bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!!.indices){
                        setBedtimeStoryActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersBedtimeStories(
                            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![i]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo,
                            i,
                            { index ->
                                bedtimeStoryIndex = index
                                resetCurrentlyPlayingRoutineIfNecessary(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    context
                                )
                            },
                            { index ->
                                navigateToBedtimeStoryScreen(
                                    navController,
                                    bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo
                                )
                            }
                        )
                    }
                }else{
                    SurpriseMeSound{}
                }
            }else{
                SurpriseMeSound{}
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(articlesTitle) {
                    top.linkTo(emptyBedtimeStoryList.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Did You Know")
        }
        Column(
            modifier = Modifier
                .constrainAs(articles) {
                    top.linkTo(articlesTitle.bottom, margin = 18.dp)
                }
                .padding(bottom = 24.dp)
        ){
            //TODO Bedtime story specific articles
            ArticlesList()
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(articles.bottom, margin = 20.dp)
                }
        ){
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SetUpRoutineCurrentlyPlayingAlertDialog(
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
                        startBedtimeStoryConfirmed(
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

private fun startBedtimeStoryConfirmed(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
){
    globalViewModel!!.continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)
    resetGeneralMediaPlayerServiceIfNecessary(
        generalMediaPlayerService,
        bedtimeStoryIndex
    )
    resetPlayButtonTextsIfNecessary(bedtimeStoryIndex)

    setCurrentlyPlayedUserBedtimeStoryRelationshipToUpdatedValue(bedtimeStoryIndex){
        playOrPauseBedtimeStoryAccordingly(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryIndex,
            context
        )

        openRoutineIsCurrentlyPlayingDialogBox = false
    }
}

fun setCurrentlyPlayedUserBedtimeStoryRelationshipToUpdatedValue(
    index: Int,
    completed: () -> Unit
) {
    UserBedtimeStoryInfoRelationshipBackend.queryApprovedUserBedtimeStoryInfoRelationshipBasedOnId(
        bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.id
    ){
        if(it.isNotEmpty()) {
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index] = it[0]
            completed()
        }
    }
}

private fun resetCurrentlyPlayingRoutineIfNecessary(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
) {
    if(routineViewModel!!.currentRoutinePlaying != null){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        startBedtimeStoryConfirmed(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }
}

private fun setBedtimeStoryActivityPlayButtonTextsCorrectly(i: Int) {
    if (bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        if (
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id ==
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![i]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.id
        ) {
            if(bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying){
                bedtimeStoryActivityPlayButtonTexts[i]!!.value = PAUSE_BEDTIME_STORY
            }else{
                bedtimeStoryActivityPlayButtonTexts[i]!!.value = START_BEDTIME_STORY
            }
        } else {
            bedtimeStoryActivityPlayButtonTexts[i]!!.value = START_BEDTIME_STORY
        }
    } else {
        bedtimeStoryActivityPlayButtonTexts[i]!!.value = START_BEDTIME_STORY
    }
}

private fun playOrPauseBedtimeStoryAccordingly(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(bedtimeStoryActivityPlayButtonTexts[index]!!.value == START_BEDTIME_STORY){
        bedtimeStoryActivityPlayButtonTexts[index]!!.value = WAIT_FOR_BEDTIME_STORY
        if(bedtimeStoryActivityUris[index]!!.value == "".toUri()) {
            retrieveUploadedBedtimeStoryAudio(generalMediaPlayerService, soundMediaPlayerService, index, context)
        }else{
            startBedtimeStory(generalMediaPlayerService, soundMediaPlayerService, index, context)
        }
    }else if(
        bedtimeStoryActivityPlayButtonTexts[index]!!.value == PAUSE_BEDTIME_STORY ||
        bedtimeStoryActivityPlayButtonTexts[index]!!.value == WAIT_FOR_BEDTIME_STORY
    ){
        pauseBedtimeStory(generalMediaPlayerService, index)
    }
}

private fun pauseBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        selfLoveViewModel!!.currentSelfLovePlaying == null &&
        prayerViewModel!!.currentPrayerPlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
            bedtimeStoryViewModel!!.bedtimeStoryTimer.pause()
            globalViewModel!!.generalPlaytimeTimer.pause()
            globalViewModel!!.resetCDT()
            bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
            deActivateBedtimeStoryGlobalControlButton(2)
        }
    }
}

private fun startBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(bedtimeStoryActivityUris[index]!!.value != "".toUri()){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            selfLoveViewModel!!.currentSelfLovePlaying == null &&
            prayerViewModel!!.currentPrayerPlaying == null
        ){
            globalViewModel!!.remainingPlayTime =
                bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.fullPlayTime -
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition
            Log.i(TAG, "bedtimeStoryViewModel_!!.remainingPlayTime = ${globalViewModel!!.remainingPlayTime}")

            generalMediaPlayerService.startMediaPlayer()

            afterPlayingBedtimeStory(
                index,
                generalMediaPlayerService
            )
        }else{
            initializeMediaPlayer(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }
}

private fun afterPlayingBedtimeStory(
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    bedtimeStoryViewModel!!.bedtimeStoryTimer.start()
    globalViewModel!!.generalPlaytimeTimer.start()
    startCDT(
        index,
        generalMediaPlayerService
    )
    setGlobalPropertiesAfterPlayingBedtimeStory(index)
}

private fun startCDT(
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    globalViewModel!!.startTheCDT(
        globalViewModel!!.remainingPlayTime.toLong(),
        generalMediaPlayerService
    ){
        resetBedtimeStoryGlobally(
            generalMediaPlayerService,
            index
        )
        deActivateResetButton()
    }
}

fun resetBedtimeStoryGlobally(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        if (
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id ==
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.id
        ) {
            if (
                generalMediaPlayerService.isMediaPlayerInitialized() &&
                selfLoveViewModel!!.currentSelfLovePlaying == null &&
                prayerViewModel!!.currentPrayerPlaying == null
            ) {
                resetBothLocalAndGlobalControlButtonsAfterReset()
                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderClicked = false
                bedtimeStoryViewModel!!.bedtimeStoryCircularSliderAngle = 0f
                bedtimeStoryViewModel!!.bedtimeStoryTimer.stop()
                bedtimeStoryViewModel!!.bedtimeStoryTimeDisplay = timerFormatMS(
                    bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!
                        .userBedtimeStoryInfoRelationshipBedtimeStoryInfo.fullPlayTime.toLong()
                )
                bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = false
                bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
                generalMediaPlayerService.onDestroy()
            }
        }
    }
}

private fun setGlobalPropertiesAfterPlayingBedtimeStory(index: Int){
    bedtimeStoryViewModel!!.currentBedtimeStoryPlaying = bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo
    bedtimeStoryViewModel!!.currentBedtimeStoryPlayingUri = bedtimeStoryActivityUris[index]!!.value
    bedtimeStoryActivityPlayButtonTexts[index]!!.value = PAUSE_BEDTIME_STORY
    bedtimeStoryViewModel!!.isCurrentBedtimeStoryPlaying = true
    deActivateBedtimeStoryGlobalControlButton(0)
    deActivateBedtimeStoryGlobalControlButton(2)
}

private fun retrieveUploadedBedtimeStoryAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    SoundBackend.retrieveAudio(
        bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.audioKeyS3,
        bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.bedtimeStoryOwner.amplifyAuthUserId
    ) {
        bedtimeStoryActivityUris[index]!!.value = it!!
        startBedtimeStory(
            generalMediaPlayerService,
            soundMediaPlayerService,
            index,
            context
        )
    }
}

private fun resetGeneralMediaPlayerServiceIfNecessary(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null) {
        if (bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!
                .userBedtimeStoryInfoRelationshipBedtimeStoryInfo.id !=
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.id) {
            generalMediaPlayerService.onDestroy()
        }
    }
}

private fun resetPlayButtonTextsIfNecessary(index: Int) {
    for(j in bedtimeStoryActivityPlayButtonTexts.indices){
        if(j != index){
            if(bedtimeStoryActivityPlayButtonTexts[j]!!.value != START_BEDTIME_STORY) {
                bedtimeStoryActivityPlayButtonTexts[j]!!.value = START_BEDTIME_STORY
            }
        }
    }
}

private fun updatePreviousAndCurrentBedtimeStoryRelationship(
    index: Int,
    continuePlayingTime: Int,
    completed: () -> Unit
){
    updatePreviousUserBedtimeStoryRelationship(continuePlayingTime){
        updateRecentlyPlayedUserBedtimeStoryRelationshipWithUserBedtimeStoryRelationship(
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!
        ) {
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index] = it
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship(continuePlayingTime) {
                    completed()
                }
            }
        }
    }
}

fun getCurrentlyPlayingTime(
    generalMediaPlayerService: GeneralMediaPlayerService
): Int{
    var result = 0
    if(generalMediaPlayerService.isMediaPlayerInitialized()){
        if(
            generalMediaPlayerService.getMediaPlayer()!!.currentPosition !=
            generalMediaPlayerService.getMediaPlayer()!!.duration
        ) {
            result = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
            Log.i(TAG, "Resultz = $result")
        }
    }
    return result
}

private fun getSeekToPos(index: Int): Int{
    var seekToPos = 0
    if(bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.continuePlayingTime != null){
        seekToPos = bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!.continuePlayingTime
    }
    return seekToPos
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    Log.i(TAG, "bedtimeStoryViewModel_!!.continuePlayingTime = ${globalViewModel!!.continuePlayingTime}")
    updatePreviousAndCurrentBedtimeStoryRelationship(
        index,
        globalViewModel!!.continuePlayingTime
    ) {
        globalViewModel!!.resetCDT()

        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(bedtimeStoryActivityUris[index]!!.value)
        val seekToPos = getSeekToPos(index)
        Log.i(TAG, "Bts Activity seekToPos = $seekToPos")
        generalMediaPlayerService.setSeekToPos(seekToPos)

        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)

        bedtimeStoryViewModel!!.bedtimeStoryTimer.setDuration(
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!
                .continuePlayingTime.toLong()
        )
        bedtimeStoryViewModel!!.bedtimeStoryTimer.setMaxDuration(
            bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!
                .userBedtimeStoryInfoRelationshipBedtimeStoryInfo.fullPlayTime.toLong()
        )
        globalViewModel!!.remainingPlayTime = bedtimeStoryViewModel!!.currentUsersBedtimeStoryRelationships!![index]!!
            .userBedtimeStoryInfoRelationshipBedtimeStoryInfo.fullPlayTime

        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()
        afterPlayingBedtimeStory(
            index,
            generalMediaPlayerService
        )
    }
}

/**
 * Keep track of the date time a user starts listening to a bedtime story
 */
fun updateCurrentUserBedtimeStoryInfoRelationshipUsageTimeStamp(
    userBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship,
    completed: (updatedUserBedtimeStoryInfoRelationship: UserBedtimeStoryInfoRelationship) -> Unit
) {
    var usageTimeStamp = userBedtimeStoryInfoRelationship.usageTimestamps
    val currentDateTime = DateUtils.formatISO8601Date(Date())

    if(usageTimeStamp != null) {
        usageTimeStamp.add(Temporal.DateTime(currentDateTime))
    }else{
        usageTimeStamp = listOf(Temporal.DateTime(currentDateTime))
    }

    val numberOfTimesPlayed = userBedtimeStoryInfoRelationship.numberOfTimesPlayed + 1

    val newUserBedtimeStoryInfoRelationship = userBedtimeStoryInfoRelationship.copyOfBuilder()
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .usageTimestamps(usageTimeStamp)
        .build()

    UserBedtimeStoryInfoRelationshipBackend.updateUserBedtimeStoryInfoRelationship(newUserBedtimeStoryInfoRelationship){
        bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship = it
        completed(it)
    }
}

fun updatePreviousUserBedtimeStoryRelationship(
    continuePlayingTime: Int,
    completed: (updatedUserBedtimeStoryRelationship: UserBedtimeStoryInfoRelationship?) -> Unit
) {
    if(bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship != null){
        Log.i(TAG, "Duration of bedtime story general timer is ${globalViewModel!!.generalPlaytimeTimer.getDuration()}")
        val playTime = globalViewModel!!.generalPlaytimeTimer.getDuration()
        globalViewModel!!.generalPlaytimeTimer.stop()

        Log.i(TAG, "Total bts play time = ${bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship!!.totalPlayTime}")
        Log.i(TAG, "play time = $playTime")
        val totalPlayTime = bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship!!.totalPlayTime + playTime
        Log.i(TAG, "final total play time = $totalPlayTime")

        var usagePlayTimes = bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        val numberOfTimesPlayed = bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship!!.numberOfTimesPlayed
        Log.i(TAG, "number of times played = $numberOfTimesPlayed")

        if(totalPlayTime > 0){
            val userBedtimeStoryInfoRelationship = bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship!!.copyOfBuilder()
                .numberOfTimesPlayed(numberOfTimesPlayed)
                .totalPlayTime(totalPlayTime.toInt())
                .continuePlayingTime(continuePlayingTime)
                .usagePlayTimes(usagePlayTimes)
                .currentlyListening(false)
                .build()

            UserBedtimeStoryInfoRelationshipBackend.updateUserBedtimeStoryInfoRelationship(userBedtimeStoryInfoRelationship){
                bedtimeStoryViewModel!!.previouslyPlayedUserBedtimeStoryRelationship = null
                completed(it)
            }
        }else{
            completed(null)
        }
    }else{
        completed(null)
    }
}

fun resetOtherGeneralMediaPlayerUsersExceptBedtimeStory(){
    if(selfLoveViewModel!!.currentSelfLovePlaying != null){
        resetSelfLoveGlobalProperties()
    }
    if(prayerViewModel!!.currentPrayerPlaying != null){
        resetPrayerGlobalProperties()
    }
}

fun resetBedtimeStoryActivityPlayButtonTexts() {
    for(j in bedtimeStoryActivityPlayButtonTexts.indices){
        bedtimeStoryActivityPlayButtonTexts[j]!!.value = START_BEDTIME_STORY
    }
}

@Composable
private fun UserSoundList(){
    Column(
    ) {
        EmptyRoutine {
            something()
        }
        SurpriseMeRoutine {
            something()
        }
    }
}

@Composable
private fun ArticlesList(){
    Column(
    ) {
        Article(
            title = "the danger of sleeping pills",
            summary = "Sleeping pills are not meant to be taken daily.",
            icon = R.drawable.danger_of_sleeping_pills_icon
        ) {
            something()
        }
        Article(
            title = "benefits of a goodnight sleep",
            summary = "Your skincare routine ends with a goodnight sleep.",
            icon = R.drawable.benefits_of_goodnight_sleep_icon
        ) {
            something()
        }
        Article(
            title = "how to be extra creative & productive?",
            summary = "Your day starts right after a goodnight sleep.",
            icon = R.drawable.extra_creative_and_productive_icon
        ) {
            something()
        }
    }
}

fun navigateToBedtimeStoryScreen(navController: NavController, bedtimeStoryData: BedtimeStoryInfoData){
    navController.navigate("${Screen.BedtimeStoryScreen.screen_route}/bedtimeStoryData=${BedtimeStoryObject.BedtimeStory.from(bedtimeStoryData)}")
}

private fun something(){

}