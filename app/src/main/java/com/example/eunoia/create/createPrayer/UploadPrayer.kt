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
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.create.createBedtimeStory.bedtimeStoryName
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.models.PrayerObject
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

var uploadedFilePrayer = mutableStateOf(File("Choose a file"))
var uploadedFileColorPrayer = mutableStateOf(WePeep)
var uploadedFileMediaPlayerPrayer = mutableStateOf(MediaPlayer())
var uploadedFileUriPrayer = mutableStateOf("".toUri())
var uploadedAudioFileLengthMilliSecondsPrayer = mutableStateOf(0L)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadPrayerUI(
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
                    resetPrayerUploadUI()
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
                text = "Upload Prayer",
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
            SwipeToResetUploadedPrayerUI(
                context
            ) {
                UserDashboardActivity.getInstanceActivity().selectAudioPrayer()
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
            if(uploadedFilePrayer.value.name != "Choose a file") {
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
                    createPrayerFromUpload(navController, context)
                }
            }
        }
    }
}

fun resetUploadPrayerMediaPlayers(){
    if(uploadedFileMediaPlayerPrayer.value.isPlaying){
        uploadedFileMediaPlayerPrayer.value.stop()
    }
    uploadedFileMediaPlayerPrayer.value.reset()
    uploadedFileMediaPlayerPrayer.value.release()
}

fun createPrayerFromUpload(
    navController: NavController,
    context: Context
){
    PrayerBackend.queryPrayerBasedOnDisplayName(prayerName){
        val tags = getPrayerTagsList()
        if (it.isEmpty()) {
            val key = "${globalViewModel!!.currentUser!!.username.lowercase()}/" +
                    "routine/" +
                    "prayer/" +
                    "uploaded/" +
                    "${prayerName.lowercase()}/" +
                    "${prayerName.lowercase()}_audio.aac"

            SoundBackend.storeAudio(uploadedFilePrayer.value.absolutePath, key){
                val prayer = PrayerObject.Prayer(
                    UUID.randomUUID().toString(),
                    UserObject.User.from(globalViewModel!!.currentUser!!),
                    globalViewModel!!.currentUser!!.id,
                    prayerName,
                    prayerShortDescription,
                    prayerLongDescription,
                    key,
                    prayerIcon,
                    uploadedAudioFileLengthMilliSecondsPrayer.value,
                    false,
                    prayerReligion,
                    prayerCountry,
                    tags,
                    listOf(),
                    listOf(),
                    listOf(),
                    PrayerAudioSource.UPLOADED,
                    PrayerApprovalStatus.PENDING,
                    PrayerCreationStatus.COMPLETED
                )
                createPrayer(prayer, context, navController)
            }
        }else{
            openPrayerNameTakenDialogBox = true
        }
    }
}

fun createPrayer(
    prayer: PrayerObject.Prayer,
    context: Context,
    navController: NavController
){
    PrayerBackend.createPrayer(prayer) { prayerData ->
        UserPrayerRelationshipBackend.createUserPrayerRelationshipObject(prayerData) {
            UserPrayerBackend.createUserPrayerObject(prayerData) {
                resetAllPrayerCreationObjects(context)
                openSavedElementDialogBox = true
                Thread.sleep(1_000)
                //resetRecordingFileAndMediaPlayer(context)
                ThreadUtils.runOnUiThread {
                    //TODO navigate to users list of pending prayers
                    navigateToPrayerScreen(navController, prayerData)
                }
            }
        }
    }
}

fun navigateToPrayerScreen(navController: NavController, prayerData: PrayerData){
    navController.navigate("${Screen.PrayerScreen.screen_route}/prayerData=${PrayerObject.Prayer.from(prayerData)}")
}

fun getPrayerTagsList(): List<String> {
    val list =  prayerTags.split(",")
    val listMut = list.toMutableList()
    listMut.removeIf {
        it.isBlank()
    }
    return listMut
}

fun resetAllPrayerCreationObjects(context: Context){
    resetNamePrayerVariables()
    resetPrayerUploadUI()
    resetPrayerRecordUI(context)
}