package com.example.eunoia.create.createBedtimeStory

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.backend.BedtimeStoryBackend
import com.example.eunoia.backend.BedtimeStoryChapterBackend
import com.example.eunoia.backend.PageBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.lifecycle.CustomLifecycleEventListener
import com.example.eunoia.models.BedtimeStoryChapterObject
import com.example.eunoia.models.PageObject
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.openConfirmDeleteBedtimeStoryDialogBox
import com.example.eunoia.ui.navigation.openConfirmDeleteChapterDialogBox
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.White
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

var chapterPages = mutableListOf<PageData?>()
private var TAG = "Chapter Pages"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BedtimeStoryChapterScreenUI(
    navController: NavController,
    bedtimeStoryChapterData: BedtimeStoryInfoChapterData,
    chapterIndex: Int,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    var numberOfPages by rememberSaveable { mutableStateOf(0) }

    PageBackend.queryPageBasedOnChapter(bedtimeStoryChapterData) {
        chapterPages.clear()
        Log.i(TAG, "Received chpter pages the third time ${it.size}")
        it.toMutableList().sortedBy { pageData ->
            pageData.pageNumber
        }
        for (page in it) {
            chapterPages.add(page)
        }
        Log.i(TAG, "chapterPages.size ${chapterPages.size}")
        numberOfPages = chapterPages.size
    }

    SetUpConfirmDeleteChapterDialogBoxUI(
        chapterPages,
        bedtimeStoryChapterData,
        navController
    )

    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        val (
            header,
            title,
            save1,
            page_column,
            all_pages,
            delete,
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
                text = bedtimeStoryChapterData.displayName,
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(delete) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            CustomizableButton(
                text = "delete",
                height = 35,
                fontSize = 15,
                textColor = White,
                backgroundColor = Black,
                corner = 50,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "morge",
                maxWidthFraction = 1F
            ) {
                //delete chapter
                openConfirmDeleteChapterDialogBox = true
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(save1) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            CustomizableButton(
                text = "new page",
                height = 35,
                fontSize = 15,
                textColor = Black,
                backgroundColor = SwansDown,
                corner = 50,
                borderStroke = 0.0,
                borderColor = SwansDown.copy(alpha = 0F),
                textType = "morge",
                maxWidthFraction = 1F
            ) {
                Log.i(TAG, "Chapter id isz ${bedtimeStoryChapterData.id}")
                val page = PageObject.Page(
                    UUID.randomUUID().toString(),
                    "Page ${chapterPages.size + 1}",
                    chapterPages.size + 1,
                    listOf(),
                    listOf(),
                    listOf(),
                    bedtimeStoryChapterData.id
                )
                PageBackend.createPage(page){
                    chapterPages.add(it)
                    numberOfPages ++
                }
                Thread.sleep(1_000)
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(page_column) {
                    top.linkTo(save1.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .verticalScroll(scrollState),
            ) {
                val (
                    pages,
                    spacer,
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(pages) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                        .fillMaxWidth()
                ) {
                    if(numberOfPages > 0){
                        for(i in chapterPages.indices){
                            Log.i(TAG, "Page index before send is ${chapterPages[i]!!.pageNumber}")
                            PageBlock(
                                navController,
                                chapterPages[i]!!,
                                chapterPages[i]!!.pageNumber - 1,
                                bedtimeStoryChapterData,
                                chapterIndex
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun SetUpConfirmDeleteChapterDialogBoxUI(
    pages: MutableList<PageData?>,
    bedtimeStoryChapterData: BedtimeStoryInfoChapterData,
    navController: NavController
) {
    if(openConfirmDeleteChapterDialogBox){
        ConfirmAlertDialog(
            "Are you sure you want to delete this chapter?",
            {
                processChapterDeletion(
                    pages,
                    bedtimeStoryChapterData,
                    navController
                )
            },
            {
                openConfirmDeleteChapterDialogBox = false
            }
        )
    }
}

fun processChapterDeletion(
    pages: MutableList<PageData?>,
    bedtimeStoryChapterData: BedtimeStoryInfoChapterData,
    navController: NavController
) {
    deleteAllPages(pages){
        deleteChapter(bedtimeStoryChapterData) {
            openConfirmDeleteChapterDialogBox = false
            bedtimeStoryChapters.removeIf {
                it!!.id == bedtimeStoryChapterData.id
            }

            //update chapter names. chapter 2 becomes chapter 1
            for(i in bedtimeStoryChapterData.chapterNumber..bedtimeStoryChapters.size){
                val newChapter = bedtimeStoryChapters[i - 1]!!.copyOfBuilder()
                    .displayName("Chapter $i")
                    .chapterNumber(i)
                    .build()

                BedtimeStoryChapterBackend.updateChapter(newChapter){
                    bedtimeStoryChapters[i - 1] = it
                    if(i == bedtimeStoryChapters.size){
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

fun deleteAllPages(
    pages: MutableList<PageData?>,
    completed: () -> Unit
) {
    if (pages.isNotEmpty()) {
        pages.forEach { page ->
            page!!.audioKeysS3.forEach { s3Key ->
                SoundBackend.deleteAudio(s3Key)
            }
            PageBackend.deletePage(page) { }
        }
    }
    completed()
}

private fun deleteChapter(
    chapter: BedtimeStoryInfoChapterData,
    completed: () -> Unit
) {
    BedtimeStoryChapterBackend.deleteChapter(chapter){
        completed()
    }
}

fun updateChapterIndexList(
    newPage: PageData,
    chapterIndex: Int,
){
    if(chapterIndex < chapterPages.size) {
        for (i in chapterPages.indices) {
            if (chapterPages[i]!!.id == newPage.id) {
                Log.i(TAG, "Old page ==> ${chapterPages[i]!!}")
                chapterPages[i] = newPage
                Log.i(TAG, "new page ==> ${chapterPages[i]!!}")
                break
            }
        }
    }
}

fun getChapterPageSize(
    chapterIndex: Int,
): Int{
    return chapterPages.size
}

fun navigateToPageScreen(
    navController: NavController,
    pageData: PageData,
    chapterData: BedtimeStoryInfoChapterData,
    chapterIndex: Int
){
    navController.navigate(
        "${Screen.PageScreen.screen_route}/" +
                "chapterPage=${PageObject.Page.from(pageData)}/" +
                "chapterData=${BedtimeStoryChapterObject.BedtimeStoryChapter.from(chapterData)}/" +
                "$chapterIndex"
    )
}