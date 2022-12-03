package com.example.eunoia.create.createPrayer

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.*
import com.example.eunoia.create.createSelfLove.*
import com.example.eunoia.models.PrayerObject
import com.example.eunoia.models.SelfLoveObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import java.util.*

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
private const val MIN_PRAYER_LONG_DESCRIPTION = 50
private const val MIN_PRAYER_TAGS = 3
private const val MAX_PRAYER_NAME = 30
private const val MAX_PRAYER_SHORT_DESCRIPTION = 50
private const val MAX_PRAYER_LONG_DESCRIPTION = 500
private const val MAX_PRAYER_TAGS = 50
var incompletePrayers = mutableListOf<MutableState<PrayerData>?>()

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NamePrayerUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    var numberOfIncompletePrayers by rememberSaveable { mutableStateOf(0) }
    PrayerBackend.queryIncompletePrayerBasedOnUser(globalViewModel!!.currentUser!!) {
        for (i in incompletePrayers.size until it.size) {
            incompletePrayers.add(mutableStateOf(it[i]!!))
        }
        numberOfIncompletePrayers = incompletePrayers.size
    }

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
            nameTitle,
            nameColumn,
            nameError,
            shortDescriptionTitle,
            prayerShortDescriptionColumn,
            prayerShortDescriptionError,
            longDescriptionTitle,
            prayerLongDescriptionColumn,
            prayerLongDescriptionError,
            religionTitle,
            religionColumn,
            religionError,
        ) = createRefs()

        val (
            tagTitle,
            tagColumn,
            tagError,
            countryTitle,
            countryColumn,
            countryError,
            iconTitle,
            icons,
            next,
            inProgress,
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
                .constrainAs(nameColumn) {
                    top.linkTo(nameTitle.bottom, margin = 4.dp)
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
                placeholder = "eg. Morning Prayer",
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
            NormalText(
                text = prayerNameErrorMessage,
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
                .constrainAs(prayerShortDescriptionColumn) {
                    top.linkTo(shortDescriptionTitle.bottom, margin = 4.dp)
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
                placeholder = "eg. When times are hard and money no dey",
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
            NormalText(
                text = prayerShortDescriptionErrorMessage,
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
                    top.linkTo(prayerShortDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            NormalText(
                text = "Summarize this self love",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(prayerLongDescriptionColumn) {
                    top.linkTo(longDescriptionTitle.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            prayerLongDescription = customizableBigOutlinedTextInput(
                maxLength = MAX_PRAYER_LONG_DESCRIPTION,
                height = 100,
                placeholder = "Make it lengthy",
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
            NormalText(
                text = prayerLongDescriptionErrorMessage,
                color = BeautyBush,
                fontSize = 11,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(religionTitle) {
                    top.linkTo(prayerLongDescriptionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            NormalText(
                text = "Religion",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(religionColumn) {
                    top.linkTo(religionTitle.bottom, margin = 4.dp)
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
            NormalText(
                text = prayerReligionErrorMessage,
                color = BeautyBush,
                fontSize = 11,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(countryTitle) {
                    top.linkTo(religionError.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
        ){
            NormalText(
                text = "Country",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(countryColumn) {
                    top.linkTo(countryTitle.bottom, margin = 4.dp)
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
            NormalText(
                text = prayerCountryErrorMessage,
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
                    top.linkTo(countryError.bottom, margin = 16.dp)
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
                .constrainAs(tagColumn) {
                    top.linkTo(tagTitle.bottom, margin = 4.dp)
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
                placeholder = "eg. love, self care",
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
            NormalText(
                text = prayerTagsErrorMessage,
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
                    PrayerBackend.queryPrayerBasedOnDisplayName(prayerName){
                        if(it.isEmpty()){
                            runOnUiThread {
                                navigateToUploadPrayer(
                                    navController
                                )
                            }
                        } else openPrayerNameTakenDialogBox = true
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
                    createPrayer(numberOfIncompletePrayers, navController)
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
            if(numberOfIncompletePrayers > 0) {
                ClickableNormalText(
                    text = "Complete another prayer",
                    color = Black,
                    12,
                    0,
                    0
                ) {
                    navController.navigate(Screen.IncompletePrayers.screen_route)
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(next.bottom, margin = 40.dp)
                }
        ){
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

private fun createPrayer(
    numberOfIncompletePrayers: Int,
    navController: NavController
){
    PrayerBackend.queryPrayerBasedOnDisplayName(prayerName){
        if(numberOfIncompletePrayers < 3){
            if (it.isEmpty()) {
                val tags = getPrayerTagsList()
                val key = "${globalViewModel!!.currentUser!!.username.lowercase()}/" +
                        "routine/" +
                        "prayer/" +
                        "recorded/" +
                        "${prayerName.lowercase()}/" +
                        "complete/" +
                        "${prayerName.lowercase()}_audio.aac"

                val prayer = PrayerObject.Prayer(
                    UUID.randomUUID().toString(),
                    UserObject.User.from(globalViewModel!!.currentUser!!),
                    globalViewModel!!.currentUser!!.id,
                    prayerName,
                    prayerShortDescription,
                    prayerLongDescription,
                    key,
                    prayerIcon,
                    0,
                    false,
                    prayerReligion,
                    prayerCountry,
                    tags,
                    listOf(),
                    listOf(),
                    listOf(),
                    PrayerAudioSource.RECORDED,
                    PrayerApprovalStatus.PENDING,
                    PrayerCreationStatus.INCOMPLETE,
                )

                PrayerBackend.createPrayer(prayer) { prayerData ->
                    UserPrayerRelationshipBackend.createUserPrayerRelationshipObject(prayerData) {
                        UserPrayerBackend.createUserPrayerObject(
                            prayerData
                        ) {
                            navigateToRecordPrayer(
                                navController,
                                prayerData
                            )
                        }
                    }
                }
            }else{
                openPrayerNameTakenDialogBox = true
            }
        }else{
            openTooManyIncompletePrayerDialogBox = true
        }
    }
}

@Composable
private fun SetupAlertDialogs(){
    if(openPrayerNameTakenDialogBox){
        AlertDialogBox(text = "The name '$prayerName' already exists")
    }
    if(openTooManyIncompletePrayerDialogBox){
        AlertDialogBox(text = "You have three prayers in progress already")
    }
}

private fun initializePrayerNameError() {
    prayerNameErrorMessage = if(
        prayerName.isNotEmpty() &&
        prayerName.length < MIN_PRAYER_NAME
    ){
        "Must be at least $MIN_PRAYER_NAME characters"
    } else{
        ""
    }
}

private fun initializePrayerShortDescriptionError() {
    prayerShortDescriptionErrorMessage = if(
        prayerShortDescription.isNotEmpty() &&
        prayerShortDescription.length < MIN_PRAYER_SHORT_DESCRIPTION
    ){
        "Must be at least $MIN_PRAYER_SHORT_DESCRIPTION characters"
    } else{
        ""
    }
}

private fun initializePrayerLongDescriptionError() {
    prayerLongDescriptionErrorMessage = if(
        prayerLongDescription.isNotEmpty() &&
        prayerLongDescription.length < MIN_PRAYER_LONG_DESCRIPTION
    ){
        "Must be at least $MIN_PRAYER_LONG_DESCRIPTION characters"
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
        "Separate hashtags with a comma"
    } else if(prayerTags.length < MIN_PRAYER_TAGS){
        "Must be at least $MIN_PRAYER_TAGS characters"
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

fun navigateToRecordPrayer(
    navController: NavController,
    prayerData: PrayerData
){
    navController.navigate("${Screen.RecordPrayer.screen_route}/prayer=${PrayerObject.Prayer.from(prayerData)}")
}