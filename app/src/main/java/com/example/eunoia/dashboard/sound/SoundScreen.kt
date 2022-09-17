package com.example.eunoia.dashboard.sound

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.PresetNameAndVolumesMapData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.models.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "SoundScreen"
var itSize = 0

var meditationBellInterval = mutableStateOf(0)
var timerTime = mutableStateOf(0L)

var showCommentBox by mutableStateOf(false)
var showAssociatedSoundWithSameVolume = mutableStateOf(false)
var comment_icon_value by mutableStateOf(R.drawable.comment_icon)
var soundPresets: PresetData? = null
var sliderPositions: MutableList<MutableState<Float>?>? = null
var sliderVolumes: MutableList<Int?>? = null
var soundPresetNameAndVolumesMapData = mutableStateOf<PresetNameAndVolumesMapData?>(null)
var soundUris = mutableListOf<Uri?>()
var defaultVolumes: MutableList<Int?>? = null
var soundScreenIcons = arrayOf(
    mutableStateOf(R.drawable.replay_sound_icon),
    mutableStateOf(R.drawable.reset_sliders_icon),
    mutableStateOf(R.drawable.sound_timer_icon),
    mutableStateOf(R.drawable.play_icon),
    mutableStateOf(R.drawable.increase_levels_icon),
    mutableStateOf(R.drawable.decrease_levels_icon),
    mutableStateOf(R.drawable.meditation_bell_icon),
)
var soundScreenBorderControlColors = arrayOf(
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
var soundScreenBackgroundControlColor1 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)
var soundScreenBackgroundControlColor2 = arrayOf(
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)

