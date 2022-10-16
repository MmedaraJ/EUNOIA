package com.example.eunoia.ui.navigation

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.eunoia.viewModels.GlobalViewModel
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
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.RecordAudioViewModel
import kotlinx.coroutines.CoroutineScope

var globalViewModel_: GlobalViewModel? = null
var recordAudioViewModel: RecordAudioViewModel? = null
var generalMediaPlayerService_: GeneralMediaPlayerService? = null

var commentCreatedDialog by mutableStateOf(false)
var openSavedElementDialogBox by mutableStateOf(false)
var openSoundNameTakenDialogBox by mutableStateOf(false)
var openPrayerNameTakenDialogBox by mutableStateOf(false)
var openPresetAlreadyExistsDialog by mutableStateOf(false)
var openSelfLoveNameTakenDialogBox by mutableStateOf(false)
var openUserAlreadyHasSoundDialogBox by mutableStateOf(false)
var openUserAlreadyHasPrayerDialogBox by mutableStateOf(false)
var openPresetNameIsAlreadyTakenDialog by mutableStateOf(false)
var openBedtimeStoryNameTakenDialogBox by mutableStateOf(false)
var openUserAlreadyHasSelfLoveDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasSoundDialogBox by mutableStateOf(false)
var openRoutineNameIsAlreadyTakenDialog by mutableStateOf(false)
var openRoutineAlreadyHasPrayerDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasPresetDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasSelfLoveDialogBox by mutableStateOf(false)
var openUserAlreadyHasBedtimeStoryDialogBox by mutableStateOf(false)
var openTooManyIncompleteBedtimeStoryDialogBox by mutableStateOf(false)
var openRoutineAlreadyHasBedtimeStoryDialogBox by mutableStateOf(false)

