package com.example.eunoia.create.createSound

import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.eunoia.create.resetEverythingBeforeCreatingAnything
import com.example.eunoia.models.SoundPresetObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File

var uploadedFiles = mutableListOf<MutableState<File>?>()
var fileColors = mutableListOf<MutableState<Color>?>()
var fileUris = mutableListOf<MutableState<Uri>?>()
var fileMediaPlayers = mutableListOf<MutableState<MediaPlayer>?>()
var fileNames = mutableListOf<MutableState<String>?>()
var audioFileLengthMilliSeconds = mutableListOf<MutableState<Long>?>()
var createSoundPresets = mutableListOf<MutableState<SoundPresetObject.SoundPreset>?>()
var selectedIndex by mutableStateOf(0)
var TAG = "Upload Sounds"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UploadSoundsUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService
){
    val context = LocalContext.current

    resetEverythingBeforeCreatingAnything(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    )

    var numberOfFiles by rememberSaveable{ mutableStateOf(0) }
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            title,
            uploadInfo,
            dropdown,
            uploaders,
            next,
            endSpace
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
                    clearAllCreationObjects()
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
                text = "Upload your audio files",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(uploadInfo) {
                    top.linkTo(title.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            AlignedLightText(
                text = "You can upload between 5 and 10 audio files. " +
                        "The more files, the richer the sound.\n\n" +
                        "How many audio files would you like to upload today?",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(dropdown) {
                    top.linkTo(uploadInfo.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            val numbers = listOf(
                "5",
                "6",
                "7",
                "8",
                "9",
                "10"
            )
            numberOfFiles = dropdownMenuSoftPeach(numbers, "Number of files").toInt()
        }
        Column(
            modifier = Modifier
                .constrainAs(uploaders) {
                    top.linkTo(dropdown.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            fileNames.clear()
            uploadedFiles.clear()
            fileColors.clear()
            fileUris.clear()
            fileMediaPlayers.clear()
            audioFileLengthMilliSeconds.clear()
            if(numberOfFiles > 0) {
                for (i in 0 until numberOfFiles) {
                    fileNames.add(remember{ mutableStateOf("") })
                    uploadedFiles.add(remember{ mutableStateOf(File("Choose a file")) })
                    fileColors.add(remember{ mutableStateOf(WePeep) })
                    fileUris.add(remember{ mutableStateOf("".toUri()) })
                    val mediaPlayer = MediaPlayer()
                    fileMediaPlayers.add(remember{ mutableStateOf(mediaPlayer) })
                    audioFileLengthMilliSeconds.add(remember{ mutableStateOf(0L) })
                    Spacer(modifier = Modifier.height(16.dp))
                    SoundUploader(i)
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(next) {
                    top.linkTo(uploaders.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            if(
                numberOfFiles > 0 &&
                validateUploadedFiles() &&
                validateFileNames() &&
                validateFileUris()
            ) {
                CustomizableButton(
                    text = "next",
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
                    navController.navigate(Screen.CreatePreset.screen_route)
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(next.bottom, margin = 40.dp)
                }
        ){
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

fun clearAllCreationObjects(){
    uploadedFiles.clear()
    fileColors.clear()
    fileUris.clear()
    for (index in fileMediaPlayers.indices) {
        if(fileMediaPlayers[index]!!.value.isPlaying) {
            fileMediaPlayers[index]!!.value.stop()
        }
        fileMediaPlayers[index]!!.value.reset()
        fileMediaPlayers[index]!!.value.release()
    }
    fileMediaPlayers.clear()
}

fun validateUploadedFiles(): Boolean{
    for(file in uploadedFiles){
        if(file!!.value.name == "Choose a file"){
            return false
        }
    }
    return true
}

fun validateFileUris(): Boolean{
    for(uri in fileUris){
        if(uri!!.value.path == ""){
            return false
        }
    }
    return true
}

fun validateFileNames(): Boolean{
    for(name in fileNames){
        if(name!!.value == ""){
            return false
        }
    }
    return true
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
fun UploadSoundsPreview() {
    /*val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        UploadSoundsUI(
            rememberNavController(),
            globalViewModel,
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }*/
}
