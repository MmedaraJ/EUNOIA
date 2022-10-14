package com.example.eunoia.dashboard.sound

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amazonaws.util.DateUtils
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserSoundRelationshipBackend
import com.example.eunoia.dashboard.home.*
import com.example.eunoia.dashboard.home.SoundForRoutine.updateRecentlyPlayedUserSoundRelationshipWithUserSoundRelationship
import com.example.eunoia.models.SoundObject
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.sound.resetGlobalControlButtons
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "Sound Activity"
var soundActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()
var soundActivityUris = mutableListOf<MutableList<Uri?>>()
var soundActivityUriVolumes = mutableListOf<MutableList<Int>>()
var soundActivityPresets = mutableListOf<MutableState<SoundPresetData?>?>()
private const val START_SOUND = "start"
private const val PAUSE_SOUND = "pause"
private const val WAIT_FOR_SOUND = "wait"

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
fun SoundActivityUI(
    navController: NavController,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    resetSoundActivityPlayButtonTexts()
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()

    var retrievedSounds by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserSoundRelationshipBackend.queryApprovedUserSoundRelationshipBasedOnUser(it) { userSoundRelationship ->
            if(soundActivityUris.size < userSoundRelationship.size) {
                for(i in userSoundRelationship.indices){
                    soundActivityPlayButtonTexts.add(mutableStateOf(START_SOUND))
                    soundActivityUriVolumes.add(mutableListOf())
                    soundActivityUris.add(mutableListOf())
                    soundActivityPresets.add(mutableStateOf(null))
                }
            }
            globalViewModel_!!.currentUsersSoundRelationships = userSoundRelationship.toMutableList()
            retrievedSounds = true
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
            favoriteSoundsTitle,
            emptySoundList,
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
                {
                    navController.popBackStack()
                },
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
                text = "Sound",
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
                .constrainAs(favoriteSoundsTitle) {
                    top.linkTo(options.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Favourite Sounds")
        }
        Column(
            modifier = Modifier
                .constrainAs(emptySoundList) {
                    top.linkTo(favoriteSoundsTitle.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            if(
                retrievedSounds &&
                globalViewModel_!!.currentUsersSoundRelationships != null
            ) {
                if(globalViewModel_!!.currentUsersSoundRelationships!!.size > 0){
                    for(i in globalViewModel_!!.currentUsersSoundRelationships!!.indices){
                        setSoundActivityPlayButtonTextsCorrectly(i)
                        SoundCard(
                            globalViewModel_!!.currentUsersSoundRelationships!![i]!!.userSoundRelationshipSound,
                            i,
                            { index ->
                                resetSoundMediaPlayerServiceIfNecessary(soundMediaPlayerService, index)
                                resetPlayButtonTextsIfNecessary(index)
                                playOrPauseMediaPlayerAccordingly(
                                    soundMediaPlayerService,
                                    index,
                                    context
                                )
                            },
                            { index ->
                                if(soundActivityPlayButtonTexts[index]!!.value != WAIT_FOR_SOUND) {
                                    soundScreenBorderControlColors[7].value = Bizarre

                                    Log.d(TAG, "Sound is 123 ${globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound}")
                                    navigateToSoundScreen(
                                        navController,
                                        globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound
                                    )
                                }
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
                    top.linkTo(emptySoundList.bottom, margin = 0.dp)
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

private fun setSoundActivityPlayButtonTextsCorrectly(i: Int) {
    if (globalViewModel_!!.currentSoundPlaying != null) {
        if (
            globalViewModel_!!.currentSoundPlaying!!.id ==
            globalViewModel_!!.currentUsersSoundRelationships!![i]!!.userSoundRelationshipSound.id
        ) {
            if(globalViewModel_!!.isCurrentSoundPlaying){
                soundActivityPlayButtonTexts[i]!!.value = PAUSE_SOUND
            }else{
                soundActivityPlayButtonTexts[i]!!.value = START_SOUND
            }
        } else {
            soundActivityPlayButtonTexts[i]!!.value = START_SOUND
        }
    } else {
        soundActivityPlayButtonTexts[i]!!.value = START_SOUND
    }
}

private fun resetPlayButtonTextsIfNecessary(index: Int) {
    for(j in soundActivityPlayButtonTexts.indices){
        if(j != index){
            if(soundActivityPlayButtonTexts[j]!!.value != START_SOUND) {
                soundActivityPlayButtonTexts[j]!!.value = START_SOUND
            }
        }
    }
}

fun resetSoundActivityPlayButtonTexts() {
    for(j in soundActivityPlayButtonTexts.indices){
        soundActivityPlayButtonTexts[j]!!.value = START_SOUND
    }
}

private fun resetSoundMediaPlayerServiceIfNecessary(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int
) {
    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound.id != globalViewModel_!!.currentSoundPlaying!!.id) {
            soundMediaPlayerService.onDestroy()
        }
    }
}

private fun playOrPauseMediaPlayerAccordingly(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(soundActivityPlayButtonTexts[index]!!.value == START_SOUND){
        soundActivityPlayButtonTexts[index]!!.value = WAIT_FOR_SOUND
        if(soundActivityUris[index].size < 1) {
            getNecessaryPresets(index) {
                retrieveSoundAudio(
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            startSound(
                soundMediaPlayerService,
                index,
                context
            )
        }
    }else if(
        soundActivityPlayButtonTexts[index]!!.value == PAUSE_SOUND ||
        soundActivityPlayButtonTexts[index]!!.value == WAIT_FOR_SOUND
    ){
        pauseSound(soundMediaPlayerService, index)
    }
}

private fun retrieveSoundAudio(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    soundActivityUris[index].clear()
    SoundBackend.listS3Sounds(
        globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound.audioKeyS3,
        globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound.soundOwner.amplifyAuthUserId
    ){ s3List ->
        s3List.items.forEachIndexed { i, item ->
            SoundBackend.retrieveAudio(
                item.key,
                globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound.soundOwner.amplifyAuthUserId
            ) { uri ->
                soundActivityUris[index].add(uri)
                if(soundActivityUris[index].size == s3List.items.size){
                    startSound(
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }
            }
        }
    }
}

private fun startSound(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(soundActivityUris[index].isNotEmpty()){
        if(soundMediaPlayerService.areMediaPlayersInitialized()){
            soundMediaPlayerService.startMediaPlayers()
            afterPlayingSound(
                index,
                context
            )
        }else{
            initializeMediaPlayers(
                soundMediaPlayerService,
                index,
                context
            )
        }
    }
}

private fun afterPlayingSound(
    index: Int,
    context: Context
){
    globalViewModel_!!.soundPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingSound(index, context)
}

private fun getNecessaryPresets(index: Int, completed: () -> Unit){
    getUserSoundPresets(
        globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound,
        globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound.soundOwner,
    ) { presetData ->
        if(presetData.isNotEmpty()) {
            soundActivityPresets[index]!!.value = presetData[0]
            soundActivityUriVolumes[index] = presetData[0].volumes
            completed()
        }
    }
}

private fun setGlobalPropertiesAfterPlayingSound(index: Int, context: Context) {
    globalViewModel_!!.currentSoundPlaying = globalViewModel_!!.currentUsersSoundRelationships!![index]!!.userSoundRelationshipSound
    Log.i(TAG, "globalViewModel_!!.currentSoundPlaying is ${globalViewModel_!!.currentSoundPlaying}")
    globalViewModel_!!.currentSoundPlayingPreset = soundActivityPresets[index]!!.value
    globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
    globalViewModel_!!.soundSliderVolumes = globalViewModel_!!.currentSoundPlayingPreset!!.volumes
    for (volume in globalViewModel_!!.currentSoundPlayingPreset!!.volumes) {
        globalViewModel_!!.currentSoundPlayingSliderPositions.add(
            mutableStateOf(volume.toFloat())
        )
    }
    globalViewModel_!!.currentSoundPlayingUris = soundActivityUris[index]
    globalViewModel_!!.currentSoundPlayingContext = context
    soundActivityPlayButtonTexts[index]!!.value = PAUSE_SOUND
    globalViewModel_!!.isCurrentSoundPlaying = true
    com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(3)
    com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(1)
    com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(0)
}

private fun updatePreviousAndCurrentSoundRelationship(
    index: Int,
    completed: () -> Unit
){
    updatePreviousUserSoundRelationship {
        updateRecentlyPlayedUserSoundRelationshipWithUserSoundRelationship(
            globalViewModel_!!.currentUsersSoundRelationships!![index]!!
        ){
            globalViewModel_!!.currentUsersSoundRelationships!![index] = it
            completed()
        }
    }
}

private fun initializeMediaPlayers(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    updatePreviousAndCurrentSoundRelationship(index) {
        resetGlobalControlButtons()
        soundMediaPlayerService.onDestroy()
        soundMediaPlayerService.setAudioUris(soundActivityUris[index])
        soundMediaPlayerService.setVolumes(soundActivityUriVolumes[index])
        resetAll(context, soundMediaPlayerService)
        createMeditationBellMediaPlayer(context)
        val intent = Intent()
        intent.action = "PLAY"
        soundMediaPlayerService.onStartCommand(intent, 0, 0)
        soundMediaPlayerService.loopMediaPlayers()

        afterPlayingSound(
            index,
            context
        )
    }
}

/**
 * Keep track of the date time a user starts listening to a sound
 */
fun updateCurrentUserSoundRelationshipUsageTimeStamp(
    userSoundRelationship: UserSoundRelationship,
    completed: (updatedUserSoundRelationship: UserSoundRelationship) -> Unit
) {
    var usageTimeStamp = userSoundRelationship.usageTimestamps
    val currentDateTime = DateUtils.formatISO8601Date(Date())

    if(usageTimeStamp != null) {
        usageTimeStamp.add(Temporal.DateTime(currentDateTime))
    }else{
        usageTimeStamp = listOf(Temporal.DateTime(currentDateTime))
    }

    val numberOfTimesPlayed = userSoundRelationship.numberOfTimesPlayed + 1

    val newUserSoundRelationship = userSoundRelationship.copyOfBuilder()
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .usageTimestamps(usageTimeStamp)
        .build()

    UserSoundRelationshipBackend.updateUserSoundRelationship(newUserSoundRelationship){
        completed(it)
    }
}

fun updatePreviousUserSoundRelationship(
    completed: (updatedUserSoundRelationship: UserSoundRelationship?) -> Unit
) {
    if(globalViewModel_!!.previouslyPlayedUserSoundRelationship != null){
        Log.i(TAG, "Duration of general timer is ${globalViewModel_!!.soundPlaytimeTimer.getDuration()}")
        val playTime = globalViewModel_!!.soundPlaytimeTimer.getDuration()
        globalViewModel_!!.soundPlaytimeTimer.stop()

        Log.i(TAG, "Total play time = ${globalViewModel_!!.previouslyPlayedUserSoundRelationship!!.totalPlayTime}")
        Log.i(TAG, "play time = $playTime")
        val totalPlayTime = globalViewModel_!!.previouslyPlayedUserSoundRelationship!!.totalPlayTime + playTime
        Log.i(TAG, "final total play time = $totalPlayTime")

        var usagePlayTimes = globalViewModel_!!.previouslyPlayedUserSoundRelationship!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        val numberOfTimesPlayed = globalViewModel_!!.previouslyPlayedUserSoundRelationship!!.numberOfTimesPlayed
        Log.i(TAG, "number of times played = $numberOfTimesPlayed")

        if(totalPlayTime > 0){
            val userSoundRelationship = globalViewModel_!!.previouslyPlayedUserSoundRelationship!!.copyOfBuilder()
                .numberOfTimesPlayed(numberOfTimesPlayed)
                .totalPlayTime(totalPlayTime.toInt())
                .usagePlayTimes(usagePlayTimes)
                .build()

            UserSoundRelationshipBackend.updateUserSoundRelationship(userSoundRelationship){
                globalViewModel_!!.previouslyPlayedUserSoundRelationship = null
                completed(it)
            }
        }else{
            completed(null)
        }
    }else{
        completed(null)
    }
}

private fun pauseSound(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int
) {
    if(soundMediaPlayerService.areMediaPlayersInitialized()) {
        if(soundMediaPlayerService.areMediaPlayersPlaying()) {
            globalViewModel_!!.soundPlaytimeTimer.pause()
            soundMediaPlayerService.pauseMediaPlayers()
            soundActivityPlayButtonTexts[index]!!.value = START_SOUND
            com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(3)
            globalViewModel_!!.isCurrentSoundPlaying = false
        }
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

fun navigateToSoundScreen(navController: NavController, soundData: SoundData){
    navController.navigate("${Screen.SoundScreen.screen_route}/sound=${SoundObject.Sound.from(soundData)}")
}

private fun something(){

}

fun clearSoundActivityPlayButtonTexts(){
    soundActivityPlayButtonTexts.clear()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        //SoundActivityUI(rememberNavController(), LocalContext.current, globalViewModel)
    }
}