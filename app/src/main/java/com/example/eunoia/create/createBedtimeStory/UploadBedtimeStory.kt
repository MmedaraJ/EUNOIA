package com.example.eunoia.create.createBedtimeStory

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amplifyframework.analytics.AnalyticsEvent
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.BedtimeStoryBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserBedtimeStoryBackend
import com.example.eunoia.backend.UserBedtimeStoryInfoRelationshipBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.util.*

var uploadedFileBedtimeStory = mutableStateOf(File("Choose a file"))
var fileColorBedtimeStory = mutableStateOf(WePeep)
var fileMediaPlayerBedtimeStory = mutableStateOf(MediaPlayer())
var fileUriBedtimeStory = mutableStateOf("".toUri())
var audioFileLengthMilliSecondsBedtimeStory = mutableStateOf(0L)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadBedtimeStoryUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    resetEverything(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    ){}

    if(openSavedElementDialogBox){
        AlertDialogBox(text = "Bedtime Story Saved. We will send you an email when it is approved")
    }
    if(openBedtimeStoryNameTakenDialogBox){
        AlertDialogBox(text = "The name '$bedtimeStoryName' already exists")
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        val (
            header,
            title,
            upload,
            next
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
                    resetBedtimeStoryUploadUI()
                    navController.popBackStack()
                },
                {
                    com.example.eunoia.ui.navigation.globalViewModel!!.bottomSheetOpenFor = "controls"
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
                text = "Upload Bedtime Story",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(upload) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            SwipeToResetBedtimeStoryUI(context){
                UserDashboardActivity.getInstanceActivity().selectAudioBedtimeStory()
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(next) {
                    top.linkTo(upload.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            if(uploadedFileBedtimeStory.value.name != "Choose a file") {
                CustomizableButton(
                    text = "create",
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
                    createBedtimeStoryFromUpload(navController)
                }
            }
        }
    }
}

fun resetUploadBedtimeStoryMediaPlayers(){
    if(fileMediaPlayerBedtimeStory.value.isPlaying){
        fileMediaPlayerBedtimeStory.value.stop()
    }
    fileMediaPlayerBedtimeStory.value.reset()
    fileMediaPlayerBedtimeStory.value.release()
}

fun createBedtimeStoryFromUpload(
    navController: NavController
){
    BedtimeStoryBackend.queryBedtimeStoryBasedOnDisplayName(bedtimeStoryName){
        if (it.isEmpty()) {
            val tags = getBedtimeStoryTagsList()
            val key = "${globalViewModel!!.currentUser!!.username.lowercase()}/" +
                    "routine/" +
                    "bedtime-story/" +
                    "uploaded/" +
                    "${bedtimeStoryName.lowercase()}/" +
                    "${bedtimeStoryName.lowercase()}_audio.aac"

            SoundBackend.storeAudio(uploadedFileBedtimeStory.value.absolutePath, key){
                val bedtimeStory = BedtimeStoryObject.BedtimeStory(
                    UUID.randomUUID().toString(),
                    UserObject.User.from(globalViewModel!!.currentUser!!),
                    globalViewModel!!.currentUser!!.id,
                    bedtimeStoryName,
                    bedtimeStoryShortDescription,
                    bedtimeStoryLongDescription,
                    key,
                    bedtimeStoryIcon,
                    audioFileLengthMilliSecondsBedtimeStory.value,
                    false,
                    tags,
                    BedtimeStoryAudioSource.UPLOADED,
                    BedtimeStoryApprovalStatus.PENDING,
                    BedtimeStoryCreationStatus.COMPLETED
                )

                BedtimeStoryBackend.createBedtimeStory(bedtimeStory) { bedtimeStoryData ->
                    UserBedtimeStoryInfoRelationshipBackend.createUserBedtimeStoryInfoRelationshipObject(bedtimeStoryData) {
                        UserBedtimeStoryBackend.createUserBedtimeStoryObject(bedtimeStoryData) {
                            resetAllBedtimeStoryCreationObjects()
                            openSavedElementDialogBox = true
                            Thread.sleep(1_000)
                            openSavedElementDialogBox = false
                            recordBedtimeStoryCreatedViaUploadPinpointEvent(bedtimeStoryData)
                            runOnUiThread {
                                //TODO navigate to users list of pending bedtime stories
                                navigateToBedtimeStoryScreen(navController, bedtimeStoryData)
                            }
                        }
                    }
                }
            }
        }else{
            openBedtimeStoryNameTakenDialogBox = true
        }
    }
}

fun recordBedtimeStoryCreatedViaUploadPinpointEvent(bedtimeStoryData: BedtimeStoryInfoData) {
    val event = AnalyticsEvent.builder()
        .name("BedtimeStoryCreatedViaUpload")
        .addProperty("UserName", bedtimeStoryData.bedtimeStoryOwner.username)
        .addProperty("Successful", true)
        .addProperty("ProcessDuration", 18000)
        .addProperty("BedtimeStoryName", bedtimeStoryData.displayName)
        .build()

    Amplify.Analytics.recordEvent(event)
}

fun resetAllBedtimeStoryCreationObjects(){
    resetNameBedtimeStoryVariables()
    resetIncompleteBedtimeStoriesVariables()
    clearPageRecordingsList()
    resetRecordBedtimeStoryVariables()
    clearBedtimeStoryChaptersList()
    resetBedtimeStoryUploadUI()
}

fun navigateToBedtimeStoryScreen(navController: NavController, bedtimeStoryInfoData: BedtimeStoryInfoData){
    navController.navigate("${Screen.BedtimeStoryScreen.screen_route}/bedtimeStoryData=${BedtimeStoryObject.BedtimeStory.from(bedtimeStoryInfoData)}")
}