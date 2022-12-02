package com.example.eunoia.create.createSound

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.navigation.openSoundNameTakenDialogBox
import com.example.eunoia.ui.navigation.soundViewModel
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var soundIcon by mutableStateOf(-1)
var soundName by mutableStateOf("")
var soundShortDescription by mutableStateOf("")
var soundTags by mutableStateOf("")
var iconSelectionTitle by mutableStateOf("")
var soundNameErrorMessage by mutableStateOf("")
var soundDescriptionErrorMessage by mutableStateOf("")
var soundTagsErrorMessage by mutableStateOf("")
const val MIN_SOUND_NAME = 5
const val MIN_SOUND_SHORT_DESCRIPTION = 10
const val MIN_SOUND_TAGS = 3
const val MAX_SOUND_NAME = 30
const val MAX_SOUND_SHORT_DESCRIPTION = 50
const val MAX_SOUND_TAGS = 50

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NameSoundUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val scrollState = rememberScrollState()

    SetupAlertDialogs()
    initializeSoundNameError()
    initializeSoundDescriptionError()
    initializeSoundTagsError()
    initializeSoundIconError()

    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            title,
            nameTitle,
            soundNameColumn,
            nameError,
            shortDescriptionTitle,
            soundShortDescriptionColumn,
            descriptionError,
            tagTitle,
            soundTagColumn,
            tag_error,
            icon_title,
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
        ) {
            BackArrowHeader(
                {
                    //TODO(reset necessary data)
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
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = "Add a sound",
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
            NormalText(
                text = "Name",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(soundNameColumn) {
                    top.linkTo(nameTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            soundName = customizedOutlinedTextInput(
                maxLength = MAX_SOUND_NAME,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "eg. Sweet sensations",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0,
                showWordCount = true
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(nameError) {
                    top.linkTo(soundNameColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            NormalText(
                text = soundNameErrorMessage,
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
            NormalText(
                text = "Get people excited in one sentence",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(soundShortDescriptionColumn) {
                    top.linkTo(shortDescriptionTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            soundShortDescription = customizedOutlinedTextInput(
                maxLength = MAX_SOUND_SHORT_DESCRIPTION,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "eg. A very chaotic boat ride with your lover",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0,
                showWordCount = true
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(descriptionError) {
                    top.linkTo(soundShortDescriptionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            NormalText(
                text = soundDescriptionErrorMessage,
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
                    top.linkTo(descriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            NormalText(
                text = "Hashtags help users find your self love",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(soundTagColumn) {
                    top.linkTo(tagTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            soundTags = customizedOutlinedTextInput(
                maxLength = MAX_SOUND_TAGS,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "eg. storm, rain, hectic",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0,
                showWordCount = true
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(tag_error) {
                    top.linkTo(soundTagColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            NormalText(
                text = soundTagsErrorMessage,
                color = BeautyBush,
                fontSize = 11,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(icon_title) {
                    top.linkTo(tag_error.bottom, margin = 48.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            NormalText(
                text = iconSelectionTitle,
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
                    top.linkTo(icon_title.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            val borders = mutableListOf<MutableState<Boolean>>()
            for(icon in soundViewModel!!.soundScreenIcons){
                borders.add(remember { mutableStateOf(false) })
            }
            soundViewModel!!.soundScreenIcons.forEachIndexed{ index, icon ->
                var cardModifier = Modifier
                    .padding(bottom = 15.dp)
                    .clickable {
                        borders.forEach { border ->
                            border.value = false
                        }
                        borders[index].value = !borders[index].value
                        soundIcon = icon.value
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
        Column(
            modifier = Modifier
                .constrainAs(next) {
                    top.linkTo(icons.bottom, margin = 32.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth(0.25F)
        ) {
            if(
                soundName.length >= MIN_SOUND_NAME &&
                soundShortDescription.length >= MIN_SOUND_SHORT_DESCRIPTION &&
                soundTags.length >= MIN_SOUND_TAGS &&
                soundIcon != -1 //&&
                //checkOtherSoundsWithSimilarNames()
            ) {
                CustomizableButton(
                    text = "next",
                    height = 35,
                    fontSize = 15,
                    textColor = Black,
                    backgroundColor = SwansDown,
                    corner = 50,
                    borderStroke = 0.0,
                    borderColor = SwansDown.copy(alpha = 0F),
                    textType = "morge",
                    maxWidthFraction = 1F
                ) {
                    var otherSoundsWithSameName by mutableStateOf(-1)
                    SoundBackend.querySoundBasedOnDisplayName(soundName){
                        otherSoundsWithSameName = if(it.isEmpty()) 0 else it.size
                    }
                    Thread.sleep(1_000)

                    if(otherSoundsWithSameName < 1) {
                        navController.navigate(Screen.UploadSounds.screen_route)
                    }else{
                        openSoundNameTakenDialogBox = true
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
    if(openSoundNameTakenDialogBox){
        AlertDialogBox(text = "The name '$soundName' already exists")
    }
}

fun checkOtherSoundsWithSimilarNames(): Boolean{
    var otherSoundsWithSameName by mutableStateOf(-1)
    SoundBackend.querySoundBasedOnDisplayName(soundName){
        otherSoundsWithSameName = if(it.isEmpty()) 0 else it.size
    }
    Thread.sleep(2_000)
    val output = otherSoundsWithSameName == 0
    if(!output) soundNameErrorMessage = "The name, $soundName, is already taken"
    return output
}

fun initializeSoundNameError() {
    soundNameErrorMessage = if(
        soundName.isNotEmpty() &&
        soundName.length < MIN_SOUND_NAME
    ){
        "Must be at least $MIN_SOUND_NAME characters"
    }else{
        ""
    }
}

fun initializeSoundDescriptionError() {
    soundDescriptionErrorMessage = if(
        soundShortDescription.isNotEmpty() &&
        soundShortDescription.length < MIN_SOUND_SHORT_DESCRIPTION
    ){
        "Must be at least $MIN_SOUND_SHORT_DESCRIPTION characters"
    }else{
        ""
    }
}

fun initializeSoundTagsError() {
    soundTagsErrorMessage = if(soundTags.isEmpty()){
        "Separate hashtags with a comma"
    } else if(soundTags.length < MIN_SOUND_TAGS){
        "Tags must be at least $MIN_SOUND_TAGS characters"
    } else{
        ""
    }
}

fun initializeSoundIconError() {
    iconSelectionTitle = if(soundIcon == -1){
        "Select icon"
    }else{
        "Icon selected"
    }
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
fun CreateSoundPreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        NameSoundUI(
            rememberNavController(),
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }
}
