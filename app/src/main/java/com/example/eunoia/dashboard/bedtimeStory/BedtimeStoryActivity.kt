package com.example.eunoia.dashboard.bedtimeStory

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryAudioSource
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserBedtimeStoryBackend
import com.example.eunoia.dashboard.home.OptionItem
import com.example.eunoia.dashboard.prayer.resetPrayerGlobalProperties
import com.example.eunoia.dashboard.selfLove.resetSelfLoveGlobalProperties
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.bedtimeStory.deActivateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope

private const val TAG = "Bedtime Story Activity"
var bedtimeStoryActivityUris = mutableListOf<MutableState<Uri>?>()
var bedtimeStoryActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()
private const val START_BEDTIME_STORY = "start"
private const val PAUSE_BEDTIME_STORY = "pause"
private const val WAIT_FOR_BEDTIME_STORY = "wait"

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
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    resetBedtimeStoryActivityPlayButtonTexts()
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedBedtimeStories by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserBedtimeStoryBackend.queryApprovedUserBedtimeStoryBasedOnUser(it) { userBedtimeStory ->
            if(bedtimeStoryActivityUris.size < userBedtimeStory.size) {
                for (i in userBedtimeStory.indices) {
                    bedtimeStoryActivityUris.add(mutableStateOf("".toUri()))
                    bedtimeStoryActivityPlayButtonTexts.add(mutableStateOf(START_BEDTIME_STORY))
                }
            }
            globalViewModel_!!.currentUsersBedtimeStories = userBedtimeStory.toMutableList()
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
                globalViewModel_!!.currentUsersBedtimeStories != null
            ){
                if(globalViewModel_!!.currentUsersBedtimeStories!!.size > 0){
                    for(i in globalViewModel_!!.currentUsersBedtimeStories!!.indices){
                        setBedtimeStoryActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersBedtimeStories(
                            globalViewModel_!!.currentUsersBedtimeStories!![i]!!.bedtimeStoryInfoData,
                            i,
                            { index ->
                                resetGeneralMediaPlayerServiceIfNecessary(generalMediaPlayerService, index)
                                resetPlayButtonTextsIfNecessary(index)
                                playOrPauseMediaPlayerAccordingly(generalMediaPlayerService, soundMediaPlayerService, index, context)
                            },
                            { index ->
                                navigateToBedtimeStoryScreen(navController, globalViewModel_!!.currentUsersBedtimeStories!![index]!!.bedtimeStoryInfoData)
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

private fun setBedtimeStoryActivityPlayButtonTextsCorrectly(i: Int) {
    if (globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (
            globalViewModel_!!.currentBedtimeStoryPlaying!!.id ==
            globalViewModel_!!.currentUsersBedtimeStories!![i]!!.bedtimeStoryInfoData.id
        ) {
            if(globalViewModel_!!.isCurrentBedtimeStoryPlaying){
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

private fun playOrPauseMediaPlayerAccordingly(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(bedtimeStoryActivityPlayButtonTexts[index]!!.value == START_BEDTIME_STORY){
        bedtimeStoryActivityPlayButtonTexts[index]!!.value = WAIT_FOR_BEDTIME_STORY
        if(bedtimeStoryActivityUris[index]!!.value == "".toUri()) {
            retrieveBedtimeStoryAudio(generalMediaPlayerService, soundMediaPlayerService, index, context)
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
        globalViewModel_!!.currentSelfLovePlaying == null &&
        globalViewModel_!!.currentBedtimeStoryPlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            bedtimeStoryActivityPlayButtonTexts[index]!!.value =
                START_BEDTIME_STORY
            globalViewModel_!!.bedtimeStoryTimer.pause()
            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
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
            globalViewModel_!!.currentSelfLovePlaying == null &&
            globalViewModel_!!.currentBedtimeStoryPlaying == null
        ){
            generalMediaPlayerService.startMediaPlayer()
        }else{
            initializeMediaPlayer(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
        globalViewModel_!!.bedtimeStoryTimer.start()
        setGlobalPropertiesAfterPlayingBedtimeStory(index)
    }
}

private fun setGlobalPropertiesAfterPlayingBedtimeStory(index: Int){
    globalViewModel_!!.currentBedtimeStoryPlaying = globalViewModel_!!.currentUsersBedtimeStories!![index]!!.bedtimeStoryInfoData
    globalViewModel_!!.currentBedtimeStoryPlayingUri = bedtimeStoryActivityUris[index]!!.value
    bedtimeStoryActivityPlayButtonTexts[index]!!.value = PAUSE_BEDTIME_STORY
    globalViewModel_!!.isCurrentBedtimeStoryPlaying = true
    deActivateBedtimeStoryGlobalControlButton(0)
    deActivateBedtimeStoryGlobalControlButton(2)
}

private fun retrieveBedtimeStoryAudio(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if (
        globalViewModel_!!.currentUsersBedtimeStories!![index]!!.bedtimeStoryInfoData.audioSource == BedtimeStoryAudioSource.UPLOADED
    ) {
        SoundBackend.retrieveAudio(
            globalViewModel_!!.currentUsersBedtimeStories!![index]!!.bedtimeStoryInfoData.audioKeyS3,
            globalViewModel_!!.currentUsersBedtimeStories!![index]!!.bedtimeStoryInfoData.bedtimeStoryOwner.amplifyAuthUserId
        ) {
            bedtimeStoryActivityUris[index]!!.value = it
            startBedtimeStory(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    } else {
        //if recorded
        //get chapter, get pages, get recordings from s3, play them one after the order
    }
}

private fun resetGeneralMediaPlayerServiceIfNecessary(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentUsersBedtimeStories!![index]!!.bedtimeStoryInfoData.id != globalViewModel_!!.currentBedtimeStoryPlaying!!.id) {
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

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    generalMediaPlayerService.onDestroy()
    generalMediaPlayerService.setAudioUri(bedtimeStoryActivityUris[index]!!.value)
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    globalViewModel_!!.bedtimeStoryTimer.setMaxDuration(globalViewModel_!!.currentUsersBedtimeStories!![index]!!.bedtimeStoryInfoData.fullPlayTime.toLong())
    globalViewModel_!!.bedtimeStoryTimer.setDuration(0L)
    resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()
    //resetAll(context, soundMediaPlayerService)
}

fun resetOtherGeneralMediaPlayerUsersExceptBedtimeStory(){
    if(globalViewModel_!!.currentSelfLovePlaying != null){
        resetSelfLoveGlobalProperties()
    }
    if(globalViewModel_!!.currentPrayerPlaying != null){
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

fun navigateToBedtimeStoryScreen(navController: NavController, bedtimeStoryInfoData: BedtimeStoryInfoData){
    navController.navigate("${Screen.BedtimeStoryScreen.screen_route}/bedtimeStoryData=${BedtimeStoryObject.BedtimeStory.from(bedtimeStoryInfoData)}")
}

private fun something(){

}