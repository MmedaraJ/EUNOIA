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
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.ChapterPageData
import com.example.eunoia.backend.ChapterPageBackend
import com.example.eunoia.models.BedtimeStoryChapterObject
import com.example.eunoia.models.ChapterPageObject
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.White
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

var chapterPages = mutableListOf<MutableList<MutableState<ChapterPageData>?>>()
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
    if(chapterIndex >= chapterPages.size){
        for(i in 0..chapterIndex){
            chapterPages.add(mutableListOf())
        }
    }
    var numberOfPages by rememberSaveable { mutableStateOf(chapterPages[chapterIndex].size) }
    ChapterPageBackend.queryChapterPageBasedOnChapter(bedtimeStoryChapterData) {
        for (i in chapterPages[chapterIndex].size until it.size) {
            chapterPages[chapterIndex].add(mutableStateOf(it[i]))
        }
        numberOfPages = chapterPages[chapterIndex].size
    }
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxHeight(),
    ) {
        val (
            header,
            title,
            save1,
            save2,
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
                globalViewModel_!!.bottomSheetOpenFor = "recordAudio"
                openBottomSheet(scope, state)
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
                Log.i(TAG, "Chapter ==>> $bedtimeStoryChapterData")
                val page = ChapterPageObject.ChapterPage(
                    UUID.randomUUID().toString(),
                    "Page ${chapterPages[chapterIndex].size + 1}",
                    chapterPages[chapterIndex].size + 1,
                    listOf(),
                    listOf(),
                    BedtimeStoryChapterObject.BedtimeStoryChapter.from(bedtimeStoryChapterData),
                )
                ChapterPageBackend.createChapterPage(page){
                    chapterPages[chapterIndex].add(mutableStateOf(it))
                    numberOfPages ++
                    Log.i(TAG, "Owner bts username ==>> ${it.bedtimeStoryInfoChapter.bedtimeStoryInfo.bedtimeStoryOwner}")
                }
                Thread.sleep(1_000)
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .constrainAs(all_pages) {
                    top.linkTo(save1.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
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
                    for(i in chapterPages[chapterIndex].indices){
                        PageBlock(navController, chapterPages[chapterIndex][i]!!.value, i)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(spacer) {
                        top.linkTo(pages.bottom, margin = 60.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

fun clearChapterPagesList(){
    chapterPages.clear()
}

fun navigateToChapterPageScreen(navController: NavController, chapterPageData: ChapterPageData, pageIndex: Int){
    navController.navigate("${Screen.ChapterPageScreen.screen_route}/chapterPage=${ChapterPageObject.ChapterPage.from(chapterPageData)}/$pageIndex")
}