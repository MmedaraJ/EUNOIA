package com.example.eunoia.create.createPrayer

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
import com.example.eunoia.backend.PrayerBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.models.PrayerObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.recordAudio.deleteRecordingFile
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.sound.openSavedElementDialogBox
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingFile
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.recordAudioViewModel
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.util.*

var recordedFilePrayer = mutableStateOf(File("Record Prayer"))
var recordedFileColorPrayer = mutableStateOf(WePeep)
var recordedFileMediaPlayerPrayer = mutableStateOf(MediaPlayer())
var recordedFileUriPrayer = mutableStateOf("".toUri())
var recordedAudioFileLengthMilliSecondsPrayer = mutableStateOf(0L)
var recordedPrayerAbsolutePath = mutableStateOf("")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordPrayerUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    if(openSavedElementDialogBox){
        AlertDialogBox(text = "Prayer Saved")
    }
    if(openPrayerNameTakenDialogBox){
        AlertDialogBox(text = "The name '$prayerName' already exists")
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
                    resetRecordingFileAndMediaPlayer(context)
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
                text = "Record Prayer",
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
            SwipeToResetRecordedPrayerUI(
                context
            ) {
                globalViewModel_!!.bottomSheetOpenFor = "recordAudio"
                recordAudioViewModel!!.currentRoutineElementWhoOwnsRecording = "prayer"
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
            if(recordedFilePrayer.value.name != "Record Prayer") {
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
                    createPrayerFromRecord(context, navController)
                }
            }
        }
    }
}

fun resetRecordingFileAndMediaPlayer(context: Context) {
    if(recordingFile != null){
        deleteRecordingFile(context)
    }
    if(recordedFileMediaPlayerPrayer.value.isPlaying){
        recordedFileMediaPlayerPrayer.value.stop()
    }
    recordedFileMediaPlayerPrayer.value.reset()
    recordedFileMediaPlayerPrayer.value.release()
}

fun createPrayerFromRecord(
    context: Context,
    navController: NavController
){
    var otherPrayersWithSameName by mutableStateOf(-1)
    PrayerBackend.queryPrayerBasedOnDisplayName(prayerName){
        otherPrayersWithSameName = if(it.isEmpty()) 0 else it.size
    }
    Thread.sleep(1_000)
    val tags = getPrayerTagsList()
    if (otherPrayersWithSameName < 1) {
        val key = "Routine/Prayer/${globalViewModel_!!.currentUser!!.username}/recorded/$prayerName/${prayerName}_audio.aac"
        SoundBackend.storeAudio(recordedFilePrayer.value.absolutePath, key){
            val prayer = PrayerObject.Prayer(
                UUID.randomUUID().toString(),
                UserObject.User.from(globalViewModel_!!.currentUser!!),
                prayerName,
                prayerDescription,
                key,
                prayerIcon,
                recordedAudioFileLengthMilliSecondsPrayer.value,
                false,
                "",
                "",
                tags,
                PrayerAudioSource.UPLOADED,
                PrayerApprovalStatus.PENDING,
            )
            createPrayer(prayer, context, navController)
        }
    }else{
        openPrayerNameTakenDialogBox = true
    }
}