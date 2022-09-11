package com.example.eunoia.create.createPrayer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.dashboard.sound.SimpleFlowRow
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var prayerIcon by mutableStateOf(-1)
var prayerName by mutableStateOf("")
var prayerDescription by mutableStateOf("")
var prayerTags by mutableStateOf("")
var prayerIconSelectionTitle by mutableStateOf("")
var prayerNameErrorMessage by mutableStateOf("")
var prayerDescriptionErrorMessage by mutableStateOf("")
var prayerTagsErrorMessage by mutableStateOf("")
const val MIN_PRAYER_NAME = 5
const val MIN_PRAYER_DESCRIPTION = 10
const val MIN_PRAYER_TAGS = 3
var openPrayerNameTakenDialogBox by mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NamePrayerUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val scrollState = rememberScrollState()

    SetupAlertDialogs()
    initializePrayerNameError()
    initializePrayerDescriptionError()
    initializePrayerTagsError()
    initializePrayerIconError()

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
            description_error,
            tagColumn,
            tagError,
            iconTitle,
            icons,
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
                text = "Add a prayer",
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
            prayerName = customizedOutlinedTextInput(
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
                text = prayerNameErrorMessage,
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
            prayerDescription = customizableBigOutlinedTextInput(
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
                .constrainAs(description_error) {
                    top.linkTo(descriptionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = prayerDescriptionErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(tagColumn) {
                    top.linkTo(description_error.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            prayerTags = customizedOutlinedTextInput(
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
                text = prayerTagsErrorMessage,
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
                text = prayerIconSelectionTitle,
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
                        prayerIcon = icon.value
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
                                contentDescription = "prayer icon",
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
                prayerName.length >= MIN_PRAYER_NAME &&
                prayerDescription.length >= MIN_PRAYER_DESCRIPTION &&
                prayerTags.length >= MIN_PRAYER_TAGS &&
                prayerIcon != -1
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
                        navigateToUploadPrayer(
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
                    runOnUiThread {
                        navigateToRecordPrayer(
                            navController
                        )
                    }
                }
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
    if(openPrayerNameTakenDialogBox){
        AlertDialogBox(text = "The name '$prayerName' already exists")
    }
}

private fun initializePrayerNameError() {
    prayerNameErrorMessage = if(prayerName.isEmpty()){
        "Name this prayer"
    } else if(prayerName.length < MIN_PRAYER_NAME){
        "Name must be at least $MIN_PRAYER_NAME characters"
    } else{
        ""
    }
}

private fun initializePrayerDescriptionError() {
    prayerDescriptionErrorMessage = if(prayerDescription.isEmpty()){
        "Describe this prayer"
    } else if(prayerDescription.length < MIN_PRAYER_DESCRIPTION){
        "Description must be at least $MIN_PRAYER_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializePrayerTagsError() {
    prayerTagsErrorMessage = if(prayerTags.isEmpty()){
        "Add tags to this prayer. Separate tags with a comma"
    } else if(prayerTags.length < MIN_PRAYER_TAGS){
        "Tags must be at least $MIN_PRAYER_TAGS characters"
    } else{
        ""
    }
}

private fun initializePrayerIconError() {
    prayerIconSelectionTitle = if(prayerIcon == -1){
        "Select icon"
    }else{
        "Icon selected"
    }
}

fun resetNamePrayerVariables(){
    prayerIcon = -1
    prayerName = ""
    prayerDescription = ""
    prayerTags = ""
    prayerIconSelectionTitle = ""
    prayerNameErrorMessage = ""
    prayerDescriptionErrorMessage = ""
    prayerTagsErrorMessage = ""
}

fun navigateToUploadPrayer(navController: NavController){
    navController.navigate(Screen.UploadPrayer.screen_route)
}

fun navigateToRecordPrayer(navController: NavController){
    navController.navigate(Screen.RecordPrayer.screen_route)
}