var associatedSound: SoundData? = null
var associatedPresetNameAndVolumesMapData: PresetNameAndVolumesMapData? = null
val childSounds = mutableStateOf(mutableListOf<SoundData>())

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoundScreen(
    navController: NavController,
    soundData: SoundData,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    globalViewModel_!!.navController = navController
    showCommentBox = false
    var retrievedPresets by rememberSaveable{ mutableStateOf(false) }

    if(globalViewModel_!!.currentSoundPlaying != null) {
        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
            soundPresets = globalViewModel_!!.currentSoundPlayingPreset
            sliderPositions = globalViewModel_!!.currentSoundPlayingSliderPositions
            sliderVolumes = globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap!!.volumes
            defaultVolumes = globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap!!.volumes
            soundPresetNameAndVolumesMapData.value =
                globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap!!
            soundUris = globalViewModel_!!.currentSoundPlayingUris!!
            //let controls match global view
            soundScreenIcons = globalViewModel_!!.soundScreenIcons
            soundScreenBorderControlColors = globalViewModel_!!.soundScreenBorderControlColors
            soundScreenBackgroundControlColor1 =
                globalViewModel_!!.soundScreenBackgroundControlColor1
            soundScreenBackgroundControlColor2 =
                globalViewModel_!!.soundScreenBackgroundControlColor2
            meditationBellInterval.value = globalViewModel_!!.soundMeditationBellInterval
            timerTime.value = globalViewModel_!!.soundTimerTime
            associatedSound = null
            associatedPresetNameAndVolumesMapData = null
            showAssociatedSoundWithSameVolume.value = false
            retrievedPresets = true
        }
    }

    if(!retrievedPresets){
        getNecessaryPresets(soundData) {
            resetControlsUI()
            associatedSound = null
            associatedPresetNameAndVolumesMapData = null
            showAssociatedSoundWithSameVolume.value = false
            retrievedPresets = true
        }
    }

    if(retrievedPresets){
        val scrollState = rememberScrollState()
        var showTapText by rememberSaveable{ mutableStateOf(true) }
        var manualTopMargin by rememberSaveable{ mutableStateOf(-260) }
        val otherSoundsThatOriginatedFromThisSound = remember{ mutableStateOf(mutableListOf<SoundData>()) }
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            val (
                header,
                mixer,
                manual,
                presetsUI,
                wordOfMouthText,
                commentIcon,
                userComment,
                sameVolumeSound,
                tip,
                otherUserFeedback,
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
                    .constrainAs(sameVolumeSound) {
                        top.linkTo(manual.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                if(showAssociatedSoundWithSameVolume.value){
                    if(
                        associatedSound != null &&
                        associatedPresetNameAndVolumesMapData != null
                    ) {
                        AssociatedSoundWithSameVolume(
                            associatedSound!!,
                            associatedPresetNameAndVolumesMapData!!.key,
                            navController
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(mixer) {
                        top.linkTo(header.bottom, margin = 50.dp)
                    }
            ) {
                Mixer(
                    soundData,
                    context,
                    soundPresets!!,
                    scope,
                    state,
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    navController
                )
            }
            SimpleFlowRow(
                verticalGap = 8.dp,
                horizontalGap = 8.dp,
                alignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .constrainAs(presetsUI) {
                        top.linkTo(sameVolumeSound.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    },
            ) {
                PresetsUI(soundPresets!!.presets, soundData, soundMediaPlayerService)
            }
            Column(
                modifier = Modifier
                    .constrainAs(wordOfMouthText) {
                        top.linkTo(presetsUI.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                StarSurroundedText("Word-of-mouth")
            }
            Column(
                modifier = Modifier
                    .constrainAs(commentIcon) {
                        top.linkTo(wordOfMouthText.top, margin = 0.dp)
                        bottom.linkTo(wordOfMouthText.bottom, margin = 0.dp)
                        end.linkTo(parent.end, margin = 4.dp)
                    }
            ){
                AnImage(
                    comment_icon_value,
                    "comment icon",
                    16.76,
                    16.79,
                    0,
                    0,
                    LocalContext.current
                ) {
                    if(globalViewModel_!!.currentSoundPlaying != null) {
                        if (globalViewModel_!!.currentSoundPlaying!!.id == soundData.id) {
                            checkIfUserIsQualifiedToComment(soundData, soundPresets!!, context)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(userComment) {
                        top.linkTo(wordOfMouthText.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                if(showCommentBox) {
                    bigOutlinedTextInput(
                        100,
                        "Share how you feel about this sound",
                        MaterialTheme.colors.primaryVariant,
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.onPrimary,
                        Black,
                        13
                    ) {
                        if (it != "") {
                            if(checkIfUserIsQualifiedToComment(soundData, soundPresets!!, context)) {
                                makeSoundObject(soundData, soundPresets!!, it)
                            }else{
                                showCommentBox = false
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(tip) {
                        top.linkTo(userComment.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                Tip()
            }
            Column(
                modifier = Modifier
                    .constrainAs(otherUserFeedback) {
                        top.linkTo(tip.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                getOtherSoundsThatOriginatedFromThisSound(soundData.originalName, soundData){
                    itSize = it.size
                    otherSoundsThatOriginatedFromThisSound.value = it.toMutableList()
                    childSounds.value = it.toMutableList()
                }
                if(
                    otherSoundsThatOriginatedFromThisSound.value.size > 0 &&
                    otherSoundsThatOriginatedFromThisSound.value.size == itSize
                ) {
                    GetTheCommentsAssociatedWithTheseSounds(
                        otherSoundsThatOriginatedFromThisSound.value,
                        soundData,
                        navController,
                        context,
                        soundMediaPlayerService
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(endSpace) {
                        top.linkTo(tip.bottom, margin = 20.dp)
                    }
            ){
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }else{
        ConstraintLayout{
            val (progressBar) = createRefs()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(progressBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

fun resetControlsUI(){
    soundScreenIcons = arrayOf(
        mutableStateOf(R.drawable.replay_sound_icon),
        mutableStateOf(R.drawable.reset_sliders_icon),
        mutableStateOf(R.drawable.sound_timer_icon),
        mutableStateOf(R.drawable.play_icon),
        mutableStateOf(R.drawable.increase_levels_icon),
        mutableStateOf(R.drawable.decrease_levels_icon),
        mutableStateOf(R.drawable.meditation_bell_icon),
    )
    soundScreenBorderControlColors = arrayOf(
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
    )
    soundScreenBackgroundControlColor1 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    soundScreenBackgroundControlColor2 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(Solitude),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
    )
}

private fun getNecessaryPresets(soundData: SoundData, completed: () -> Unit){
    getSoundPresets(soundData) { presetData ->
        sliderPositions = mutableListOf()
        sliderVolumes = mutableListOf()
        soundPresets = presetData
        for(volume in presetData.presets[0].volumes){
            if(sliderVolumes!!.size < presetData.presets[0].volumes!!.size) {
                sliderVolumes!!.add(volume)
            }
        }

        defaultVolumes = mutableListOf()
        for(volume in presetData.presets[0].volumes){
            if(defaultVolumes!!.size < presetData.presets[0].volumes!!.size) {
                defaultVolumes!!.add(volume)
            }
        }

        for(volume in sliderVolumes!!){
            if(sliderPositions!!.size < sliderVolumes!!.size) {
                sliderPositions!!.add(mutableStateOf(volume!!.toFloat()))
            }
        }
        soundPresetNameAndVolumesMapData.value = presetData.presets[0]
        meditationBellInterval.value = 0
        timerTime.value = 0L
        completed()
    }
}

fun setSliderPositions(){
    var volumes = listOf<Int>()
    for(preset in globalViewModel_!!.currentSoundPlayingPreset?.presets!!){
        if(preset.key == "current_volumes"){
            volumes = preset.volumes
        }
    }
    Log.i(TAG, "Volumes ==>> $volumes")
    globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
    for(volume in volumes){
        globalViewModel_!!.currentSoundPlayingSliderPositions.add(mutableStateOf(volume.toFloat()))
    }
}

fun getOtherSoundsThatOriginatedFromThisSound(
    originalSoundName: String,
    soundData: SoundData,
    completed: (soundList: List<SoundData>) -> Unit
) {
    SoundBackend.queryOtherSoundsBasedOnOriginalName(originalSoundName, soundData){ sounds ->
        completed(sounds)
    }
}

fun getSoundPresets(sound: SoundData, completed: (presetData: PresetData) -> Unit){
    PresetBackend.queryPresetBasedOnSound(sound){
        completed(it)
    }
}

fun checkIfSliderPositionsAreDifferentFromOriginalCurrentVolumes(presetData: PresetData): Boolean{
    var samePresets = 0
    for(presetMap in presetData.presets){
        var sameVolume = 0
        for(i in sliderPositions!!.indices)
            if (sliderPositions!![i]!!.value == presetMap.volumes[i].toFloat())
                sameVolume++

        if(sameVolume == sliderPositions!!.size){
            samePresets++
        }
    }
    return samePresets == 0
}

fun checkIfUserIsQualifiedToComment(
    soundData: SoundData,
    presetData: PresetData,
    context: Context
):Boolean {
    Log.i(TAG, "display name for sound iS $displayName")
    SoundBackend.querySoundBasedOnDisplayName(displayName){
        if(it.isEmpty()) {
            if (displayName.isNotEmpty()) {
                if (checkIfSliderPositionsAreDifferentFromOriginalCurrentVolumes(presetData)) {
                    showCommentBox = !showCommentBox
                } else {
                    showCommentBox = false
                    Log.i(TAG, "You must change the volume before commenting")
                    runOnUiThread{
                        Toast.makeText(
                            context,
                            "You must change the volume before commenting",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else {
                showCommentBox = false
                Log.i(TAG, "Name the sound")
                runOnUiThread{
                    Toast.makeText(
                        context,
                        "Name the sound",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        else {
            showCommentBox = false
            Log.i(TAG, "The name, $displayName, is already taken")
            runOnUiThread{
                Toast.makeText(
                    context,
                    "The name, $displayName, is already taken",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    return showCommentBox
}

fun makeSoundObject(
    soundData: SoundData,
    soundPresets: PresetData,
    comment: String
){
    val sound = SoundObject.Sound(
        UUID.randomUUID().toString(),
        UserObject.User.from(soundData.soundOwner),
        soundData.originalName,
        displayName,
        soundData.shortDescription,
        soundData.longDescription,
        soundData.audioKeyS3,
        soundData.icon,
        soundData.colorHex,
        soundData.fullPlayTime.toLong(),
        true,
        soundData.tags,
        soundData.audioNames,
        soundData.approvalStatus
    )
    SoundBackend.createSound(sound){
        showCommentBox = false
        if (globalViewModel_!!.currentUser != null) {
            var userAlreadyHasSound = false
            for (userSound in globalViewModel_!!.currentUser!!.sounds) {
                if (
                    userSound.soundData.id == it.id &&
                    userSound.userData.id == globalViewModel_!!.currentUser!!.id
                ) {
                    userAlreadyHasSound = true
                }
            }
            if (!userAlreadyHasSound) {
                UserSoundBackend.createUserSoundObject(it) {

                }
            }
        }
        createSoundPreset(it, soundPresets)
        createSoundComment(it, comment)
    }
}

private fun createSoundComment(soundData: SoundData, newComment: String){
    val comment = CommentObject.Comment(
        UUID.randomUUID().toString(),
        soundData,
        UserObject.signedInUser().value!!.data,
        newComment
    )
    CommentBackend.createComment(comment){

    }
}

private fun createSoundPreset(soundData: SoundData, soundPresets: PresetData){
    val preset = PresetObject.Preset(
        UUID.randomUUID().toString(),
        soundData
    )
    PresetBackend.createPreset(preset){ newPresetData ->
        createPresetNameAndVolumesMapData(newPresetData, soundPresets)
    }
}

private fun createPresetNameAndVolumesMapData(newPresetData: PresetData, currentPresetData: PresetData){
    for(i in currentPresetData.presets.indices){
        val volumes = mutableListOf<Int>()
        if(currentPresetData.presets[i].key == "current_volumes") {
            for (j in globalViewModel_!!.currentSoundPlayingSliderPositions.indices){
                volumes.add(globalViewModel_!!.currentSoundPlayingSliderPositions[j]!!.value.toInt())
            }
        }
        else {
            for (j in currentPresetData.presets[i].volumes.indices) {
                volumes.add(currentPresetData.presets[i].volumes[j].toInt())
            }
        }
        val presetVolume = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            currentPresetData.presets[i].key,
            volumes,
            newPresetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(presetVolume){
            //Clear comment box
            showCommentBox = false
        }
    }
    Log.i(TAG, "Successfully created comment")
}

fun navigateBack(navController: NavController) {
    navController.popBackStack()
}

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
fun Preview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        //SoundScreen(rememberNavController(), "pouring_rain", LocalContext.current, globalViewModel)
    }
}