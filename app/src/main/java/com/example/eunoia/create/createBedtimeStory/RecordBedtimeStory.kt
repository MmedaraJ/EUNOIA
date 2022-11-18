package com.example.eunoia.create.createBedtimeStory

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryCreationStatus
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.models.BedtimeStoryChapterObject
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.openConfirmDeleteBedtimeStoryDialogBox
import com.example.eunoia.ui.navigation.openSavedElementDialogBox
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.getRandomString
import com.example.eunoia.utils.retrieveUriDuration
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.util.*

var selectedIndex by mutableStateOf(0)
var bedtimeStoryChapters = mutableListOf<BedtimeStoryInfoChapterData?>()
private var TAG = "Record Bedtime Story"

private var icons = arrayOf(
    mutableStateOf(R.drawable.ic_baseline_delete),
    mutableStateOf(R.drawable.ic_baseline_check),
    mutableStateOf(R.drawable.ic_baseline_add),
)
private var borderControlColors = arrayOf(
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
private var backgroundControlColor1 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)
private var backgroundControlColor2 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordBedtimeStoryUI(
    navController: NavController,
    bedtimeStoryData: BedtimeStoryInfoData,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    val context = LocalContext.current

    resetEverything(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    ){}

    var numberOfChapters by rememberSaveable { mutableStateOf(bedtimeStoryChapters.size) }
    BedtimeStoryChapterBackend.queryBedtimeStoryChapterBasedOnBedtimeStory(bedtimeStoryData) {
        val itMutable = it.toMutableList()
        itMutable.sortBy { chapterListItem ->
            chapterListItem.chapterNumber
        }
        for (i in bedtimeStoryChapters.size until itMutable.size) {
            bedtimeStoryChapters.add(itMutable[i])
        }
        numberOfChapters = bedtimeStoryChapters.size
    }

    val scrollState = rememberScrollState()

    SetUpConfirmDeleteBedtimeStoryDialogBoxUI(
        bedtimeStoryChapters,
        bedtimeStoryData,
        navController
    )

    SetUpSavedBedtimeStoryDialogBoxUI()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        val (
            header,
            title,
            delete,
            save1,
            chapter_column,
            controls,
            all_chapters,
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
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
                .constrainAs(title) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = bedtimeStoryData.displayName,
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(controls) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ){
            icons.forEachIndexed { index, icon ->
                if(
                    index == 0 ||
                    (index == 1 && numberOfChapters > 0) ||
                    index == 2
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .gradientBackground(
                                listOf(
                                    backgroundControlColor1[index].value,
                                    backgroundControlColor2[index].value
                                ),
                                angle = 45f
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    borderControlColors[index].value
                                ),
                                RoundedCornerShape(50.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnImageWithColor(
                            icon.value,
                            "icon",
                            borderControlColors[index].value,
                            16.dp,
                            16.dp,
                            0,
                            0
                        ) {
                            when (index) {
                                0 -> {
                                    openConfirmDeleteBedtimeStoryDialogBox = true
                                }
                                1 -> {
                                    completeBedtimeStory(
                                        bedtimeStoryData,
                                        context
                                    ) { updatedBedtimeStoryData ->
                                        userBedtimeStories.removeIf {
                                            it!!.id == updatedBedtimeStoryData.id
                                        }
                                        openSavedElementDialogBox = true
                                        Thread.sleep(1_000)
                                        navController.popBackStack()
                                    }
                                }
                                2 -> {
                                    val chapter = BedtimeStoryChapterObject.BedtimeStoryChapter(
                                        UUID.randomUUID().toString(),
                                        "Chapter ${bedtimeStoryChapters.size + 1}",
                                        bedtimeStoryChapters.size + 1,
                                        BedtimeStoryObject.BedtimeStory.from(bedtimeStoryData),
                                        bedtimeStoryData.id
                                    )
                                    BedtimeStoryChapterBackend.createBedtimeStoryInfoChapter(chapter) {
                                        bedtimeStoryChapters.add(it)
                                        numberOfChapters++
                                    }
                                    Thread.sleep(1_000)
                                }
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(chapter_column) {
                    top.linkTo(controls.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    //.fillMaxHeight(0.6f)
                    .verticalScroll(scrollState),
            ) {
                val (
                    chapters,
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(chapters) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                        .fillMaxWidth()
                ) {
                    if(numberOfChapters > 0){
                        for(i in bedtimeStoryChapters.indices){
                            ChapterBlock(
                                navController,
                                bedtimeStoryChapters[i]!!,
                                bedtimeStoryChapters[i]!!.chapterNumber,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

fun completeBedtimeStory(
    bedtimeStoryData: BedtimeStoryInfoData,
    context: Context,
    completed: (bedtimeStoryData: BedtimeStoryInfoData) -> Unit
) {
    val outFile = File(context.externalCacheDir!!.absolutePath + "/${getRandomString(5)}_audio.aac")
    getAllUris(bedtimeStoryData){
        concatenateAllUris(outFile, context) {
            val key = "Routine/" +
                    "BedtimeStories/" +
                    "${globalViewModel_!!.currentUser!!.username}/" +
                    "recorded/" +
                    "${bedtimeStoryData.displayName}_complete/" +
                    "${bedtimeStoryData.displayName}_audio.aac"

            val fullPlayTime = retrieveUriDuration(
                outFile.path,
                context
            )

            SoundBackend.storeAudio(
                outFile.absolutePath,
                key
            ) { s3Key ->
                val newBedtimeStory = bedtimeStoryData.copyOfBuilder()
                    .audioKeyS3(s3Key)
                    .fullPlayTime(fullPlayTime)
                    .creationStatus(BedtimeStoryCreationStatus.COMPLETED)
                    .build()

                BedtimeStoryBackend.updateBedtimeStory(newBedtimeStory) {
                    completed(it)
                }
            }
        }
    }
}

fun concatenateAllUris(
    outFile: File,
    context: Context,
    completed: () -> Unit
) {
    var inputStr = ""
    var concatStr = ""

    for(i in allUriTriples.indices){
        inputStr += "-i ${allUriTriples[i].uri.path} "
        concatStr += "[$i:a]"
    }

    Log.i(TAG, "Output file before: 0. ${outFile.length()}")

    val rc = FFmpeg.execute("$inputStr-filter_complex \"${concatStr}concat=n=${allUriTriples.size}:v=0:a=1\" -y ${outFile.absolutePath}")

    when (rc) {
        RETURN_CODE_SUCCESS -> {
            Log.i(Config.TAG, "Command execution completed successfully.")
            val len = retrieveUriDuration(
                outFile.path,
                context
            )
            Log.i(TAG, "Output file done: 1. $len")
            completed()
        }
        RETURN_CODE_CANCEL -> {
            Log.i(Config.TAG, "Command execution cancelled by user.")
        }
        else -> {
            Log.i(
                Config.TAG,
                String.format("Command execution failed with rc=$rc and the output below.", rc)
            )
            Config.printLastCommandOutput(Log.INFO)
        }
    }
}

val allUriTriples = mutableListOf<AllUriTriples>()

fun getAllUris(
    bedtimeStoryData: BedtimeStoryInfoData,
    completed: () -> Unit
) {
    allUriTriples.clear()
    BedtimeStoryChapterBackend.queryBedtimeStoryChapterBasedOnBedtimeStory(
        bedtimeStoryData
    ){ chapters ->
        if(chapters.isNotEmpty()){
            chapters.forEachIndexed{ chapterIndex, chapter ->
                PageBackend.queryPageBasedOnChapter(
                    chapter
                ){ pages ->
                    if(pages.isNotEmpty()) {
                        pages.forEachIndexed{ pageIndex, page ->
                            page.audioKeysS3.forEachIndexed { i, s3Key ->
                                val triple = AllUriTriples(
                                    chapterIndex,
                                    pageIndex,
                                    "".toUri()
                                )
                                allUriTriples.add(triple)
                                retrieveCorrespondingUri(
                                    s3Key,
                                    allUriTriples.size - 1,
                                ){
                                    completed()
                                }
                            }
                        }
                    }
                }
            }
        }else{
            completed()
        }
    }
}

private fun retrieveCorrespondingUri(
    s3Key: String?,
    i: Int,
    completed: () -> Unit
) {
    SoundBackend.retrieveAudio(
        s3Key!!,
        globalViewModel_!!.currentUser!!.amplifyAuthUserId
    ) {
        if (it != null) {
            allUriTriples[i].uri = it

            if(
                allUriTriples.none{ triple ->
                    triple.uri == "".toUri()
                }
            ){
                allUriTriples.sortWith(
                    compareBy(
                        { triple ->
                            triple.chapterIndex
                        },
                        { triple ->
                            triple.pageIndex
                        }
                    )
                )
                Log.i(TAG, "All uris seize is = ${allUriTriples.size}")
                completed()
            }
        }
    }
}

data class AllUriTriples(
    var chapterIndex: Int,
    var pageIndex: Int,
    var uri: Uri
)

private fun deleteAllPagesAndChapters(
    bedtimeStoryChapters: MutableList<BedtimeStoryInfoChapterData?>,
    completed: () -> Unit
) {
    bedtimeStoryChapters.forEach { chapter ->
        PageBackend.queryPageBasedOnChapter(chapter!!) {
            if(it.isNotEmpty()){
                it.forEach { page ->
                    page.audioKeysS3.forEach { s3Key ->
                        SoundBackend.deleteAudio(s3Key)
                    }
                    PageBackend.deletePage(page){ }
                }
            }
            deleteChapter(chapter)
        }
    }
    completed()
}

private fun deleteChapter(
    chapter: BedtimeStoryInfoChapterData,
) {
    BedtimeStoryChapterBackend.deleteChapter(chapter){}
}

private fun deleteUserBedtimeStory(
    bedtimeStoryData: BedtimeStoryInfoData,
    completed: () -> Unit
) {
    UserBedtimeStoryBackend.queryUserBedtimeStoryBasedOnUserAndBedtimeStory(
        globalViewModel_!!.currentUser!!,
        bedtimeStoryData
    ){
        if(it.isNotEmpty()){
            UserBedtimeStoryBackend.deleteUserBedtimeStory(it[0]!!){
                completed()
            }
        }else{
            completed()
        }
    }
}

private fun deleteUserBedtimeStoryRelationship(
    bedtimeStoryData: BedtimeStoryInfoData,
    completed: () -> Unit
) {
    UserBedtimeStoryInfoRelationshipBackend.queryUserBedtimeStoryInfoRelationshipBasedOnUserAndBedtimeStoryInfo(
        globalViewModel_!!.currentUser!!,
        bedtimeStoryData
    ){
        if(it.isNotEmpty()){
            UserBedtimeStoryInfoRelationshipBackend.deleteUserBedtimeStoryInfoRelationship(it[0]!!){
                completed()
            }
        }else{
            completed()
        }
    }
}

private fun deleteBedtimeStory(
    bedtimeStoryData: BedtimeStoryInfoData,
    completed: () -> Unit
) {
    BedtimeStoryBackend.deleteBedtimeStory(bedtimeStoryData){
        completed()
    }
}

@Composable
private fun SetUpConfirmDeleteBedtimeStoryDialogBoxUI(
    bedtimeStoryChapters: MutableList<BedtimeStoryInfoChapterData?>,
    bedtimeStoryData: BedtimeStoryInfoData,
    navController: NavController,
){
    if(openConfirmDeleteBedtimeStoryDialogBox){
        ConfirmAlertDialog(
            "Are you sure you want to delete this bedtime story?",
            {
                processBedtimeStoryDeletion(
                    bedtimeStoryChapters,
                    bedtimeStoryData,
                    navController
                )
            },
            {
                openConfirmDeleteBedtimeStoryDialogBox = false
            }
        )
    }
}

@Composable
fun SetUpSavedBedtimeStoryDialogBoxUI(){
    if(openSavedElementDialogBox){
        AlertDialogBox(text = "Bedtime story saved. We will send you an email when it is approved")
    }
}

fun processBedtimeStoryDeletion(
    bedtimeStoryChapters: MutableList<BedtimeStoryInfoChapterData?>,
    bedtimeStoryData: BedtimeStoryInfoData,
    navController: NavController
) {
    deleteAllPagesAndChapters(bedtimeStoryChapters){
        deleteUserBedtimeStoryRelationship(bedtimeStoryData) {
            deleteUserBedtimeStory(bedtimeStoryData) {
                deleteBedtimeStory(bedtimeStoryData) {
                    openConfirmDeleteBedtimeStoryDialogBox = false
                    userBedtimeStories.removeIf {
                        it!!.id == bedtimeStoryData.id
                    }
                    navController.popBackStack()
                }
            }
        }
    }
}

fun navigateToBedtimeStoryChapterScreen(navController: NavController, chapterData: BedtimeStoryInfoChapterData, chapterIndex: Int){
    navController.navigate("${Screen.BedtimeStoryChapterScreen.screen_route}/bedtimeStoryChapter=${BedtimeStoryChapterObject.BedtimeStoryChapter.from(chapterData)}/$chapterIndex")
}

fun clearBedtimeStoryChaptersList(){
    bedtimeStoryChapters.clear()
}

fun resetRecordBedtimeStoryVariables(){
    selectedIndex = 0
}


@OptIn(ExperimentalMaterialApi::class)
@Preview(
    showBackground = true,
    name = "Light mode"
)
@Preview(
    showBackground = true,
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun RecordBedtimeStoryPreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        /*RecordBedtimeStoryUI(
            rememberNavController(),
            globalViewModel,
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )*/
    }
}