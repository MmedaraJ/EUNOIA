package com.example.eunoia.create.createBedtimeStory

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.backend.BedtimeStoryChapterBackend
import com.example.eunoia.models.BedtimeStoryChapterObject
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

var selectedIndex by mutableStateOf(0)
var pageNumbersPerChapter = mutableListOf<MutableState<Int>?>()
private var bedtimeStoryChapters = mutableListOf<MutableState<BedtimeStoryInfoChapterData>?>()
private var TAG = "Record Bedtime Story"
var fromBack = mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordBedtimeStoryUI(
    navController: NavController,
    bedtimeStoryData: BedtimeStoryInfoData,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    var numberOfChapters by rememberSaveable { mutableStateOf(bedtimeStoryChapters.size) }
    BedtimeStoryChapterBackend.queryBedtimeStoryChapterBasedOnBedtimeStory(bedtimeStoryData) {
        for (i in bedtimeStoryChapters.size until it.size) {
            bedtimeStoryChapters.add(mutableStateOf(it[i]))
        }
        numberOfChapters = bedtimeStoryChapters.size
    }
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        val (
            header,
            title,
            delete,
            save1,
            save2,
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
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(save1) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.3F)
        ) {
            CustomizableButton(
                text = "new chapter",
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
                val chapter = BedtimeStoryChapterObject.BedtimeStoryChapter(
                    UUID.randomUUID().toString(),
                    "Chapter ${bedtimeStoryChapters.size + 1}",
                    bedtimeStoryChapters.size + 1,
                    BedtimeStoryObject.BedtimeStory.from(bedtimeStoryData),
                )
                BedtimeStoryChapterBackend.createBedtimeStoryInfoChapter(chapter){
                    bedtimeStoryChapters.add(mutableStateOf(it))
                    numberOfChapters ++
                }
                Thread.sleep(1_000)
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .constrainAs(all_chapters) {
                    top.linkTo(save1.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .padding(horizontal = 0.dp)
                .verticalScroll(scrollState),
        ) {
            val (
                chapters,
                spacer,
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
                        ChapterBlock(navController, bedtimeStoryChapters[i]!!.value, i)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(spacer) {
                        top.linkTo(chapters.bottom, margin = 60.dp)
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

fun navigateToBedtimeStoryChapterScreen(navController: NavController, chapterData: BedtimeStoryInfoChapterData, chapterIndex: Int){
    navController.navigate("${Screen.BedtimeStoryChapterScreen.screen_route}/bedtimeStoryChapter=${BedtimeStoryChapterObject.BedtimeStoryChapter.from(chapterData)}/$chapterIndex")
}

fun clearBedtimeStoryChaptersList(){
    bedtimeStoryChapters.clear()
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