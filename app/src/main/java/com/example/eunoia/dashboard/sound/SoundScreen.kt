package com.example.eunoia.dashboard.sound

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.CountDownTimer
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
import com.amplifyframework.datastore.generated.model.*
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.models.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "SoundScreen"
var itPresetsSize = 0
var itCommentsSize = 0

var newPresetName = ""

var meditationBellInterval = mutableStateOf(0)
var timerTime = mutableStateOf(0L)

var showCommentBox by mutableStateOf(false)
var showAssociatedSoundWithSameVolume = mutableStateOf(false)
var comment_icon_value by mutableStateOf(R.drawable.comment_icon)
var soundPreset: SoundPresetData? = null
var sliderPositions: MutableList<MutableState<Float>?>? = null
var sliderVolumes: MutableList<Int>? = null
var allOriginalSoundPresets: MutableSet<SoundPresetData>? = null
var allUserSoundPresets: MutableSet<SoundPresetData>? = null
var otherPresetsThatOriginatedFromThisSound: MutableList<SoundPresetData>? = null
var commentsForThisSound = mutableListOf<CommentData>()
var soundUris = mutableListOf<Uri?>()
var countDownTimer: CountDownTimer? = null

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
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Black),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
    mutableStateOf(Bizarre),
)
var soundScreenBackgroundControlColor1 = arrayOf(
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(SoftPeach),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)
var soundScreenBackgroundControlColor2 = arrayOf(
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(Solitude),
    mutableStateOf(White),
    mutableStateOf(White),
    mutableStateOf(White),
)

var associatedPreset: SoundPresetData? = null
var mergedPresets = mutableSetOf<SoundPresetData>()


