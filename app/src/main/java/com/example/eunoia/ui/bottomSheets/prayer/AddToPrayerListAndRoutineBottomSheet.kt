package com.example.eunoia.ui.bottomSheets.prayer

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
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.closeBottomSheet
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.sound.checkIfRoutineNameIsTaken
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.OldLace
import com.example.eunoia.ui.theme.SwansDown
import com.example.eunoia.utils.displayRoutineNameForBottomSheet
import kotlinx.coroutines.CoroutineScope
import java.util.*

private const val TAG = "AddToPrayerListAndRoutineBottomSheet"
var userRoutinesSize = 0
private const val MAX_ROUTINE_PLAYTIME = 2_700_000
private const val MAX_PRAYER_PLAYTIME = 300_000

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToPrayerListAndRoutineBottomSheet(
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
                prayerList,
                divider,
                routineItems
            ) = createRefs()
            ConstraintLayout(
                modifier = Modifier
                    .background(OldLace)
                    .constrainAs(prayerList) {
                        top.linkTo(parent.top, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(divider.top, margin = 0.dp)
                    }
                    .clickable {
                        addToPrayerListClicked(scope, state)
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
                        text = "Add to your prayer list",
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
                        globalViewModel!!.bottomSheetOpenFor = "addPrayerToRoutine"
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

@Composable
private fun SetUpAlertDialogs(){
    if(openSavedElementDialogBox){
        AlertDialogBox("Saved!")
    }
    if(openRoutineNameIsAlreadyTakenDialog){
        AlertDialogBox("Routine name already exists")
    }
    if(openUserAlreadyHasPrayerDialogBox){
        AlertDialogBox(text = "You already have this prayer")
    }
    if(openRoutineAlreadyHasPrayerDialogBox){
        AlertDialogBox(text = "Routine already has this prayer")
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun addToPrayerListClicked(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    if (prayerViewModel!!.currentPrayerToBeAdded != null) {
        if (globalViewModel!!.currentUser != null) {
            UserPrayerRelationshipBackend.queryUserPrayerRelationshipBasedOnUserAndPrayer(
                globalViewModel!!.currentUser!!,
                prayerViewModel!!.currentPrayerToBeAdded!!
            ){
                if (it.isEmpty()) {
                    UserPrayerRelationshipBackend.createUserPrayerRelationshipObject(
                        prayerViewModel!!.currentPrayerToBeAdded!!
                    ) {
                        UserPrayerBackend.createUserPrayerObject(
                            prayerViewModel!!.currentPrayerToBeAdded!!
                        ) {
                            closeBottomSheet(scope, state)
                            openSavedElementDialogBox = true
                        }
                    }
                }else{
                    closeBottomSheet(scope, state)
                    openUserAlreadyHasPrayerDialogBox = true
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectRoutineForPrayer(
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    SetUpAlertDialogs()
    val userRoutineRelationships = remember{ mutableStateOf(mutableListOf<UserRoutineRelationship?>()) }
    UserRoutineRelationshipBackend.queryUserRoutineRelationshipBasedOnUser(globalViewModel!!.currentUser!!){
        userRoutinesSize = it.size
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
                        globalViewModel!!.bottomSheetOpenFor = "inputRoutineNameForPrayer"
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
    setUpUserRoutineRelationshipPrayer(
        userRoutineRelationship,
        {
            closeBottomSheet(scope, state)
            openRoutineAlreadyHasPrayerDialogBox = true
        }
    ) {
        setUpUserPrayerRelationship {
            setUpUserPrayer {
                closeBottomSheet(scope, state)
                openSavedElementDialogBox = true
            }
        }
    }
}

fun setUpUserRoutineRelationshipPrayer(
    userRoutineRelationship: UserRoutineRelationship,
    alreadyExists: () -> Unit,
    completed: () -> Unit
) {
    UserRoutineRelationshipPrayerBackend.queryUserRoutineRelationshipPrayerBasedOnPrayerAndUserRoutineRelationship(
        userRoutineRelationship,
        prayerViewModel!!.currentPrayerToBeAdded!!
    ){
        if (it.isEmpty()) {
            if (!userRoutineRelationship.playingOrder.contains("prayer")) {
                updateUserRoutineRelationshipThatDoesNotContainPrayer(userRoutineRelationship){
                    completed()
                }
            }else{
                updateUserRoutineRelationshipThatContainsPrayer(userRoutineRelationship){
                    completed()
                }
            }
        }else{
            alreadyExists()
        }
    }
}

private fun updateUserRoutineRelationshipThatDoesNotContainPrayer(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
) {
    val playOrder = userRoutineRelationship.playingOrder
    playOrder.add("prayer")

    //updated play time should be <= 45'
    var playTime = userRoutineRelationship.fullPlayTime
    if(playTime < MAX_ROUTINE_PLAYTIME && prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime > playTime) {
        playTime = prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime
        if (playTime > MAX_ROUTINE_PLAYTIME) {
            playTime = MAX_ROUTINE_PLAYTIME
        }
    }

    var prayerPlaytime = prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime
    if(prayerPlaytime > MAX_PRAYER_PLAYTIME){
        prayerPlaytime = MAX_PRAYER_PLAYTIME
    }

    val numberOfSteps = userRoutineRelationship.numberOfSteps + 1

    val newUserRoutineRelationship = userRoutineRelationship.copyOfBuilder()
        .numberOfSteps(numberOfSteps)
        .fullPlayTime(playTime)
        .prayerPlayTime(prayerPlaytime)
        .currentPrayerPlayingIndex(0)
        .currentPrayerContinuePlayingTime(0)
        .playingOrder(playOrder)
        .build()

    UserRoutineRelationshipBackend.updateUserRoutineRelationship(newUserRoutineRelationship) { updatedUserRoutineRelationship ->
        UserRoutineRelationshipPrayerBackend.createUserRoutineRelationshipPrayerObject(
            prayerViewModel!!.currentPrayerToBeAdded!!,
            updatedUserRoutineRelationship
        ) {
            RoutinePrayerBackend.createRoutinePrayerObject(
                prayerViewModel!!.currentPrayerToBeAdded!!,
                updatedUserRoutineRelationship.userRoutineRelationshipRoutine
            ) {
                completed()
            }
        }
    }
}

private fun updateUserRoutineRelationshipThatContainsPrayer(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
) {
    var playTime = userRoutineRelationship.fullPlayTime
    if(playTime < MAX_ROUTINE_PLAYTIME && prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime > playTime) {
        playTime = prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime
        if (playTime > MAX_ROUTINE_PLAYTIME) {
            playTime = MAX_ROUTINE_PLAYTIME
        }
    }

    val newUserRoutineRelationship = userRoutineRelationship.copyOfBuilder()
        .fullPlayTime(playTime)
        .build()

    UserRoutineRelationshipBackend.updateUserRoutineRelationship(newUserRoutineRelationship) { updatedUserRoutineRelationship ->
        UserRoutineRelationshipPrayerBackend.createUserRoutineRelationshipPrayerObject(
            prayerViewModel!!.currentPrayerToBeAdded!!,
            updatedUserRoutineRelationship
        ) {
            RoutinePrayerBackend.createRoutinePrayerObject(
                prayerViewModel!!.currentPrayerToBeAdded!!,
                updatedUserRoutineRelationship.userRoutineRelationshipRoutine
            ) {
                completed()
            }
        }
    }
}

fun setUpUserPrayer(completed: () -> Unit) {
    UserPrayerBackend.queryUserPrayerBasedOnUserAndPrayer(
        globalViewModel!!.currentUser!!,
        prayerViewModel!!.currentPrayerToBeAdded!!
    ){
        if (it.isEmpty()) {
            UserPrayerBackend.createUserPrayerObject(
                prayerViewModel!!.currentPrayerToBeAdded!!
            ) {
                completed()
            }
        }else{
            completed()
        }
    }
}

fun setUpUserPrayerRelationship(completed: () -> Unit) {
    UserPrayerRelationshipBackend.queryUserPrayerRelationshipBasedOnUserAndPrayer(
        globalViewModel!!.currentUser!!,
        prayerViewModel!!.currentPrayerToBeAdded!!
    ){
        if (it.isEmpty()) {
            UserPrayerRelationshipBackend.createUserPrayerRelationshipObject(
                prayerViewModel!!.currentPrayerToBeAdded!!
            ) {
                completed()
            }
        }else{
            completed()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun inputRoutineNameForPrayer(
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
                    checkIfRoutineNameIsTaken {
                        if (!it) {
                            globalViewModel!!.bottomSheetOpenFor = "selectRoutineColorForPrayer"
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
fun SelectRoutineColorForPrayer(
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
                            routineViewModel!!.routineColorToBeAdded = color
                            globalViewModel!!.bottomSheetOpenFor = "selectRoutineIconForPrayer"
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
fun SelectRoutineIconForPrayer(
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
            soundViewModel!!.soundScreenIcons.forEach{ icon ->
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
                            backgroundColor = Color(routineViewModel!!.routineColorToBeAdded!!),
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
    routineViewModel!!.routineIconToBeAdded = icon
    //45' default playtime
    var playTime = MAX_ROUTINE_PLAYTIME
    if(prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime < playTime){
        playTime = prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime
    }

    var prayerPlayTime = MAX_PRAYER_PLAYTIME
    if(prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime < prayerPlayTime){
        prayerPlayTime = prayerViewModel!!.currentPrayerToBeAdded!!.fullPlayTime
    }

    val routine = RoutineObject.Routine(
        id = UUID.randomUUID().toString(),
        routineOwner = UserObject.signedInUser().value!!,
        routineOwnerId = UserObject.signedInUser().value!!.id,
        originalName = routineViewModel!!.routineNameToBeAdded,
        displayName = routineViewModel!!.routineNameToBeAdded,
        numberOfSteps = 2,
        fullPlayTime = playTime.toLong(),
        icon = routineViewModel!!.routineIconToBeAdded!!,
        visibleToOthers = false,
        colorHex = routineViewModel!!.routineColorToBeAdded!!.toInt(),
        playSoundDuringStretch = true,
        playSoundDuringPrayer = true,
        playSoundDuringBreathing = true,
        playSoundDuringSelfLove = true,
        playSoundDuringBedtimeStory = true,
        playSoundDuringSleep = true,
        eachSoundPlayTime = 900000,
        prayerPlayTime = prayerPlayTime,
        bedtimeStoryPlayTime = 1200000,
        selfLovePlayTime = 600000,
        stretchTime = 600000,
        breathingTime = 600000,
        currentBedtimeStoryPlayingIndex = -1,
        currentBedtimeStoryContinuePlayingTime = -1,
        currentSelfLovePlayingIndex = -1,
        currentSelfLoveContinuePlayingTime = -1,
        currentPrayerPlayingIndex = 0,
        currentPrayerContinuePlayingTime = 0,
        playingOrder = listOf("sleep", "prayer")
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
                setUpRoutineAndUserPrayerAfterMakingNewRoutine(updatedUserRoutineRelationship) {
                    closeBottomSheet(scope, state)
                    openSavedElementDialogBox = true
                }
            }
        }
    }
}

fun setUpRoutineAndUserPrayerAfterMakingNewRoutine(
    userRoutineRelationship: UserRoutineRelationship,
    completed: () -> Unit
){
    UserRoutineRelationshipPrayerBackend.createUserRoutineRelationshipPrayerObject(
        prayerViewModel!!.currentPrayerToBeAdded!!,
        userRoutineRelationship
    ) {
        RoutinePrayerBackend.createRoutinePrayerObject(
            prayerViewModel!!.currentPrayerToBeAdded!!,
            userRoutineRelationship.userRoutineRelationshipRoutine
        ) {
            setUpUserPrayerRelationship {
                setUpUserPrayer {
                    completed()
                }
            }
        }
    }
}