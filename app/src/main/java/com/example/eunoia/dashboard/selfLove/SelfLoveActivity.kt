package com.example.eunoia.dashboard.selfLove

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
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.amplifyframework.datastore.generated.model.UserSelfLoveRelationship
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserSelfLoveRelationshipBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.bedtimeStory.getCurrentlyPlayingTime
import com.example.eunoia.dashboard.bedtimeStory.resetBedtimeStoryGlobalProperties
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.home.OptionItem
import com.example.eunoia.dashboard.home.SelfLoveForRoutine
import com.example.eunoia.dashboard.home.updatePreviousUserRoutineRelationship
import com.example.eunoia.dashboard.prayer.resetPrayerGlobalProperties
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.openRoutineIsCurrentlyPlayingDialogBox
import com.example.eunoia.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "Self Love Activity"
var selfLoveActivityUris = mutableListOf<MutableState<Uri>?>()
var selfLoveActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()
private const val START_SELF_LOVE = "start"
private const val PAUSE_SELF_LOVE = "pause"
private const val WAIT_FOR_SELF_LOVE = "wait"

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
    true,
    false,
    false,
    true,
    false,
    false,
    true,
    false,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelfLoveActivityUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val context = LocalContext.current

    SetUpRoutineCurrentlyPlayingAlertDialogSelfLove(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    )

    resetSelfLoveActivityPlayButtonTexts()
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedSelfLove by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserSelfLoveRelationshipBackend.queryApprovedUserSelfLoveRelationshipBasedOnUser(it) { userSelfLoveRelationships ->
            if(selfLoveActivityUris.size < userSelfLoveRelationships.size) {
                for (i in userSelfLoveRelationships.indices) {
                    selfLoveActivityUris.add(mutableStateOf("".toUri()))
                    selfLoveActivityPlayButtonTexts.add(mutableStateOf(START_SELF_LOVE))
                }
            }
            globalViewModel_!!.currentUsersSelfLoveRelationships = userSelfLoveRelationships.toMutableList()
            retrievedSelfLove = true
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
            favoriteSelfLoveTitle,
            emptySelfLoveList,
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
                    globalViewModel_!!.bottomSheetOpenFor = "controls"
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
                text = "Self Love",
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
                .constrainAs(favoriteSelfLoveTitle) {
                    top.linkTo(options.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Favourite Self Loves")
        }
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .constrainAs(emptySelfLoveList) {
                    top.linkTo(favoriteSelfLoveTitle.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            if(
                retrievedSelfLove &&
                globalViewModel_!!.currentUsersSelfLoveRelationships != null
            ){
                if(globalViewModel_!!.currentUsersSelfLoveRelationships!!.size > 0){
                    for(i in globalViewModel_!!.currentUsersSelfLoveRelationships!!.indices){
                        setSelfLoveActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersSelfLoves(
                            globalViewModel_!!.currentUsersSelfLoveRelationships!![i]!!.userSelfLoveRelationshipSelfLove,
                            i,
                            { index ->
                                selfLoveIndex = index
                                resetCurrentlyPlayingRoutineIfNecessary(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    context
                                )
                            },
                            { index ->
                                navigateToSelfLoveScreen(
                                    navController,
                                    globalViewModel_!!.currentUsersSelfLoveRelationships!![index]!!.userSelfLoveRelationshipSelfLove
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
                    top.linkTo(emptySelfLoveList.bottom, margin = 0.dp)
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

var selfLoveIndex = -1

@Composable
private fun SetUpRoutineCurrentlyPlayingAlertDialogSelfLove(
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
                        startSelfLoveConfirmed(
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

private fun startSelfLoveConfirmed(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
){
    resetGeneralMediaPlayerServiceIfNecessary(
        generalMediaPlayerService,
        selfLoveIndex
    )
    resetPlayButtonTextsIfNecessary(selfLoveIndex)
    playOrPauseMediaPlayerAccordingly(
        generalMediaPlayerService,
        soundMediaPlayerService,
        selfLoveIndex,
        context
    )
    openRoutineIsCurrentlyPlayingDialogBox = false
}

private fun resetCurrentlyPlayingRoutineIfNecessary(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
) {
    if(globalViewModel_!!.currentRoutinePlaying != null){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        startSelfLoveConfirmed(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }
}

private fun setSelfLoveActivityPlayButtonTextsCorrectly(i: Int) {
    if (globalViewModel_!!.currentSelfLovePlaying != null) {
        if (
            globalViewModel_!!.currentSelfLovePlaying!!.id ==
            globalViewModel_!!.currentUsersSelfLoveRelationships!![i]!!.userSelfLoveRelationshipSelfLove.id
        ) {
            if(globalViewModel_!!.isCurrentSelfLovePlaying){
                selfLoveActivityPlayButtonTexts[i]!!.value = PAUSE_SELF_LOVE
            }else{
                selfLoveActivityPlayButtonTexts[i]!!.value = START_SELF_LOVE
            }
        } else {
            selfLoveActivityPlayButtonTexts[i]!!.value = START_SELF_LOVE
        }
    } else {
        selfLoveActivityPlayButtonTexts[i]!!.value = START_SELF_LOVE
    }
}

private fun playOrPauseMediaPlayerAccordingly(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(selfLoveActivityPlayButtonTexts[index]!!.value == START_SELF_LOVE){
        selfLoveActivityPlayButtonTexts[index]!!.value = WAIT_FOR_SELF_LOVE
        if(selfLoveActivityUris[index]!!.value == "".toUri()) {
            retrieveSelfLoveAudio(generalMediaPlayerService, soundMediaPlayerService, index, context)
        }else{
            startSelfLove(generalMediaPlayerService, soundMediaPlayerService, index, context)
        }
    }else if(
        selfLoveActivityPlayButtonTexts[index]!!.value == PAUSE_SELF_LOVE ||
        selfLoveActivityPlayButtonTexts[index]!!.value == WAIT_FOR_SELF_LOVE
    ){
        pauseSelfLove(generalMediaPlayerService, index)
    }
}

private fun pauseSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        globalViewModel_!!.currentBedtimeStoryPlaying == null &&
        globalViewModel_!!.currentPrayerPlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            selfLoveActivityPlayButtonTexts[index]!!.value = START_SELF_LOVE
            globalViewModel_!!.selfLoveTimer.pause()
            globalViewModel_!!.generalPlaytimeTimer.pause()
            globalViewModel_!!.isCurrentSelfLovePlaying = false
            deActivateSelfLoveGlobalControlButton(2)
        }
    }
}

private fun startSelfLove(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(selfLoveActivityUris[index]!!.value != "".toUri()){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentBedtimeStoryPlaying == null &&
            globalViewModel_!!.currentPrayerPlaying == null
        ){
            generalMediaPlayerService.startMediaPlayer()
            afterPlayingSelfLove(index)
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

private fun afterPlayingSelfLove(index: Int){
    globalViewModel_!!.selfLoveTimer.start()
    globalViewModel_!!.generalPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingSelfLove(index)
}

private fun setGlobalPropertiesAfterPlayingSelfLove(index: Int){
    globalViewModel_!!.currentSelfLovePlaying = globalViewModel_!!.currentUsersSelfLoveRelationships!![index]!!.userSelfLoveRelationshipSelfLove
    globalViewModel_!!.currentSelfLovePlayingUri = selfLoveActivityUris[index]!!.value
    selfLoveActivityPlayButtonTexts[index]!!.value = PAUSE_SELF_LOVE
    globalViewModel_!!.isCurrentSelfLovePlaying = true
    deActivateSelfLoveGlobalControlButton(0)
    deActivateSelfLoveGlobalControlButton(2)
}

private fun retrieveSelfLoveAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    SoundBackend.retrieveAudio(
        globalViewModel_!!.currentUsersSelfLoveRelationships!![index]!!.userSelfLoveRelationshipSelfLove.audioKeyS3,
        globalViewModel_!!.currentUsersSelfLoveRelationships!![index]!!.userSelfLoveRelationshipSelfLove.selfLoveOwner.amplifyAuthUserId
    ) {
        selfLoveActivityUris[index]!!.value = it!!
        startSelfLove(
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
    if(globalViewModel_!!.currentSelfLovePlaying != null) {
        if (globalViewModel_!!.currentUsersSelfLoveRelationships!![index]!!.userSelfLoveRelationshipSelfLove.id != globalViewModel_!!.currentSelfLovePlaying!!.id) {
            generalMediaPlayerService.onDestroy()
        }
    }
}

private fun resetPlayButtonTextsIfNecessary(index: Int) {
    for(j in selfLoveActivityPlayButtonTexts.indices){
        if(j != index){
            if(selfLoveActivityPlayButtonTexts[j]!!.value != START_SELF_LOVE) {
                selfLoveActivityPlayButtonTexts[j]!!.value = START_SELF_LOVE
            }
        }
    }
}

private fun updatePreviousAndCurrentSelfLoveRelationship(
    index: Int,
    continuePlayingTime: Int,
    completed: () -> Unit
){
    updatePreviousUserSelfLoveRelationship(continuePlayingTime){
        SelfLoveForRoutine.updateRecentlyPlayedUserSelfLoveRelationshipWithUserSelfLoveRelationship(
            globalViewModel_!!.currentUsersSelfLoveRelationships!![index]!!
        ) {
            globalViewModel_!!.currentUsersSelfLoveRelationships!![index] = it
            updatePreviousUserPrayerRelationship {
                updatePreviousUserBedtimeStoryRelationship(continuePlayingTime) {
                    completed()
                }
            }
        }
    }
}

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)
    updatePreviousAndCurrentSelfLoveRelationship(
        index,
        continuePlayingTime
    ) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(selfLoveActivityUris[index]!!.value)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        globalViewModel_!!.selfLoveTimer.setMaxDuration(globalViewModel_!!.currentUsersSelfLoveRelationships!![index]!!.userSelfLoveRelationshipSelfLove.fullPlayTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptSelfLove()
    }
    afterPlayingSelfLove(index)
}

/**
 * Keep track of the date time a user starts listening to a self love
 */
fun updateCurrentUserSelfLoveRelationshipUsageTimeStamp(
    userSelfLoveRelationship: UserSelfLoveRelationship,
    completed: (updatedUserSelfLoveRelationship: UserSelfLoveRelationship) -> Unit
) {
    var usageTimeStamp = userSelfLoveRelationship.usageTimestamps
    val currentDateTime = DateUtils.formatISO8601Date(Date())

    if(usageTimeStamp != null) {
        usageTimeStamp.add(Temporal.DateTime(currentDateTime))
    }else{
        usageTimeStamp = listOf(Temporal.DateTime(currentDateTime))
    }

    val numberOfTimesPlayed = userSelfLoveRelationship.numberOfTimesPlayed + 1

    val newUserSelfLoveRelationship = userSelfLoveRelationship.copyOfBuilder()
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .usageTimestamps(usageTimeStamp)
        .build()

    UserSelfLoveRelationshipBackend.updateUserSelfLoveRelationship(newUserSelfLoveRelationship){
        completed(it)
    }
}

fun updatePreviousUserSelfLoveRelationship(
    continuePlayingTime: Int,
    completed: (updatedUserSelfLoveRelationship: UserSelfLoveRelationship?) -> Unit
) {
    if(globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship != null){
        Log.i(TAG, "Duration of SelfLove general timer is ${globalViewModel_!!.generalPlaytimeTimer.getDuration()}")
        val playTime = globalViewModel_!!.generalPlaytimeTimer.getDuration()
        globalViewModel_!!.generalPlaytimeTimer.stop()

        Log.i(TAG, "Total bts play time = ${globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship!!.totalPlayTime}")
        Log.i(TAG, "play time = $playTime")
        val totalPlayTime = globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship!!.totalPlayTime + playTime
        Log.i(TAG, "final total play time = $totalPlayTime")

        var usagePlayTimes = globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        val numberOfTimesPlayed = globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship!!.numberOfTimesPlayed
        Log.i(TAG, "number of times played = $numberOfTimesPlayed")

        if(totalPlayTime > 0){
            val userSelfLoveRelationship = globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship!!.copyOfBuilder()
                .numberOfTimesPlayed(numberOfTimesPlayed)
                .totalPlayTime(totalPlayTime.toInt())
                .usagePlayTimes(usagePlayTimes)
                .build()

            UserSelfLoveRelationshipBackend.updateUserSelfLoveRelationship(userSelfLoveRelationship){
                globalViewModel_!!.previouslyPlayedUserSelfLoveRelationship = null
                completed(it)
            }
        }else{
            completed(null)
        }
    }else{
        completed(null)
    }
}

fun resetOtherGeneralMediaPlayerUsersExceptSelfLove(){
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null){
        resetBedtimeStoryGlobalProperties()
    }
    if(globalViewModel_!!.currentPrayerPlaying != null){
        resetPrayerGlobalProperties()
    }
}

fun resetSelfLoveActivityPlayButtonTexts() {
    for(j in selfLoveActivityPlayButtonTexts.indices){
        selfLoveActivityPlayButtonTexts[j]!!.value = START_SELF_LOVE
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

fun navigateToSelfLoveScreen(navController: NavController, selfLoveData: SelfLoveData){
    navController.navigate("${Screen.SelfLoveScreen.screen_route}/selfLoveData=${SelfLoveObject.SelfLove.from(selfLoveData)}")
}

private fun something(){

}