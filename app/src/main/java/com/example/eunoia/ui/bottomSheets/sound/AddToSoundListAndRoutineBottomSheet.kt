package com.example.eunoia.ui.bottomSheets

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.models.PresetObject
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.SoundObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import java.util.*

var openSavedElementDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasSoundDialogBox by mutableStateOf(false)
private const val TAG = "AddToSoundListAndRoutineBottomSheet"
var userRoutinesSize = 0

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToSoundListAndRoutineBottomSheet(
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openUserAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "You already have this sound")
    }
    if(openRoutineAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "Routine already has this sound")
    }
    Card(
        modifier = Modifier
            .height(92.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(OldLace),
        ) {
            val (
                soundList,
                divider,
                routineItems
            ) = createRefs()
            ConstraintLayout(
                modifier = Modifier
                    .background(OldLace)
                    .constrainAs(soundList) {
                        top.linkTo(parent.top, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(divider.top, margin = 0.dp)
                    }
                    .clickable {
                        if (globalViewModel_!!.currentSoundToBeAdded != null) {
                            if (globalViewModel_!!.currentUser != null) {
                                var userAlreadyHasSound = false
                                for (userSound in globalViewModel_!!.currentUser!!.sounds) {
                                    Log.i(TAG, "${globalViewModel_!!.currentUser!!.sounds}")
                                    if (
                                        userSound.soundData.id == globalViewModel_!!.currentSoundToBeAdded!!.id &&
                                        userSound.userData.id == globalViewModel_!!.currentUser!!.id
                                    ) {
                                        userAlreadyHasSound = true
                                        break
                                    }
                                }
                                if (!userAlreadyHasSound) {
                                    UserSoundBackend.createUserSoundObject(globalViewModel_!!.currentSoundToBeAdded!!) {
                                        closeBottomSheet(scope, state)
                                        openSavedElementDialogBox = true
                                    }
                                } else {
                                    closeBottomSheet(scope, state)
                                    openUserAlreadyHasSoundDialogBox = true
                                }
                            }
                        }
                    }
                    .fillMaxWidth()
            ) {
                val (
                    text
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(parent.top, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                        }
                ) {
                    NormalText(
                        text = "Add to your sound list",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(parent.top, margin = 0.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 48f, y = 0f),
                        end = Offset(x = canvasWidth - 48f, y = 0f),
                        color = Black,
                        strokeWidth = 0.3F
                    )
                }
            }
            ConstraintLayout(
                modifier = Modifier
                    .background(OldLace)
                    .constrainAs(routineItems) {
                        top.linkTo(divider.bottom, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
                    .clickable {
                        if(globalViewModel_!!.currentPresetToBeAdded == null) {
                            globalViewModel_!!.bottomSheetOpenFor = "inputPresetName"
                        }else{
                            globalViewModel_!!.bottomSheetOpenFor = "addToRoutine"
                        }
                        openBottomSheet(scope, state)
                    }
                    .fillMaxWidth()
            ) {
                val (
                    routine,
                    arrow
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(routine) {
                            top.linkTo(parent.top, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                        }
                ) {
                    NormalText(
                        text = "Add to a routine",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(arrow) {
                            top.linkTo(routine.top, margin = 0.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            bottom.linkTo(routine.bottom, margin = 0.dp)
                        }
                ) {
                    AnImage(
                        R.drawable.little_right_arrow,
                        "add to routine",
                        6.0,
                        4.0,
                        0,
                        0,
                        LocalContext.current
                    ) {

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectRoutine(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openUserAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "You already have this preset")
    }
    if(openRoutineAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "Routine already has this preset")
    }
    //val userRoutines = globalViewModel_!!.currentUser!!.routinesOwnedByUser
    val userRoutines = remember{ mutableStateOf(mutableListOf<UserRoutine?>()) }
    UserRoutineBackend.queryUserRoutineBasedOnUser(globalViewModel_!!.currentUser!!){
        userRoutinesSize = it.size
        userRoutines.value = it.toMutableList()
        Log.i(TAG, "1. ${it}")
    }
    //if(userRoutines.value.size > 0 && userRoutines.value.size == userRoutinesSize) {
        ConstraintLayout(
            modifier = Modifier
                .background(OldLace)
        ) {
            val (
                title,
                routines,
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
            ) {
                NormalText(
                    text = "Add to",
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            SimpleFlowRow(
                verticalGap = 8.dp,
                horizontalGap = 8.dp,
                alignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .constrainAs(routines) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .clickable {
                            globalViewModel_!!.bottomSheetOpenFor = "inputRoutineName"
                            openBottomSheet(scope, state)
                        }
                ) {
                    Box{
                        Card(
                            modifier = Modifier
                                .size(71.dp, 71.dp)
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            backgroundColor = SwansDown,
                            elevation = 8.dp
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.star_1),
                                contentDescription = "new routine",
                                colorFilter = ColorFilter.tint(Black),
                                modifier = Modifier
                                    .size(width = 25.64.dp, height = 25.64.dp)
                                    .padding(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    NormalText(
                        text = "new routine",
                        color = MaterialTheme.colors.primary,
                        fontSize = 9,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                if(userRoutines.value.size > 0 && userRoutines.value.size == userRoutinesSize){
                    Log.i(TAG, "2. ${userRoutines.value}")
                    userRoutines.value.forEach{ userRoutine ->
                        var routinePresetsSize = 0
                        val routinePresets = remember{ mutableStateOf(mutableListOf<RoutinePreset?>()) }
                        RoutinePresetBackend.queryRoutinePresetBasedOnRoutine(userRoutine!!.routineData){
                            routinePresetsSize = it.size
                            routinePresets.value = it.toMutableList()
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .clickable {
                                    if (routinePresets.value.size > 0 && routinePresets.value.size == routinePresetsSize) {
                                        if (globalViewModel_!!.currentPresetToBeAdded == null) {
                                            makePrivatePresetObject() {
                                                setUpUserSound(userRoutine)
                                                setUpRoutineSound(userRoutine)
                                                setUpRoutinePreset(
                                                    routinePresets,
                                                    userRoutine,
                                                    scope,
                                                    state
                                                )
                                            }
                                        } else {
                                            setUpUserPreset() {
                                                setUpUserSound(userRoutine)
                                                setUpRoutineSound(userRoutine)
                                                setUpRoutinePreset(
                                                    routinePresets,
                                                    userRoutine,
                                                    scope,
                                                    state
                                                )
                                            }
                                        }
                                    }
                                }
                        ) {
                            Box{
                                Card(
                                    modifier = Modifier
                                        .size(71.dp, 71.dp)
                                        .fillMaxWidth(),
                                    shape = MaterialTheme.shapes.small,
                                    backgroundColor = Color(userRoutine.routineData.colorHex),
                                    elevation = 8.dp
                                ) {
                                    Image(
                                        painter = painterResource(id = userRoutine.routineData.icon),
                                        contentDescription = "routine icon",
                                        modifier = Modifier
                                            .size(width = 25.64.dp, height = 25.64.dp)
                                            .padding(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            var goodDisplayName = userRoutine.routineData.displayName
                            if(goodDisplayName != null){
                                if(goodDisplayName.length > 11){
                                    goodDisplayName = goodDisplayName.substring(0, 12) + ".."
                                }
                            }
                            NormalText(
                                text = goodDisplayName,
                                color = MaterialTheme.colors.primary,
                                fontSize = 9,
                                xOffset = 0,
                                yOffset = 0
                            )
                        }
                    }
                }
            }
        }
    //}
}

@OptIn(ExperimentalMaterialApi::class)
fun setUpRoutinePreset(
    routinePresets: MutableState<MutableList<RoutinePreset?>>,
    userRoutine: UserRoutine,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    var routineAlreadyHasPreset = false

    for (routinePreset in routinePresets.value) {
        if (routinePreset!!.presetData.id == globalViewModel_!!.currentPresetToBeAdded!!.id) {
            routineAlreadyHasPreset = true
        }
    }

    if (!routineAlreadyHasPreset) {
        RoutinePresetBackend.createRoutinePresetObject(
            globalViewModel_!!.currentPresetToBeAdded!!,
            userRoutine.routineData
        ) {
            closeBottomSheet(scope, state)
            openSavedElementDialogBox = true
        }
    } else {
        closeBottomSheet(scope, state)
        openRoutineAlreadyHasSoundDialogBox = true
    }
}

fun setUpRoutineSound(userRoutine: UserRoutine){
    var routineSoundsSize = 0
    var routineSounds = mutableListOf<RoutineSound?>()

    RoutineSoundBackend.queryRoutineSoundBasedOnRoutine(userRoutine.routineData){
        routineSoundsSize = it.size
        routineSounds = it.toMutableList()
    }

    if (routineSounds.size > 0 && routineSounds.size == routineSoundsSize) {
        var routineAlreadyHasSound = false

        for (routineSound in routineSounds) {
            if (routineSound!!.soundData.id == globalViewModel_!!.currentSoundToBeAdded!!.id) {
                routineAlreadyHasSound = true
            }
        }

        if (!routineAlreadyHasSound) {
            if (!userRoutine.routineData.playingOrder.contains("sound")) {
                userRoutine.routineData.playingOrder.add("sound")
                RoutineBackend.updateRoutine(userRoutine.routineData) {

                }
            }

            RoutineSoundBackend.createRoutineSoundObject(
                globalViewModel_!!.currentSoundToBeAdded!!,
                userRoutine.routineData
            ) {}
        }
    }
}

fun setUpUserSound(userRoutine: UserRoutine){
    var userSoundsSize = 0
    var userSounds = mutableListOf<UserSound?>()

    UserSoundBackend.queryUserSoundBasedOnUser(userRoutine.userData){
        userSoundsSize = it.size
        userSounds = it.toMutableList()
    }

    if (userSounds.size > 0 && userSounds.size == userSoundsSize) {
        var userAlreadyHasSound = false
        for (userSound in userSounds) {
            if (userSound!!.soundData.id == globalViewModel_!!.currentSoundToBeAdded!!.id) {
                userAlreadyHasSound = true
            }
        }
        if (!userAlreadyHasSound) {
            UserSoundBackend.createUserSoundObject(
                globalViewModel_!!.currentSoundToBeAdded!!
            ) {}
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun setUpRoutineAndUserPresetAfterMakingNewRoutine(
    routineData: RoutineData,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    RoutinePresetBackend.createRoutinePresetObject(
        globalViewModel_!!.currentPresetToBeAdded!!,
        routineData
    ) {
        //setup user sound
        if (globalViewModel_!!.currentPresetToBeAdded != null) {
            if (globalViewModel_!!.currentUser != null) {
                var userAlreadyHasPreset = false
                for (userPreset in globalViewModel_!!.currentUser!!.presets) {
                    if (
                        userPreset.presetData.id == globalViewModel_!!.currentPresetToBeAdded!!.id &&
                        userPreset.userData.id == globalViewModel_!!.currentUser!!.id
                    ) {
                        userAlreadyHasPreset = true
                    }
                }
                if (!userAlreadyHasPreset) {
                    UserPresetBackend.createUserPresetObject(
                        globalViewModel_!!.currentPresetToBeAdded!!
                    ) {

                    }
                }
            }
        }
        closeBottomSheet(scope, state)
        openSavedElementDialogBox = true
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun setUpRoutineAndUserSoundAfterMakingNewRoutine(
    routineData: RoutineData,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    RoutineSoundBackend.createRoutineSoundObject(
        globalViewModel_!!.currentSoundToBeAdded!!,
        routineData
    ) {
        //setup user sound
        if (globalViewModel_!!.currentSoundToBeAdded != null) {
            if (globalViewModel_!!.currentUser != null) {
                var userAlreadyHasSound = false
                for (userSound in globalViewModel_!!.currentUser!!.sounds) {
                    if (
                        userSound.soundData.id == globalViewModel_!!.currentSoundToBeAdded!!.id &&
                        userSound.userData.id == globalViewModel_!!.currentUser!!.id
                    ) {
                        userAlreadyHasSound = true
                    }
                }
                if (!userAlreadyHasSound) {
                    UserSoundBackend.createUserSoundObject(
                        globalViewModel_!!.currentSoundToBeAdded!!
                    ) {

                    }
                }
            }
        }
    }
}

fun setUpUserPreset(completed: () -> Unit){
    if (globalViewModel_!!.currentUser != null) {
        var userAlreadyHasPreset = false
        for (userPreset in globalViewModel_!!.currentUser!!.presets) {
            if (
                userPreset.presetData.id == globalViewModel_!!.currentPresetToBeAdded!!.id &&
                userPreset.userData.id == globalViewModel_!!.currentUser!!.id
            ) {
                userAlreadyHasPreset = true
            }
        }
        if (!userAlreadyHasPreset) {
            UserPresetBackend.createUserPresetObject(globalViewModel_!!.currentPresetToBeAdded!!) {
                completed()
            }
        }
    }
}

private fun makePrivatePresetObject(completed: (presetData: PresetData) -> Unit){
    val preset = PresetObject.Preset(
        UUID.randomUUID().toString(),
        UserObject.User.from(globalViewModel_!!.currentUser!!),
        globalViewModel_!!.presetNameToBeCreated,
        sliderVolumes!!.toList(),
        SoundObject.Sound.from(globalViewModel_!!.currentSoundToBeAdded!!),
        PresetPublicityStatus.PRIVATE
    )

    PresetBackend.createPreset(preset) { presetData ->
        if (globalViewModel_!!.currentUser != null) {
            var userAlreadyHasPreset = false
            for (userPreset in globalViewModel_!!.currentUser!!.presets) {
                if (
                    userPreset.presetData.id == presetData.id &&
                    userPreset.userData.id == globalViewModel_!!.currentUser!!.id
                ) {
                    userAlreadyHasPreset = true
                }
            }
            if (!userAlreadyHasPreset) {
                UserPresetBackend.createUserPresetObject(presetData) {
                    globalViewModel_!!.currentPresetToBeAdded = presetData
                    if(globalViewModel_!!.currentAllUserSoundPreset == null) {
                        globalViewModel_!!.currentAllUserSoundPreset = mutableListOf()
                    }
                    globalViewModel_!!.currentAllUserSoundPreset!!.add(presetData)
                    allUserSoundPresets!!.add(presetData)
                    completed(presetData)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InputRoutineName(
    scope: CoroutineScope,
    state: ModalBottomSheetState
): String {
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openUserAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "You already have this sound")
    }
    if(openRoutineAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "Routine already has this sound")
    }
    var name by rememberSaveable{ mutableStateOf("") }
    ConstraintLayout(
        modifier = Modifier
            .background(OldLace)
    ) {
        val (
            title,
            routineName,
            done
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        ) {
            NormalText(
                text = "Name this routine",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(routineName) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        ) {
            name = standardOutlinedTextInputMax30(211, 50, "Routine name", 0)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(done) {
                    top.linkTo(routineName.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
        ) {
            StandardBlueButton("Routine name"){
                if(globalViewModel_!!.routineNameToBeAdded != ""){
                    globalViewModel_!!.bottomSheetOpenFor = "selectRoutineColor"
                    openBottomSheet(scope, state)
                }
            }
        }
    }
    return name
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InputPresetName(
    scope: CoroutineScope,
    state: ModalBottomSheetState
): String {
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openUserAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "You already have this sound")
    }
    if(openRoutineAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "Routine already has this sound")
    }
    var name by rememberSaveable{ mutableStateOf("") }
    ConstraintLayout(
        modifier = Modifier
            .background(OldLace)
    ) {
        val (
            title,
            routineName,
            done
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        ) {
            NormalText(
                text = "Name this preset",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(routineName) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        ) {
            name = standardOutlinedTextInputMax30(211, 50, "Preset name", 0)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(done) {
                    top.linkTo(routineName.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
        ) {
            StandardBlueButton("Preset name"){
                if(globalViewModel_!!.presetNameToBeCreated != ""){
                    globalViewModel_!!.bottomSheetOpenFor = "addToRoutine"
                    openBottomSheet(scope, state)
                }
            }
        }
    }
    return name
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectRoutineColor(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openUserAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "You already have this sound")
    }
    if(openRoutineAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "Routine already has this sound")
    }
    val allColors = listOf(
        0xFFC6413C,
        0xFF264190,
        0xFFEFEDE7,
        0xFFDAD4EC,
        0xFFF3E7E9,
        0xFFEBBA9A,
        0xFFBCDBDB,
        0xFFB1BEE3,
        0xFFEEB9CE,
        0xFF576693,
        0xFFA9ECC4,
        0xFFEB9A9A,
        0xFFBC8450,
        0xFF706969,
        0xFF78A6BA,
        0xFFE3EEFF,
        0xFFEDD7D5,
        0xFFFEE5B6,
        0xFFF6EAE3,
        0xFFF6D7DE,
        0xFFFAE3EC,
        0xFFFBC2EB,
        0xFFE6DEE9,
    )
    ConstraintLayout(
        modifier = Modifier
            .background(OldLace)
    ) {
        val (
            title,
            colors,
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        ) {
            NormalText(
                text = "Select a color for your routine",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        SimpleFlowRow(
            verticalGap = 8.dp,
            horizontalGap = 8.dp,
            alignment = Alignment.Start,
            modifier = Modifier
                .constrainAs(colors) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
        ) {
            allColors.forEach{ color ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .clickable {
                            globalViewModel_!!.routineColorToBeAdded = color
                            globalViewModel_!!.bottomSheetOpenFor = "selectRoutineIcon"
                            openBottomSheet(scope, state)
                        }
                ) {
                    Box{
                        Card(
                            modifier = Modifier
                                .size(71.dp, 71.dp)
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            backgroundColor = Color(color),
                            elevation = 8.dp
                        ) {
                            /*AnImageWithColor(
                                id = R.drawable.icon_square,
                                contentDescription = "routine color",
                                color = Color(color),
                                width = 25.64.dp,
                                height = 25.64.dp,
                                xOffset = 0,
                                yOffset = 0
                            ) {

                            }*/
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectRoutineIcon(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openUserAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "You already have this sound")
    }
    if(openRoutineAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "Routine already has this sound")
    }
    ConstraintLayout(
        modifier = Modifier
            .background(OldLace)
    ) {
        val (
            title,
            colors,
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        ) {
            NormalText(
                text = "Select an icon for your routine",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        SimpleFlowRow(
            verticalGap = 8.dp,
            horizontalGap = 8.dp,
            alignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(colors) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
        ) {
            globalViewModel_!!.soundScreenIcons.forEach{ icon ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .clickable {
                            globalViewModel_!!.routineIconToBeAdded = icon.value
                            val routine = RoutineObject.Routine(
                                UUID.randomUUID().toString(),
                                UserObject.signedInUser().value!!,
                                globalViewModel_!!.routineNameToBeAdded,
                                globalViewModel_!!.routineNameToBeAdded,
                                2,
                                0,
                                globalViewModel_!!.currentSoundPlaying!!.fullPlayTime.toLong(),
                                globalViewModel_!!.routineIconToBeAdded!!,
                                false,
                                globalViewModel_!!.routineColorToBeAdded!!.toInt(),
                                true,
                                true,
                                true,
                                true,
                                true,
                                true,
                                globalViewModel_!!.currentSoundPlaying!!.fullPlayTime,
                                0,
                                0,
                                0,
                                0,
                                0,
                                -1,
                                -1,
                                -1,
                                -1,
                                -1,
                                -1,
                                listOf("sleep", "sound")
                            )
                            RoutineBackend.createRoutine(routine) {
                                UserRoutineBackend.createUserRoutineObject(it) { userRoutine ->
                                    Log.i(TAG, "1123. $userRoutine")
                                }

                                setUpRoutineAndUserSoundAfterMakingNewRoutine(
                                    it,
                                    scope,
                                    state
                                )

                                if (globalViewModel_!!.currentPresetToBeAdded == null) {
                                    makePrivatePresetObject { preset ->
                                        setUpRoutineAndUserPresetAfterMakingNewRoutine(
                                            it,
                                            scope,
                                            state
                                        )
                                    }
                                } else{
                                    setUpRoutineAndUserPresetAfterMakingNewRoutine(
                                        it,
                                        scope,
                                        state
                                    )
                                }
                            }
                        }
                ) {
                    Box{
                        Card(
                            modifier = Modifier
                                .size(71.dp, 71.dp)
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            backgroundColor = Color(globalViewModel_!!.routineColorToBeAdded!!),
                            elevation = 8.dp
                        ) {
                            Image(
                                painter = painterResource(id = icon.value),
                                contentDescription = "color",
                                modifier = Modifier
                                    .size(width = 25.64.dp, height = 25.64.dp)
                                    .padding(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun createUserSoundIfUserDoesNotAlreadyHaveSoundFromBottomSheet(
    soundData: SoundData,
    scope: CoroutineScope,
    state: ModalBottomSheetState
):String{
    var userAlreadyHasSound = false
    var str = ""
    for(userSound in globalViewModel_!!.currentUser!!.sounds){
        if(
            userSound.soundData == soundData &&
            userSound.userData == globalViewModel_!!.currentUser
        ){
            userAlreadyHasSound = true
        }
    }
    Log.i(TAG, "User sound boolean is $userAlreadyHasSound")
    if(!userAlreadyHasSound){
        UserSoundBackend.createUserSoundObject(soundData){
            closeBottomSheet(scope, state)
            str = "openSavedSoundDialogBox"
        }
    }else {
        closeBottomSheet(scope, state)
        str = "openUserAlreadyHasSoundDialogBox"
    }
    return str
}