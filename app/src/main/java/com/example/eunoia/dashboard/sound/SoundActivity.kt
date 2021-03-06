package com.example.eunoia.dashboard.sound

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
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
import com.example.eunoia.backend.UserRoutineBackend
import com.example.eunoia.backend.UserSoundBackend
import com.example.eunoia.dashboard.home.*
import com.example.eunoia.models.PresetNameAndVolumesMapObject
import com.example.eunoia.models.SoundObject
import com.example.eunoia.mvvm.soundMvvm.model.SoundModel
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.*

private val TAG = "Sound Activity"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoundActivityUI(
    navController: NavController,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
) {
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    globalViewModel_!!.currentUser?.let {
        UserSoundBackend.queryUserSoundBasedOnUser(it) { userSound ->
            globalViewModel_!!.currentUsersSounds = userSound.toMutableList()
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
            favorite_sounds_title,
            emptySoundList,
            articles_title,
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
                .constrainAs(favorite_sounds_title) {
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
                    top.linkTo(favorite_sounds_title.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            if(globalViewModel_!!.currentUsersSounds != null){
                if(globalViewModel_!!.currentUsersSounds!!.size > 0)
                {
                    for(sound in globalViewModel_!!.currentUsersSounds!!){
                        if(sound!!.soundData.approvalStatus == SoundApprovalStatus.APPROVED){
                            var soundPreset by remember{mutableStateOf<PresetData?>(null)}
                            var soundPresetMap by remember{mutableStateOf<PresetNameAndVolumesMapData?>(null)}
                            val uris = remember{mutableListOf<Uri>()}
                            DisplayUsersSounds(
                                sound.soundData,
                                {
                                    getSoundPresets(sound.soundData) { presetData ->
                                        soundPreset = presetData
                                        for(preset in soundPreset!!.presets){
                                            if(preset.key == "current_volumes"){
                                                soundPresetMap = preset
                                                globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap = preset
                                            }
                                        }
                                    }
                                    retrieveAudioUris(uris, sound.soundData)
                                },
                                {startText ->
                                    if(startText == "start"){
                                        globalViewModel_!!.currentSoundPlayingPreset = null
                                        resetAll(context)
                                        globalViewModel_!!.currentSoundPlaying = sound.soundData
                                        globalViewModel_!!.currentSoundPlayingContext = context
                                        if(soundPreset != null) {
                                            globalViewModel_!!.currentSoundPlayingPreset = soundPreset
                                            globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap = soundPresetMap
                                            var volumes = listOf<Int>()
                                            for (preset in soundPreset!!.presets) {
                                                if (preset.key == "current_volumes") {
                                                    volumes = preset.volumes
                                                }
                                            }
                                            Log.i(TAG, "Volumes ==>> $volumes")
                                            globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
                                            for (volume in volumes) {
                                                globalViewModel_!!.currentSoundPlayingSliderPositions.add(
                                                    mutableStateOf(volume.toFloat())
                                                )
                                            }
                                            createMeditationBellMediaPlayer(context)
                                            Log.i(TAG, "Uris ==>> $uris")
                                            if(uris.size > 0) {
                                                playSounds(
                                                    uris,
                                                    context,
                                                    3
                                                )
                                            }
                                        }
                                    }else if(startText == "stop"){
                                        pauseSounds(context, 3)
                                    }
                                },
                                {
                                    globalViewModel_!!.currentSoundPlayingPreset = null
                                    resetAll(context)
                                    navigateToSoundScreen(navController, sound.soundData)
                                }
                            )
                        }
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
                .constrainAs(articles_title) {
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
                    top.linkTo(articles_title.bottom, margin = 18.dp)
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

@Composable
private fun OptionsList(navController: NavController, context: Context){
    var sound by remember{ mutableStateOf<SoundData?>(null) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(
            displayName = "pouring rain",
            icon = R.drawable.pouring_rain_icon,
            71,
            71,
            false,
            0,
            0,
            { displayName ->
                Log.i(TAG, "About to get pouring rain again")
                if(globalViewModel_!!.currentSoundPlaying == null) {
                    SoundBackend.querySoundBasedOnDisplayName(displayName) {
                        if (it.isNotEmpty()) {
                            sound = it[0]
                        }
                    }
                }else{
                    if (globalViewModel_!!.currentSoundPlaying!!.displayName != displayName) {
                        SoundBackend.querySoundBasedOnDisplayName(displayName) {
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
        OptionItem(displayName = "coffee house", icon = R.drawable.coffee_house_icon, 71, 71, true, 35, -10, {}){ something() }
        OptionItem(displayName = "library", icon = R.drawable.library_icon, 71, 71, false, 0, 0, {}){ something() }
        OptionItem(displayName = "baking", icon = R.drawable.baking_icon, 71, 71, false, 0, 0, {}){ something() }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(displayName = "beach waves", icon = R.drawable.beach_waves_icon, 71, 71, false, 0, 0, {}){ something() }
        OptionItem(displayName = "next door", icon = R.drawable.next_door_icon, 71, 71, false, 0, 0, {}){ something() }
        OptionItem(displayName = "keyboard", icon = R.drawable.keyboard_icon, 71, 71, true, 35, -10, {}){ something() }
        OptionItem(displayName = "train track", icon = R.drawable.train_track_icon, 71, 71, true, 35, -10, {}){ something() }
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
    globalViewModel_!!.currentSoundPlaying = soundData
    navController.navigate("${Screen.SoundScreen.screen_route}/sound=${SoundObject.Sound.from(soundData)}")
}

private fun something(){

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        //SoundActivityUI(rememberNavController(), LocalContext.current, globalViewModel)
    }
}