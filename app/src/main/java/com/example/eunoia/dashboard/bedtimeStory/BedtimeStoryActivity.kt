package com.example.eunoia.dashboard.bedtimeStory

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
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
import com.amplifyframework.datastore.generated.model.BedtimeStoryAudioSource
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfoRelationship
import com.example.eunoia.R
import com.example.eunoia.backend.BedtimeStoryChapterBackend
import com.example.eunoia.backend.PageBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserBedtimeStoryInfoRelationshipBackend
import com.example.eunoia.create.createBedtimeStory.*
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
import com.example.eunoia.ui.bottomSheets.bedtimeStory.activateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.bedtimeStory.deActivateBedtimeStoryGlobalControlButton
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.openRoutineIsCurrentlyPlayingDialogBox
import com.example.eunoia.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "Bedtime Story Activity"
var bedtimeStoryActivityUris = mutableListOf<MutableState<Uri>?>()
var bedtimeStoryActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()

/**
 * List = lengths
 * List = lengthTotals
 * List = s3Keys
 * List = uris
 */
var recordedBedtimeStoryActivityLengths = mutableListOf<MutableList<Int>>()
var recordedBedtimeStoryActivityLengthTotals = mutableListOf<MutableList<Int>>()
var recordedBedtimeStoryActivityS3Keys = mutableListOf<MutableList<String>>()
var recordedBedtimeStoryActivityUris = mutableListOf<MutableList<Uri>>()
var recordedBedtimeStoryActivityChapters = mutableListOf<MutableList<Int>>()
var recordedBedtimeStoryActivityPages = mutableListOf<MutableList<Int>>()

