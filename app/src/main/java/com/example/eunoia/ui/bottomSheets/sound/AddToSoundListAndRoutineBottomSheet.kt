package com.example.eunoia.ui.bottomSheets.sound

import androidx.compose.foundation.*
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
import com.example.eunoia.models.*
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.displayRoutineNameForBottomSheet
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "AddToSoundListAndRoutineBottomSheet"
var userRoutinesSize = 0
private const val MAX_ROUTINE_PLAYTIME = 2_700_000

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToSoundListAndRoutineBottomSheet(
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    SetUpAlertDialogs()
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
                        addToSoundListClicked(scope, state)
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
                        if (globalViewModel_!!.currentPresetToBeAdded == null) {
                            globalViewModel_!!.bottomSheetOpenFor = "inputPresetName"
                        } else {
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
private fun addToSoundListClicked(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if (globalViewModel_!!.currentSoundToBeAdded != null) {
        if (globalViewModel_!!.currentUser != null) {
            UserSoundRelationshipBackend.queryUserSoundRelationshipBasedOnUserAndSound(
                globalViewModel_!!.currentUser!!,
                globalViewModel_!!.currentSoundToBeAdded!!
            ){
                if(it.isEmpty()){
                    UserSoundRelationshipBackend.createUserSoundRelationshipObject(
                        globalViewModel_!!.currentSoundToBeAdded!!
                    ) {
                        UserSoundBackend.createUserSoundObject(
                            globalViewModel_!!.currentSoundToBeAdded!!
                        ) {
                            closeBottomSheet(scope, state)
                            openSavedElementDialogBox = true
                        }
                    }
                }else{
                    closeBottomSheet(scope, state)
                    openUserAlreadyHasSoundDialogBox = true
                }
            }
        }
    }
}

@Composable
private fun SetUpAlertDialogs(){
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openUserAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "You already have this sound")
    }
    if(openRoutineAlreadyHasSoundDialogBox){
        AlertDialogBox(text = "Routine already has this sound")
    }
    if(openRoutineAlreadyHasPresetDialogBox){
        AlertDialogBox(text = "Routine already has this preset")
    }
    if(openRoutineNameIsAlreadyTakenDialog){
        AlertDialogBox("Routine name already exists")
    }
    if(openPresetNameIsAlreadyTakenDialog){
        AlertDialogBox("Preset name already exists")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectRoutineForSound(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    SetUpAlertDialogs()
    val userRoutineRelationships = remember{ mutableStateOf(mutableListOf<UserRoutineRelationship?>()) }
    UserRoutineRelationshipBackend.queryUserRoutineRelationshipBasedOnUser(globalViewModel_!!.currentUser!!){
        com.example.eunoia.ui.bottomSheets.prayer.userRoutinesSize = it.size
        userRoutineRelationships.value = it.toMutableList()
    }

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
                if(userRoutineRelationships.value.size > 0 && userRoutineRelationships.value.size == userRoutinesSize){
                    userRoutineRelationships.value.forEach{ userRoutineRelationship ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .clickable {
                                    selectRoutineClicked(
                                        userRoutineRelationship!!,
                                        scope,
                                        state
                                    )
                                }
                        ) {
                            Box{
                                Card(
                                    modifier = Modifier
                                        .size(71.dp, 71.dp)
                                        .fillMaxWidth(),
                                    shape = MaterialTheme.shapes.small,
                                    backgroundColor = Color(userRoutineRelationship!!.userRoutineRelationshipRoutine.colorHex),
                                    elevation = 8.dp
                                ) {
                                    Image(
                                        painter = painterResource(id = userRoutineRelationship.userRoutineRelationshipRoutine.icon),
                                        contentDescription = "routine icon",
                                        modifier = Modifier
                                            .size(width = 25.64.dp, height = 25.64.dp)
                                            .padding(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            val goodDisplayName = displayRoutineNameForBottomSheet(userRoutineRelationship!!)
                            AlignedNormalText(
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
}

@OptIn(ExperimentalMaterialApi::class)
private fun selectRoutineClicked(
    userRoutineRelationship: UserRoutineRelationship,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if (globalViewModel_!!.currentPresetToBeAdded == null) {
        makePrivatePresetObject{
            setUpRoutinePreset(userRoutineRelationship, {}){
                setUpUserRoutineRelationshipSound(userRoutineRelationship) {
                    setUpUserSoundRelationship {
                        setUpUserSound {
                            closeBottomSheet(scope, state)
                            openSavedElementDialogBox = true
                        }
                    }
                }
            }
        }
    } else {
        setUpRoutinePreset(
            userRoutineRelationship,
            {
                closeBottomSheet(scope, state)
                openRoutineAlreadyHasPresetDialogBox = true
            }
        ){
            setUpUserRoutineRelationshipSound(userRoutineRelationship) {
                setUpUserSoundRelationship {
                    setUpUserSound {
                        setUpUserPresetRelationship {
                            setUpUserPreset {
                                closeBottomSheet(scope, state)
                                openSavedElementDialogBox = true
                            }
                        }
                    }
                }
            }
        }
    }
}

fun setUpRoutinePreset(
    userRoutineRelationship: UserRoutineRelationship,
    alreadyExists: () -> Unit,
    completed: () -> Unit,
) {
    UserRoutineRelationshipSoundPresetBackend.queryUserRoutineRelationshipSoundPresetBasedOnRoutineAndSoundPreset(
        userRoutineRelationship,
        globalViewModel_!!.currentPresetToBeAdded!!
    ){
        if (it.isEmpty()) {
            UserRoutineRelationshipSoundPresetBackend.createUserRoutineRelationshipSoundPresetObject(
                globalViewModel_!!.currentPresetToBeAdded!!,
                userRoutineRelationship
            ) {
                completed()
            }
        } else {
            alreadyExists()
        }
    }
}

fun setUpUserRoutineRelationshipSound(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
){
    UserRoutineRelationshipSoundBackend.queryUserRoutineRelationshipSoundBasedOnUserRoutineRelationshipAndSound(
        userRoutineRelationship,
        globalViewModel_!!.currentSoundToBeAdded!!
    ){
        if (it.isEmpty()) {
            if (!userRoutineRelationship.playingOrder.contains("sound")) {
                updateUserRoutineRelationshipThatDoesNotContainSound(userRoutineRelationship){
                    completed()
                }
            }else{
                updateUserRoutineRelationshipThatContainsSound(userRoutineRelationship){
                    completed()
                }
            }
        }else{
            completed()
        }
    }
}

private fun updateUserRoutineRelationshipThatDoesNotContainSound(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
) {
    val playOrder = userRoutineRelationship.playingOrder
    playOrder.add("sound")

    val numberOfSteps = userRoutineRelationship.numberOfSteps + 1

    val newUserRoutineRelationship = userRoutineRelationship.copyOfBuilder()
        .numberOfSteps(numberOfSteps)
        .playingOrder(playOrder)
        .build()

    UserRoutineRelationshipBackend.updateUserRoutineRelationship(newUserRoutineRelationship) { updatedUserRoutineRelationship ->
        UserRoutineRelationshipSoundBackend.createUserRoutineRelationshipSoundObject(
            globalViewModel_!!.currentSoundToBeAdded!!,
            updatedUserRoutineRelationship
        ) {
            RoutineSoundBackend.createRoutineSoundObject(
                globalViewModel_!!.currentSoundToBeAdded!!,
                updatedUserRoutineRelationship.userRoutineRelationshipRoutine
            ) {
                completed()
            }
        }
    }
}

private fun updateUserRoutineRelationshipThatContainsSound(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
) {
    UserRoutineRelationshipSoundBackend.createUserRoutineRelationshipSoundObject(
        globalViewModel_!!.currentSoundToBeAdded!!,
        userRoutineRelationship
    ) {
        RoutineSoundBackend.createRoutineSoundObject(
            globalViewModel_!!.currentSoundToBeAdded!!,
            userRoutineRelationship.userRoutineRelationshipRoutine
        ) {
            completed()
        }
    }
}

fun setUpUserSound(completed: () -> Unit){
    UserSoundBackend.queryUserSoundBasedOnSoundAndUser(
        globalViewModel_!!.currentUser!!,
        globalViewModel_!!.currentSoundToBeAdded!!
    ){
        if(it.isEmpty()){
            UserSoundBackend.createUserSoundObject(
                globalViewModel_!!.currentSoundToBeAdded!!
            ) {
                completed()
            }
        }else{
            completed()
        }
    }
}

fun setUpUserSoundRelationship(completed: () -> Unit){
    UserSoundRelationshipBackend.queryUserSoundRelationshipBasedOnUserAndSound(
        globalViewModel_!!.currentUser!!,
        globalViewModel_!!.currentSoundToBeAdded!!
    ){
        if(it.isEmpty()){
            UserSoundRelationshipBackend.createUserSoundRelationshipObject(
                globalViewModel_!!.currentSoundToBeAdded!!
            ) {
                completed()
            }
        }else{
            completed()
        }
    }
}

fun setUpRoutineAndUserPresetAfterMakingNewRoutine(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
){
    UserRoutineRelationshipSoundPresetBackend.createUserRoutineRelationshipSoundPresetObject(
        globalViewModel_!!.currentPresetToBeAdded!!,
        userRoutineRelationship
    ) {
        RoutineSoundPresetBackend.createRoutineSoundPresetObject(
            globalViewModel_!!.currentPresetToBeAdded!!,
            userRoutineRelationship.userRoutineRelationshipRoutine
        ) {
            setUpUserPresetRelationship {
                setUpUserPreset {
                    completed()
                }
            }
        }
    }
}

fun setUpRoutineAndUserSoundAfterMakingNewRoutine(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
){
    UserRoutineRelationshipSoundBackend.createUserRoutineRelationshipSoundObject(
        globalViewModel_!!.currentSoundToBeAdded!!,
        userRoutineRelationship
    ) {
        RoutineSoundBackend.createRoutineSoundObject(
            globalViewModel_!!.currentSoundToBeAdded!!,
            userRoutineRelationship.userRoutineRelationshipRoutine
        ) {
            setUpUserSoundRelationship {
                setUpUserSound {
                    completed()
                }
            }
        }
    }
}

fun setUpUserPreset(
    completed: () -> Unit
){
    UserSoundPresetBackend.queryUserSoundPresetBasedOnUserAndSoundPreset(
        globalViewModel_!!.currentUser!!,
        globalViewModel_!!.currentPresetToBeAdded!!
    ){
        if(it.isEmpty()){
            UserSoundPresetBackend.createUserSoundPresetObject(
                globalViewModel_!!.currentPresetToBeAdded!!
            ) {
                completed()
            }
        }else{
            completed()
        }
    }
}

fun setUpUserPresetRelationship(
    completed: () -> Unit
){
    UserSoundPresetRelationshipBackend.queryUserSoundPresetRelationshipBasedOnUserAndSoundPreset(
        globalViewModel_!!.currentUser!!,
        globalViewModel_!!.currentPresetToBeAdded!!
    ){
        if(it.isEmpty()){
            UserSoundPresetRelationshipBackend.createUserSoundPresetRelationshipObject(
                globalViewModel_!!.currentPresetToBeAdded!!
            ) {
                completed()
            }
        }else{
            completed()
        }
    }
}

private fun makePrivatePresetObject(
    completed: (presetData: SoundPresetData) -> Unit
){
    val preset = SoundPresetObject.SoundPreset(
        UUID.randomUUID().toString(),
        UserObject.User.from(globalViewModel_!!.currentUser!!),
        globalViewModel_!!.currentUser!!.id,
        globalViewModel_!!.presetNameToBeCreated,
        sliderVolumes!!.toList(),
        globalViewModel_!!.currentSoundToBeAdded!!.id,
        SoundPresetPublicityStatus.PRIVATE
    )

    SoundPresetBackend.createSoundPreset(preset) { presetData ->
        UserSoundPresetRelationshipBackend.createUserSoundPresetRelationshipObject(presetData) {
            UserSoundPresetBackend.createUserSoundPresetObject(presetData) {
                globalViewModel_!!.currentPresetToBeAdded = presetData
                if (globalViewModel_!!.currentAllUserSoundPreset == null) {
                    globalViewModel_!!.currentAllUserSoundPreset = mutableSetOf()
                }
                globalViewModel_!!.currentAllUserSoundPreset!!.add(presetData)
                allUserSoundPresets!!.add(presetData)
                completed(presetData)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun inputRoutineName(
    scope: CoroutineScope,
    state: ModalBottomSheetState
): String {
    SetUpAlertDialogs()
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
                if(name.isNotEmpty()){
                    checkIfRoutineNameIsTaken{
                        if(!it){
                            globalViewModel_!!.bottomSheetOpenFor = "selectRoutineColor"
                            openBottomSheet(scope, state)
                        }else{
                            openRoutineNameIsAlreadyTakenDialog = true
                        }
                    }
                }
            }
        }
    }
    return name
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun inputPresetName(
    scope: CoroutineScope,
    state: ModalBottomSheetState
): String {
    SetUpAlertDialogs()
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
                    checkIfPresetNameIsTaken{
                        if(!it){
                            globalViewModel_!!.bottomSheetOpenFor = "addToRoutine"
                            openBottomSheet(scope, state)
                        }else{
                            openPresetNameIsAlreadyTakenDialog = true
                        }
                    }
                }
            }
        }
    }
    return name
}

private fun checkIfPresetNameIsTaken(completed: (bool: Boolean) -> Unit){
    SoundPresetBackend.queryPublicSoundPresetsBasedOnDisplayNameAndSound(
        globalViewModel_!!.presetNameToBeCreated,
        globalViewModel_!!.currentSoundToBeAdded!!
    ) {
        if(it.isEmpty()){
            completed(false)
        }else{
            completed(true)
        }
    }
}

fun checkIfRoutineNameIsTaken(completed: (bool: Boolean) -> Unit){
    RoutineBackend.queryRoutinesBasedOnDisplayName(
        globalViewModel_!!.routineNameToBeAdded
    ) {
        if(it.isEmpty()){
            completed(false)
        }else{
            completed(true)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectRoutineColor(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
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
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .background(OldLace)
            .verticalScroll(scrollState)
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
                        ) {}
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
    SetUpAlertDialogs()
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
                            newRoutineIconSelected(
                                icon.value,
                                scope,
                                state
                            )
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
private fun newRoutineIconSelected(
    icon: Int,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    globalViewModel_!!.routineIconToBeAdded = icon

    //45' default playtime
    var playTime = MAX_ROUTINE_PLAYTIME
    if(globalViewModel_!!.currentSoundToBeAdded!!.fullPlayTime < playTime){
        playTime = globalViewModel_!!.currentSoundToBeAdded!!.fullPlayTime
    }

    val routine = RoutineObject.Routine(
        id = UUID.randomUUID().toString(),
        routineOwner = UserObject.signedInUser().value!!,
        routineOwnerId = UserObject.signedInUser().value!!.id,
        originalName = globalViewModel_!!.routineNameToBeAdded,
        displayName = globalViewModel_!!.routineNameToBeAdded,
        numberOfSteps = 2,
        fullPlayTime = playTime.toLong(),
        icon = globalViewModel_!!.routineIconToBeAdded!!,
        visibleToOthers = false,
        colorHex = globalViewModel_!!.routineColorToBeAdded!!.toInt(),
        playSoundDuringStretch = true,
        playSoundDuringPrayer = true,
        playSoundDuringBreathing = true,
        playSoundDuringSelfLove = true,
        playSoundDuringBedtimeStory = true,
        playSoundDuringSleep = true,
        eachSoundPlayTime = 900000,
        prayerPlayTime = 300000,
        bedtimeStoryPlayTime = 1200000,
        selfLovePlayTime = 600000,
        stretchTime = 600000,
        breathingTime = 600000,
        currentBedtimeStoryPlayingIndex = -1,
        currentBedtimeStoryContinuePlayingTime = -1,
        currentSelfLovePlayingIndex = -1,
        currentSelfLoveContinuePlayingTime = -1,
        currentPrayerPlayingIndex = -1,
        currentPrayerContinuePlayingTime = -1,
        playingOrder = listOf("sleep", "sound")
    )

    createRoutineAndOtherNecessaryData(
        routine,
        scope,
        state
    )
}

@OptIn(ExperimentalMaterialApi::class)
private fun createRoutineAndOtherNecessaryData(
    routine: RoutineObject.Routine,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    RoutineBackend.createRoutine(routine) { newRoutine ->
        UserRoutineRelationshipBackend.createUserRoutineRelationshipObject(newRoutine) { updatedUserRoutineRelationship ->
            UserRoutineBackend.createUserRoutineObject(newRoutine) {
                setUpRoutineAndUserSoundAfterMakingNewRoutine(updatedUserRoutineRelationship) {
                    if (globalViewModel_!!.currentPresetToBeAdded == null) {
                        makePrivatePresetObject {
                            closeBottomSheet(scope, state)
                            openSavedElementDialogBox = true
                        }
                    } else {
                        setUpRoutineAndUserPresetAfterMakingNewRoutine(updatedUserRoutineRelationship) {
                            closeBottomSheet(scope, state)
                            openSavedElementDialogBox = true
                        }
                    }
                }
            }
        }
    }
}
