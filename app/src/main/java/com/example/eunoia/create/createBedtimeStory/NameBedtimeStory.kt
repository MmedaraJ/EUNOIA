package com.example.eunoia.create.createBedtimeStory

import android.content.res.Configuration
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
import com.example.eunoia.dashboard.sound.SimpleFlowRow
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

var bedtimeStoryIcon by mutableStateOf(-1)
var bedtimeStoryName by mutableStateOf("")
var bedtimeStoryDescription by mutableStateOf("")
var bedtimeStoryTags by mutableStateOf("")
var bedtimeStoryIconSelectionTitle by mutableStateOf("")
var bedtimeStoryNameErrorMessage by mutableStateOf("")
var bedtimeStoryDescriptionErrorMessage by mutableStateOf("")
var bedtimeStoryTagsErrorMessage by mutableStateOf("")
const val MIN_BEDTIME_STORY_NAME = 5
const val MIN_BEDTIME_STORY_DESCRIPTION = 10
const val MIN_BEDTIME_STORY_TAGS = 3
var incompleteBedtimeStories = mutableListOf<MutableState<BedtimeStoryInfoData>?>()
var openBedtimeStoryNameTakenDialogBox by mutableStateOf(false)
var openTooManyIncompleteBedtimeStoryDialogBox by mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NameBedtimeStoryUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    clearChapterPagesList()
    clearBedtimeStoryChaptersList()
    clearPageRecordingsList()
    SetupAlertDialogs()

    var numberOfIncompleteBedtimeStories by rememberSaveable { mutableStateOf(-1) }
    BedtimeStoryBackend.queryIncompleteBedtimeStoryBasedOnUser(globalViewModel_!!.currentUser!!) {
        for (i in incompleteBedtimeStories.size until it.size) {
            incompleteBedtimeStories.add(mutableStateOf(it[i]!!))
        }
        numberOfIncompleteBedtimeStories = incompleteBedtimeStories.size
    }
    val scrollState = rememberScrollState()

    initializeBedtimeStoryNameError()
    initializeBedtimeStoryDescriptionError()
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
            nameColumn,
            name_error,
            descriptionColumn,
            descriptionError,
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
            modifier = Modifier
                .constrainAs(nameColumn) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            bedtimeStoryName = customizedOutlinedTextInput(
                width = 0,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                inputFontSize = 16,
                placeholder = "Name",
                placeholderFontSize = 16,
                offset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(name_error) {
                    top.linkTo(nameColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = bedtimeStoryNameErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(descriptionColumn) {
                    top.linkTo(name_error.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            bedtimeStoryDescription = customizableBigOutlinedTextInput(
                height = 100,
                placeholder = "Description",
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
                .constrainAs(descriptionError) {
                    top.linkTo(descriptionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = bedtimeStoryDescriptionErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(tagColumn) {
                    top.linkTo(descriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            bedtimeStoryTags = customizedOutlinedTextInput(
                width = 0,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                inputFontSize = 16,
                placeholder = "Tags",
                placeholderFontSize = 16,
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
            AlignedLightText(
                text = bedtimeStoryTagsErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(iconTitle) {
                    top.linkTo(tagError.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            NormalText(
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
            for(icon in globalViewModel_!!.icons){
                borders.add(remember { mutableStateOf(false) })
            }
            globalViewModel_!!.icons.forEachIndexed{ index, icon ->
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
                bedtimeStoryDescription.length >= MIN_BEDTIME_STORY_DESCRIPTION &&
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
                    runOnUiThread {
                        navigateToUploadBedtimeStory(
                            navController
                        )
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
        "Name this bedtime story"
    } else if(bedtimeStoryName.length < MIN_BEDTIME_STORY_NAME){
        "Name must be at least $MIN_BEDTIME_STORY_NAME characters"
    } else{
        ""
    }
}

private fun initializeBedtimeStoryDescriptionError() {
    bedtimeStoryDescriptionErrorMessage = if(bedtimeStoryDescription.isEmpty()){
        "Describe this bedtime story"
    } else if(bedtimeStoryDescription.length < MIN_BEDTIME_STORY_DESCRIPTION){
        "Description must be at least $MIN_BEDTIME_STORY_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializeBedtimeStoryTagsError() {
    bedtimeStoryTagsErrorMessage = if(bedtimeStoryTags.isEmpty()){
        "Add tags to this bedtime story. Separate tags with a comma"
    } else if(bedtimeStoryTags.length < MIN_BEDTIME_STORY_TAGS){
        "Tags must be at least $MIN_BEDTIME_STORY_TAGS characters"
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
    var otherBedtimeStoriesWithSameName by mutableStateOf(-1)
    BedtimeStoryBackend.queryBedtimeStoryBasedOnDisplayName(bedtimeStoryName){
        otherBedtimeStoriesWithSameName = if(it.isEmpty()) 0 else it.size
    }
    Thread.sleep(1_000)
    val tags = getBedtimeStoryTagsList()
    if(numberOfIncompleteBedtimeStories != -1){
        if(numberOfIncompleteBedtimeStories < 3){
            if (otherBedtimeStoriesWithSameName == 0) {
                val bedtimeStory = BedtimeStoryObject.BedtimeStory(
                    UUID.randomUUID().toString(),
                    UserObject.User.from(globalViewModel_!!.currentUser!!),
                    bedtimeStoryName,
                    bedtimeStoryDescription,
                    "",
                    bedtimeStoryIcon,
                    0,
                    false,
                    tags,
                    BedtimeStoryAudioSource.RECORDED,
                    BedtimeStoryApprovalStatus.PENDING,
                    BedtimeStoryCreationStatus.INCOMPLETE
                )
                BedtimeStoryBackend.createBedtimeStory(bedtimeStory) { bedtimeStoryData ->
                    if (globalViewModel_!!.currentUser != null) {
                        var userAlreadyHasBedtimeStory = false
                        for (userBedtimeStory in globalViewModel_!!.currentUser!!.bedtimeStories) {
                            if (
                                userBedtimeStory.bedtimeStoryInfoData.id == bedtimeStoryData.id &&
                                userBedtimeStory.userData.id == globalViewModel_!!.currentUser!!.id
                            ) {
                                userAlreadyHasBedtimeStory = true
                            }
                        }
                        if (!userAlreadyHasBedtimeStory) {
                            UserBedtimeStoryBackend.createUserBedtimeStoryObject(
                                bedtimeStoryData
                            ) {
                                runOnUiThread {
                                    navigateToRecordBedtimeStory(
                                        navController,
                                        bedtimeStoryData
                                    )
                                }
                            }
                        }
                    }
                }
            }else{
                if(otherBedtimeStoriesWithSameName > 0){
                    openBedtimeStoryNameTakenDialogBox = true
                }
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
    bedtimeStoryDescription = ""
    bedtimeStoryTags = ""
    bedtimeStoryIconSelectionTitle = ""
    bedtimeStoryNameErrorMessage = ""
    bedtimeStoryDescriptionErrorMessage = ""
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