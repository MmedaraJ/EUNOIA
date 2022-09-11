package com.example.eunoia.create.createSelfLove

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
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.example.eunoia.dashboard.sound.SimpleFlowRow
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var selfLoveIcon by mutableStateOf(-1)
var selfLoveName by mutableStateOf("")
var selfLoveDescription by mutableStateOf("")
var selfLoveTags by mutableStateOf("")
var selfLoveIconSelectionTitle by mutableStateOf("")
var selfLoveNameErrorMessage by mutableStateOf("")
var selfLoveDescriptionErrorMessage by mutableStateOf("")
var selfLoveTagsErrorMessage by mutableStateOf("")
const val MIN_SELF_LOVE_NAME = 5
const val MIN_SELF_LOVE_DESCRIPTION = 10
const val MIN_SELF_LOVE_TAGS = 3
var openSelfLoveNameTakenDialogBox by mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NameSelfLoveUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val scrollState = rememberScrollState()

    SetupAlertDialogs()
    initializeSelfLoveNameError()
    initializeSelfLoveDescriptionError()
    initializeSelfLoveTagsError()
    initializeSelfLoveIconError()

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
                text = "Add a self love",
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
            selfLoveName = customizedOutlinedTextInput(
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
                text = selfLoveNameErrorMessage,
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
            selfLoveDescription = customizableBigOutlinedTextInput(
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
                text = selfLoveDescriptionErrorMessage,
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
            selfLoveTags = customizedOutlinedTextInput(
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
                text = selfLoveTagsErrorMessage,
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
                text = selfLoveIconSelectionTitle,
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
                        selfLoveIcon = icon.value
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
                                contentDescription = "self love icon",
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
                selfLoveName.length >= MIN_SELF_LOVE_NAME &&
                selfLoveDescription.length >= MIN_SELF_LOVE_DESCRIPTION &&
                selfLoveTags.length >= MIN_SELF_LOVE_TAGS &&
                selfLoveIcon != -1
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
                        navigateToUploadSelfLove(
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
                    //Don't create self love yet. Create it after recording audio
                    //navigate to record self love
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
    if(openSelfLoveNameTakenDialogBox){
        AlertDialogBox(text = "The name '$selfLoveName' already exists")
    }
}

private fun initializeSelfLoveNameError() {
    selfLoveNameErrorMessage = if(selfLoveName.isEmpty()){
        "Name this self love"
    } else if(selfLoveName.length < MIN_SELF_LOVE_NAME){
        "Name must be at least $MIN_SELF_LOVE_NAME characters"
    } else{
        ""
    }
}

private fun initializeSelfLoveDescriptionError() {
    selfLoveDescriptionErrorMessage = if(selfLoveDescription.isEmpty()){
        "Describe this self love"
    } else if(selfLoveDescription.length < MIN_SELF_LOVE_DESCRIPTION){
        "Description must be at least $MIN_SELF_LOVE_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializeSelfLoveTagsError() {
    selfLoveTagsErrorMessage = if(selfLoveTags.isEmpty()){
        "Add tags to this self love. Separate tags with a comma"
    } else if(selfLoveTags.length < MIN_SELF_LOVE_TAGS){
        "Tags must be at least $MIN_SELF_LOVE_TAGS characters"
    } else{
        ""
    }
}

private fun initializeSelfLoveIconError() {
    selfLoveIconSelectionTitle = if(selfLoveIcon == -1){
        "Select icon"
    }else{
        "Icon selected"
    }
}

fun navigateToUploadSelfLove(navController: NavController){
    //navController.navigate(Screen.UploadBedtimeStory.screen_route)
}