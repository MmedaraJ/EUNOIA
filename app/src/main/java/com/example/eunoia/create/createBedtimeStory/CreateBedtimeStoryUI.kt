package com.example.eunoia.create.createBedtimeStory

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.ChapterPageData
import com.example.eunoia.R
import com.example.eunoia.ui.components.CustomizableLRButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.components.WrappedPurpleBackgroundStart
import com.example.eunoia.ui.theme.BeautyBush
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.WePeep

@Composable
fun Or(){
    ConstraintLayout {
        val (
            line_1,
            text,
            line_2
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(line_1){
                    top.linkTo(text.top, margin = 0.dp)
                    end.linkTo(text.start, margin = 3.dp)
                    bottom.linkTo(text.bottom, margin = 0.dp)
                }
        ) {
            Canvas(modifier = Modifier){
                val canvasWidth = size.width
                drawLine(
                    start = Offset(x = canvasWidth, y = 0f),
                    end = Offset(x = canvasWidth - 140f, y = 0f),
                    color = Black,
                    strokeWidth = 1F
                )
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(text){
                    top.linkTo(parent.top, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = "or",
                color = Black,
                fontSize = 12,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(line_2){
                    top.linkTo(text.top, margin = 0.dp)
                    start.linkTo(text.end, margin = 3.dp)
                    bottom.linkTo(text.bottom, margin = 0.dp)
                }
        ) {
            Canvas(modifier = Modifier){
                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = 140f, y = 0f),
                    color = Black,
                    strokeWidth = 1F
                )
            }
        }
    }
}

@Composable
fun ChapterBlock(
    navController: NavController,
    chapterData: BedtimeStoryInfoChapterData,
    index: Int
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                navigateToBedtimeStoryChapterScreen(navController, chapterData, index)
            }
    ) {
        val pagesText = if(chapterData.pages.size == 1) "page" else "pages"
        WrappedPurpleBackgroundStart(
            chapterData.displayName,
            "${chapterData.pages.size} $pagesText"
        ) {
        }
    }
}

@Composable
fun PageBlock(
    navController: NavController,
    pageData: ChapterPageData,
    index: Int
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {

            }
    ) {
        CustomizableLRButton(
            text = pageData.displayName,
            height = 55,
            fontSize = 16,
            textColor = Black,
            backgroundColor = WePeep,
            corner = 10,
            borderStroke = 0.0,
            borderColor = Black.copy(alpha = 0F),
            textType = "light",
            maxWidthFraction = 1F,
            R.drawable.little_right_arrow,
            8,
            14,
            BeautyBush
        ) {
            navigateToChapterPageScreen(navController, pageData, index)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun RecordingBlock(recording: MutableMap.MutableEntry<MutableState<String>, MutableState<String>>){

}