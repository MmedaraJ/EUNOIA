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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.create.resetEverything
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.models.SelfLoveObject
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
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.util.*

var uploadedFileSelfLove = mutableStateOf(File("Choose a file"))
var uploadedFileColorSelfLove = mutableStateOf(WePeep)
var uploadedFileMediaPlayerSelfLove = mutableStateOf(MediaPlayer())
var uploadedFileUriSelfLove = mutableStateOf("".toUri())
var uploadedAudioFileLengthMilliSecondsSelfLove = mutableStateOf(0L)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadSelfLoveUI(
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
                    resetSelfLoveUploadUI()
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
                text = "Upload Self Love",
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
            SwipeToResetUploadedSelfLoveUI(
                context
            ) {
                UserDashboardActivity.getInstanceActivity().selectAudioSelfLove()
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
            if(uploadedFileSelfLove.value.name != "Choose a file") {
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
                    createSelfLoveFromUpload(navController, context)
                }
            }
        }
    }
}

fun resetUploadSelfLoveMediaPlayers(){
    if(uploadedFileMediaPlayerSelfLove.value.isPlaying){
        uploadedFileMediaPlayerSelfLove.value.stop()
    }
    uploadedFileMediaPlayerSelfLove.value.reset()
    uploadedFileMediaPlayerSelfLove.value.release()
}

fun createSelfLoveFromUpload(
    navController: NavController,
    context: Context
){
    SelfLoveBackend.querySelfLoveBasedOnDisplayName(selfLoveName){
        val tags = getSelfLoveTagsList()
        val lyrics = getSelfLoveLyricsList()
        if (it.isEmpty()) {
            val key = "${globalViewModel!!.currentUser!!.username.lowercase()}/" +
                    "routine/" +
                    "self-love/" +
                    "uploaded/" +
                    "${selfLoveName.lowercase()}/" +
                    "${selfLoveName.lowercase()}_audio.aac"

            SoundBackend.storeAudio(uploadedFileSelfLove.value.absolutePath, key){
                val selfLove = SelfLoveObject.SelfLove(
                    UUID.randomUUID().toString(),
                    UserObject.User.from(globalViewModel!!.currentUser!!),
                    globalViewModel!!.currentUser!!.id,
                    selfLoveName,
                    selfLoveShortDescription,
                    selfLoveLongDescription,
                    key,
                    selfLoveIcon,
                    uploadedAudioFileLengthMilliSecondsSelfLove.value,
                    false,
                    lyrics,
                    tags,
                    listOf(),
                    listOf(),
                    listOf(),
                    SelfLoveAudioSource.UPLOADED,
                    SelfLoveApprovalStatus.PENDING,
                    SelfLoveCreationStatus.COMPLETED
                )
                createSelfLove(selfLove, context, navController)
            }
        }else{
            openSelfLoveNameTakenDialogBox = true
        }
    }
}

fun createSelfLove(
    selfLove: SelfLoveObject.SelfLove,
    context: Context,
    navController: NavController
){
    SelfLoveBackend.createSelfLove(selfLove) { selfLoveData ->
        UserSelfLoveRelationshipBackend.createUserSelfLoveRelationshipObject(selfLoveData) {
            UserSelfLoveBackend.createUserSelfLoveObject(selfLoveData) {
                resetAllSelfLoveCreationObjects(context)
                openSavedElementDialogBox = true
                Thread.sleep(1_000)
                ThreadUtils.runOnUiThread {
                    //TODO navigate to users list of pending SelfLoves
                    navigateToSelfLoveScreen(navController, selfLoveData)
                }
            }
        }
    }
}

fun navigateToSelfLoveScreen(navController: NavController, selfLoveData: SelfLoveData){
    navController.navigate("${Screen.SelfLoveScreen.screen_route}/selfLoveData=${SelfLoveObject.SelfLove.from(selfLoveData)}")
}

fun getSelfLoveTagsList(): List<String> {
    val list =  selfLoveTags.split(",")
    val listMut = list.toMutableList()
    listMut.removeIf {
        it.isBlank()
    }
    return listMut
}

fun getSelfLoveLyricsList():List<String> {
    return selfLoveLyrics.split("\r?\n|\r".toRegex())
}

fun resetAllSelfLoveCreationObjects(context: Context){
    resetNameSelfLoveVariables()
    resetSelfLoveUploadUI()
    resetSelfLoveRecordUI(context)
}