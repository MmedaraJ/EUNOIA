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
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.backend.PageBackend
import com.example.eunoia.models.PageObject
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

var chapterPages = mutableListOf<MutableList<MutableState<PageData>?>>()
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
        for(i in chapterPages.size..chapterIndex){
            chapterPages.add(mutableListOf())
        }
    }

    var numberOfPages by rememberSaveable { mutableStateOf(chapterPages[chapterIndex].size) }

    PageBackend.queryPageBasedOnChapter(bedtimeStoryChapterData) {
        chapterPages[chapterIndex].clear()
        Log.i(TAG, "Received chpter pages ${it.size}")
        for (i in it.indices) {
            chapterPages[chapterIndex].add(mutableStateOf(it[i]))
        }
        numberOfPages = chapterPages[chapterIndex].size
    }
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp),
            //.fillMaxHeight(),
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
                Log.i(TAG, "Chapter id isz ${bedtimeStoryChapterData.id}")
                val page = PageObject.Page(
                    UUID.randomUUID().toString(),
                    "Page ${chapterPages[chapterIndex].size + 1}",
                    chapterPages[chapterIndex].size + 1,
                    listOf(),
                    listOf(),
                    listOf(),
                    bedtimeStoryChapterData.id
                )
                PageBackend.createPage(page){
                    chapterPages[chapterIndex].add(mutableStateOf(it))
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
                        for(i in chapterPages[chapterIndex].indices){
                            Log.i(TAG, "Page index before send is ${chapterPages[chapterIndex][i]!!.value.pageNumber}")
                            PageBlock(navController, chapterPages[chapterIndex][i]!!.value, chapterPages[chapterIndex][i]!!.value.pageNumber - 1)
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

fun clearPagesList(){
    chapterPages.clear()
}

fun navigateToPageScreen(navController: NavController, pageData: PageData, pageIndex: Int){
    navController.navigate("${Screen.PageScreen.screen_route}/chapterPage=${PageObject.Page.from(pageData)}/$pageIndex")
}