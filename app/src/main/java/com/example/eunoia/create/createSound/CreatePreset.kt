package com.example.eunoia.create.createSound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.models.PresetNameAndVolumesMapObject
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SwansDown
import kotlinx.coroutines.CoroutineScope

var presetName by mutableStateOf("")
var volumeErrorMessage by mutableStateOf("")
var nameErrorMessage by mutableStateOf("")
var sliderVolumes = mutableListOf<MutableState<Int>?>()

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
                    saveThisPreset()
                    navController.navigate(Screen.CreatePreset.screen_route)
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