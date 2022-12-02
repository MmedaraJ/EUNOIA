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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.example.eunoia.backend.PrayerBackend
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.navigation.openPrayerNameTakenDialogBox
import com.example.eunoia.ui.navigation.soundViewModel
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var prayerIcon by mutableStateOf(-1)
var prayerName by mutableStateOf("")
var prayerShortDescription by mutableStateOf("")
var prayerLongDescription by mutableStateOf("")
var prayerReligion by mutableStateOf("")
var prayerCountry by mutableStateOf("")
var prayerTags by mutableStateOf("")
var prayerIconSelectionTitle by mutableStateOf("")
var prayerNameErrorMessage by mutableStateOf("")
var prayerShortDescriptionErrorMessage by mutableStateOf("")
var prayerLongDescriptionErrorMessage by mutableStateOf("")
var prayerReligionErrorMessage by mutableStateOf("")
var prayerCountryErrorMessage by mutableStateOf("")
var prayerTagsErrorMessage by mutableStateOf("")
private const val MIN_PRAYER_NAME = 5
private const val MIN_PRAYER_SHORT_DESCRIPTION = 10
private const val MIN_PRAYER_LONG_DESCRIPTION = 10
private const val MIN_PRAYER_TAGS = 3
private const val MAX_PRAYER_NAME = 30
private const val MAX_PRAYER_SHORT_DESCRIPTION = 50
private const val MAX_PRAYER_LONG_DESCRIPTION = 200
private const val MAX_PRAYER_TAGS = 50

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NamePrayerUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    SetupAlertDialogs()
    initializePrayerNameError()
    initializePrayerShortDescriptionError()
    initializePrayerLongDescriptionError()
    initializePrayerReligionError()
    initializePrayerCountryError()
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
            nameError,
            prayerShortDescriptionColumn,
            prayerShortDescriptionError,
            prayerLongDescriptionColumn,
            prayerLongDescriptionError,
            tagColumn,
            tagError,
            religionColumn,
            religionError,
            countryColumn,
            countryError,
            iconTitle,
            icons,
        ) = createRefs()

        val (
            next,
            endSpace,
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
                    resetAllPrayerCreationObjects(context)
                    navController.popBackStack()
                },
                {
                    com.example.eunoia.ui.navigation.globalViewModel!!.bottomSheetOpenFor = "controls"
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
                maxLength = MAX_PRAYER_NAME,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "Name",
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
                .constrainAs(prayerShortDescriptionColumn) {
                    top.linkTo(nameError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            prayerShortDescription = customizedOutlinedTextInput(
                maxLength = MAX_PRAYER_SHORT_DESCRIPTION,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "Short description",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0,
                showWordCount = true
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(prayerShortDescriptionError) {
                    top.linkTo(prayerShortDescriptionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = prayerShortDescriptionErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(prayerLongDescriptionColumn) {
                    top.linkTo(prayerShortDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            prayerLongDescription = customizableBigOutlinedTextInput(
                maxLength = MAX_PRAYER_LONG_DESCRIPTION,
                height = 100,
                placeholder = "Long description",
                backgroundColor = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                textColor = Black,
                placeholderColor = BeautyBush,
                placeholderTextSize = 16,
                inputFontSize = 16,
                true
            ){}
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(prayerLongDescriptionError) {
                    top.linkTo(prayerLongDescriptionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = prayerLongDescriptionErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(religionColumn) {
                    top.linkTo(prayerLongDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            val religions = listOf(
                "Baha'i",
                "Buddhism",
                "Christianity",
                "Confucianism",
                "Hinduism",
                "Islam",
                "Jainism",
                "Judaism",
                "Shinto",
                "Sikhism",
                "Taoism",
                "Zoroastrianism"
            )
            prayerReligion = dropdownMenuSoftPeach(religions, "Religion")
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(religionError) {
                    top.linkTo(religionColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = prayerReligionErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(countryColumn) {
                    top.linkTo(religionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            val countries = listOf(
                "Afghanistan",
                "Albania",
                "Algeria",
                "Andorra",
                "Angola",
                "Antigua and Barbuda",
                "Argentina",
                "Armenia",
                "Australia",
                "Austria",
                "Azerbaijan",
                "The Bahamas",
                "Bahrain",
                "Bangladesh",
                "Barbados",
                "Belarus",
                "Belgium",
                "Belize",
                "Benin",
                "Bhutan",
                "Bolivia",
                "Bosnia and Herzegovina",
                "Botswana",
                "Brazil",
                "Brunei",
                "Bulgaria",
                "Burkina Faso",
                "Burundi",
                "Cabo Verde",
                "Cambodia",
                "Cameroon",
                "Canada",
                "Central African Republic",
                "Chad",
                "Chile",
                "China",
                "Colombia",
                "Comoros",
                "Congo, Democratic Republic of the",
                "Congo, Republic of the",
                "Costa Rica",
                "Côte d’Ivoire",
                "Croatia",
                "Cuba",
                "Cyprus",
                "Czech Republic",
                "Denmark",
                "Djibouti",
                "Dominica",
                "Dominican Republic",
                "East Timor (Timor-Leste)",
                "Ecuador",
                "Egypt",
                "El Salvador",
                "Equatorial Guinea",
                "Eritrea",
                "Estonia",
                "Eswatini",
                "Ethiopia",
                "Fiji",
                "Finland",
                "France",
                "Gabon",
                "The Gambia",
                "Georgia",
                "Germany",
                "Ghana",
                "Greece",
                "Grenada",
                "Guatemala",
                "Guinea",
                "Guinea-Bissau",
                "Guyana",
                "Haiti",
                "Honduras",
                "Hungary",
                "Iceland",
                "India",
                "Indonesia",
                "Iran",
                "Iraq",
                "Ireland",
                "Israel",
                "Italy",
                "Jamaica",
                "Japan",
                "Jordan",
                "Kazakhstan",
                "Kenya",
                "Kiribati",
                "Korea, North",
                "Korea, South",
                "Kosovo",
                "Kuwait",
                "Kyrgyzstan",
                "Laos",
                "Latvia",
                "Lebanon",
                "Lesotho",
                "Liberia",
                "Libya",
                "Liechtenstein",
                "Lithuania",
                "Luxembourg",
                "Madagascar",
                "Malawi",
                "Malaysia",
                "Maldives",
                "Mali",
                "Malta",
                "Marshall Islands",
                "Mauritania",
                "Mauritius",
                "Mexico",
                "Micronesia, Federated States of",
                "Moldova",
                "Monaco",
                "Mongolia",
                "Montenegro",
                "Morocco",
                "Mozambique",
                "Myanmar (Burma)",
                "Namibia",
                "Nauru",
                "Nepal",
                "Netherlands",
                "New Zealand",
                "Nicaragua",
                "Niger",
                "Nigeria",
                "North Macedonia",
                "Norway",
                "Oman",
                "Pakistan",
                "Palau",
                "Panama",
                "Papua New Guinea",
                "Paraguay",
                "Peru",
                "Philippines",
                "Poland",
                "Portugal",
                "Qatar",
                "Romania",
                "Russia",
                "Rwanda",
                "Saint Kitts and Nevis",
                "Saint Lucia",
                "Saint Vincent and the Grenadines",
                "Samoa",
                "San Marino",
                "Sao Tome and Principe",
                "Saudi Arabia",
                "Senegal",
                "Serbia",
                "Seychelles",
                "Sierra Leone",
                "Singapore",
                "Slovakia",
                "Slovenia",
                "Solomon Islands",
                "Somalia",
                "South Africa",
                "Spain",
                "Sri Lanka",
                "Sudan",
                "Sudan, South",
                "Suriname",
                "Sweden",
                "Switzerland",
                "Syria",
                "Taiwan",
                "Tajikistan",
                "Tanzania",
                "Thailand",
                "Togo",
                "Tonga",
                "Trinidad and Tobago",
                "Tunisia",
                "Turkey",
                "Turkmenistan",
                "Tuvalu",
                "Uganda",
                "Ukraine",
                "United Arab Emirates",
                "United Kingdom",
                "United States",
                "Uruguay",
                "Uzbekistan",
                "Vanuatu",
                "Vatican City",
                "Venezuela",
                "Vietnam",
                "Yemen",
                "Zambia",
                "Zimbabwe"
            )
            prayerCountry = dropdownMenuSoftPeach(countries, "Country")
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(countryError) {
                    top.linkTo(countryColumn.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            AlignedLightText(
                text = prayerCountryErrorMessage,
                color = Black,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(tagColumn) {
                    top.linkTo(countryError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            prayerTags = customizedOutlinedTextInput(
                maxLength = MAX_PRAYER_TAGS,
                height = 55,
                color = SoftPeach,
                focusedBorderColor = BeautyBush,
                unfocusedBorderColor = SoftPeach,
                inputFontSize = 16,
                placeholder = "Tags",
                placeholderFontSize = 16,
                placeholderColor = BeautyBush,
                offset = 0,
                showWordCount = true
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
                prayerShortDescription.length >= MIN_PRAYER_SHORT_DESCRIPTION &&
                prayerLongDescription.length >= MIN_PRAYER_LONG_DESCRIPTION &&
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
                    var otherPrayersWithSameName by mutableStateOf(-1)
                    PrayerBackend.queryPrayerBasedOnDisplayName(prayerName){
                        otherPrayersWithSameName = if(it.isEmpty()) 0 else it.size
                    }
                    Thread.sleep(1_000)

                    if(otherPrayersWithSameName < 1) {
                        runOnUiThread {
                            navigateToUploadPrayer(
                                navController
                            )
                        }
                    }else{
                        openPrayerNameTakenDialogBox = true
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
                    var otherPrayersWithSameName by mutableStateOf(-1)
                    PrayerBackend.queryPrayerBasedOnDisplayName(prayerName){
                        otherPrayersWithSameName = if(it.isEmpty()) 0 else it.size
                    }
                    Thread.sleep(1_000)

                    if(otherPrayersWithSameName < 1) {
                        runOnUiThread {
                            navigateToRecordPrayer(
                                navController
                            )
                        }
                    }else{
                        openPrayerNameTakenDialogBox = true
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

private fun initializePrayerShortDescriptionError() {
    prayerShortDescriptionErrorMessage = if(prayerShortDescription.isEmpty()){
        "Provide short description"
    } else if(prayerShortDescription.length < MIN_PRAYER_SHORT_DESCRIPTION){
        "Short description must be at least $MIN_PRAYER_SHORT_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializePrayerLongDescriptionError() {
    prayerLongDescriptionErrorMessage = if(prayerLongDescription.isEmpty()){
        "Provide long description"
    } else if(prayerLongDescription.length < MIN_PRAYER_LONG_DESCRIPTION){
        "Long description must be at least $MIN_PRAYER_LONG_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializePrayerReligionError() {
    prayerReligionErrorMessage = if(prayerReligion == "0"){
        "What religion is this prayer suited for?"
    } else{
        ""
    }
}

private fun initializePrayerCountryError() {
    prayerCountryErrorMessage = if(prayerCountry == "0"){
        "What country is this prayer suited for?"
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
    prayerShortDescription = ""
    prayerLongDescription = ""
    prayerTags = ""
    prayerIconSelectionTitle = ""
    prayerNameErrorMessage = ""
    prayerShortDescriptionErrorMessage = ""
    prayerLongDescriptionErrorMessage = ""
    prayerTagsErrorMessage = ""
}

fun navigateToUploadPrayer(navController: NavController){
    navController.navigate(Screen.UploadPrayer.screen_route)
}

fun navigateToRecordPrayer(navController: NavController){
    navController.navigate(Screen.RecordPrayer.screen_route)
}