var openRoutineIsCurrentlyPlayingDialogBox by mutableStateOf(false)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiBottomNavApp(
    globalViewModel: GlobalViewModel = viewModel(),
) {
    globalViewModel_ = globalViewModel
    recordAudioViewModel = viewModel()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { globalViewModel_!!.allowBottomSheetClose }
    )
    val scope = rememberCoroutineScope()
    val generalMediaPlayerService = GeneralMediaPlayerService()
    generalMediaPlayerService_ = generalMediaPlayerService
    val soundMediaPlayerService = SoundMediaPlayerService()
    EunoiaApp(
        globalViewModel,
        scope,
        modalBottomSheetState,
        generalMediaPlayerService,
        soundMediaPlayerService,
    ) { screen ->
        MultiNavTabContent(
            screen,
            globalViewModel,
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
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
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
                        when(globalViewModel.bottomSheetOpenFor){
                            /**
                             * Currently playing controls
                             */
                            "controls" -> {
                                BottomSheetAllControls(
                                    globalViewModel,
                                    scope,
                                    state,
                                    generalMediaPlayerService,
                                    soundMediaPlayerService,
                                )
                                globalViewModel_!!.allowBottomSheetClose = true
                            }

                            /**
                             * Add sound to list or routine
                             */
                            "addToSoundListOrRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                AddToSoundListAndRoutineBottomSheet(scope, state)
                            }
                            "addToRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineForSound(scope, state)
                            }
                            "inputRoutineName" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                globalViewModel_!!.routineNameToBeAdded = inputRoutineName(scope, state)
                            }
                            "inputPresetName" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                globalViewModel_!!.presetNameToBeCreated = inputPresetName(scope, state)
                            }
                            "selectRoutineColor" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineColor(scope, state)
                            }
                            "selectRoutineIcon" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineIcon(scope, state)
                            }

                            /**
                             * Add bedtime story to list or routine
                             */
                            "addToBedtimeStoryListOrRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                AddToBedtimeStoryListAndRoutineBottomSheet(scope, state)
                            }
                            "addBedtimeStoryToRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineForBedtimeStory(scope, state)
                            }
                            "inputRoutineNameForBedtimeStory" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                globalViewModel_!!.routineNameToBeAdded = inputRoutineNameForBedtimeStory(scope, state)
                            }
                            "selectRoutineColorForBedtimeStory" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineColorForBedtimeStory(scope, state)
                            }
                            "selectRoutineIconForBedtimeStory" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineIconForBedtimeStory(scope, state)
                            }

                            /**
                             * Add self love to list or routine
                             */
                            "addToSelfLoveListOrRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                AddToSelfLoveListAndRoutineBottomSheet(scope, state)
                            }
                            "addSelfLoveToRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineForSelfLove(scope, state)
                            }
                            "inputRoutineNameForSelfLove" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                globalViewModel_!!.routineNameToBeAdded = inputRoutineNameForSelfLove(scope, state)
                            }
                            "selectRoutineColorForSelfLove" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineColorForSelfLove(scope, state)
                            }
                            "selectRoutineIconForSelfLove" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineIconForSelfLove(scope, state)
                            }
                            "showSelfLoveLyrics" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                ShowSelfLoveLyricsBottomSheet(scope, state)
                            }

                            /**
                             * Add prayer to list or routine
                             */
                            "addToPrayerListOrRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                AddToPrayerListAndRoutineBottomSheet(scope, state)
                            }
                            "addPrayerToRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineForPrayer(scope, state)
                            }
                            "inputRoutineNameForPrayer" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                globalViewModel_!!.routineNameToBeAdded = inputRoutineNameForPrayer(scope, state)
                            }
                            "selectRoutineColorForPrayer" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineColorForPrayer(scope, state)
                            }
                            "selectRoutineIconForPrayer" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineIconForPrayer(scope, state)
                            }

                            /**
                             * Record audio
                             */
                            "recordAudio" -> {
                                globalViewModel_!!.allowBottomSheetClose = false
                                RecordAudio(globalViewModel, scope, state, generalMediaPlayerService)
                            }

                            "" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                BottomSheetAllControls(
                                    globalViewModel,
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
                                    onClick = {
                                        if(currentTab == item){
                                            when(item){
                                                Screen.Dashboard -> {
                                                    if(dashboardNavController != null){
                                                        dashboardNavController!!.popBackStack()
                                                    }
                                                }
                                                Screen.Search -> {
                                                    if(searchNavController != null){
                                                        searchNavController!!.popBackStack()
                                                    }
                                                }
                                                Screen.Create -> {
                                                    if(createNavController != null){
                                                        createNavController!!.popBackStack()
                                                    }
                                                }
                                                Screen.Feedback -> {
                                                    if(feedbackNavController != null){
                                                        feedbackNavController!!.popBackStack()
                                                    }
                                                }
                                                Screen.Account -> {
                                                    if(accountNavController != null){
                                                        accountNavController!!.popBackStack()
                                                    }
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
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiNavTabContent(
    screen: Screen,
    globalViewModel: GlobalViewModel,
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
        Screen.Dashboard -> DashboardTab(dashboardNavState, globalViewModel, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Create -> CreateTab(createNavState, globalViewModel, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Search -> SearchTab(searchNavState, globalViewModel, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Feedback -> FeedbackTab(feedbackNavState, globalViewModel, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        Screen.Account -> AccountTab(accountNavState, globalViewModel, scope, state, generalMediaPlayerService, soundMediaPlayerService)
        else -> DashboardTab(dashboardNavState, globalViewModel, scope, state, generalMediaPlayerService, soundMediaPlayerService)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardTab(
    navState: MutableState<Bundle>,
    globalViewModel: GlobalViewModel,
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
                globalViewModel,
                scope,
                state,
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
                globalViewModel,
                LocalContext.current,
                scope,
                state
            )
        }
        composable(Screen.Article.screen_route) {
            Log.i("Article", "You are now on the Article tab")
            ArticleUI(
                navController,
                globalViewModel
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
            Log.i("Routine Screen", "You are now on the ${userRoutineRelationship!!.userRoutineRelationshipRoutine.displayName} tab")
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
    globalViewModel: GlobalViewModel,
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
                globalViewModel = globalViewModel,
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
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
            )
        }
        composable(Screen.UploadSounds.screen_route) {
            Log.i("UploadSounds", "You are now on the UploadSounds tab")
            UploadSoundsUI(
                navController = navController,
                globalViewModel = globalViewModel,
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
                globalViewModel = globalViewModel,
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
                globalViewModel = globalViewModel,
                scope = scope,
                state = state,
                generalMediaPlayerService
            )
        }
        composable(Screen.UploadBedtimeStory.screen_route) {
            Log.i("UploadBedtimeStory", "You are now on the UploadBedtimeStory tab")
            UploadBedtimeStoryUI(
                navController = navController,
                globalViewModel = globalViewModel,
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
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
            )
        }
        composable(
            "${Screen.PageScreen.screen_route}/chapterPage={chapterPage}/{pageIndex}",
            arguments = listOf(
                navArgument("chapterPage") {
                    type = PageObject.PageType()
                },
                navArgument("pageIndex") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val chapterPage = backStackEntry.arguments?.getParcelable<PageObject.Page>("chapterPage")
            Log.i("ChapterPageScreen", "You are now on the ${chapterPage!!.displayName} tab")
           PageScreenUI(
                navController = navController,
               chapterPage.data,
                backStackEntry.arguments?.getString("pageIndex")!!.toInt(),
                globalViewModel = globalViewModel,
                scope = scope,
                state = state,
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
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
            )
        }
        composable(Screen.NamePrayer.screen_route) {
            Log.i("NamePrayer", "You are now on the NamePrayer tab")
            NamePrayerUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
            )
        }
        composable(Screen.UploadPrayer.screen_route) {
            Log.i("UploadPrayer", "You are now on the UploadPrayer tab")
            UploadPrayerUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(Screen.RecordPrayer.screen_route) {
            Log.i("RecordPrayer", "You are now on the RecordPrayer tab")
            RecordPrayerUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(Screen.NameSelfLove.screen_route) {
            Log.i("NameSelfLove", "You are now on the NameSelfLove tab")
            NameSelfLoveUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
            )
        }
        composable(Screen.UploadSelfLove.screen_route) {
            Log.i("UploadSelfLove", "You are now on the UploadSelfLove tab")
            UploadSelfLoveUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state,
                soundMediaPlayerService,
                generalMediaPlayerService
            )
        }
        composable(Screen.RecordSelfLove.screen_route) {
            Log.i("RecordSelfLove", "You are now on the RecordSelfLove tab")
            RecordSelfLoveUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state,
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
    globalViewModel: GlobalViewModel,
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
    globalViewModel: GlobalViewModel,
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
            FeedbackUI(navController = navController, context = LocalContext.current, globalViewModel)
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(
                navController,
                globalViewModel,
                LocalContext.current,
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
    globalViewModel: GlobalViewModel,
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
            PricingUI(navController = navController, globalViewModel)
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(
                navController,
                globalViewModel,
                LocalContext.current,
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