package com.example.eunoia.dashboard.prayer

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
import com.amplifyframework.datastore.generated.model.PrayerData
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserPrayerBackend
import com.example.eunoia.dashboard.bedtimeStory.resetBedtimeStoryGlobalProperties
import com.example.eunoia.dashboard.home.OptionItemTest
import com.example.eunoia.dashboard.selfLove.resetSelfLoveGlobalProperties
import com.example.eunoia.models.PrayerObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.prayer.deActivatePrayerGlobalControlButton
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope

private const val TAG = "Prayer Activity"
var prayerActivityUris = mutableListOf<MutableState<Uri>?>()
var prayerActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()
private const val START_PRAYER = "start"
private const val PAUSE_PRAYER = "pause"
private const val WAIT_FOR_PRAYER = "wait"

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
    resetPrayerActivityPlayButtonTexts()
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedPrayer by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserPrayerBackend.queryApprovedUserPrayerBasedOnUser(it) { userPrayer ->
            if(prayerActivityUris.size < userPrayer.size) {
                for (i in userPrayer.indices) {
                    prayerActivityUris.add(mutableStateOf("".toUri()))
                    prayerActivityPlayButtonTexts.add(mutableStateOf(START_PRAYER))
                }
            }
            globalViewModel_!!.currentUsersPrayers = userPrayer.toMutableList()
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
                globalViewModel_!!.currentUsersPrayers != null
            ){
                if(globalViewModel_!!.currentUsersPrayers!!.size > 0){
                    for(i in globalViewModel_!!.currentUsersPrayers!!.indices){
                        setPrayerActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersPrayers(
                            globalViewModel_!!.currentUsersPrayers!![i]!!.prayerData,
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
                                navigateToPrayerScreen(
                                    navController,
                                    globalViewModel_!!.currentUsersPrayers!![index]!!.prayerData
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

private fun setPrayerActivityPlayButtonTextsCorrectly(i: Int) {
    if (globalViewModel_!!.currentPrayerPlaying != null) {
        if (
            globalViewModel_!!.currentPrayerPlaying!!.id ==
            globalViewModel_!!.currentUsersPrayers!![i]!!.prayerData.id
        ) {
            if(globalViewModel_!!.isCurrentPrayerPlaying){
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
        globalViewModel_!!.currentBedtimeStoryPlaying == null &&
        globalViewModel_!!.currentSelfLovePlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            prayerActivityPlayButtonTexts[index]!!.value = START_PRAYER
            globalViewModel_!!.prayerTimer.pause()
            globalViewModel_!!.isCurrentPrayerPlaying = false
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
            globalViewModel_!!.currentBedtimeStoryPlaying == null &&
            globalViewModel_!!.currentSelfLovePlaying == null
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
        globalViewModel_!!.prayerTimer.start()
        setGlobalPropertiesAfterPlayingPrayer(index)
    }
}

private fun setGlobalPropertiesAfterPlayingPrayer(index: Int){
    globalViewModel_!!.currentPrayerPlaying = globalViewModel_!!.currentUsersPrayers!![index]!!.prayerData
    globalViewModel_!!.currentPrayerPlayingUri = prayerActivityUris[index]!!.value
    prayerActivityPlayButtonTexts[index]!!.value = PAUSE_PRAYER
    globalViewModel_!!.isCurrentPrayerPlaying = true
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
        globalViewModel_!!.currentUsersPrayers!![index]!!.prayerData.audioKeyS3,
        globalViewModel_!!.currentUsersPrayers!![index]!!.prayerData.prayerOwner.amplifyAuthUserId
    ) {
        prayerActivityUris[index]!!.value = it
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
    if(globalViewModel_!!.currentPrayerPlaying != null) {
        if (globalViewModel_!!.currentUsersPrayers!![index]!!.prayerData.id != globalViewModel_!!.currentPrayerPlaying!!.id) {
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

private fun initializeMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    generalMediaPlayerService.onDestroy()
    generalMediaPlayerService.setAudioUri(prayerActivityUris[index]!!.value)
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    globalViewModel_!!.prayerTimer.setMaxDuration(globalViewModel_!!.currentUsersPrayers!![index]!!.prayerData.fullPlayTime.toLong())
    globalViewModel_!!.prayerTimer.setDuration(0L)
    resetOtherGeneralMediaPlayerUsersExceptPrayer()
    //resetAll(context, soundMediaPlayerService)
}

fun resetOtherGeneralMediaPlayerUsersExceptPrayer(){
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null){
        resetBedtimeStoryGlobalProperties()
    }
    if(globalViewModel_!!.currentSelfLovePlaying != null){
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