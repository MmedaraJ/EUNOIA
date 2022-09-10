package com.example.eunoia.create.createSound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundApprovalStatus
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.backend.PresetBackend
import com.example.eunoia.backend.PresetNameAndVolumesMapBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.backend.UserSoundBackend
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.models.*
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.ui.theme.WePeep
import com.example.eunoia.utils.formatMilliSecond
import kotlinx.coroutines.CoroutineScope
import java.util.*

var presetName by mutableStateOf("")
var volumeErrorMessage by mutableStateOf("")
var nameErrorMessage by mutableStateOf("")
var fullPlayTime by mutableStateOf(0)
var sliderVolumes = mutableListOf<MutableState<Int>?>()
var saving by mutableStateOf(false)
var go by mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreatePresetUI(
    navController: NavController,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    presetName = ""
    val scrollState = rememberScrollState()
    var showTapText by rememberSaveable{ mutableStateOf(true) }
    var manualTopMargin by rememberSaveable{ mutableStateOf(-260) }
    sliderVolumes.clear()
    for(index in fileUris.indices){
        Log.i(TAG, "File uris ==>> $fileUris")
        sliderVolumes.add(remember {
            mutableStateOf(5)
        })
    }
    setUpMediaPlayers(context)
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            mixer,
            manual,
            customizedPresets,
            newPreset,
            savePreset,
            message,
            info,
            done,
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
                    //showControls = false
                    navigateBack(navController)
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
                .constrainAs(manual) {
                    top.linkTo(mixer.bottom, margin = manualTopMargin.dp)
                }
                .wrapContentHeight()
        ) {
            ControlPanelManual(showTapText){
                showTapText = !showTapText
                manualTopMargin = if (manualTopMargin == 6) -260 else 6
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(mixer) {
                    top.linkTo(header.bottom, margin = 50.dp)
                }
        ) {
            CreatePresetMixer(context)
        }
        Column(
            modifier = Modifier
                .constrainAs(customizedPresets) {
                    top.linkTo(manual.bottom, margin = 12.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Customized Presets")
        }
        Column(
            modifier = Modifier
                .constrainAs(message) {
                    top.linkTo(customizedPresets.bottom, margin = 12.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            LightText(
                text = "You can create your own presets for your sound by moving the sliders and hit the Save button.",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(savePreset) {
                    top.linkTo(message.bottom, margin = 12.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            SavePreset()
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(info) {
                    top.linkTo(savePreset.bottom, margin = 12.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = volumeErrorMessage,
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
            Spacer(modifier = Modifier.height(4.dp))
            AlignedLightText(
                text = nameErrorMessage,
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(newPreset) {
                    top.linkTo(info.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            if(
                presetName.isNotEmpty() &&
                volumesDoNotAlreadyExist()
            ) {
                CustomizableButton(
                    text = "new preset",
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
                    resetAll()
                    saveThisPreset()
                    navController.navigate(Screen.CreatePreset.screen_route)
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(done) {
                    top.linkTo(info.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            if(
                presetName.isNotEmpty() &&
                volumesDoNotAlreadyExist() &&
                nameDoesNotAlreadyExist()
            ) {
                CustomizableButton(
                    text = "done",
                    height = 35,
                    fontSize = 15,
                    textColor = Black,
                    backgroundColor = WePeep,
                    corner = 50,
                    borderStroke = 0.0,
                    borderColor = WePeep.copy(alpha = 0F),
                    textType = "morge",
                    maxWidthFraction = 1F
                ) {
                    saving = true
                    saveThisPreset()
                    saveAudioFilesToS3()
                    createSound(navController)
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(newPreset.bottom, margin = 20.dp)
                }
        ){
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    if(saving){
        AlertDialogBox("Saving...")
    }else{
        if(go) {
            resetAll()
            navController.navigate(Screen.Create.screen_route)
            go = false
        }
    }
}

fun resetAll(){
    soundIcon = -1
    soundName = ""
    shortDescription = ""
    iconSelectionTitle = ""
    uploadedFiles.clear()
    fileColors.clear()
    fileUris.clear()
    fileMediaPlayers.clear()
    fileNames.clear()
    soundPresetNameAndVolumesMap.clear()
    selectedIndex = 0
    presetName = ""
    fullPlayTime = 0
    sliderVolumes.clear()
}

fun saveAudioFilesToS3(){
    for(i in uploadedFiles.indices) {
        val key = "Routine/Sounds/${globalViewModel_!!.currentUser!!.username}/$soundName/${i}_${uploadedFiles[i]!!.value.name}"
        SoundBackend.storeAudio(uploadedFiles[i]!!.value.absolutePath, key){}
    }
}

fun createSound(navController: NavController){
    val fileNameList = getFileNameList()
    val maxPlayTime = getMaxPlayTime()
    val tags = getSoundTagsList()

    val sound = SoundObject.Sound(
        UUID.randomUUID().toString(),
        UserObject.User.from(globalViewModel_!!.currentUser!!),
        soundName,
        soundName,
        shortDescription,
        shortDescription,
        "Routine/Sounds/${globalViewModel_!!.currentUser!!.username}/$soundName/",
        soundIcon,
        0xFFEBBA9A.toInt(),
        maxPlayTime,
        false,
        tags,
        fileNameList,
        SoundApprovalStatus.PENDING
    )

    SoundBackend.createSound(sound){
        createUserSound(it)
        createSoundPreset(it, navController)
    }
}

fun getFileNameList():List<String> {
    val fileNameList = mutableListOf<String>()
    for(name in fileNames){
        fileNameList.add(name!!.value)
    }
    return fileNameList
}

fun getMaxPlayTime(): Long {
    var maxPlayTime = 0L
    for(playTime in audioFileLengthMilliSeconds){
        if(playTime!!.value > maxPlayTime){
            maxPlayTime = playTime.value
        }
    }
    return maxPlayTime
}

fun getSoundTagsList():List<String> {
    return soundTags.split(",")
}

private fun createUserSound(soundData: SoundData){
    val userSoundModel = UserSoundObject.UserSoundModel(
        UUID.randomUUID().toString(),
        SoundObject.Sound.from(soundData),
        UserObject.signedInUser().value!!
    )
    UserSoundBackend.createUserSound(userSoundModel){

    }
}

private fun createSoundPreset(soundData: SoundData, navController: NavController){
    val preset = PresetObject.Preset(
        UUID.randomUUID().toString(),
        soundData
    )
    PresetBackend.createPreset(preset){
        createPresetNameAndVolumesMapData(it, soundData, navController)
    }
}

private fun createPresetNameAndVolumesMapData(presetData: PresetData, soundData: SoundData, navController: NavController) {
    for(i in soundPresetNameAndVolumesMap.indices){
        soundPresetNameAndVolumesMap[i]!!.value.preset = presetData
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(soundPresetNameAndVolumesMap[i]!!.value){
            if(i == soundPresetNameAndVolumesMap.size - 1){
                saving = false
                go = true
            }
        }
    }
}

fun saveThisPreset(){
    val volumes = mutableListOf<Int>()
    for(volume in sliderVolumes){
        volumes.add(volume!!.value)
    }
    val presetVolume = mutableStateOf(PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
        presetName,
        volumes,
        null
    ))
    soundPresetNameAndVolumesMap.add(presetVolume)
    sliderVolumes.clear()
}

fun volumesDoNotAlreadyExist(): Boolean{
    for(i in soundPresetNameAndVolumesMap.indices){
        val volumes = soundPresetNameAndVolumesMap[i]!!.value.volumes
        var count = 0
        for(j in volumes.indices) {
            if (sliderVolumes[j]!!.value == volumes[j]){
                count++
            }
        }
        if(count == volumes.size){
            volumeErrorMessage = "This volumes already exists"
            return false
        }
    }
    volumeErrorMessage = ""
    return true
}

fun setUpMediaPlayers(context: Context){
    for(mp in fileMediaPlayers){
        if(mp!!.value.isPlaying){
            mp.value.stop()
        }
        mp.value.reset()
        mp.value.release()
    }
    fileMediaPlayers.clear()
    for(index in fileUris.indices){
        val mediaPlayer = MediaPlayer()
        fileMediaPlayers.add(mutableStateOf(mediaPlayer))
    }
    Log.i(TAG, "Set up media player ${fileMediaPlayers.size}")
    fileMediaPlayers.forEachIndexed { i, media ->
        media!!.value.apply {
            reset()
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, fileUris[i]!!.value)
            setVolume(
                5.toFloat()/10,
                5.toFloat()/10
            )
            prepare()
        }
    }
}