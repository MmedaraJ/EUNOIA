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
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserSoundBackend
import com.example.eunoia.dashboard.home.*
import com.example.eunoia.models.SoundObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.sound.resetGlobalControlButtons
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.*

private const val TAG = "Sound Activity"
var soundActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()
var soundActivityUris = mutableListOf<MutableList<Uri?>>()
var soundActivityUriVolumes = mutableListOf<MutableList<Int>>()
var soundActivityPresets = mutableListOf<MutableState<PresetData?>?>()
private const val START_SOUND = "start"
private const val PAUSE_SOUND = "pause"
private const val WAIT_FOR_SOUND = "wait"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoundActivityUI(
    navController: NavController,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    resetSoundActivityPlayButtonTexts()
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedSounds by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserSoundBackend.queryUserSoundBasedOnUser(it) { userSound ->
            if(soundActivityUris.size < userSound.size) {
                for(i in userSound.indices){
                    soundActivityPlayButtonTexts.add(mutableStateOf(START_SOUND))
                    soundActivityUriVolumes.add(mutableListOf())
                    soundActivityUris.add(mutableListOf())
                    soundActivityPresets.add(mutableStateOf(null))
                }
            }
            globalViewModel_!!.currentUsersSounds = userSound.toMutableList()
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
            OptionsList(navController, context)
        }
        Column(
            modifier = Modifier
                .constrainAs(favoriteSoundsTitle) {
                    top.linkTo(options.bottom, margin = 0.dp)
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
                globalViewModel_!!.currentUsersSounds != null
            ) {
                if(globalViewModel_!!.currentUsersSounds!!.size > 0){
                    for(i in globalViewModel_!!.currentUsersSounds!!.indices){
                        setSoundActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersSounds(
                            globalViewModel_!!.currentUsersSounds!![i]!!.soundData,
                            i,
                            { index ->
                                resetSoundMediaPlayerServiceIfNecessary(soundMediaPlayerService, index)
                                resetPlayButtonTextsIfNecessary(index)
                                playOrPauseMediaPlayerAccordingly(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    index,
                                    context
                                )
                            },
                            { index ->
                                if(soundActivityPlayButtonTexts[index]!!.value != WAIT_FOR_SOUND) {
                                    soundScreenBorderControlColors[7].value = Bizarre
                                    navigateToSoundScreen(
                                        navController,
                                        globalViewModel_!!.currentUsersSounds!![index]!!.soundData
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
            globalViewModel_!!.currentUsersSounds!![i]!!.soundData.id
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
        if (globalViewModel_!!.currentUsersSounds!![index]!!.soundData.id != globalViewModel_!!.currentSoundPlaying!!.id) {
            soundMediaPlayerService.onDestroy()
        }
    }
}

private fun playOrPauseMediaPlayerAccordingly(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    if(soundActivityPlayButtonTexts[index]!!.value == START_SOUND){
        soundActivityPlayButtonTexts[index]!!.value = WAIT_FOR_SOUND
        if(soundActivityUris[index].size < 1) {
            getNecessaryPresets(index) {
                retrieveSoundAudio(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            startSound(
                soundMediaPlayerService,
                generalMediaPlayerService,
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
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    SoundBackend.listS3Sounds(
        globalViewModel_!!.currentUsersSounds!![index]!!.soundData.audioKeyS3,
        globalViewModel_!!.currentUsersSounds!![index]!!.soundData.soundOwner.amplifyAuthUserId
    ){ s3List ->
        s3List.items.forEachIndexed { i, item ->
            SoundBackend.retrieveAudio(
                item.key,
                globalViewModel_!!.currentUsersSounds!![index]!!.soundData.soundOwner.amplifyAuthUserId
            ) { uri ->
                soundActivityUris[index].add(uri)
                if(i == s3List.items.size - 1){
                    startSound(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
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
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    if(soundActivityUris[index].isNotEmpty()){
        if(soundMediaPlayerService.areMediaPlayersInitialized()){
            soundMediaPlayerService.startMediaPlayers()
        }else{
            initializeMediaPlayers(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
        setGlobalPropertiesAfterPlayingSound(index, context)
    }
}

private fun getNecessaryPresets(index: Int, completed: () -> Unit){
    getUserSoundPresets(
        globalViewModel_!!.currentUsersSounds!![index]!!.soundData,
        globalViewModel_!!.currentUsersSounds!![index]!!.soundData.soundOwner,
    ) { presetData ->
        soundActivityPresets[index]!!.value = presetData[0]
        soundActivityUriVolumes[index] = presetData[0].volumes
        completed()
    }
}

private fun setGlobalPropertiesAfterPlayingSound(index: Int, context: Context) {
    globalViewModel_!!.currentSoundPlaying = globalViewModel_!!.currentUsersSounds!![index]!!.soundData
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
}

private fun initializeMediaPlayers(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
){
    generalMediaPlayerService.onDestroy()
    soundMediaPlayerService.onDestroy()
    soundMediaPlayerService.setAudioUris(soundActivityUris[index])
    soundMediaPlayerService.setVolumes(soundActivityUriVolumes[index])
    val intent = Intent()
    intent.action = "PLAY"
    soundMediaPlayerService.onStartCommand(intent, 0, 0)
    resetAll(context, soundMediaPlayerService)
    createMeditationBellMediaPlayer(context)
    resetGlobalControlButtons()
}

private fun pauseSound(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int
) {
    if(soundMediaPlayerService.areMediaPlayersInitialized()) {
        if(soundMediaPlayerService.areMediaPlayersPlaying()) {
            soundMediaPlayerService.pauseMediaPlayers()
            soundActivityPlayButtonTexts[index]!!.value = START_SOUND
            com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(3)
            globalViewModel_!!.isCurrentSoundPlaying = false
        }
    }
}

@Composable
private fun OptionsList(navController: NavController, context: Context){
    var sound by remember{ mutableStateOf<SoundData?>(null) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(
            displayName = "pouring\nrain",
            icon = R.drawable.pouring_rain_icon,
            71,
            71,
            false,
            0,
            0,
            { displayName ->
                Log.i(TAG, "About to get pouring rain again")
                if(globalViewModel_!!.currentSoundPlaying == null) {
                    SoundBackend.querySoundBasedOnDisplayName("pouring rain") {
                        if (it.isNotEmpty()) {
                            sound = it[0]
                        }
                    }
                }else{
                    if (globalViewModel_!!.currentSoundPlaying!!.displayName != "pouring rain") {
                        SoundBackend.querySoundBasedOnDisplayName("pouring rain") {
                            if (it.isNotEmpty()) {
                                sound = it[0]
                            }
                        }
                    }
                }
            }
        ){
            if (sound != null) {
                navigateToSoundScreen(navController, sound!!)
            }else{
                navigateToSoundScreen(navController, globalViewModel_!!.currentSoundPlaying!!)
            }
        }
        OptionItem(displayName = "coffee\nhouse", icon = R.drawable.coffee_house_icon, 71, 71, true, 35, -10, {}){ something() }
        OptionItem(displayName = "library", icon = R.drawable.library_icon, 71, 71, false, 0, 0, {}){ something() }
        OptionItem(displayName = "baking", icon = R.drawable.baking_icon, 71, 71, false, 0, 0, {}){ something() }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(displayName = "beach\nwaves", icon = R.drawable.beach_waves_icon, 71, 71, false, 0, 0, {}){ something() }
        OptionItem(displayName = "next door", icon = R.drawable.next_door_icon, 71, 71, false, 0, 0, {}){ something() }
        OptionItem(displayName = "keyboard", icon = R.drawable.keyboard_icon, 71, 71, true, 35, -10, {}){ something() }
        OptionItem(displayName = "train\ntrack", icon = R.drawable.train_track_icon, 71, 71, true, 35, -10, {}){ something() }
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