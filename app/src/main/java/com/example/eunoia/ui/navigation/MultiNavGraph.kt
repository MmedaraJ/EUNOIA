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
import com.example.eunoia.dashboard.routine.RoutineScreen
import com.example.eunoia.models.*
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.settings.eightHourCountdown.EightHourCountdownUI
import com.example.eunoia.ui.bottomSheets.*
import com.example.eunoia.ui.bottomSheets.recordAudio.RecordAudio
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.RecordAudioViewModel
import kotlinx.coroutines.CoroutineScope

var globalViewModel_: GlobalViewModel? = null
var recordAudioViewModel: RecordAudioViewModel? = null

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
                            "controls" -> {
                                BottomSheetAllControls(
                                    globalViewModel,
                                    scope,
                                    state,
                                    generalMediaPlayerService,
                                    soundMediaPlayerService
                                )
                                globalViewModel_!!.allowBottomSheetClose = true
                            }
                            "addToSoundListOrRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                AddToSoundListAndRoutineBottomSheet(scope, state)
                            }
                            "addToRoutine" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutine(scope, state)
                            }
                            "inputRoutineName" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                globalViewModel_!!.routineNameToBeAdded = InputRoutineName(scope, state)
                            }
                            "inputPresetName" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                globalViewModel_!!.presetNameToBeCreated = InputPresetName(scope, state)
                            }
                            "selectRoutineColor" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineColor(scope, state)
                            }
                            "selectRoutineIcon" -> {
                                globalViewModel_!!.allowBottomSheetClose = true
                                SelectRoutineIcon(scope, state)
                            }
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

    DisposableEffect(Unit) {
        val callback = NavController.OnDestinationChangedListener { navController, _, _ ->
            navController.saveState()
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
                generalMediaPlayerService
            )
        }
        composable(
            Screen.Sound.screen_route
        ) {
            Log.i("Sound", "You are now on the Sound tab")
            SoundActivityUI(
                navController,
                LocalContext.current,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
            )
        }
        composable(
            Screen.BedtimeStory.screen_route
        ) {
            Log.i("BedtimeStory", "You are now on the BedtimeStory tab")
            BedtimeStoryActivityUI(
                navController,
                LocalContext.current,
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
                LocalContext.current,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
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
                generalMediaPlayerService = generalMediaPlayerService
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
        }
        composable(
            "${Screen.RoutineScreen.screen_route}/routine={routine}",
            arguments = listOf(navArgument("routine") {type = RoutineObject.RoutineType()})
        ) { backStackEntry ->
            val routine = backStackEntry.arguments?.getParcelable<RoutineObject.Routine>("routine")
            Log.i("Routine Screen", "You are now on the ${routine!!.displayName} tab")
            RoutineScreen(
                navController,
                LocalContext.current,
                routine.data,
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
                LocalContext.current,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
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
                state = state
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
                state = state
            )
        }
        composable(Screen.UploadBedtimeStory.screen_route) {
            Log.i("UploadBedtimeStory", "You are now on the UploadBedtimeStory tab")
            UploadBedtimeStoryUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
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
            "${Screen.ChapterPageScreen.screen_route}/chapterPage={chapterPage}/{pageIndex}",
            arguments = listOf(
                navArgument("chapterPage") {
                    type = ChapterPageObject.ChapterPageType()
                },
                navArgument("pageIndex") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val chapterPage = backStackEntry.arguments?.getParcelable<ChapterPageObject.ChapterPage>("chapterPage")
            Log.i("ChapterPageScreen", "You are now on the ${chapterPage!!.displayName} tab")
           ChapterPageScreenUI(
                navController = navController,
               chapterPage.data,
                backStackEntry.arguments?.getString("pageIndex")!!.toInt(),
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
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
                bedtimeStoryData!!.data,
                scope = scope,
                state = state,
                generalMediaPlayerService = generalMediaPlayerService
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
                state = state
            )
        }
        composable(Screen.RecordPrayer.screen_route) {
            Log.i("RecordPrayer", "You are now on the RecordPrayer tab")
            RecordPrayerUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
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
                state = state
            )
        }
        composable(Screen.RecordSelfLove.screen_route) {
            Log.i("RecordSelfLove", "You are now on the RecordSelfLove tab")
            RecordSelfLoveUI(
                navController = navController,
                globalViewModel = globalViewModel,
                scope = scope,
                state = state
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
                LocalContext.current,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
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
                LocalContext.current,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
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
                LocalContext.current,
                scope,
                state,
                generalMediaPlayerService,
                soundMediaPlayerService
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