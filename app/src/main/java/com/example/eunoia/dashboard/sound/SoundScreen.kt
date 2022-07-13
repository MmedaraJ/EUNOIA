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
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.models.*
import com.example.eunoia.mvvm.commentMvvm.model.CommentModel
import com.example.eunoia.mvvm.presetMvvm.model.PresetModel
import com.example.eunoia.mvvm.soundMvvm.model.SoundModel
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "SoundScreen"
private val comment = mutableStateOf("")
var uris: MutableList<Uri>? = null
var itSize = 0
var globalViewModel_: GlobalViewModel? = null

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoundScreen(
    navController: NavController,
    soundDisplayName: String,
    context: Context,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    globalViewModel_ = globalViewModel
    //globalViewModel.currentSoundPlayingContext = context
    var sound by remember{ mutableStateOf<SoundData?>(null) }
    var soundPreset by remember{ mutableStateOf<PresetData?>(null) }
    SoundBackend.querySoundBasedOnDisplayName(soundDisplayName, LocalContext.current){
        sound = it
        //globalViewModel.changeCurrentSoundPlaying(sound)
        getSoundPresets(sound!!){presetData ->
            soundPreset = presetData
            //globalViewModel.changeCurrentSoundPlayingPreset(presetData)
        }
    }
    if(sound == null || soundPreset == null){
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
    }else{
        val scrollState = rememberScrollState()
        var showTapText by rememberSaveable{ mutableStateOf(true) }
        var manualTopMargin by rememberSaveable{ mutableStateOf(-260) }
        val commentSounds = remember{ mutableStateOf(mutableListOf<SoundData>()) }
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            val (
                header,
                mixer,
                manual,
                word_of_mouth_text,
                user_comment,
                tip,
                other_user_feedback,
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
                        navigateBack(navController)
                    },
                    {
                        openBottomSheet(scope, state)
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
                Log.i(TAG, "Volumes0 == ${soundPreset?.presets!!}")
                Log.i(TAG, "Volumes1 == ${soundPreset!!}")
                Log.i(TAG, "Volumes2 == $sound")
                globalViewModel_!!.currentSoundPlayingSliderPositions = arrayOf(
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(0)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(1)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(2)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(3)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(4)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(5)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(6)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(7)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(8)?.let { mutableStateOf(it.toFloat()) } },
                    remember { soundPreset?.presets?.get(1)?.volumes?.get(9)?.let { mutableStateOf(it.toFloat()) } },
                )
                sound?.let {
                    Mixer(it, context, soundPreset!!)
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(word_of_mouth_text) {
                        top.linkTo(manual.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                StarSurroundedText("Word-of-mouth")
            }
            Column(
                modifier = Modifier
                    .constrainAs(user_comment) {
                        top.linkTo(word_of_mouth_text.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
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
                        Log.i(TAG, "Comment input completed: $it")
                        createComment(sound!!, soundPreset!!, it, context)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(tip) {
                        top.linkTo(user_comment.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                Tip()
            }
            Column(
                modifier = Modifier
                    .constrainAs(other_user_feedback) {
                        top.linkTo(tip.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                getOtherUsersComments(sound!!.originalName){
                    itSize = it.size
                    commentSounds.value = it.toMutableList()
                }
                if(commentSounds.value.size > 0 && commentSounds.value.size == itSize) {
                    CommentsForSound(commentSounds.value, sound!!, navController, context)
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
    }
}

fun getOtherUsersComments(originalSoundName: String, completed: (soundList: List<SoundData>) -> Unit) {
    SoundBackend.querySoundBasedOnOriginalName(originalSoundName){ sounds ->
        completed(sounds)
    }
}

fun getSoundPresets(sound: SoundData, completed: (presetData: PresetData) -> Unit){
    PresetBackend.queryPresetBasedOnSound(sound){
        completed(it)
    }
}

fun checkIfSliderPositionsAreDifferentFromOriginalCurrentVolumes(presetData: PresetData): Boolean{
    var sameVolume = 0
    for(i in globalViewModel_!!.currentSoundPlayingSliderPositions.indices)
        if (globalViewModel_!!.currentSoundPlayingSliderPositions[i]!!.value == presetData.presets[1].volumes[i].toFloat())
            sameVolume++
    return sameVolume != globalViewModel_!!.currentSoundPlayingSliderPositions.size
}

fun createComment(
    soundData: SoundData,
    presetData: PresetData,
    comment: String,
    context: Context
){
    Log.i(TAG, "display name for sound iS $displayName")
    if(displayName.isNotEmpty())
        if(displayName != soundData.displayName)
            if(globalViewModel_!!.currentSoundPlayingSliderPositions.isNotEmpty())
                if(checkIfSliderPositionsAreDifferentFromOriginalCurrentVolumes(presetData)) {
                    makeSoundObject(soundData, presetData, comment)
                    //makeLocalSoundObject(soundData, presetData, context, comment)
                }
                else {
                    Log.i(TAG, "You must change the volume before commenting")
                    Toast.makeText(
                        context,
                        "You must change the volume before commenting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        else {
            Log.i(TAG, "The name, $displayName, is already taken")
            Toast.makeText(
                context,
                "The name, $displayName, is already taken",
                Toast.LENGTH_SHORT
            ).show()
        }
    else {
        Log.i(TAG, "Sound name cannot be empty")
        Toast.makeText(context, "Sound name cannot be empty", Toast.LENGTH_SHORT).show()
    }
}

/*
fun makeLocalSoundObject(
    soundData: SoundData,
    soundPresets: PresetData,
    context: Context,
    comment: String
) {
    if(uris!!.size > 0){
        val uriStrings = mutableListOf<String>()
        for(uri in uris!!){
            uriStrings.add(uri.toString())
        }
        for(uri in uriStrings){
            Log.i(TAG, "fgjdbsjofvndnso uristring is string ${uri is String}")
        }
        if(uriStrings.size > 0) {
            val sound = SoundModel(
                soundData.originalOwner.username,
                UserObject.signedInUser().value!!.username,
                soundData.originalName,
                soundData.displayName,
                soundData.shortDescription,
                soundData.longDescription,
                soundData.icon,
                soundData.fullPlayTime,
                soundData.visibleToOthers,
                soundData.audioNames,
                uriStrings,
            )
            UserDashboardActivity.soundViewModel.insertSound(context, sound)
            UserDashboardActivity.soundViewModel.getSoundBasedOnDisplayName(
                context,
                soundData.displayName
            )!!.observe(UserDashboardActivity.owner) {
                Log.i(TAG, "Sound retrieved $it")
                if(it.size != 0){
                    createLocalSoundPreset(it[0], soundPresets, context, comment)
                }else{
                    Log.i(TAG, "Size is 0")
                }
            }
        }else{
            Log.i(TAG, "Uri s iz ${uriStrings.size}")
        }
    }
}

fun createLocalSoundPreset(
    sound: SoundModel?,
    soundPresets: PresetData,
    context: Context,
    comment: String
) {
    for(soundPreset in soundPresets.presets){
        val preset = PresetModel(
            sound!!.id!!,
            soundPreset.key,
            soundPreset.volumes
        )
        UserDashboardActivity.presetViewModel.insertPreset(context, preset)
    }
    createLocalSoundComment(sound!!, context, comment)
}

fun createLocalSoundComment(
    sound: SoundModel?,
    context: Context,
    comment: String
) {
    val soundComment = CommentModel(
        sound!!.id!!,
        comment
    )
    Log.i(TAG, soundComment.commentText)
    UserDashboardActivity.commentViewModel.insertComment(context, soundComment)
}
*/

fun makeSoundObject(
    soundData: SoundData,
    soundPresets: PresetData,
    comment: String
){
    val sound = SoundObject.Sound(
        UUID.randomUUID().toString(),
        soundData.originalOwner,
        UserObject.signedInUser().value!!.data,
        soundData.originalName,
        displayName,
        soundData.shortDescription,
        soundData.longDescription,
        soundData.audioKeyS3,
        soundData.icon,
        soundData.fullPlayTime,
        true,
        soundData.audioNames,
    )
    SoundBackend.createSound(sound){
        createSoundPreset(it, soundPresets)
        createSoundComment(it, comment)
    }
}

private fun createSoundComment(soundData: SoundData, newComment: String){
    val comment = CommentObject.Comment(
        UUID.randomUUID().toString(),
        soundData,
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
        if(i == 1)
            for(j in globalViewModel_!!.currentSoundPlayingSliderPositions.indices)
                volumes.add(globalViewModel_!!.currentSoundPlayingSliderPositions[j]!!.value.toInt())
        else
            for(j in currentPresetData.presets[i].volumes.indices)
                volumes.add(currentPresetData.presets[i].volumes[j].toInt())
        val presetVolume = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            currentPresetData.presets[i].key,
            volumes,
            newPresetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(presetVolume){
            //Clear comment box
        }
    }
    Log.i(TAG, "Successfully created comment")
}

fun navigateBack(navController: NavController) {
    navController.popBackStack()
}

fun navigateToNewSound(navController: NavController, soundData: SoundData) {
    navController.navigate("${Screen.SoundScreen.screen_route}/${soundData.displayName}")
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