package com.example.eunoia.dashboard.sound

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.models.*
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.math.max

private const val TAG = "SoundScreen"
var itSize = 0
var showCommentBox by mutableStateOf(false)
var comment_icon_value by mutableStateOf(R.drawable.comment_icon)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoundScreen(
    navController: NavController,
    soundData: SoundData,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    globalViewModel_!!.navController = navController
    showCommentBox = false
    if(globalViewModel_!!.currentSoundPlayingPreset == null){
        var soundPreset by remember{ mutableStateOf<PresetData?>(null) }
        getSoundPresets(soundData) { presetData ->
            soundPreset = presetData
            globalViewModel_!!.currentSoundPlayingPreset = soundPreset
            for(preset in globalViewModel_!!.currentSoundPlayingPreset?.presets!!){
                if(preset.key == "current_volumes"){
                    globalViewModel_!!.currentSoundPlayingPresetNameAndVolumesMap = preset
                }
            }
        }
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
                presetsUI,
                word_of_mouth_text,
                comment_icon,
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
                var volumes = listOf<Int>()
                for(preset in globalViewModel_!!.currentSoundPlayingPreset?.presets!!){
                    if(preset.key == "current_volumes"){
                        volumes = preset.volumes
                    }
                }
                Log.i(TAG, "Volumes ==>> $volumes")
                globalViewModel_!!.currentSoundPlayingSliderPositions.clear()
                for(volume in volumes){
                    globalViewModel_!!.currentSoundPlayingSliderPositions.add(remember{mutableStateOf(volume.toFloat())})
                }
                Mixer(
                    soundData,
                    context,
                    globalViewModel_!!.currentSoundPlayingPreset!!,
                    scope,
                    state
                )
            }
            SimpleFlowRow(
                verticalGap = 8.dp,
                horizontalGap = 8.dp,
                alignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .constrainAs(presetsUI) {
                        top.linkTo(manual.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    },
            ) {
                PresetsUI(globalViewModel_!!.currentSoundPlayingPreset?.presets!!)
            }
            Column(
                modifier = Modifier
                    .constrainAs(word_of_mouth_text) {
                        top.linkTo(presetsUI.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                StarSurroundedText("Word-of-mouth")
            }
            Column(
                modifier = Modifier
                    .constrainAs(comment_icon) {
                        top.linkTo(word_of_mouth_text.top, margin = 0.dp)
                        bottom.linkTo(word_of_mouth_text.bottom, margin = 0.dp)
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
                    checkIfUserIsQualifiedToComment(soundData, globalViewModel_!!.currentSoundPlayingPreset!!, context)
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(user_comment) {
                        top.linkTo(word_of_mouth_text.bottom, margin = 12.dp)
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
                            Log.i(TAG, "Comment input completed: $it")
                            makeSoundObject(soundData, globalViewModel_!!.currentSoundPlayingPreset!!, it)
                        }
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
                getOtherUsersComments(soundData.originalName){
                    itSize = it.size
                    commentSounds.value = it.toMutableList()
                }
                if(commentSounds.value.size > 0 && commentSounds.value.size == itSize) {
                    CommentsForSound(commentSounds.value, soundData, navController, context)
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

@Composable
fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    val hGapPx = horizontalGap.roundToPx()
    val vGapPx = verticalGap.roundToPx()
    val rows = mutableListOf<MeasuredRow>()
    val itemConstraints = constraints.copy(minWidth = 0)

    for (measurable in measurables) {
        val lastRow = rows.lastOrNull()
        val placeable = measurable.measure(itemConstraints)
        if (lastRow != null && lastRow.width + hGapPx + placeable.width <= constraints.maxWidth) {
            lastRow.items.add(placeable)
            lastRow.width += hGapPx + placeable.width
            lastRow.height = max(lastRow.height, placeable.height)
        } else {
            val nextRow = MeasuredRow(
                items = mutableListOf(placeable),
                width = placeable.width,
                height = placeable.height
            )
            rows.add(nextRow)
        }
    }

    val width = rows.maxOfOrNull { row -> row.width } ?: 0
    val height = rows.sumBy { row -> row.height } + max(vGapPx.times(rows.size - 1), 0)
    val coercedWidth = width.coerceIn(constraints.minWidth, constraints.maxWidth)
    val coercedHeight = height.coerceIn(constraints.minHeight, constraints.maxHeight)

    layout(coercedWidth, coercedHeight) {
        var y = 0
        for (row in rows) {
            var x = when(alignment) {
                Alignment.Start -> 0
                Alignment.CenterHorizontally -> (coercedWidth - row.width) / 2
                Alignment.End -> coercedWidth - row.width
                else -> throw Exception("unsupported alignment")
            }
            for (item in row.items) {
                item.place(x, y)
                x += item.width + hGapPx
            }
            y += row.height + vGapPx
        }
    }
}

private data class MeasuredRow(
    val items: MutableList<Placeable>,
    var width: Int,
    var height: Int
)

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
    var samePresets = 0
    for(presetMap in presetData.presets){
        var sameVolume = 0
        for(i in globalViewModel_!!.currentSoundPlayingSliderPositions.indices)
            if (globalViewModel_!!.currentSoundPlayingSliderPositions[i]!!.value == presetMap.volumes[i].toFloat())
                sameVolume++
        if(sameVolume == globalViewModel_!!.currentSoundPlayingSliderPositions.size){
            samePresets++
        }
    }
    return samePresets == 0
}

fun checkIfUserIsQualifiedToComment(
    soundData: SoundData,
    presetData: PresetData,
    context: Context
){
    Log.i(TAG, "display name for sound iS $displayName")
    SoundBackend.querySoundBasedOnDisplayName(displayName){
        if(it.isEmpty()) {
            if (displayName.isNotEmpty()) {
                if (globalViewModel_!!.currentSoundPlayingSliderPositions.isNotEmpty()) {
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
        soundData.audioNames,
        soundData.approvalStatus
    )
    SoundBackend.createSound(sound){
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