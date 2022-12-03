package com.example.eunoia.ui.navigation

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eunoia.dashboard.article.ArticleUI
import com.example.eunoia.dashboard.home.UserDashboardActivityUI
import com.example.eunoia.dashboard.sound.SoundScreen
import com.example.eunoia.dashboard.sound.SoundActivityUI
import com.example.eunoia.feedback.FeedbackUI
import com.example.eunoia.pricing.PricingUI
import com.example.eunoia.settings.Settings
import com.example.eunoia.ui.screens.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.example.eunoia.create.createSound.NameSoundUI
import com.example.eunoia.create.CreateUI
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.create.createPrayer.*
import com.example.eunoia.create.createSelfLove.*
import com.example.eunoia.create.createSound.*
import com.example.eunoia.dashboard.bedtimeStory.BedtimeStoryActivityUI
import com.example.eunoia.dashboard.bedtimeStory.BedtimeStoryScreen
import com.example.eunoia.dashboard.prayer.PrayerActivityUI
import com.example.eunoia.dashboard.prayer.PrayerScreen
import com.example.eunoia.dashboard.routine.*
import com.example.eunoia.dashboard.routine.userRoutineRelationshipScreen.UserRoutineRelationshipScreen
import com.example.eunoia.dashboard.selfLove.SelfLoveActivityUI
import com.example.eunoia.dashboard.selfLove.SelfLoveScreen
import com.example.eunoia.models.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.settings.eightHourCountdown.EightHourCountdownUI
import com.example.eunoia.ui.bottomSheets.*
import com.example.eunoia.ui.bottomSheets.bedtimeStory.*
import com.example.eunoia.ui.bottomSheets.prayer.*
import com.example.eunoia.ui.bottomSheets.recordAudio.RecordAudio
import com.example.eunoia.ui.bottomSheets.selfLove.*
import com.example.eunoia.ui.bottomSheets.sound.*
import com.example.eunoia.ui.components.AlignedNormalText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.*
import kotlinx.coroutines.CoroutineScope

var globalViewModel: GlobalViewModel? = null
var recordAudioViewModel: RecordAudioViewModel? = null
var bedtimeStoryViewModel: BedtimeStoryViewModel? = null
var soundViewModel: SoundViewModel? = null
var prayerViewModel: PrayerViewModel? = null
var selfLoveViewModel: SelfLoveViewModel? = null
var routineViewModel: RoutineViewModel? = null
var generalMediaPlayerService_: GeneralMediaPlayerService? = null

var commentCreatedDialog by mutableStateOf(false)
var openSavedElementDialogBox by mutableStateOf(false)
var openSoundNameTakenDialogBox by mutableStateOf(false)
var openPrayerNameTakenDialogBox by mutableStateOf(false)
var openPresetAlreadyExistsDialog by mutableStateOf(false)
var openSelfLoveNameTakenDialogBox by mutableStateOf(false)
var openConfirmDeletePageDialogBox by mutableStateOf(false)
var openUserAlreadyHasSoundDialogBox by mutableStateOf(false)
var openConfirmDeletePrayerDialogBox by mutableStateOf(false)
var openUserAlreadyHasPrayerDialogBox by mutableStateOf(false)
var openConfirmDeleteChapterDialogBox by mutableStateOf(false)
var openPresetNameIsAlreadyTakenDialog by mutableStateOf(false)
var openBedtimeStoryNameTakenDialogBox by mutableStateOf(false)
var openConfirmDeleteSelfLoveDialogBox by mutableStateOf(false)
var openUserAlreadyHasSelfLoveDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasSoundDialogBox by mutableStateOf(false)
var openRoutineNameIsAlreadyTakenDialog by mutableStateOf(false)
var openRoutineAlreadyHasPrayerDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasPresetDialogBox by mutableStateOf(false)
var openTooManyIncompletePrayerDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasSelfLoveDialogBox by mutableStateOf(false)
var openConfirmDeleteBedtimeStoryDialogBox by mutableStateOf(false)
var openRoutineIsCurrentlyPlayingDialogBox by mutableStateOf(false)
var openTooManyIncompleteSelfLoveDialogBox by mutableStateOf(false)
var openUserAlreadyHasBedtimeStoryDialogBox by mutableStateOf(false)
var openTooManyIncompleteBedtimeStoryDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasBedtimeStoryDialogBox by mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiBottomNavApp() {
    globalViewModel = viewModel()
    recordAudioViewModel = viewModel()
    bedtimeStoryViewModel = viewModel()
    soundViewModel = viewModel()
    prayerViewModel = viewModel()
    selfLoveViewModel = viewModel()
    routineViewModel = viewModel()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { globalViewModel!!.allowBottomSheetClose }
    )
    val scope = rememberCoroutineScope()
    val generalMediaPlayerService = GeneralMediaPlayerService()
    generalMediaPlayerService_ = generalMediaPlayerService
    val soundMediaPlayerService = SoundMediaPlayerService()
    EunoiaApp(
        scope,
        modalBottomSheetState,
        viewModel(),
        generalMediaPlayerService,
        soundMediaPlayerService,
    ) { screen ->
        MultiNavTabContent(
            screen,
            scope,
            modalBottomSheetState,
            generalMediaPlayerService,
            soundMediaPlayerService,
        )
    }
}