@Composable
private fun SetUpAlertDialogs(){
    if(commentCreatedDialog){
        AlertDialogBox("Comment Created")
    }
    if(openPresetAlreadyExistsDialog){
        AlertDialogBox("This preset already exists")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SoundScreen(
    navController: NavController,
    soundData: SoundData,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
){
    val context = LocalContext.current

    SetUpRoutineCurrentlyPlayingAlertDialogSoundUI(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context,
        soundData
    )

    SetUpAlertDialogs()
    globalViewModel!!.navController = navController
    showCommentBox = false
    var retrievedPresets by rememberSaveable{ mutableStateOf(false) }

    if(soundViewModel!!.currentSoundPlaying != null) {
        if (soundViewModel!!.currentSoundPlaying!!.id == soundData.id) {
            setParametersFromGlobalVariables{
                retrievedPresets = true
            }
        }else{
            getNecessaryPresets(soundData) {
                showAssociatedSoundWithSameVolume.value = false
                retrievedPresets = true
            }
        }
    }else{
        getNecessaryPresets(soundData) {
            showAssociatedSoundWithSameVolume.value = false
            retrievedPresets = true
        }
    }

    if(retrievedPresets){
        val scrollState = rememberScrollState()
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
                        globalViewModel!!.bottomSheetOpenFor = "controls"
                        openBottomSheet(scope, state)
                    },
                    {
                        //navController.navigate(Screen.Settings.screen_route)
                    }
                )
            }
            var showTapText by rememberSaveable{ mutableStateOf(true) }
            var manualTopMargin by rememberSaveable{ mutableStateOf(-32) }
            Column(
                modifier = Modifier
                    .constrainAs(manual) {
                        top.linkTo(mixer.bottom, margin = manualTopMargin.dp)
                    }
                    .wrapContentHeight()
            ) {
                ControlPanelManual(showTapText){
                    showTapText = !showTapText
                    manualTopMargin = if (manualTopMargin == -32) 0 else -32
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
                getPresetsWithCommentsThatOriginatedFromThisSound(soundData){
                    itPresetsSize = it.size
                    otherPresetsThatOriginatedFromThisSound = it.toMutableList()
                }

                if(showAssociatedSoundWithSameVolume.value){
                    if(associatedPreset != null) {
                        AssociatedPresetWithSameVolume(
                            soundData,
                            associatedPreset!!,
                            soundMediaPlayerService
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
                Log.i(TAG, "Sound Preset: $soundPreset")
                Mixer(
                    soundData,
                    context,
                    soundPreset!!,
                    scope,
                    state,
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

                if(!allOriginalSoundPresets.isNullOrEmpty()) {
                    allOriginalSoundPresets?.let { mergedPresets.addAll(it) }
                }
                if(!allUserSoundPresets.isNullOrEmpty()) {
                    allUserSoundPresets?.let { mergedPresets.addAll(it) }
                }

                PresetsUI(mergedPresets, soundData, soundMediaPlayerService)
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
                    if(soundViewModel!!.currentSoundPlaying != null) {
                        if (soundViewModel!!.currentSoundPlaying!!.id == soundData.id) {
                            checkIfItIsOkayToOpenCommentBox(context) {}
                        }
                    }else{
                        //toast: you must be listening to the sound before commenting
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
                    newPresetName = customizedOutlinedTextInput(
                        maxLength = 30,
                        height = 55,
                        color = White,
                        focusedBorderColor = Black,
                        unfocusedBorderColor = Black,
                        inputFontSize = 13,
                        placeholder = "Preset name",
                        placeholderFontSize = 13,
                        placeholderColor = Black,
                        offset = 0,
                        showWordCount = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                        if (it != "" && newPresetName.isNotEmpty()) {
                            Log.i(TAG, "You may comment")
                            checkIfPresetNameIsTaken(soundData, context){ show ->
                                Log.i(TAG, "Show comment value is $show")

                                if(show) {
                                    checkIfItIsOkayToOpenCommentBox(context) { bool ->
                                        if(bool) {
                                            Log.i(TAG, "You can comment")
                                            makePublicPresetObject(soundData, it)
                                        }
                                    }
                                } else {
                                    showCommentBox = false
                                    Log.i(TAG, "You cannot comment")
                                }
                            }
                        } else {
                            showCommentBox = false
                            Toast.makeText(
                                context,
                                "Fields cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.i(TAG, "You definitely cannot comment")
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
            var retrievedComments by rememberSaveable{ mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .constrainAs(otherUserFeedback) {
                        top.linkTo(tip.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                getCommentsForThisSound(soundData){
                    itCommentsSize = it.size
                    commentsForThisSound = it.toMutableList()
                    retrievedComments = true
                }

                if(retrievedComments) {
                    CommentsUI(
                        commentsForThisSound,
                        soundData,
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

private fun setParametersFromGlobalVariables(completed: () -> Unit) {
    setUpParameters{
        setAllOriginalSoundPresets{
            setAllUserSoundPresets{
                completed()
            }
        }
    }
}

fun setUpParameters(completed: () -> Unit) {
    if(soundViewModel!!.currentSoundPlayingUris != null) {
        soundPreset = soundViewModel!!.currentSoundPlayingPreset
        sliderPositions = soundViewModel!!.currentSoundPlayingSliderPositions
        sliderVolumes = soundViewModel!!.soundSliderVolumes
        soundUris = soundViewModel!!.currentSoundPlayingUris!!

        for (i in soundScreenIcons.indices) {
            soundScreenIcons[i] = soundViewModel!!.soundScreenIcons[i]
        }
        for (i in soundScreenBorderControlColors.indices) {
            soundScreenBorderControlColors[i] = soundViewModel!!.soundScreenBorderControlColors[i]
        }
        for (i in soundScreenBackgroundControlColor1.indices) {
            soundScreenBackgroundControlColor1[i] =
                soundViewModel!!.soundScreenBackgroundControlColor1[i]
        }
        for (i in soundScreenBackgroundControlColor2.indices) {
            soundScreenBackgroundControlColor2[i] =
                soundViewModel!!.soundScreenBackgroundControlColor2[i]
        }

        meditationBellInterval.value = soundViewModel!!.soundMeditationBellInterval
        timerTime.value = soundViewModel!!.soundTimerTime
        associatedPreset = soundPreset
        countDownTimer = soundViewModel!!.soundCountDownTimer
        showAssociatedSoundWithSameVolume.value = false
        otherPresetsThatOriginatedFromThisSound = mutableListOf()
    }
    completed()
}

fun setAllUserSoundPresets(completed: () -> Unit) {
    allUserSoundPresets = mutableSetOf()
    getUserSoundPresets(
        soundViewModel!!.currentSoundPlaying!!,
        globalViewModel!!.currentUser!!
    ) { presetList ->
        if(presetList.isNotEmpty()) {
            allUserSoundPresets!!.addAll(presetList)
        }
        completed()
    }
}

fun setAllOriginalSoundPresets(completed: () -> Unit) {
    allOriginalSoundPresets = mutableSetOf()
    getUserSoundPresets(
        soundViewModel!!.currentSoundPlaying!!,
        soundViewModel!!.currentSoundPlaying!!.soundOwner
    ) {
        if(it.isNotEmpty()) {
            allOriginalSoundPresets!!.addAll(it)
        }
        completed()
    }
}

fun resetControlsUI(){
    soundScreenIcons[3].value = R.drawable.play_icon

    for(i in soundScreenBorderControlColors.indices){
        if(i == 3){
            soundScreenBorderControlColors[i].value = Black
        }else{
            soundScreenBorderControlColors[i].value = Bizarre
        }
    }

    for(i in soundScreenBackgroundControlColor1.indices){
        if(i == 3){
            soundScreenBackgroundControlColor1[i].value = SoftPeach
        }else{
            soundScreenBackgroundControlColor1[i].value = White
        }
    }

    for(i in soundScreenBackgroundControlColor2.indices){
        if(i == 3){
            soundScreenBackgroundControlColor2[i].value = Solitude
        }else{
            soundScreenBackgroundControlColor2[i].value = White
        }
    }
    Thread.sleep(1_000)
}

private fun getNecessaryPresets(soundData: SoundData, completed: () -> Unit){
    getUserSoundPresets(
        soundData,
        soundData.soundOwner
    ) { presets ->
        if(presets.isNotEmpty()){
            sliderPositions = mutableListOf()
            sliderVolumes = mutableListOf()
            allOriginalSoundPresets = mutableSetOf()
            allUserSoundPresets = mutableSetOf()
            otherPresetsThatOriginatedFromThisSound = mutableListOf()
            soundPreset = presets[0]
            associatedPreset = soundPreset

            for(volume in presets[0].volumes){
                if(
                    sliderVolumes!!.size < presets[0].volumes!!.size &&
                    sliderPositions!!.size < presets[0].volumes!!.size
                ) {
                    sliderVolumes!!.add(volume)
                    sliderPositions!!.add(mutableStateOf(volume!!.toFloat()))
                }
            }

            allOriginalSoundPresets!!.addAll(presets)
            meditationBellInterval.value = 0
            timerTime.value = 0L

            getUserSoundPresets(
                soundData,
                globalViewModel!!.currentUser!!
            ) { presetList ->
                if(presetList.isNotEmpty()) {
                    allUserSoundPresets!!.addAll(presetList)
                }
                resetControlsUI()
                completed()
            }
        }
    }
}

fun getUserSoundPresets(
    sound: SoundData,
    userData: UserData,
    completed: (presets: MutableList<SoundPresetData>) -> Unit
){
    SoundPresetBackend.queryUserSoundPresetsBasedOnSound(sound, userData){
        completed(it)
    }
}

fun getPresetsWithCommentsThatOriginatedFromThisSound(
    soundData: SoundData,
    completed: (presetList: List<SoundPresetData>) -> Unit
) {
    SoundPresetBackend.querySoundPresetsWithCommentsBasedOnSound(soundData){ presets ->
        completed(presets)
    }
}

fun getCommentsForThisSound(
    soundData: SoundData,
    completed: (commentList: List<CommentData>) -> Unit
) {
    CommentBackend.queryCommentsBasedOnSound(soundData){ comments ->
        completed(comments)
    }
}

fun checkIfSliderPositionsAreDifferentFromOriginalCurrentVolumes(presets: MutableList<PresetData>): Boolean{
    var samePresets = 0
    for(preset in presets){
        var sameVolume = 0
        for(i in sliderPositions!!.indices)
            if (sliderPositions!![i]!!.value == preset.volumes[i].toFloat())
                sameVolume++

        if(sameVolume == sliderPositions!!.size){
            samePresets++
        }
    }
    return samePresets == 0
}

fun checkIfItIsOkayToOpenCommentBox(context: Context, completed: (boolean: Boolean) -> Unit){
    if(associatedPreset == null){
        showCommentBox = !showCommentBox
    }
    if(!showCommentBox) {
        runOnUiThread {
            Toast.makeText(
                context,
                "This preset already exists",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    completed(showCommentBox)
}

fun checkIfPresetNameIsTaken(
    soundData: SoundData,
    context: Context,
    completed: (bool: Boolean) -> Unit
) {
    SoundPresetBackend.queryPublicSoundPresetsBasedOnDisplayNameAndSound(
        newPresetName,
        soundData
    ) {
        if (it.isNotEmpty()) {
            Log.i(TAG, "The name, $newPresetName, is already taken")
            runOnUiThread {
                Toast.makeText(
                    context,
                    "The name, $newPresetName, is already taken",
                    Toast.LENGTH_SHORT
                ).show()
            }
            showCommentBox = false
            completed(showCommentBox)
        }
        showCommentBox = true
        completed(showCommentBox)
    }
}

fun makePublicPresetObject(
    soundData: SoundData,
    comment: String
){
    val preset = SoundPresetObject.SoundPreset(
        UUID.randomUUID().toString(),
        UserObject.User.from(globalViewModel!!.currentUser!!),
        globalViewModel!!.currentUser!!.id,
        newPresetName,
        sliderVolumes!!.toList(),
        SoundObject.Sound.from(soundData).id,
        SoundPresetPublicityStatus.PUBLIC
    )

    SoundPresetBackend.createSoundPreset(preset){ newPreset ->
        UserSoundPresetBackend.createUserSoundPresetObject(newPreset) {
            allUserSoundPresets!!.add(newPreset)
            createSoundComment(soundData, newPreset, comment)
        }
    }
}

private fun createSoundComment(
    soundData: SoundData,
    presetData: SoundPresetData,
    newComment: String
){
    val comment = CommentObject.Comment(
        UUID.randomUUID().toString(),
        UserObject.User.from(globalViewModel!!.currentUser!!),
        globalViewModel!!.currentUser!!.id,
        newComment,
        SoundObject.Sound.from(soundData),
        soundData.id,
        SoundPresetObject.SoundPreset.from(presetData),
        presetData.id
    )
    CommentBackend.createComment(comment){
        showCommentBox = false
        commentCreatedDialog = true
    }
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
    EUNOIATheme {
        //SoundScreen(rememberNavController(), "pouring_rain", LocalContext.current, soundViewModel)
    }
}