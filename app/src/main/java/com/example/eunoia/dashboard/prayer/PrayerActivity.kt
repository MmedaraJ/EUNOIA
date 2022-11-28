package com.example.eunoia.dashboard.prayer

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
import com.amplifyframework.datastore.generated.model.PrayerData
import com.amplifyframework.datastore.generated.model.UserPrayerRelationship
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserPrayerRelationshipBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.bedtimeStory.resetBedtimeStoryGlobalProperties
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.home.OptionItem
import com.example.eunoia.dashboard.home.PrayerForRoutine
import com.example.eunoia.dashboard.home.updatePreviousUserRoutineRelationship
import com.example.eunoia.dashboard.selfLove.resetSelfLoveGlobalProperties
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.models.PrayerObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.prayer.deActivatePrayerGlobalControlButton
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "Prayer Activity"
var prayerActivityUris = mutableListOf<MutableState<Uri>?>()
var prayerActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()
private const val START_PRAYER = "start"
private const val PAUSE_PRAYER = "pause"
private const val WAIT_FOR_PRAYER = "wait"

var keepGoing by mutableStateOf(false)

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
fun PrayerActivityUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val context = LocalContext.current

    SetUpRoutineCurrentlyPlayingAlertDialogPrayerUI(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    )

    resetPrayerActivityPlayButtonTexts()
    globalViewModel!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedPrayer by rememberSaveable{ mutableStateOf(false) }
    globalViewModel!!.currentUser?.let {
        UserPrayerRelationshipBackend.queryApprovedUserPrayerRelationshipBasedOnUser(it) { userPrayerRelationships ->
            if(prayerActivityUris.size < userPrayerRelationships.size) {
                for (i in userPrayerRelationships.indices) {
                    prayerActivityUris.add(mutableStateOf("".toUri()))
                    prayerActivityPlayButtonTexts.add(mutableStateOf(START_PRAYER))
                }
            }
            prayerViewModel!!.currentUsersPrayerRelationships = userPrayerRelationships.toMutableList()
            retrievedPrayer = true
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
            favoritePrayerTitle,
            emptyPrayerList,
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
                text = "Prayer",
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
                .constrainAs(favoritePrayerTitle) {
                    top.linkTo(options.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Favourite Prayers")
        }
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .constrainAs(emptyPrayerList) {
                    top.linkTo(favoritePrayerTitle.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            if(
                retrievedPrayer &&
                prayerViewModel!!.currentUsersPrayerRelationships != null
            ){
                if(prayerViewModel!!.currentUsersPrayerRelationships!!.size > 0){
                    for(i in prayerViewModel!!.currentUsersPrayerRelationships!!.indices){
                        setPrayerActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersPrayers(
                            prayerViewModel!!.currentUsersPrayerRelationships!![i]!!.userPrayerRelationshipPrayer,
                            i,
                            generalMediaPlayerService,
                            soundMediaPlayerService,
                            { index ->
                                prayerIndex = index
                                resetCurrentlyPlayingRoutineIfNecessary(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    context
                                )
                            },
                            { index ->
                                navigateToPrayerScreen(
                                    navController,
                                    prayerViewModel!!.currentUsersPrayerRelationships!![index]!!.userPrayerRelationshipPrayer
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
                    top.linkTo(emptyPrayerList.bottom, margin = 0.dp)
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
            //TODO prayer specific articles
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

private var prayerIndex = -1

@Composable
private fun SetUpRoutineCurrentlyPlayingAlertDialogPrayerUI(
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
                        startPrayerConfirmed(
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

private fun startPrayerConfirmed(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
){
    resetGeneralMediaPlayerServiceIfNecessary(
        generalMediaPlayerService,
        prayerIndex
    )
    resetPlayButtonTextsIfNecessary(prayerIndex)
    playOrPauseMediaPlayerAccordingly(
        generalMediaPlayerService,
        soundMediaPlayerService,
        prayerIndex,
        context
    )
    openRoutineIsCurrentlyPlayingDialogBox = false
}

private fun resetCurrentlyPlayingRoutineIfNecessary(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
) {
    if(routineViewModel!!.currentRoutinePlaying != null){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        startPrayerConfirmed(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }
}

private fun setPrayerActivityPlayButtonTextsCorrectly(i: Int) {
    if (prayerViewModel!!.currentPrayerPlaying != null) {
        if (
            prayerViewModel!!.currentPrayerPlaying!!.id ==
            prayerViewModel!!.currentUsersPrayerRelationships!![i]!!.userPrayerRelationshipPrayer.id
        ) {
            if(prayerViewModel!!.isCurrentPrayerPlaying){
                prayerActivityPlayButtonTexts[i]!!.value = PAUSE_PRAYER
            }else{
                prayerActivityPlayButtonTexts[i]!!.value = START_PRAYER
            }
        } else {
            prayerActivityPlayButtonTexts[i]!!.value = START_PRAYER
        }
    } else {
        prayerActivityPlayButtonTexts[i]!!.value = START_PRAYER
    }
}

private fun playOrPauseMediaPlayerAccordingly(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(prayerActivityPlayButtonTexts[index]!!.value == START_PRAYER){
        prayerActivityPlayButtonTexts[index]!!.value = WAIT_FOR_PRAYER
        if(prayerActivityUris[index]!!.value == "".toUri()) {
            retrievePrayerAudio(generalMediaPlayerService, soundMediaPlayerService, index, context)
        }else{
            startPrayer(generalMediaPlayerService, soundMediaPlayerService, index, context)
        }
    }else if(
        prayerActivityPlayButtonTexts[index]!!.value == PAUSE_PRAYER ||
        prayerActivityPlayButtonTexts[index]!!.value == WAIT_FOR_PRAYER
    ){
        pausePrayer(generalMediaPlayerService, index)
    }
}

private fun pausePrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
        selfLoveViewModel!!.currentSelfLovePlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            prayerActivityPlayButtonTexts[index]!!.value = START_PRAYER
            prayerViewModel!!.prayerTimer.pause()
            globalViewModel!!.generalPlaytimeTimer.pause()
            prayerViewModel!!.isCurrentPrayerPlaying = false
            deActivatePrayerGlobalControlButton(2)
        }
    }
}

private fun startPrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(prayerActivityUris[index]!!.value != "".toUri()){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying == null &&
            selfLoveViewModel!!.currentSelfLovePlaying == null
        ){
            generalMediaPlayerService.startMediaPlayer()
            afterPlayingPrayer(index)
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

private fun afterPlayingPrayer(index: Int){
    prayerViewModel!!.prayerTimer.start()
    globalViewModel!!.generalPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingPrayer(index)
}

private fun setGlobalPropertiesAfterPlayingPrayer(index: Int){
    prayerViewModel!!.currentPrayerPlaying = prayerViewModel!!.currentUsersPrayerRelationships!![index]!!.userPrayerRelationshipPrayer
    Log.i(TAG, "Prayer is ff ${prayerViewModel!!.currentUsersPrayerRelationships!![index]!!.userPrayerRelationshipPrayer.displayName}")
    prayerViewModel!!.currentPrayerPlayingUri = prayerActivityUris[index]!!.value
    prayerActivityPlayButtonTexts[index]!!.value = PAUSE_PRAYER
    prayerViewModel!!.isCurrentPrayerPlaying = true
    deActivatePrayerGlobalControlButton(0)
    deActivatePrayerGlobalControlButton(2)
}

private fun retrievePrayerAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    SoundBackend.retrieveAudio(
        prayerViewModel!!.currentUsersPrayerRelationships!![index]!!.userPrayerRelationshipPrayer.audioKeyS3,
        prayerViewModel!!.currentUsersPrayerRelationships!![index]!!.userPrayerRelationshipPrayer.prayerOwner.amplifyAuthUserId
    ) {
        prayerActivityUris[index]!!.value = it!!
        startPrayer(
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
    if(prayerViewModel!!.currentPrayerPlaying != null) {
        if (prayerViewModel!!.currentUsersPrayerRelationships!![index]!!.userPrayerRelationshipPrayer.id != prayerViewModel!!.currentPrayerPlaying!!.id) {
            generalMediaPlayerService.onDestroy()
        }
    }
}

private fun resetPlayButtonTextsIfNecessary(index: Int) {
    for(j in prayerActivityPlayButtonTexts.indices){
        if(j != index){
            if(prayerActivityPlayButtonTexts[j]!!.value != START_PRAYER) {
                prayerActivityPlayButtonTexts[j]!!.value = START_PRAYER
            }
        }
    }
}

private fun updatePreviousAndCurrentPrayerRelationship(
    index: Int,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
){
    updatePreviousUserPrayerRelationship{
        PrayerForRoutine.updateRecentlyPlayedUserPrayerRelationshipWithUserPrayerRelationship(
            prayerViewModel!!.currentUsersPrayerRelationships!![index]!!
        ) {
            prayerViewModel!!.currentUsersPrayerRelationships!![index] = it
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
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    updatePreviousAndCurrentPrayerRelationship(
        index,
        generalMediaPlayerService
    ) {
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(prayerActivityUris[index]!!.value)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        prayerViewModel!!.prayerTimer.setMaxDuration(prayerViewModel!!.currentUsersPrayerRelationships!![index]!!.userPrayerRelationshipPrayer.fullPlayTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptPrayer()
    }
    afterPlayingPrayer(index)
}

/**
 * Keep track of the date time a user starts listening to a prayer
 */
fun updateCurrentUserPrayerRelationshipUsageTimeStamp(
    userPrayerRelationship: UserPrayerRelationship,
    completed: (updatedUserPrayerRelationship: UserPrayerRelationship) -> Unit
) {
    var usageTimeStamp = userPrayerRelationship.usageTimestamps
    val currentDateTime = DateUtils.formatISO8601Date(Date())

    if(usageTimeStamp != null) {
        usageTimeStamp.add(Temporal.DateTime(currentDateTime))
    }else{
        usageTimeStamp = listOf(Temporal.DateTime(currentDateTime))
    }

    val numberOfTimesPlayed = userPrayerRelationship.numberOfTimesPlayed + 1

    val newUserPrayerRelationship = userPrayerRelationship.copyOfBuilder()
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .usageTimestamps(usageTimeStamp)
        .build()

    UserPrayerRelationshipBackend.updateUserPrayerRelationship(newUserPrayerRelationship){
        completed(it)
    }
}

fun updatePreviousUserPrayerRelationship(
    completed: (updatedUserPrayerRelationship: UserPrayerRelationship?) -> Unit
) {
    if(prayerViewModel!!.previouslyPlayedUserPrayerRelationship != null){
        Log.i(TAG, "Duration of prayer general timer is ${globalViewModel!!.generalPlaytimeTimer.getDuration()}")
        val playTime = globalViewModel!!.generalPlaytimeTimer.getDuration()
        globalViewModel!!.generalPlaytimeTimer.stop()

        Log.i(TAG, "Total bts play time = ${prayerViewModel!!.previouslyPlayedUserPrayerRelationship!!.totalPlayTime}")
        Log.i(TAG, "play time = $playTime")
        val totalPlayTime = prayerViewModel!!.previouslyPlayedUserPrayerRelationship!!.totalPlayTime + playTime
        Log.i(TAG, "final total play time = $totalPlayTime")

        var usagePlayTimes = prayerViewModel!!.previouslyPlayedUserPrayerRelationship!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        if(totalPlayTime > 0){
            val userPrayerRelationship = prayerViewModel!!.previouslyPlayedUserPrayerRelationship!!.copyOfBuilder()
                .totalPlayTime(totalPlayTime.toInt())
                .usagePlayTimes(usagePlayTimes)
                .build()

            UserPrayerRelationshipBackend.updateUserPrayerRelationship(userPrayerRelationship){
                prayerViewModel!!.previouslyPlayedUserPrayerRelationship = null
                completed(it)
            }
        }else{
            completed(null)
        }
    }else{
        completed(null)
    }
}

fun resetOtherGeneralMediaPlayerUsersExceptPrayer(){
    if(bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null){
        resetBedtimeStoryGlobalProperties()
    }
    if(selfLoveViewModel!!.currentSelfLovePlaying != null){
        resetSelfLoveGlobalProperties()
    }
}

fun resetPrayerActivityPlayButtonTexts() {
    for(j in prayerActivityPlayButtonTexts.indices){
        prayerActivityPlayButtonTexts[j]!!.value = START_PRAYER
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

fun navigateToPrayerScreen(navController: NavController, prayerData: PrayerData){
    navController.navigate("${Screen.PrayerScreen.screen_route}/prayerData=${PrayerObject.Prayer.from(prayerData)}")
}

private fun something(){

}