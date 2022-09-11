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
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.openSavedElementDialogBox
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.CustomizableButton
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.viewModels.GlobalViewModel
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
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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

fun createSelfLoveFromUpload(
    navController: NavController,
    context: Context
){
    var otherSelfLovesWithSameName by mutableStateOf(-1)
    SelfLoveBackend.querySelfLoveBasedOnDisplayName(selfLoveName){
        otherSelfLovesWithSameName = if(it.isEmpty()) 0 else it.size
    }
    Thread.sleep(1_000)
    val tags = getSelfLoveTagsList()
    val lyrics = getSelfLoveLyricsList()
    if (otherSelfLovesWithSameName < 1) {
        val key = "Routine/SelfLove/${globalViewModel_!!.currentUser!!.username}/uploaded/$selfLoveName/${selfLoveName}_audio.aac"
        SoundBackend.storeAudio(uploadedFileSelfLove.value.absolutePath, key){
            val selfLove = SelfLoveObject.SelfLove(
                UUID.randomUUID().toString(),
                UserObject.User.from(globalViewModel_!!.currentUser!!),
                selfLoveName,
                selfLoveDescription,
                key,
                selfLoveIcon,
                uploadedAudioFileLengthMilliSecondsSelfLove.value,
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

fun createSelfLove(
    SelfLove: SelfLoveObject.SelfLove,
    context: Context,
    navController: NavController
){
    SelfLoveBackend.createSelfLove(SelfLove) { selfLoveData ->
        if (globalViewModel_!!.currentUser != null) {
            var userAlreadyHasSelfLove = false
            for (userSelfLove in globalViewModel_!!.currentUser!!.selfLoves) {
                if (
                    userSelfLove.selfLoveData.id == selfLoveData.id &&
                    userSelfLove.userData.id == globalViewModel_!!.currentUser!!.id
                ) {
                    userAlreadyHasSelfLove = true
                }
            }
            if (!userAlreadyHasSelfLove) {
                UserSelfLoveBackend.createUserSelfLoveObject(
                    selfLoveData
                ) {
                    resetAllSelfLoveCreationObjects(context)
                    openSavedElementDialogBox = true
                    Thread.sleep(1_000)
                    ThreadUtils.runOnUiThread {
                        /* navController.backQueue.removeIf { it.destination.route == Screen.NameBedtimeStory.screen_route }
                         navController.backQueue.removeIf { it.destination.route == Screen.UploadBedtimeStory.screen_route }*/
                        //TODO navigate to users list of pending SelfLoves
                        navController.navigate(Screen.Create.screen_route)
                    }
                }
            }
        }
    }
}

fun getSelfLoveTagsList():List<String> {
    return selfLoveTags.split(",")
}

fun getSelfLoveLyricsList():List<String> {
    return selfLoveLyrics.split("\r?\n|\r".toRegex())
}

fun resetAllSelfLoveCreationObjects(context: Context){
    resetNameSelfLoveVariables()
    resetSelfLoveUploadUI()
    resetSelfLoveRecordUI(context)
}