private var recordedCDT: CountDownTimer? = null
private var recordedPlayingIndex by mutableStateOf(0)
private var remainingPlayTime by mutableStateOf(0)

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
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    var retrievedBedtimeStories by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserBedtimeStoryInfoRelationshipBackend.queryApprovedUserBedtimeStoryInfoRelationshipBasedOnUser(it) { userBedtimeStoryRelationship ->
            if(bedtimeStoryActivityUris.size < userBedtimeStoryRelationship.size) {
                for (i in userBedtimeStoryRelationship.indices) {
                    bedtimeStoryActivityUris.add(mutableStateOf("".toUri()))
                    bedtimeStoryActivityPlayButtonTexts.add(mutableStateOf(START_BEDTIME_STORY))
                    recordedBedtimeStoryActivityLengths.add(mutableListOf())
                    recordedBedtimeStoryActivityLengthTotals.add(mutableListOf())
                    recordedBedtimeStoryActivityS3Keys.add(mutableListOf())
                    recordedBedtimeStoryActivityUris.add(mutableListOf())
                    recordedBedtimeStoryActivityChapters.add(mutableListOf())
                    recordedBedtimeStoryActivityPages.add(mutableListOf())
                }
            }
            globalViewModel_!!.currentUsersBedtimeStoryRelationships = userBedtimeStoryRelationship.toMutableList()
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
                globalViewModel_!!.currentUsersBedtimeStoryRelationships != null
            ){
                if(globalViewModel_!!.currentUsersBedtimeStoryRelationships!!.size > 0){
                    for(i in globalViewModel_!!.currentUsersBedtimeStoryRelationships!!.indices){
                        setBedtimeStoryActivityPlayButtonTextsCorrectly(i)
                        DisplayUsersBedtimeStories(
                            globalViewModel_!!.currentUsersBedtimeStoryRelationships!![i]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo,
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
                                    globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo
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

private var bedtimeStoryIndex = -1

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
    resetGeneralMediaPlayerServiceIfNecessary(
        generalMediaPlayerService,
        bedtimeStoryIndex
    )
    resetPlayButtonTextsIfNecessary(bedtimeStoryIndex)

    if(
        globalViewModel_!!.currentUsersBedtimeStoryRelationships!![bedtimeStoryIndex]!!
            .userBedtimeStoryInfoRelationshipBedtimeStoryInfo.audioSource == BedtimeStoryAudioSource.UPLOADED
    ) {
        recordedPlayingIndex = 0
        playOrPauseUploadedMediaPlayerAccordingly(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryIndex,
            context
        )
    }else{
        playOrPauseRecordedMediaPlayerAccordingly(
            generalMediaPlayerService,
            soundMediaPlayerService,
            bedtimeStoryIndex,
            context
        )
    }

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
        startBedtimeStoryConfirmed(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }
}

private fun setBedtimeStoryActivityPlayButtonTextsCorrectly(i: Int) {
    if (globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (
            globalViewModel_!!.currentBedtimeStoryPlaying!!.id ==
            globalViewModel_!!.currentUsersBedtimeStoryRelationships!![i]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.id
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

private fun playOrPauseUploadedMediaPlayerAccordingly(
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

private fun playOrPauseRecordedMediaPlayerAccordingly(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(bedtimeStoryActivityPlayButtonTexts[index]!!.value == START_BEDTIME_STORY){
        bedtimeStoryActivityPlayButtonTexts[index]!!.value = WAIT_FOR_BEDTIME_STORY
        if(recordedBedtimeStoryActivityUris[index].isEmpty()) {
            retrieveAllRecordedBedtimeStoryInfo(index){
                recordedPlayingIndex = 0
                startRecordedBedtimeStory(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            startRecordedBedtimeStory(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }else if(
        bedtimeStoryActivityPlayButtonTexts[index]!!.value == PAUSE_BEDTIME_STORY ||
        bedtimeStoryActivityPlayButtonTexts[index]!!.value == WAIT_FOR_BEDTIME_STORY
    ){
        pauseBedtimeStory(generalMediaPlayerService, index)
    }
}

fun retrieveAllRecordedBedtimeStoryInfo(
    index: Int,
    completed: () -> Unit
) {
    BedtimeStoryChapterBackend.queryBedtimeStoryChapterBasedOnBedtimeStory(
        globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo
    ){ chapters ->
        if(chapters.isNotEmpty()){
            var totalLength = 0
            var totalIndexes = 0
            Log.i(TAG, "chapters list is $chapters")
            chapters.sortBy{
                it.chapterNumber
            }
            chapters.forEachIndexed{ chapterIndex, chapter ->
                PageBackend.queryPageBasedOnChapter(
                    chapter
                ){ pages ->
                    if(pages.isNotEmpty()) {
                        pages.sortBy {
                            it.pageNumber
                        }
                        Log.i(TAG, "pages list is $pages")
                        pages.forEachIndexed { pageIndex, page ->
                            totalIndexes += page.audioNames.size
                            page.audioNames.forEachIndexed { i, _ ->
                                totalLength += page.audioLength[i]
                                recordedBedtimeStoryActivityLengths[index].add(page.audioLength[i])
                                recordedBedtimeStoryActivityLengthTotals[index].add(totalLength)
                                recordedBedtimeStoryActivityS3Keys[index].add(page.audioKeysS3[i])
                                recordedBedtimeStoryActivityChapters[index].add(chapter.chapterNumber)
                                recordedBedtimeStoryActivityPages[index].add(page.pageNumber)
                                recordedBedtimeStoryActivityUris[index].add("".toUri())

                                Log.i(TAG, "page.audioKeysS3[i] = ${page.audioKeysS3[i]}")
                                Log.i(TAG, "i  ez = $i")

                                retrieveCorrespondingUri(
                                    page.audioKeysS3[i],
                                    recordedBedtimeStoryActivityUris[index].size - 1,
                                    index
                                ){
                                    completed()
                                }
                            }
                        }
                    }else{
                        completed()
                    }
                }
            }
        }else{
            completed()
        }
    }
}

fun retrieveCorrespondingUri(
    s3Key: String?,
    i: Int,
    index: Int,
    completed: () -> Unit
) {
    SoundBackend.retrieveAudio(
        s3Key!!,
        globalViewModel_!!.currentUser!!.amplifyAuthUserId
    ) {
        if (it != null) {
            recordedBedtimeStoryActivityUris[index][i] = it
            Log.i(
                TAG,
                "recordedBedtimeStoryActivityUris[index] = ${recordedBedtimeStoryActivityUris[index]}"
            )

            if (
                !recordedBedtimeStoryActivityUris[index].contains("".toUri())
            ) {
                Log.i(
                    TAG,
                    "recordedBedtimeStoryActivityLengths[index] = ${recordedBedtimeStoryActivityLengths[index]}"
                )
                Log.i(
                    TAG,
                    "recordedBedtimeStoryActivityLengthTotals[index] = ${recordedBedtimeStoryActivityLengthTotals[index]}"
                )
                Log.i(
                    TAG,
                    "recordedBedtimeStoryActivityS3Keys[index] = ${recordedBedtimeStoryActivityS3Keys[index]}"
                )
                Log.i(
                    TAG,
                    "recordedBedtimeStoryActivityChapters[index] = ${recordedBedtimeStoryActivityChapters[index]}"
                )
                Log.i(
                    TAG,
                    "recordedBedtimeStoryActivityPages[index] = ${recordedBedtimeStoryActivityPages[index]}"
                )
                Log.i(
                    TAG,
                    "recordedBedtimeStoryActivityUris[index] = ${recordedBedtimeStoryActivityUris[index]}"
                )
                completed()
            }
        }
    }
}

private fun pauseBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        globalViewModel_!!.currentSelfLovePlaying == null &&
        globalViewModel_!!.currentPrayerPlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
            globalViewModel_!!.bedtimeStoryTimer.pause()
            globalViewModel_!!.generalPlaytimeTimer.pause()
            globalViewModel_!!.isCurrentBedtimeStoryPlaying = false
            if(
                globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!
                    .userBedtimeStoryInfoRelationshipBedtimeStoryInfo.audioSource ==
                BedtimeStoryAudioSource.RECORDED
            ){
                resetRecordedCDT()
            }
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
            globalViewModel_!!.currentPrayerPlaying == null
        ){
            generalMediaPlayerService.startMediaPlayer()
            afterPlayingBedtimeStory(index)
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

private fun startRecordedBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(recordedBedtimeStoryActivityUris[index].isNotEmpty()){
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentSelfLovePlaying == null &&
            globalViewModel_!!.currentPrayerPlaying == null
        ){
            generalMediaPlayerService.startMediaPlayer()
            remainingPlayTime = recordedBedtimeStoryActivityLengths[index][recordedPlayingIndex] -
                    generalMediaPlayerService.getMediaPlayer()!!.currentPosition
            Log.i(TAG, "Remainning playtime is $remainingPlayTime")
            startRecordedCDT(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
            afterPlayingRecordedBedtimeStory(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }else{
            initializeRecordedMediaPlayer(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }else{
        bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
    }
}

private fun afterPlayingBedtimeStory(index: Int){
    globalViewModel_!!.bedtimeStoryTimer.start()
    globalViewModel_!!.generalPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingBedtimeStory(index)
}

private fun afterPlayingRecordedBedtimeStory(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    globalViewModel_!!.bedtimeStoryTimer.start()
    globalViewModel_!!.generalPlaytimeTimer.start()
    setGlobalPropertiesAfterPlayingBedtimeStory(index)
}

private fun setGlobalPropertiesAfterPlayingBedtimeStory(index: Int){
    globalViewModel_!!.currentBedtimeStoryPlaying = globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo
    globalViewModel_!!.currentBedtimeStoryPlayingUri = bedtimeStoryActivityUris[index]!!.value
    bedtimeStoryActivityPlayButtonTexts[index]!!.value = PAUSE_BEDTIME_STORY
    globalViewModel_!!.isCurrentBedtimeStoryPlaying = true
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
        globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.audioKeyS3,
        globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.bedtimeStoryOwner.amplifyAuthUserId
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
    if(globalViewModel_!!.currentBedtimeStoryPlaying != null) {
        if (globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.id != globalViewModel_!!.currentBedtimeStoryPlaying!!.id) {
            generalMediaPlayerService.onDestroy()
            recordedPlayingIndex = 0
        }
    }else{
        recordedPlayingIndex = 0
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
    completed: () -> Unit
){
    updatePreviousUserBedtimeStoryRelationship{
        updateRecentlyPlayedUserBedtimeStoryRelationshipWithUserBedtimeStoryRelationship(
            globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!
        ) {
            globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index] = it
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship {
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
    updatePreviousAndCurrentBedtimeStoryRelationship(index) {
        resetRecordedCDT()
        recordedPlayingIndex = 0
        generalMediaPlayerService.onDestroy()
        generalMediaPlayerService.setAudioUri(bedtimeStoryActivityUris[index]!!.value)
        val intent = Intent()
        intent.action = "PLAY"
        generalMediaPlayerService.onStartCommand(intent, 0, 0)
        globalViewModel_!!.bedtimeStoryTimer.setMaxDuration(globalViewModel_!!.currentUsersBedtimeStoryRelationships!![index]!!.userBedtimeStoryInfoRelationshipBedtimeStoryInfo.fullPlayTime.toLong())
        resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()
    }
    afterPlayingBedtimeStory(index)
}

private fun resetRecordedCDT(){
    if(recordedCDT != null){
        recordedCDT!!.cancel()
        recordedCDT = null
    }
}

/**
 * Start count down timer
 *
 * @param time duration of the count down timer
 * @param completed runs when count down timer is finished
 */
private fun startBedtimeStoryRecordedCountDownTimer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context,
    time: Long,
    completed: () -> Unit
) {
    if (recordedCDT == null) {
        recordedCDT = object : CountDownTimer(time, 1) {
            override fun onTick(millisUntilFinished: Long) {
                if(generalMediaPlayerService.isMediaPlayerInitialized()){
                    if(generalMediaPlayerService.isMediaPlayerPlaying()){
                        val currPos = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
                        if(currPos == generalMediaPlayerService.getMediaPlayer()!!.duration){
                            Log.i(TAG, "Something fishy. 1. millisUntil finihsed = $millisUntilFinished")
                            Log.i(TAG, "Something fishy. 2. currPos = ${generalMediaPlayerService.getMediaPlayer()!!.currentPosition}")
                            Log.i(TAG, "Something fishy. 3. duration = ${generalMediaPlayerService.getMediaPlayer()!!.duration}")

                            recordedPlayingIndex += 1
                            resetRecordingCDT()
                            generalMediaPlayerService.onDestroy()

                            if(
                                recordedPlayingIndex < recordedBedtimeStoryActivityUris[index].size
                            ){
                                if(recordedBedtimeStoryActivityUris[index][recordedPlayingIndex] == "".toUri()){
                                    recordedPlayingIndex += 1
                                    remainingPlayTime = if(recordedPlayingIndex < recordedBedtimeStoryActivityUris[index].size) {
                                        recordedBedtimeStoryActivityLengths[index][recordedPlayingIndex]
                                    }else{
                                        0
                                    }
                                    startRecordedCDT(
                                        generalMediaPlayerService,
                                        soundMediaPlayerService,
                                        index,
                                        context
                                    )
                                }else {
                                    startRecordedBedtimeStory(
                                        generalMediaPlayerService,
                                        soundMediaPlayerService,
                                        index,
                                        context
                                    )
                                }
                            }else{
                                recordedPlayingIndex = 0
                                generalMediaPlayerService.onDestroy()
                                resetRecordingCDT()
                                bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
                                activateBedtimeStoryGlobalControlButton(2)
                                deActivateBedtimeStoryGlobalControlButton(0)
                            }
                        }
                    }
                }
                if(millisUntilFinished.toInt() % 10000 == 0) {
                    Log.i(TAG, "Recorded bts timer up: $millisUntilFinished")
                }
            }

            override fun onFinish() {
                completed()
                Log.i(TAG, "Timer stopped recorded bts")
            }
        }
    }
    recordedCDT!!.start()
}

private fun startRecordedCDT(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    Log.i(TAG, "about to start cdt $remainingPlayTime")
    if(
        recordedPlayingIndex < recordedBedtimeStoryActivityUris[index].size
    ){
        startBedtimeStoryRecordedCountDownTimer(
            generalMediaPlayerService,
            soundMediaPlayerService,
            index,
            context,
            remainingPlayTime.toLong()
        ) {
            Log.i(TAG, "DONE cdt")
            //after a uri is done playing, play the next one
            recordedPlayingIndex += 1

            resetRecordingCDT()
            generalMediaPlayerService.onDestroy()

            //if all uris for this bedtime story have been played, stop media player
            if(
                recordedPlayingIndex < recordedBedtimeStoryActivityUris[index].size
            ){
                if(recordedBedtimeStoryActivityUris[index][recordedPlayingIndex] == "".toUri()){
                    recordedPlayingIndex += 1
                    remainingPlayTime = if(recordedPlayingIndex < recordedBedtimeStoryActivityUris[index].size) {
                        recordedBedtimeStoryActivityLengths[index][recordedPlayingIndex]
                    }else{
                        0
                    }
                    startRecordedCDT(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }else {
                    startRecordedBedtimeStory(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                recordedPlayingIndex = 0
                generalMediaPlayerService.onDestroy()
                resetRecordingCDT()
                bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
                activateBedtimeStoryGlobalControlButton(2)
                deActivateBedtimeStoryGlobalControlButton(0)
            }
        }
    }else{
        recordedPlayingIndex = 0
        generalMediaPlayerService.onDestroy()
        resetRecordingCDT()
        bedtimeStoryActivityPlayButtonTexts[index]!!.value = START_BEDTIME_STORY
        activateBedtimeStoryGlobalControlButton(2)
        deActivateBedtimeStoryGlobalControlButton(0)
    }
}

private fun initializeRecordedMediaPlayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    updatePreviousAndCurrentBedtimeStoryRelationship(index) {
    }
    resetRecordedCDT()
    generalMediaPlayerService.onDestroy()
    generalMediaPlayerService.setAudioUri(recordedBedtimeStoryActivityUris[index][recordedPlayingIndex])
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    Log.i(TAG, "remainingPlayTime after initialization is $remainingPlayTime")
    globalViewModel_!!.bedtimeStoryTimer.setMaxDuration(recordedBedtimeStoryActivityLengthTotals[index].last().toLong())
    resetOtherGeneralMediaPlayerUsersExceptBedtimeStory()

    remainingPlayTime = recordedBedtimeStoryActivityLengths[index][recordedPlayingIndex]
    startRecordedCDT(
        generalMediaPlayerService,
        soundMediaPlayerService,
        index,
        context
    )
    afterPlayingRecordedBedtimeStory(
        generalMediaPlayerService,
        soundMediaPlayerService,
        index,
        context
    )
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
        globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship = it
        completed(it)
    }
}

fun updatePreviousUserBedtimeStoryRelationship(
    completed: (updatedUserBedtimeStoryRelationship: UserBedtimeStoryInfoRelationship?) -> Unit
) {
    if(globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship != null){
        Log.i(TAG, "Duration of bedtime story general timer is ${globalViewModel_!!.generalPlaytimeTimer.getDuration()}")
        val playTime = globalViewModel_!!.generalPlaytimeTimer.getDuration()
        globalViewModel_!!.generalPlaytimeTimer.stop()

        Log.i(TAG, "Total bts play time = ${globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship!!.totalPlayTime}")
        Log.i(TAG, "play time = $playTime")
        val totalPlayTime = globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship!!.totalPlayTime + playTime
        Log.i(TAG, "final total play time = $totalPlayTime")

        var usagePlayTimes = globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        val numberOfTimesPlayed = globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship!!.numberOfTimesPlayed
        Log.i(TAG, "number of times played = $numberOfTimesPlayed")

        if(totalPlayTime > 0){
            val userBedtimeStoryInfoRelationship = globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship!!.copyOfBuilder()
                .numberOfTimesPlayed(numberOfTimesPlayed)
                .totalPlayTime(totalPlayTime.toInt())
                .usagePlayTimes(usagePlayTimes)
                .build()

            UserBedtimeStoryInfoRelationshipBackend.updateUserBedtimeStoryInfoRelationship(userBedtimeStoryInfoRelationship){
                globalViewModel_!!.previouslyPlayedUserBedtimeStoryRelationship = null
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

fun navigateToBedtimeStoryScreen(navController: NavController, bedtimeStoryData: BedtimeStoryInfoData){
    navController.navigate("${Screen.BedtimeStoryScreen.screen_route}/bedtimeStoryData=${BedtimeStoryObject.BedtimeStory.from(bedtimeStoryData)}")
}

private fun something(){

}