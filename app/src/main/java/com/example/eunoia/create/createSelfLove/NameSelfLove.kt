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
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.*
import com.example.eunoia.backend.SelfLoveBackend
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.navigation.openSelfLoveNameTakenDialogBox
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var selfLoveIcon by mutableStateOf(-1)
var selfLoveName by mutableStateOf("")
var selfLoveShortDescription by mutableStateOf("")
var selfLoveLongDescription by mutableStateOf("")
var selfLoveLyrics by mutableStateOf("")
var selfLoveTags by mutableStateOf("")
var selfLoveIconSelectionTitle by mutableStateOf("")
var selfLoveNameErrorMessage by mutableStateOf("")
var selfLoveShortDescriptionErrorMessage by mutableStateOf("")
var selfLoveLongDescriptionErrorMessage by mutableStateOf("")
var selfLoveLyricsErrorMessage by mutableStateOf("")
var selfLoveTagsErrorMessage by mutableStateOf("")
private const val MIN_SELF_LOVE_NAME = 5
private const val MIN_SELF_LOVE_SHORT_DESCRIPTION = 10
private const val MIN_SELF_LOVE_LONG_DESCRIPTION = 10
private const val MIN_SELF_LOVE_TAGS = 3
private const val MAX_SELF_LOVE_NAME = 30
private const val MAX_SELF_LOVE_SHORT_DESCRIPTION = 50
private const val MAX_SELF_LOVE_LONG_DESCRIPTION = 200
private const val MAX_SELF_LOVE_TAGS = 50
private const val MAX_SELF_LOVE_LYRICS = 500

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
    initializeSelfLoveShortDescriptionError()
    initializeSelfLoveLongDescriptionError()
    initializeSelfLoveLyricsError()
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
            nameError,
            shortDescriptionColumn,
            shortDescriptionError,
            longDescriptionColumn,
            longDescriptionError,
            lyricsColumn,
            lyricsError,
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
                maxLength = MAX_SELF_LOVE_NAME,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "Name",
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
                .constrainAs(shortDescriptionColumn) {
                    top.linkTo(nameError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            selfLoveShortDescription = customizedOutlinedTextInput(
                maxLength = MAX_SELF_LOVE_SHORT_DESCRIPTION,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "Short description",
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
            AlignedLightText(
                text = selfLoveShortDescriptionErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(longDescriptionColumn) {
                    top.linkTo(shortDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            selfLoveLongDescription = customizableBigOutlinedTextInput(
                maxLength = MAX_SELF_LOVE_LONG_DESCRIPTION,
                height = 100,
                placeholder = "Long description",
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
            AlignedLightText(
                text = selfLoveLongDescriptionErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(lyricsColumn) {
                    top.linkTo(longDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            selfLoveLyrics = customizableBigOutlinedTextInput(
                maxLength = MAX_SELF_LOVE_LYRICS,
                height = 150,
                placeholder = "Lyrics",
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
                .constrainAs(lyricsError) {
                    top.linkTo(lyricsColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = selfLoveLyricsErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(tagColumn) {
                    top.linkTo(lyricsError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            selfLoveTags = customizedOutlinedTextInput(
                maxLength = MAX_SELF_LOVE_TAGS,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "Tags",
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
                selfLoveShortDescription.length >= MIN_SELF_LOVE_SHORT_DESCRIPTION &&
                selfLoveLongDescription.length >= MIN_SELF_LOVE_LONG_DESCRIPTION &&
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
                    var otherSelfLovesWithSameName by mutableStateOf(-1)
                    SelfLoveBackend.querySelfLoveBasedOnDisplayName(selfLoveName){
                        otherSelfLovesWithSameName = if(it.isEmpty()) 0 else it.size
                    }
                    Thread.sleep(1_000)

                    if (otherSelfLovesWithSameName < 1) {
                        runOnUiThread {
                            navigateToUploadSelfLove(
                                navController
                            )
                        }
                    }else{
                        openSelfLoveNameTakenDialogBox = true
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
                    var otherSelfLovesWithSameName by mutableStateOf(-1)
                    SelfLoveBackend.querySelfLoveBasedOnDisplayName(selfLoveName){
                        otherSelfLovesWithSameName = if(it.isEmpty()) 0 else it.size
                    }
                    Thread.sleep(1_000)

                    if (otherSelfLovesWithSameName < 1) {
                        runOnUiThread {
                            navigateToRecordSelfLove(
                                navController
                            )
                        }
                    }else{
                        openSelfLoveNameTakenDialogBox = true
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

private fun initializeSelfLoveShortDescriptionError() {
    selfLoveShortDescriptionErrorMessage = if(selfLoveShortDescription.isEmpty()){
        "Provide short description"
    } else if(selfLoveShortDescription.length < MIN_SELF_LOVE_SHORT_DESCRIPTION){
        "Short description must be at least $MIN_SELF_LOVE_SHORT_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializeSelfLoveLongDescriptionError() {
    selfLoveLongDescriptionErrorMessage = if(selfLoveLongDescription.isEmpty()){
        "Provide long description"
    } else if(selfLoveLongDescription.length < MIN_SELF_LOVE_LONG_DESCRIPTION){
        "Long description must be at least $MIN_SELF_LOVE_LONG_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializeSelfLoveLyricsError() {
    selfLoveLyricsErrorMessage = "Separate lyrics with a new line"
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

fun resetNameSelfLoveVariables(){
    selfLoveIcon = -1
    selfLoveName = ""
    selfLoveShortDescription = ""
    selfLoveLongDescription = ""
    selfLoveLyrics = ""
    selfLoveTags = ""
    selfLoveIconSelectionTitle = ""
    selfLoveNameErrorMessage = ""
    selfLoveShortDescriptionErrorMessage = ""
    selfLoveLongDescriptionErrorMessage = ""
    selfLoveLyricsErrorMessage = ""
    selfLoveTagsErrorMessage = ""
}

fun navigateToUploadSelfLove(navController: NavController){
    navController.navigate(Screen.UploadSelfLove.screen_route)
}

fun navigateToRecordSelfLove(navController: NavController){
    navController.navigate(Screen.RecordSelfLove.screen_route)
}