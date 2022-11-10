package com.example.eunoia.create.createBedtimeStory

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.BedtimeStoryBackend
import com.example.eunoia.backend.UserBedtimeStoryBackend
import com.example.eunoia.backend.UserBedtimeStoryInfoRelationshipBackend
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

private var TAG = "NameBedtimeStoryUI"

var bedtimeStoryIcon by mutableStateOf(-1)
var bedtimeStoryName by mutableStateOf("")
var bedtimeStoryShortDescription by mutableStateOf("")
var bedtimeStoryLongDescription by mutableStateOf("")
var bedtimeStoryTags by mutableStateOf("")
var bedtimeStoryIconSelectionTitle by mutableStateOf("")
var bedtimeStoryNameErrorMessage by mutableStateOf("")
var bedtimeStoryShortDescriptionErrorMessage by mutableStateOf("")
var bedtimeStoryLongDescriptionErrorMessage by mutableStateOf("")
var bedtimeStoryTagsErrorMessage by mutableStateOf("")
private const val MIN_BEDTIME_STORY_NAME = 5
private const val MIN_BEDTIME_STORY_SHORT_DESCRIPTION = 10
private const val MIN_BEDTIME_STORY_LONG_DESCRIPTION = 50
private const val MIN_BEDTIME_STORY_TAGS = 3
private const val MAX_BEDTIME_STORY_NAME = 30
private const val MAX_BEDTIME_STORY_SHORT_DESCRIPTION = 50
private const val MAX_BEDTIME_STORY_LONG_DESCRIPTION = 200
private const val MAX_BEDTIME_STORY_TAGS = 50
var incompleteBedtimeStories = mutableListOf<MutableState<BedtimeStoryInfoData>?>()

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NameBedtimeStoryUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    clearPagesList()
    clearBedtimeStoryChaptersList()
    clearPageRecordingsList()
    SetupAlertDialogs()

    var numberOfIncompleteBedtimeStories by rememberSaveable { mutableStateOf(0) }
    BedtimeStoryBackend.queryIncompleteBedtimeStoryBasedOnUser(globalViewModel_!!.currentUser!!) {
        for (i in incompleteBedtimeStories.size until it.size) {
            incompleteBedtimeStories.add(mutableStateOf(it[i]!!))
        }
        numberOfIncompleteBedtimeStories = incompleteBedtimeStories.size
    }
    val scrollState = rememberScrollState()

    initializeBedtimeStoryNameError()
    initializeBedtimeStoryShortDescriptionError()
    initializeBedtimeStoryLongDescriptionError()
    initializeBedtimeStoryTagsError()
    initializeBedtimeStoryIconError()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            title,
            nameTitle,
            nameColumn,
            nameError,
            shortDescriptionTitle,
            shortDescriptionColumn,
            shortDescriptionError,
            longDescriptionTitle,
            longDescriptionColumn,
            longDescriptionError
        ) = createRefs()

        val (
            tagTitle,
            tagColumn,
            tagError,
            iconTitle,
            icons,
            inProgress,
            next,
            endSpace
        ) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            BackArrowHeader(
                {
                    resetAllBedtimeStoryCreationObjects()
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
                text = "Add a bedtime story",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(nameTitle) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = "Name",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(nameColumn) {
                    top.linkTo(nameTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            bedtimeStoryName = customizedOutlinedTextInput(
                maxLength = MAX_BEDTIME_STORY_NAME,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "eg. Mary's Blistered Toe",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(nameError) {
                    top.linkTo(nameColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = bedtimeStoryNameErrorMessage,
                color = BeautyBush,
                fontSize = 11,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(shortDescriptionTitle) {
                    top.linkTo(nameError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = "Get people excited in one sentence",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(shortDescriptionColumn) {
                    top.linkTo(shortDescriptionTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            bedtimeStoryShortDescription = customizedOutlinedTextInput(
                maxLength = MAX_BEDTIME_STORY_SHORT_DESCRIPTION,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "eg. When a man looses his mind over love",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(shortDescriptionError) {
                    top.linkTo(shortDescriptionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = bedtimeStoryShortDescriptionErrorMessage,
                color = BeautyBush,
                fontSize = 11,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(longDescriptionTitle) {
                    top.linkTo(shortDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = "Summarize this story",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(longDescriptionColumn) {
                    top.linkTo(longDescriptionTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            bedtimeStoryLongDescription = customizableBigOutlinedTextInput(
                maxLength = MAX_BEDTIME_STORY_LONG_DESCRIPTION,
                height = 100,
                placeholder = "Make it lengthy",
                backgroundColor = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                textColor = Black,
                placeholderColor = BeautyBush,
                placeholderTextSize = 16,
                inputFontSize = 16,
            ){}
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(longDescriptionError) {
                    top.linkTo(longDescriptionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = bedtimeStoryLongDescriptionErrorMessage,
                color = BeautyBush,
                fontSize = 11,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(tagTitle) {
                    top.linkTo(longDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = "Hashtags help users find your story",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(tagColumn) {
                    top.linkTo(tagTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            bedtimeStoryTags = customizedOutlinedTextInput(
                maxLength = MAX_BEDTIME_STORY_TAGS,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "eg. thriller, spy, dangerous",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(tagError) {
                    top.linkTo(tagColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = bedtimeStoryTagsErrorMessage,
                color = BeautyBush,
                fontSize = 11,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(iconTitle) {
                    top.linkTo(tagError.bottom, margin = 48.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            AlignedNormalText(
                text = bedtimeStoryIconSelectionTitle,
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        SimpleFlowRow(
            verticalGap = 8.dp,
            horizontalGap = 8.dp,
            alignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(icons) {
                    top.linkTo(iconTitle.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            val borders = mutableListOf<MutableState<Boolean>>()
            for(icon in globalViewModel_!!.soundScreenIcons){
                borders.add(remember { mutableStateOf(false) })
            }
            globalViewModel_!!.soundScreenIcons.forEachIndexed{ index, icon ->
                var cardModifier = Modifier
                    .padding(bottom = 15.dp)
                    .clickable {
                        borders.forEach { border ->
                            border.value = false
                        }
                        borders[index].value = !borders[index].value
                        bedtimeStoryIcon = icon.value
                    }

                if (borders[index].value) {
                    cardModifier = cardModifier.then(
                        Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = cardModifier
                ){
                    Box{
                        Card(
                            modifier = Modifier
                                .size(70.dp, 70.dp)
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            backgroundColor = White,
                            elevation = 8.dp
                        ) {
                            Image(
                                painter = painterResource(id = icon.value),
                                contentDescription = "bedtime story icon",
                                modifier = Modifier
                                    .size(width = 25.64.dp, height = 25.64.dp)
                                    .padding(20.dp)
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(next) {
                    top.linkTo(icons.bottom, margin = 32.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(
                bedtimeStoryName.length >= MIN_BEDTIME_STORY_NAME &&
                bedtimeStoryShortDescription.length >= MIN_BEDTIME_STORY_SHORT_DESCRIPTION &&
                bedtimeStoryLongDescription.length >= MIN_BEDTIME_STORY_LONG_DESCRIPTION &&
                bedtimeStoryTags.length >= MIN_BEDTIME_STORY_TAGS &&
                bedtimeStoryIcon != -1
            ) {
                CustomizableButton(
                    text = "Choose a file",
                    height = 55,
                    fontSize = 16,
                    textColor = Black,
                    backgroundColor = WePeep,
                    corner = 10,
                    borderStroke = 0.0,
                    borderColor = Black.copy(alpha = 0F),
                    textType = "light",
                    maxWidthFraction = 1F
                ) {
                    var otherBedtimeStoriesWithSameName by mutableStateOf(-1)
                    BedtimeStoryBackend.queryBedtimeStoryBasedOnDisplayName(bedtimeStoryName){
                        otherBedtimeStoriesWithSameName = if(it.isEmpty()) 0 else it.size
                    }
                    Thread.sleep(1_000)

                    if (otherBedtimeStoriesWithSameName < 1) {
                        runOnUiThread {
                            navigateToUploadBedtimeStory(
                                navController
                            )
                        }
                    }else{
                        openBedtimeStoryNameTakenDialogBox = true
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Or()
                Spacer(modifier = Modifier.height(8.dp))
                CustomizableButton(
                    text = "Record audio",
                    height = 55,
                    fontSize = 16,
                    textColor = Black,
                    backgroundColor = WePeep,
                    corner = 10,
                    borderStroke = 0.0,
                    borderColor = Black.copy(alpha = 0F),
                    textType = "light",
                    maxWidthFraction = 1F
                ) {
                    Log.i(TAG, "Go create bts")
                    createBedtimeStory(numberOfIncompleteBedtimeStories, navController)
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(inProgress) {
                    top.linkTo(next.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            ClickableNormalText(
                text = "Complete another bedtime story",
                color = Black,
                12,
                0,
                0
            ) {
                navController.navigate(Screen.IncompleteBedtimeStories.screen_route)
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

@Composable
private fun SetupAlertDialogs(){
    if(openBedtimeStoryNameTakenDialogBox){
        AlertDialogBox(text = "The name '$bedtimeStoryName' already exists")
    }
    if(openTooManyIncompleteBedtimeStoryDialogBox){
        AlertDialogBox(text = "You have three bedtime stories in progress already")
    }
}

private fun initializeBedtimeStoryNameError() {
    bedtimeStoryNameErrorMessage = if(bedtimeStoryName.isEmpty()){
        ""
    } else if(bedtimeStoryName.length < MIN_BEDTIME_STORY_NAME){
        "Must be at least $MIN_BEDTIME_STORY_NAME characters"
    } else{
        ""
    }
}

private fun initializeBedtimeStoryShortDescriptionError() {
    bedtimeStoryShortDescriptionErrorMessage = if(bedtimeStoryShortDescription.isEmpty()){
        ""
    } else if(bedtimeStoryShortDescription.length < MIN_BEDTIME_STORY_SHORT_DESCRIPTION){
        "Must be at least $MIN_BEDTIME_STORY_SHORT_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializeBedtimeStoryLongDescriptionError() {
    bedtimeStoryLongDescriptionErrorMessage = if(bedtimeStoryLongDescription.isEmpty()){
        ""
    } else if(bedtimeStoryLongDescription.length < MIN_BEDTIME_STORY_LONG_DESCRIPTION){
        "Must be at least $MIN_BEDTIME_STORY_LONG_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializeBedtimeStoryTagsError() {
    bedtimeStoryTagsErrorMessage = if(bedtimeStoryTags.isEmpty()){
        "Separate hashtags with a comma"
    } else if(bedtimeStoryTags.length < MIN_BEDTIME_STORY_TAGS){
        "Must be at least $MIN_BEDTIME_STORY_TAGS characters"
    } else{
        ""
    }
}

private fun initializeBedtimeStoryIconError() {
    bedtimeStoryIconSelectionTitle = if(bedtimeStoryIcon == -1){
        "Select icon"
    }else{
        "Icon selected"
    }
}

private fun createBedtimeStory(
    numberOfIncompleteBedtimeStories: Int,
    navController: NavController
){
    BedtimeStoryBackend.queryBedtimeStoryBasedOnDisplayName(bedtimeStoryName){
        val tags = getBedtimeStoryTagsList()

        Log.i(TAG, "numberOfIncompleteBedtimeStories -- $numberOfIncompleteBedtimeStories")
        if(numberOfIncompleteBedtimeStories < 3){
            Log.i(TAG, "Nxt 1")
            if (it.isEmpty()) {
                Log.i(TAG, "Nxt 2")
                val key = "Routine/" +
                        "BedtimeStories/" +
                        "${globalViewModel_!!.currentUser!!.username}/" +
                        "recorded/" +
                        "$bedtimeStoryName/"

                //TODO Compute full play time
                val bedtimeStory = BedtimeStoryObject.BedtimeStory(
                    UUID.randomUUID().toString(),
                    UserObject.User.from(globalViewModel_!!.currentUser!!),
                    globalViewModel_!!.currentUser!!.id,
                    bedtimeStoryName,
                    bedtimeStoryShortDescription,
                    bedtimeStoryLongDescription,
                    key,
                    bedtimeStoryIcon,
                    0,
                    false,
                    tags,
                    BedtimeStoryAudioSource.RECORDED,
                    BedtimeStoryApprovalStatus.PENDING,
                    BedtimeStoryCreationStatus.INCOMPLETE
                )
                BedtimeStoryBackend.createBedtimeStory(bedtimeStory) { bedtimeStoryData ->
                    UserBedtimeStoryInfoRelationshipBackend.createUserBedtimeStoryInfoRelationshipObject(bedtimeStoryData) {
                        UserBedtimeStoryBackend.createUserBedtimeStoryObject(
                            bedtimeStoryData
                        ) {
                            navigateToRecordBedtimeStory(
                                navController,
                                bedtimeStoryData
                            )
                        }
                    }
                }
            }else{
                openBedtimeStoryNameTakenDialogBox = true
            }
        }else{
            openTooManyIncompleteBedtimeStoryDialogBox = true
        }
    }
}

fun getBedtimeStoryTagsList():List<String> {
    return bedtimeStoryTags.split(",")
}

fun resetNameBedtimeStoryVariables(){
    bedtimeStoryIcon = -1
    bedtimeStoryName = ""
    bedtimeStoryShortDescription = ""
    bedtimeStoryTags = ""
    bedtimeStoryIconSelectionTitle = ""
    bedtimeStoryNameErrorMessage = ""
    bedtimeStoryShortDescriptionErrorMessage = ""
    bedtimeStoryTagsErrorMessage = ""
    incompleteBedtimeStories.clear()
}

fun navigateToRecordBedtimeStory(navController: NavController, bedtimeStoryData: BedtimeStoryInfoData){
    navController.navigate("${Screen.RecordBedtimeStory.screen_route}/bedtimeStory=${BedtimeStoryObject.BedtimeStory.from(bedtimeStoryData)}")
}

fun navigateToUploadBedtimeStory(navController: NavController){
    navController.navigate(Screen.UploadBedtimeStory.screen_route)
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
fun CreateBedtimeStoryPreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        NameBedtimeStoryUI(
            rememberNavController(),
            globalViewModel,
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }
}