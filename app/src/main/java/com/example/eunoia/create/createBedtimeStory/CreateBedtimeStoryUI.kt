package com.example.eunoia.create.createBedtimeStory

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ResetTv
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.R
import com.example.eunoia.backend.PageBackend
import com.example.eunoia.create.createSound.*
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.CustomizableLRButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.components.WrappedPurpleBackgroundStart
import com.example.eunoia.ui.theme.BeautyBush
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.ui.theme.White
import java.io.File

@Composable
fun ChapterBlock(
    navController: NavController,
    chapterData: BedtimeStoryInfoChapterData,
    index: Int
){
    var numberOfPages by rememberSaveable { mutableStateOf(false) }
    var pages = mutableListOf<PageData>()
    PageBackend.queryPageBasedOnChapter(chapterData){
        pages = it.toMutableList()
        numberOfPages = true
    }

    if(numberOfPages) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    navigateToBedtimeStoryChapterScreen(navController, chapterData, index)
                }
        ) {
            val pagesText = if (pages.size == 1) "page" else "pages"
            WrappedPurpleBackgroundStart(
                chapterData.displayName,
                "${pages.size} $pagesText"
            ) {
            }
        }
    }
}

@Composable
fun PageBlock(
    navController: NavController,
    pageData: PageData,
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
            navigateToPageScreen(navController, pageData, index)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun RecordingBlock(recording: MutableMap.MutableEntry<MutableState<String>, MutableState<String>>){

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToResetBedtimeStoryUI(
    context: Context,
    fileUploaded: () -> Unit
){
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmStateChange = {
            Log.i(TAG, "Confirm state change ==>> $it")
            if(it == DismissValue.DismissedToStart){
                resetBedtimeStoryUploadUI()
            }
            true
        }
    )
    if (dismissState.currentValue != DismissValue.Default) {
        LaunchedEffect(Unit) {
            dismissState.reset()
        }
    }
    SwipeToDismiss(
        state = dismissState,
        /***  create dismiss alert Background */
        background = {
            val color by animateColorAsState(
                targetValue = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> Color.Transparent
                    DismissDirection.EndToStart -> Color.Red
                    null -> Color.Transparent
                }
            )
            val scale by animateFloatAsState(
                targetValue = if(dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f
            )
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            if (direction == DismissDirection.EndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ResetTv,
                            contentDescription = "reset",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .scale(scale)
                        )
                        NormalText(
                            text = "Reset",
                            color = White,
                            fontSize = 13,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
        },
        /**** Dismiss Content */
        dismissContent = {
            CustomizableButton(
                text = uploadedFileBedtimeStory.value.name,
                height = 55,
                fontSize = 16,
                textColor = Black,
                backgroundColor = fileColorBedtimeStory.value,
                corner = 10,
                borderStroke = 0.0,
                borderColor = Black.copy(alpha = 0F),
                textType = "light",
                maxWidthFraction = 1F
            ) {
                if(uploadedFileBedtimeStory.value.name == "Choose a file"){
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fileUploaded()
                    } else {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            3
                        )
                    }
                }else{
                    if(fileMediaPlayerBedtimeStory.value.isPlaying){
                        fileMediaPlayerBedtimeStory.value.stop()
                        fileMediaPlayerBedtimeStory.value.reset()
                    }else {
                        fileMediaPlayerBedtimeStory.value.apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            setDataSource(context, fileUriBedtimeStory.value)
                            setVolume(1f, 1f)
                            prepare()
                            start()
                        }
                    }
                }
            }
        },
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
        },
        /*** Set Direction to dismiss */
        directions = setOf(DismissDirection.EndToStart),
    )
}

fun resetBedtimeStoryUploadUI() {
    uploadedFileBedtimeStory.value = File("Choose a file")
    fileColorBedtimeStory.value = WePeep
    resetUploadBedtimeStoryMediaPlayers()
    fileMediaPlayerBedtimeStory.value = MediaPlayer()
    fileUriBedtimeStory.value = "".toUri()
    audioFileLengthMilliSecondsBedtimeStory.value = 0L
}