package com.example.eunoia.create.createSelfLove

import android.content.Context
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
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.SelfLoveBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.create.resetEverything
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.recordAudio.deleteRecordingFile
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingFile
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.util.*

var recordedFileSelfLove = mutableStateOf(File("Record Self Love"))
var recordedFileColorSelfLove = mutableStateOf(WePeep)
var recordedFileMediaPlayerSelfLove = mutableStateOf(MediaPlayer())
var recordedFileUriSelfLove = mutableStateOf("".toUri())
var recordedAudioFileLengthMilliSecondsSelfLove = mutableStateOf(0L)
var recordedSelfLoveAbsolutePath = mutableStateOf("")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordSelfLoveUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
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
        AlertDialogBox(text = "Self Love Saved")
    }
    if(openSelfLoveNameTakenDialogBox){
        AlertDialogBox(text = "The name '$selfLoveName' already exists")
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        val (
            header,
            title,
            record,
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
                    resetRecordingFileAndMediaSelfLove(context)
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
                text = "Record Self Love",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(record) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            SwipeToResetRecordedSelfLoveUI(
                context
            ) {
                globalViewModel_!!.bottomSheetOpenFor = "recordAudio"
                recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording = "selfLove"
                openBottomSheet(scope, state)
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(next) {
                    top.linkTo(record.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            if(recordedFileSelfLove.value.name != "Record Self Love") {
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
                    createSelfLoveFromRecord(context, navController)
                }
            }
        }
    }
}

fun resetRecordingFileAndMediaSelfLove(context: Context) {
    if(recordingFile != null){
        deleteRecordingFile(context)
    }
    if(recordedFileMediaPlayerSelfLove.value.isPlaying){
        recordedFileMediaPlayerSelfLove.value.stop()
    }
    recordedFileMediaPlayerSelfLove.value.reset()
    recordedFileMediaPlayerSelfLove.value.release()
}

fun createSelfLoveFromRecord(
    context: Context,
    navController: NavController
){
    var otherSelfLovesWithSameName by mutableStateOf(-1)
    SelfLoveBackend.querySelfLoveBasedOnDisplayName(selfLoveName){
        otherSelfLovesWithSameName = if(it.isEmpty()) 0 else it.size
    }
    Thread.sleep(1_000)
    val tags = getSelfLoveTagsList()
    val lyrics = getSelfLoveLyricsList()
    if (otherSelfLovesWithSameName < 1) {
        val key = "Routine/SelfLove/${globalViewModel_!!.currentUser!!.username}/recorded/$selfLoveName/${selfLoveName}_audio.aac"
        SoundBackend.storeAudio(recordedFileSelfLove.value.absolutePath, key){
            val selfLove = SelfLoveObject.SelfLove(
                UUID.randomUUID().toString(),
                UserObject.User.from(globalViewModel_!!.currentUser!!),
                globalViewModel_!!.currentUser!!.id,
                selfLoveName,
                selfLoveShortDescription,
                selfLoveLongDescription,
                key,
                selfLoveIcon,
                recordedAudioFileLengthMilliSecondsSelfLove.value,
                false,
                lyrics,
                tags,
                SelfLoveAudioSource.UPLOADED,
                SelfLoveApprovalStatus.PENDING,
            )
            createSelfLove(selfLove, context, navController)
        }
    }else{
        openSelfLoveNameTakenDialogBox = true
    }
}