var dashboardNavController: NavController? = null
var searchNavController: NavController? = null
var createNavController: NavController? = null
var feedbackNavController: NavController? = null
var accountNavController: NavController? = null

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EunoiaApp(
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    globalViewModel: GlobalViewModel,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    bodyContent: @Composable (Screen) -> Unit
){
    var currentTab by rememberSaveable(
        saver = screenSaver()
    ) {
        mutableStateOf(Screen.Dashboard)
    }
    EUNOIATheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ){
            ModalBottomSheetLayout(
                sheetContent = {
                    Box(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 1.dp)
                    ) {
                        when(globalViewModel!!.bottomSheetOpenFor){
                            /**
                             * Currently playing controls
                             */
                            "controls" -> {
                                BottomSheetAllControls(
                                    scope,
                                    state,
                                    generalMediaPlayerService,
                                    soundMediaPlayerService,
                                )
                                globalViewModel!!.allowBottomSheetClose = true
                            }

                            /**
                             * Add sound to list or routine
                             */
                            "addToSoundListOrRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                AddToSoundListAndRoutineBottomSheet(scope, state)
                            }
                            "addToRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineForSound(scope, state)
                            }
                            "inputRoutineName" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                routineViewModel!!.routineNameToBeAdded = inputRoutineName(scope, state)
                            }
                            "inputPresetName" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                soundViewModel!!.presetNameToBeCreated = inputPresetName(scope, state)
                            }
                            "selectRoutineColor" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineColor(scope, state)
                            }
                            "selectRoutineIcon" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineIcon(scope, state)
                            }

                            /**
                             * Add bedtime story to list or routine
                             */
                            "addToBedtimeStoryListOrRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                AddToBedtimeStoryListAndRoutineBottomSheet(scope, state)
                            }
                            "addBedtimeStoryToRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineForBedtimeStory(scope, state)
                            }
                            "inputRoutineNameForBedtimeStory" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                routineViewModel!!.routineNameToBeAdded = inputRoutineNameForBedtimeStory(scope, state)
                            }
                            "selectRoutineColorForBedtimeStory" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineColorForBedtimeStory(scope, state)
                            }
                            "selectRoutineIconForBedtimeStory" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineIconForBedtimeStory(scope, state)
                            }

                            /**
                             * Add self love to list or routine
                             */
                            "addToSelfLoveListOrRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                AddToSelfLoveListAndRoutineBottomSheet(scope, state)
                            }
                            "addSelfLoveToRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineForSelfLove(scope, state)
                            }
                            "inputRoutineNameForSelfLove" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                routineViewModel!!.routineNameToBeAdded = inputRoutineNameForSelfLove(scope, state)
                            }
                            "selectRoutineColorForSelfLove" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineColorForSelfLove(scope, state)
                            }
                            "selectRoutineIconForSelfLove" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineIconForSelfLove(scope, state)
                            }
                            "showSelfLoveLyrics" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                ShowSelfLoveLyricsBottomSheet(scope, state)
                            }

                            /**
                             * Add prayer to list or routine
                             */
                            "addToPrayerListOrRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                AddToPrayerListAndRoutineBottomSheet(scope, state)
                            }
                            "addPrayerToRoutine" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineForPrayer(scope, state)
                            }
                            "inputRoutineNameForPrayer" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                routineViewModel!!.routineNameToBeAdded = inputRoutineNameForPrayer(scope, state)
                            }
                            "selectRoutineColorForPrayer" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineColorForPrayer(scope, state)
                            }
                            "selectRoutineIconForPrayer" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                SelectRoutineIconForPrayer(scope, state)
                            }

                            /**
                             * Record audio
                             */
                            "recordAudio" -> {
                                globalViewModel!!.allowBottomSheetClose = false
                                RecordAudio(
                                    scope,
                                    state,
                                    generalMediaPlayerService
                                )
                            }

                            "" -> {
                                globalViewModel!!.allowBottomSheetClose = true
                                BottomSheetAllControls(
                                    scope,
                                    state,
                                    generalMediaPlayerService,
                                    soundMediaPlayerService
                                )
                            }
                        }
                    }
                },
                sheetState = state,
                sheetBackgroundColor = OldLace,
                sheetShape = RoundedCornerShape(topStart =  10.dp, topEnd = 10.dp)
            ) {
                Scaffold(
                    content = {padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            bodyContent(currentTab)
                        }
                    },/*
                drawerContent = {
                    Settings(navController = rememberNavController(), globalViewModel)
                },*/
                    bottomBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                NormalText(
                                    text = "now playing",
                                    color = Black,
                                    fontSize = 14,
                                    xOffset = 0,
                                    yOffset = 0
                                )
                            }
                            val items = listOf(
                                Screen.Dashboard,
                                Screen.Search,
                                Screen.Create,
                                Screen.Feedback,
                                Screen.Account
                            )
                            BottomNavigation(
                                backgroundColor = MaterialTheme.colors.background,
                                contentColor = MaterialTheme.colors.primary,
                                modifier = Modifier.height(height = 50.dp)
                            ) {
                                /*val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route*/
                                //val dashboardNavController = rememberNavController()
                                items.forEach { item ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                painterResource(id = item.icon),
                                                contentDescription = item.title,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        },
                                        selectedContentColor = Color.Black,
                                        unselectedContentColor = Grey,
                                        alwaysShowLabel = true,
                                        selected = currentTab == item,
                                        label = {
                                            AlignedNormalText(
                                                item.title,
                                                Black,
                                                9,
                                                0,
                                                0
                                            )
                                        },
                                        onClick = {
                                            if (currentTab == item) {
                                                when (item) {
                                                    Screen.Dashboard -> {
                                                        dashboardNavController!!.popBackStack()
                                                    }
                                                    Screen.Search -> {
                                                        searchNavController!!.popBackStack()
                                                    }
                                                    Screen.Create -> {
                                                        createNavController!!.popBackStack()
                                                    }
                                                    Screen.Feedback -> {
                                                        feedbackNavController!!.popBackStack()
                                                    }
                                                    Screen.Account -> {
                                                        accountNavController!!.popBackStack()
                                                    }
                                                    else -> {}
                                                }
                                            }

                                            currentTab = item
                                            /*navController.navigate(item.screen_route) {
                                            navController.graph.startDestinationRoute?.let { screen_route ->
                                                popUpTo(item.screen_route) {
                                                    saveState = true
                                                }
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }*/
                                        },
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiNavTabContent(
    screen: Screen,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val dashboardNavState = rememberSaveable(
        saver = navStateSaver()
    ) { mutableStateOf(Bundle()) }
    val createNavState = rememberSaveable(
        saver = navStateSaver()
    ) { mutableStateOf(Bundle()) }
    val searchNavState = rememberSaveable(
        saver = navStateSaver()
    ) { mutableStateOf(Bundle()) }
    val feedbackNavState = rememberSaveable(
        saver = navStateSaver()
    ) { mutableStateOf(Bundle()) }
    val accountNavState = rememberSaveable(
        saver = navStateSaver()
    ) { mutableStateOf(Bundle()) }
    when (screen) {
        Screen.Dashboard -> DashboardTab(dashboardNavState, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Create -> CreateTab(createNavState, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Search -> SearchTab(searchNavState, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Feedback -> FeedbackTab(feedbackNavState, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Account -> AccountTab(accountNavState, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        else -> DashboardTab(dashboardNavState, scope, state, generalMediaPlayerService, soundMediaPlayerService)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardTab(
    navState: MutableState<Bundle>,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val navController = rememberNavController()
    dashboardNavController = navController

    DisposableEffect(Unit) {
        val callback = NavController.OnDestinationChangedListener { navController, _, _ ->
            navState.value = navController.saveState() ?: Bundle()
        }
        navController.addOnDestinationChangedListener(callback)
        navController.restoreState(navState.value)

        onDispose {
            navController.removeOnDestinationChangedListener(callback)
            // workaround for issue where back press is intercepted
            // outside this tab, even after this Composable is disposed
            navController.enableOnBackPressed(false)
        }
    }

    /*Log.i(TAG, "1. navController og $navController")
    Log.i(TAG, "2. navController o1 $dashboardNavController")
    Log.i(TAG, "3. currentBackStackEntry ${navController.currentBackStackEntryAsState().value!!}")*/

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.screen_route
    ) {
        composable(
            Screen.Dashboard.screen_route
        ) {
            Log.i("Dashboard", "You are now on the Dashboard tab")
            UserDashboardActivityUI(
                navController,
                scope,
                state,
                viewModel(),
                viewModel(),
                viewModel(),
                viewModel(),
                viewModel(),
                viewModel(),
                generalMediaPlayerService,
                soundMediaPlayerService
            )
        }
        composable(
            Screen.Sound.screen_route
        ) {
            Log.i("Sound", "You are now on the Sound tab")
            SoundActivityUI(
                navController,
                scope,
                state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(
            Screen.BedtimeStory.screen_route
        ) {
            Log.i("BedtimeStory", "You are now on the BedtimeStory tab")
            BedtimeStoryActivityUI(
                navController,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
            )
        }
        composable(
            "${Screen.SoundScreen.screen_route}/sound={sound}",
            arguments = listOf(navArgument("sound") {type = SoundObject.SoundType()})
        ) { backStackEntry ->
            val sound = backStackEntry.arguments?.getParcelable<SoundObject.Sound>("sound")
            Log.i("Sound Screen", "You are now on the ${sound!!.display_name} tab")
            SoundScreen(
                navController,
                sound.data,
                scope,
                state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(
            "${Screen.BedtimeStoryScreen.screen_route}/bedtimeStoryData={bedtimeStoryData}",
            arguments = listOf(
                navArgument("bedtimeStoryData") {
                    type = BedtimeStoryObject.BedtimeStoryType()
                }
            )
        ) { backStackEntry ->
            val bedtimeStoryData = backStackEntry.arguments?.getParcelable<BedtimeStoryObject.BedtimeStory>("bedtimeStoryData")
            Log.i("BedtimeStoryScreen", "You are now on the ${bedtimeStoryData!!.displayName} tab")
            BedtimeStoryScreen(
                navController = navController,
                bedtimeStoryInfoData = bedtimeStoryData.data,
                scope = scope,
                state = state,
                generalMediaPlayerService = generalMediaPlayerService,
                soundMediaPlayerService = soundMediaPlayerService
            )
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(
                navController,
                scope,
                state
            )
        }
        composable(Screen.Article.screen_route) {
            Log.i("Article", "You are now on the Article tab")
            ArticleUI(
                navController,
            )
        }
        composable(
            Screen.EightHourCountdown.screen_route
        ) {
            Log.i("EightHourCountdown", "You are now on the EightHourCountdown tab")
            EightHourCountdownUI(
                navController,
                LocalContext.current,
                scope,
                state,
                generalMediaPlayerService
            )
        }/*
        composable(
            "${Screen.RoutineScreen.screen_route}/userRoutineRelationship={userRoutineRelationship}",
            arguments = listOf(
                navArgument("userRoutineRelationship") {
                    type = UserRoutineRelationshipObject.UserRoutineRelationshipType()
                }
            )
        ) { backStackEntry ->
            val userRoutineRelationship = backStackEntry.arguments?.getParcelable<UserRoutineRelationshipObject.UserRoutineRelationshipModel>("userRoutineRelationship")
            Log.i("Routine Screen", "You are now on the ${userRoutineRelationship!!.userRoutineRelationshipRoutine.displayName} tab")
            UserRoutineRelationshipScreen(
                navController,
                userRoutineRelationship!!.data,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
            )
        }*/
        composable(
            "${Screen.UserRoutineRelationshipScreen.screen_route}/userRoutineRelationship={userRoutineRelationship}",
            arguments = listOf(
                navArgument("userRoutineRelationship") {
                    type = UserRoutineRelationshipObject.UserRoutineRelationshipType()
                }
            )
        ) { backStackEntry ->
            val userRoutineRelationship = backStackEntry.arguments?.getParcelable<UserRoutineRelationshipObject.UserRoutineRelationshipModel>("userRoutineRelationship")
            Log.i("UserRoutineRelationship Screen", "You are now on the ${userRoutineRelationship!!.userRoutineRelationshipRoutine.displayName} tab")
            UserRoutineRelationshipScreen(
                navController,
                userRoutineRelationship.data,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
            )
        }
        composable(
            Screen.SelfLove.screen_route
        ) {
            Log.i("SelfLove", "You are now on the SelfLove tab")
            SelfLoveActivityUI(
                navController,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
            )
        }
        composable(
            "${Screen.SelfLoveScreen.screen_route}/selfLoveData={selfLoveData}",
            arguments = listOf(
                navArgument("selfLoveData") {
                    type = SelfLoveObject.SelfLoveType()
                }
            )
        ) { backStackEntry ->
            val selfLoveData = backStackEntry.arguments?.getParcelable<SelfLoveObject.SelfLove>("selfLoveData")
            Log.i("SelfLoveScreen", "You are now on the ${selfLoveData!!.displayName} tab")
            SelfLoveScreen(
                navController = navController,
                selfLoveData = selfLoveData.data,
                scope = scope,
                state = state,
                generalMediaPlayerService = generalMediaPlayerService,
                soundMediaPlayerService = soundMediaPlayerService
            )
        }
        composable(
            Screen.Prayer.screen_route
        ) {
            Log.i("Prayer", "You are now on the Prayer tab")
            PrayerActivityUI(
                navController,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
            )
        }
        composable(
            "${Screen.PrayerScreen.screen_route}/prayerData={prayerData}",
            arguments = listOf(
                navArgument("prayerData") {
                    type = PrayerObject.PrayerType()
                }
            )
        ) { backStackEntry ->
            val prayerData = backStackEntry.arguments?.getParcelable<PrayerObject.Prayer>("prayerData")
            Log.i("PrayerScreen", "You are now on the ${prayerData!!.displayName} tab")
            PrayerScreen(
                navController = navController,
                prayerData = prayerData.data,
                scope = scope,
                state = state,
                generalMediaPlayerService = generalMediaPlayerService,
                soundMediaPlayerService = soundMediaPlayerService
            )
        }
        composable(
            "${Screen.RoutinePresetScreen.screen_route}/userRoutineRelationship={userRoutineRelationship}",
            arguments = listOf(
                navArgument("userRoutineRelationship") {
                    type = UserRoutineRelationshipObject.UserRoutineRelationshipType()
                }
            )
        ) { backStackEntry ->
            val userRoutineRelationship = backStackEntry.arguments?.getParcelable<UserRoutineRelationshipObject.UserRoutineRelationshipModel>("userRoutineRelationship")
            RoutinePresetScreen(
                navController,
                LocalContext.current,
                userRoutineRelationship!!.data,
                scope,
                state
            )
        }
        composable(
            "${Screen.RoutinePrayerScreen.screen_route}/userRoutineRelationship={userRoutineRelationship}",
            arguments = listOf(
                navArgument("userRoutineRelationship") {
                    type = UserRoutineRelationshipObject.UserRoutineRelationshipType()
                }
            )
        ) { backStackEntry ->
            val userRoutineRelationship = backStackEntry.arguments?.getParcelable<UserRoutineRelationshipObject.UserRoutineRelationshipModel>("userRoutineRelationship")
            RoutinePrayerScreen(
                navController,
                LocalContext.current,
                userRoutineRelationship!!.data,
                scope,
                state
            )
        }
        composable(
            "${Screen.RoutineBedtimeStoryScreen.screen_route}/userRoutineRelationship={userRoutineRelationship}",
            arguments = listOf(
                navArgument("userRoutineRelationship") {
                    type = UserRoutineRelationshipObject.UserRoutineRelationshipType()
                }
            )
        ) { backStackEntry ->
            val userRoutineRelationship = backStackEntry.arguments?.getParcelable<UserRoutineRelationshipObject.UserRoutineRelationshipModel>("userRoutineRelationship")
            RoutineBedtimeStoryScreen(
                navController,
                LocalContext.current,
                userRoutineRelationship!!.data,
                scope,
                state
            )
        }
        composable(
            "${Screen.RoutineSelfLoveScreen.screen_route}/userRoutineRelationship={userRoutineRelationship}",
            arguments = listOf(
                navArgument("userRoutineRelationship") {
                    type = UserRoutineRelationshipObject.UserRoutineRelationshipType()
                }
            )
        ) { backStackEntry ->
            val userRoutineRelationship = backStackEntry.arguments?.getParcelable<UserRoutineRelationshipObject.UserRoutineRelationshipModel>("userRoutineRelationship")
            RoutineSelfLoveScreen(
                navController,
                LocalContext.current,
                userRoutineRelationship!!.data,
                scope,
                state
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateTab(
    navState: MutableState<Bundle>,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val navController = rememberNavController()
    createNavController = navController

    DisposableEffect(Unit) {
        val callback = NavController.OnDestinationChangedListener { controller, _, _ ->
            navState.value = controller.saveState() ?: Bundle()
        }
        navController.addOnDestinationChangedListener(callback)
        navController.restoreState(navState.value)

        onDispose {
            navController.removeOnDestinationChangedListener(callback)
            // workaround for issue where back press is intercepted
            // outside this tab, even after this Composable is disposed
            navController.enableOnBackPressed(false)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Create.screen_route //navController.currentDestination.
    ) {
        composable(Screen.Create.screen_route) {
            Log.i("Create", "You are now on the Create tab")
            CreateUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(
            "${Screen.SoundScreen.screen_route}/sound={sound}",
            arguments = listOf(
                navArgument("sound") {
                    type = SoundObject.SoundType()
                }
            )
        ) { backStackEntry ->
            val sound = backStackEntry.arguments?.getParcelable<SoundObject.Sound>("sound")
            Log.i("Sound Screen", "You are now on the ${sound!!.display_name} tab")
            SoundScreen(
                navController,
                sound.data,
                scope,
                state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(Screen.NameSound.screen_route) {
            Log.i("CreateSound", "You are now on the CreateSound tab")
            NameSoundUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(Screen.UploadSounds.screen_route) {
            Log.i("UploadSounds", "You are now on the UploadSounds tab")
            UploadSoundsUI(
                navController = navController,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(Screen.CreatePreset.screen_route) {
            Log.i("CreatePreset", "You are now on the CreatePreset tab")
            CreatePresetUI(
                navController = navController,
                LocalContext.current,
                //globalViewModel = globalViewModel,
                scope = scope,
                state = state,
                soundMediaPlayerService
            )
        }
        composable(Screen.NameBedtimeStory.screen_route) {
            Log.i("NameBedtimeStory", "You are now on the NameBedtimeStory tab")
            NameBedtimeStoryUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(
            "${Screen.RecordBedtimeStory.screen_route}/bedtimeStory={bedtimeStory}",
            arguments = listOf(
                navArgument("bedtimeStory") {
                    type = BedtimeStoryObject.BedtimeStoryType()
                }
            )
        ) { backStackEntry ->
            val bedtimeStory = backStackEntry.arguments?.getParcelable<BedtimeStoryObject.BedtimeStory>("bedtimeStory")
            Log.i("RecordBedtimeStory", "You are now on the record ${bedtimeStory!!.displayName} tab")
            RecordBedtimeStoryUI(
                navController = navController,
                bedtimeStory.data,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(Screen.UploadBedtimeStory.screen_route) {
            Log.i("UploadBedtimeStory", "You are now on the UploadBedtimeStory tab")
            UploadBedtimeStoryUI(
                navController = navController,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService,
            )
        }
        composable(
            "${Screen.BedtimeStoryChapterScreen.screen_route}/bedtimeStoryChapter={bedtimeStoryChapter}/{chapterIndex}",
            arguments = listOf(
                navArgument("bedtimeStoryChapter") {
                    type = BedtimeStoryChapterObject.BedtimeStoryChapterType()
                },
                navArgument("chapterIndex") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val bedtimeStoryChapter = backStackEntry.arguments?.getParcelable<BedtimeStoryChapterObject.BedtimeStoryChapter>("bedtimeStoryChapter")
            Log.i("BedtimeStoryChapterScreen", "You are now on the ${bedtimeStoryChapter!!.displayName} tab")
            BedtimeStoryChapterScreenUI(
                navController = navController,
                bedtimeStoryChapter.data,
                backStackEntry.arguments?.getString("chapterIndex")!!.toInt(),
                scope = scope,
                state = state
            )
        }
        composable(
            "${Screen.PageScreen.screen_route}/chapterPage={chapterPage}/chapterData={chapterData}/{chapterIndex}",
            arguments = listOf(
                navArgument("chapterPage") {
                    type = PageObject.PageType()
                },
                navArgument("chapterData") {
                    type = BedtimeStoryChapterObject.BedtimeStoryChapterType()
                },
                navArgument("chapterIndex") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val chapterPage = backStackEntry.arguments?.getParcelable<PageObject.Page>("chapterPage")
            val chapterData = backStackEntry.arguments?.getParcelable<BedtimeStoryChapterObject.BedtimeStoryChapter>("chapterData")
            Log.i("ChapterPageScreen", "You are now on the ${chapterPage!!.displayName} tab")
           PageScreenUI(
                navController = navController,
                chapterPage.data,
                chapterData!!.data,
                backStackEntry.arguments?.getString("chapterIndex")!!.toInt(),
                scope = scope,
                state = state,
                PageViewModel(),
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(
            "${Screen.BedtimeStoryScreen.screen_route}/bedtimeStoryData={bedtimeStoryData}",
            arguments = listOf(
                navArgument("bedtimeStoryData") {
                    type = BedtimeStoryObject.BedtimeStoryType()
                }
            )
        ) { backStackEntry ->
            val bedtimeStoryData = backStackEntry.arguments?.getParcelable<BedtimeStoryObject.BedtimeStory>("bedtimeStoryData")
            Log.i("BedtimeStoryScreen", "You are now on the BedtimeStoryScreen tab")
            BedtimeStoryScreen(
                navController = navController,
                bedtimeStoryInfoData = bedtimeStoryData!!.data,
                scope = scope,
                state = state,
                generalMediaPlayerService = generalMediaPlayerService,
                soundMediaPlayerService = soundMediaPlayerService
            )
        }
        composable(Screen.IncompleteBedtimeStories.screen_route) {
            Log.i("IncompleteBedtimeStories", "You are now on the IncompleteBedtimeStories tab")
            IncompleteBedtimeStoriesUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(Screen.IncompleteSelfLoves.screen_route) {
            Log.i("IncompleteSelfLoves", "You are now on the IncompleteSelfLoves tab")
            IncompleteSelfLovesUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(Screen.IncompletePrayers.screen_route) {
            Log.i("IncompletePrayers", "You are now on the IncompletePrayers tab")
            IncompletePrayersUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(Screen.NamePrayer.screen_route) {
            Log.i("NamePrayer", "You are now on the NamePrayer tab")
            NamePrayerUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(Screen.UploadPrayer.screen_route) {
            Log.i("UploadPrayer", "You are now on the UploadPrayer tab")
            UploadPrayerUI(
                navController = navController,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(
            "${Screen.RecordPrayer.screen_route}/prayer={prayer}",
            arguments = listOf(
                navArgument("prayer") {
                    type = PrayerObject.PrayerType()
                }
            )
        ) { backStackEntry ->
            val prayer = backStackEntry.arguments?.getParcelable<PrayerObject.Prayer>("prayer")
            Log.i("RecordPrayer", "You are now on the RecordPrayer tab")
            RecordPrayerUI(
                navController = navController,
                scope = scope,
                state = state,
                prayerData = prayer!!.data,
                PrayerRecordingViewModel(),
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(Screen.NameSelfLove.screen_route) {
            Log.i("NameSelfLove", "You are now on the NameSelfLove tab")
            NameSelfLoveUI(
                navController = navController,
                scope = scope,
                state = state
            )
        }
        composable(Screen.UploadSelfLove.screen_route) {
            Log.i("UploadSelfLove", "You are now on the UploadSelfLove tab")
            UploadSelfLoveUI(
                navController = navController,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(
            "${Screen.RecordSelfLove.screen_route}/selfLove={selfLove}",
            arguments = listOf(
                navArgument("selfLove") {
                    type = SelfLoveObject.SelfLoveType()
                }
            )
        ) { backStackEntry ->
            val selfLove = backStackEntry.arguments?.getParcelable<SelfLoveObject.SelfLove>("selfLove")
            Log.i("RecordSelfLove", "You are now on the RecordSelfLove tab")
            RecordSelfLoveUI(
                navController = navController,
                scope = scope,
                state = state,
                selfLoveData = selfLove!!.data,
                SelfLoveRecordingViewModel(),
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(
            "${Screen.PrayerScreen.screen_route}/prayerData={prayerData}",
            arguments = listOf(
                navArgument("prayerData") {
                    type = PrayerObject.PrayerType()
                }
            )
        ) { backStackEntry ->
            val prayerData = backStackEntry.arguments?.getParcelable<PrayerObject.Prayer>("prayerData")
            Log.i("PrayerScreen", "You are now on the ${prayerData!!.displayName} tab")
            PrayerScreen(
                navController = navController,
                prayerData = prayerData.data,
                scope = scope,
                state = state,
                generalMediaPlayerService = generalMediaPlayerService,
                soundMediaPlayerService = soundMediaPlayerService
            )
        }
        composable(
            "${Screen.SelfLoveScreen.screen_route}/selfLoveData={selfLoveData}",
            arguments = listOf(
                navArgument("selfLoveData") {
                    type = SelfLoveObject.SelfLoveType()
                }
            )
        ) { backStackEntry ->
            val selfLoveData = backStackEntry.arguments?.getParcelable<SelfLoveObject.SelfLove>("selfLoveData")
            Log.i("SelfLoveScreen", "You are now on the ${selfLoveData!!.displayName} tab")
            SelfLoveScreen(
                navController = navController,
                selfLoveData = selfLoveData.data,
                scope = scope,
                state = state,
                generalMediaPlayerService = generalMediaPlayerService,
                soundMediaPlayerService = soundMediaPlayerService
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchTab(
    navState: MutableState<Bundle>,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val navController = rememberNavController()
    searchNavController = navController

    DisposableEffect(Unit) {
        val callback = NavController.OnDestinationChangedListener { controller, _, _ ->
            navState.value = controller.saveState() ?: Bundle()
        }
        navController.addOnDestinationChangedListener(callback)
        navController.restoreState(navState.value)

        onDispose {
            navController.removeOnDestinationChangedListener(callback)
            // workaround for issue where back press is intercepted
            // outside this tab, even after this Composable is disposed
            navController.enableOnBackPressed(false)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Search.screen_route
    ) {
        composable(Screen.Search.screen_route) {
            Log.i("Search", "You are now on the Search tab")
            Text(text = "Search")
        }
        composable(
            "${Screen.SoundScreen.screen_route}/sound={sound}",
            arguments = listOf(navArgument("sound") {type = SoundObject.SoundType()})
        ) { backStackEntry ->
            val sound = backStackEntry.arguments?.getParcelable<SoundObject.Sound>("sound")
            Log.i("Sound Screen", "You are now on the ${sound!!.display_name} tab")
            SoundScreen(
                navController,
                sound.data,
                scope,
                state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedbackTab(
    navState: MutableState<Bundle>,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val navController = rememberNavController()
    feedbackNavController = navController

    DisposableEffect(Unit) {
        val callback = NavController.OnDestinationChangedListener { controller, _, _ ->
            navState.value = controller.saveState() ?: Bundle()
        }
        navController.addOnDestinationChangedListener(callback)
        navController.restoreState(navState.value)

        onDispose {
            navController.removeOnDestinationChangedListener(callback)
            // workaround for issue where back press is intercepted
            // outside this tab, even after this Composable is disposed
            navController.enableOnBackPressed(false)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Feedback.screen_route
    ) {
        composable(Screen.Feedback.screen_route) {
            Log.i("Feedback", "You are now on the Feedback tab")
            FeedbackUI(
                navController = navController
            )
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(
                navController,
                scope,
                state
            )
        }
        composable(
            "${Screen.SoundScreen.screen_route}/sound={sound}",
            arguments = listOf(navArgument("sound") {type = SoundObject.SoundType()})
        ) { backStackEntry ->
            val sound = backStackEntry.arguments?.getParcelable<SoundObject.Sound>("sound")
            Log.i("Sound Screen", "You are now on the ${sound!!.display_name} tab")
            SoundScreen(
                navController,
                sound.data,
                scope,
                state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountTab(
    navState: MutableState<Bundle>,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
) {
    val navController = rememberNavController()
    accountNavController = navController

    DisposableEffect(Unit) {
        val callback = NavController.OnDestinationChangedListener { controller, _, _ ->
            navState.value = controller.saveState() ?: Bundle()
        }
        navController.addOnDestinationChangedListener(callback)
        navController.restoreState(navState.value)

        onDispose {
            navController.removeOnDestinationChangedListener(callback)
            // workaround for issue where back press is intercepted
            // outside this tab, even after this Composable is disposed
            navController.enableOnBackPressed(false)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Account.screen_route
    ) {
        composable(Screen.Account.screen_route) {
            Log.i("Account", "You are now on the Account tab")
            PricingUI(
                navController = navController
            )
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(
                navController,
                scope,
                state
            )
        }
        composable(
            "${Screen.SoundScreen.screen_route}/sound={sound}",
            arguments = listOf(navArgument("sound") {type = SoundObject.SoundType()})
        ) { backStackEntry ->
            val sound = backStackEntry.arguments?.getParcelable<SoundObject.Sound>("sound")
            Log.i("Sound Screen", "You are now on the ${sound!!.display_name} tab")
            SoundScreen(
                navController,
                sound.data,
                scope,
                state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
    }
}

/**
 * Saver to save and restore the current tab across config change and process death.
 */
fun screenSaver(): Saver<MutableState<Screen>, *> = Saver(
    save = { it.value.saveState() },
    restore = { mutableStateOf(Screen.restoreState(it)) }
)
//
fun navStateSaver(): Saver<MutableState<Bundle>, out Any> = Saver(
    save = { it.value },
    restore = { mutableStateOf(it) }
)