package com.example.eunoia.dashboard.selfLove

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryAudioSource
import com.amplifyframework.datastore.generated.model.SelfLoveAudioSource
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserSelfLoveBackend
import com.example.eunoia.dashboard.bedtimeStory.resetBedtimeStoryGlobalProperties
import com.example.eunoia.dashboard.home.OptionItemTest
import com.example.eunoia.dashboard.prayer.resetPrayerGlobalProperties
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.bedtimeStory.deActivateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.selfLove.deActivateSelfLoveGlobalControlButton
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope

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
    resetSelfLoveActivityPlayButtonTexts()
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedSelfLove by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserSelfLoveBackend.queryApprovedUserSelfLoveBasedOnUser(it) { userSelfLove ->
            if(selfLoveActivityUris.size < userSelfLove.size) {
                for (i in userSelfLove.indices) {
                    selfLoveActivityUris.add(mutableStateOf("".toUri()))
                    selfLoveActivityPlayButtonTexts.add(mutableStateOf(START_SELF_LOVE))
                }
            }
            globalViewModel_!!.currentUsersSelfLoves = userSelfLove.toMutableList()
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
            OptionItemTest(
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
                globalViewModel_!!.currentUsersSelfLoves != null
            ){
                if(globalViewModel_!!.currentUsersSelfLoves!!.size > 0){
                    for(i in globalViewModel_!!.currentUsersSelfLoves!!.indices){
                        setSelfLoveActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersSelfLoves(
                            globalViewModel_!!.currentUsersSelfLoves!![i]!!.selfLoveData,
                            i,
                            { index ->
                                resetGeneralMediaPlayerServiceIfNecessary(generalMediaPlayerService, index)
                                resetPlayButtonTextsIfNecessary(index)
                                playOrPauseMediaPlayerAccordingly(
                                    generalMediaPlayerService,
                                    soundMediaPlayerService,
                                    index,
                                    context
                                )
                            },
                            { index ->
                                navigateToSelfLoveScreen(
                                    navController,
                                    globalViewModel_!!.currentUsersSelfLoves!![index]!!.selfLoveData
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

private fun setSelfLoveActivityPlayButtonTextsCorrectly(i: Int) {
    if (globalViewModel_!!.currentSelfLovePlaying != null) {
        if (
            globalViewModel_!!.currentSelfLovePlaying!!.id ==
            globalViewModel_!!.currentUsersSelfLoves!![i]!!.selfLoveData.id
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
        }else{
            initializeMediaPlayer(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
        globalViewModel_!!.selfLoveTimer.start()
        setGlobalPropertiesAfterPlayingSelfLove(index)
    }
}

private fun setGlobalPropertiesAfterPlayingSelfLove(index: Int){
    globalViewModel_!!.currentSelfLovePlaying = globalViewModel_!!.currentUsersSelfLoves!![index]!!.selfLoveData
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
        globalViewModel_!!.currentUsersSelfLoves!![index]!!.selfLoveData.audioKeyS3,
        globalViewModel_!!.currentUsersSelfLoves!![index]!!.selfLoveData.selfLoveOwner.amplifyAuthUserId
    ) {
        selfLoveActivityUris[index]!!.value = it
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
        if (globalViewModel_!!.currentUsersSelfLoves!![index]!!.selfLoveData.id != globalViewModel_!!.currentSelfLovePlaying!!.id) {
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

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    generalMediaPlayerService.onDestroy()
    generalMediaPlayerService.setAudioUri(selfLoveActivityUris[index]!!.value)
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    globalViewModel_!!.selfLoveTimer.setMaxDuration(globalViewModel_!!.currentUsersSelfLoves!![index]!!.selfLoveData.fullPlayTime.toLong())
    globalViewModel_!!.selfLoveTimer.setDuration(0L)
    resetOtherGeneralMediaPlayerUsersExceptSelfLove()
    //resetAll(context, soundMediaPlayerService)
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