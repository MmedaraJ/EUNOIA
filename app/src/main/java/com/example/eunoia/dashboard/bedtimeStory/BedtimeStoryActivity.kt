package com.example.eunoia.dashboard.bedtimeStory

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryAudioSource
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.R
import com.example.eunoia.backend.BedtimeStoryBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.dashboard.home.OptionItem
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.SoundObject
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope

private val TAG = "Bedtime Story Activity"
var bedtimeStoryActivityMediaPlayers = mutableListOf<MutableState<MediaPlayer>?>()
var bedtimeStoryActivityUris = mutableListOf<MutableState<Uri>?>()
var bedtimeStoryActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()
var alreadyInitialized = false
const val START_BEDTIME_STORY = "start"
const val PAUSE_BEDTIME_STORY = "pause"
const val WAIT_FOR_BEDTIME_STORY = "wait"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BedtimeStoryActivityUI(
    navController: NavController,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
) {
    clearBedtimeStoryActivityPlayButtonTexts()
    //bedtimeStoryActivityMediaPlayers.clear()
    //bedtimeStoryActivityUris.clear()
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    globalViewModel_!!.currentUser?.let {
        BedtimeStoryBackend.queryCompleteApprovedBedtimeStoryBasedOnUser(it) { userBedtimeStory ->
            for (i in userBedtimeStory.indices) {
                val mediaPlayer = MediaPlayer()
                bedtimeStoryActivityMediaPlayers.add(mutableStateOf(mediaPlayer))
                bedtimeStoryActivityUris.add(mutableStateOf("".toUri()))
            }
            globalViewModel_!!.currentUsersBedtimeStories = userBedtimeStory.toMutableList()
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
            OptionsList(navController, context)
        }
        Column(
            modifier = Modifier
                .constrainAs(favoriteBedtimeStoryTitle) {
                    top.linkTo(options.bottom, margin = 0.dp)
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
            if(globalViewModel_!!.currentUsersBedtimeStories != null){
                if(globalViewModel_!!.currentUsersBedtimeStories!!.size > 0)
                {
                    for(i in globalViewModel_!!.currentUsersBedtimeStories!!.indices){
                        if(globalViewModel_!!.currentBedtimeStoryPlaying != null){
                            if(
                                globalViewModel_!!.currentBedtimeStoryPlaying!!.id ==
                                globalViewModel_!!.currentUsersBedtimeStories!![i]!!.id
                            ) {
                                bedtimeStoryActivityPlayButtonTexts.add(remember { mutableStateOf(PAUSE_BEDTIME_STORY) })
                            }
                            else{
                                bedtimeStoryActivityPlayButtonTexts.add(remember{ mutableStateOf(START_BEDTIME_STORY) })
                            }
                        }
                        else{
                            bedtimeStoryActivityPlayButtonTexts.add(remember{ mutableStateOf(START_BEDTIME_STORY) })
                        }
                        DisplayUsersBedtimeStories(
                            globalViewModel_!!.currentUsersBedtimeStories!![i]!!,
                            i,
                            { index ->
                                if(
                                    globalViewModel_!!.currentUsersBedtimeStories!![index]!!.audioSource == BedtimeStoryAudioSource.UPLOADED
                                ){
                                    SoundBackend.retrieveAudio(globalViewModel_!!.currentUsersBedtimeStories!![index]!!.audioKeyS3){
                                        bedtimeStoryActivityUris[index]!!.value = it
                                    }
                                }else{
                                    //if recorded
                                    //get chapter, get pages, get recordings from s3, play them one after the order
                                }
                            },
                            { index ->
                                //resetAllForBedtimeStory(context)
                                //resetBedtimeStoryActivityPlayButtonTexts()
                                if(globalViewModel_!!.currentUsersBedtimeStories!![i]!! != globalViewModel_!!.currentBedtimeStoryPlaying) {
                                    resetBedtimeStoryActivityMediaPlayers()
                                }
                                for(j in bedtimeStoryActivityPlayButtonTexts.indices){
                                    if(j != index){
                                        if(bedtimeStoryActivityPlayButtonTexts[j]!!.value != START_BEDTIME_STORY) {
                                            bedtimeStoryActivityPlayButtonTexts[j]!!.value = START_BEDTIME_STORY
                                        }
                                    }
                                }
                                if(bedtimeStoryActivityPlayButtonTexts[index]!!.value == START_BEDTIME_STORY){
                                    bedtimeStoryActivityPlayButtonTexts[index]!!.value = WAIT_FOR_BEDTIME_STORY
                                    if(bedtimeStoryActivityUris[index]!!.value != "".toUri()){
                                        startBedtimeStoryActivityMediaPlayer(context, index)
                                    }
                                }else if(
                                    bedtimeStoryActivityPlayButtonTexts[index]!!.value == PAUSE_BEDTIME_STORY ||
                                    bedtimeStoryActivityPlayButtonTexts[index]!!.value == WAIT_FOR_BEDTIME_STORY
                                ){
                                    startBedtimeStoryActivityMediaPlayer(context, index)
                                }
                            },
                            { index ->
                                //releaseBedtimeStoryActivityMediaPlayers()
                                //resetBedtimeStoryActivityPlayButtonTexts()
                                //bedtimeStoryActivityUris.clear()
                                //resetAllForBedtimeStory(context)
                                navigateToBedtimeStoryScreen(navController, globalViewModel_!!.currentUsersBedtimeStories!![index]!!)
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

fun startBedtimeStoryActivityMediaPlayer(context: Context, index: Int){
    if(alreadyInitialized){
        if (bedtimeStoryActivityMediaPlayers[index]!!.value.isPlaying) {
            bedtimeStoryActivityMediaPlayers[index]!!.value.pause()
            bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
            //TODO pause bedtime story controls
        }else{
            bedtimeStoryActivityMediaPlayers[index]!!.value.start()
            bedtimeStoryActivityPlayButtonTexts[index]!!.value = PAUSE_BEDTIME_STORY
            //TODO start bedtime story controls
        }
    }else{
        bedtimeStoryActivityMediaPlayers[index]!!.value.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, bedtimeStoryActivityUris[index]!!.value)
            setVolume(1f, 1f)
            prepare()
            start()
        }
        globalViewModel_!!.currentBedtimeStoryPlaying = globalViewModel_!!.currentUsersBedtimeStories!![index]
        bedtimeStoryActivityPlayButtonTexts[index]!!.value = PAUSE_BEDTIME_STORY
        alreadyInitialized = true
        //TODO start bedtime story controls
    }
}

fun resetBedtimeStoryActivityMediaPlayers(){
    for(mp in bedtimeStoryActivityMediaPlayers){
        if(mp!!.value.isPlaying){
            mp.value.stop()
        }
        mp.value.reset()
    }
    alreadyInitialized = false
}

fun resetBedtimeStoryActivityPlayButtonTexts(){
    for(text in bedtimeStoryActivityPlayButtonTexts){
        text!!.value = START_BEDTIME_STORY
    }
}

fun releaseBedtimeStoryActivityMediaPlayers(){
    for(mp in bedtimeStoryActivityMediaPlayers){
        if(mp!!.value.isPlaying){
            mp.value.stop()
        }
        mp.value.reset()
        mp.value.release()
    }
    //bedtimeStoryActivityMediaPlayers.clear()
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
            displayName = "pouring\n   rain",
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

fun navigateToBedtimeStoryScreen(navController: NavController, bedtimeStoryInfoData: BedtimeStoryInfoData){
    navController.navigate("${Screen.BedtimeStoryScreen.screen_route}/bedtimeStoryData=${BedtimeStoryObject.BedtimeStory.from(bedtimeStoryInfoData)}")
}

private fun something(){

}

fun clearBedtimeStoryActivityPlayButtonTexts(){
    bedtimeStoryActivityPlayButtonTexts.clear()
}