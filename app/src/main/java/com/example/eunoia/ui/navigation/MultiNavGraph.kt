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
import com.example.eunoia.create.createSound.NameSoundUI
import com.example.eunoia.create.CreateUI
import com.example.eunoia.create.createSound.CreatePresetUI
import com.example.eunoia.create.createSound.UploadSoundsUI
import com.example.eunoia.models.SoundObject
import com.example.eunoia.ui.bottomSheets.*
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope

var globalViewModel_: GlobalViewModel? = null

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiBottomNavApp(globalViewModel: GlobalViewModel = viewModel()) {
    globalViewModel_ = globalViewModel
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    EunoiaApp(
        globalViewModel,
        scope,
        modalBottomSheetState
    ) {screen ->
        MultiNavTabContent(
            screen = screen,
            globalViewModel,
            scope,
            modalBottomSheetState
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EunoiaApp(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
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
                            "controls" -> BottomSheetAllControls(globalViewModel, scope, state)
                            "addToSoundListOrRoutine" -> AddToSoundListAndRoutineBottomSheet(scope, state)
                            "addToRoutine" -> SelectRoutine(scope, state)
                            "inputRoutineName" -> globalViewModel_!!.routineNameToBeAdded = InputRoutineName(scope, state)
                            "selectRoutineColor" -> SelectRoutineColor(scope, state)
                            "selectRoutineIcon" -> SelectRoutineIcon(scope, state)
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
        Screen.Dashboard -> DashboardTab(dashboardNavState, globalViewModel, scope, state)
        Screen.Create -> CreateTab(createNavState, globalViewModel, scope, state)
        Screen.Search -> SearchTab(searchNavState, globalViewModel, scope, state)
        Screen.Feedback -> FeedbackTab(feedbackNavState, globalViewModel, scope, state)
        Screen.Account -> AccountTab(accountNavState, globalViewModel, scope, state)
        else -> DashboardTab(dashboardNavState, globalViewModel, scope, state)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardTab(
    navState: MutableState<Bundle>,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
) {
    val navController = rememberNavController()

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
                state
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
                state
            )
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(
                navController,
                globalViewModel
            )
        }
        composable(Screen.Article.screen_route) {
            Log.i("Article", "You are now on the Article tab")
            ArticleUI(
                navController,
                globalViewModel
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
            arguments = listOf(navArgument("sound") {type = SoundObject.SoundType()})
        ) { backStackEntry ->
            val sound = backStackEntry.arguments?.getParcelable<SoundObject.Sound>("sound")
            Log.i("Sound Screen", "You are now on the ${sound!!.display_name} tab")
            SoundScreen(
                navController,
                sound.data,
                LocalContext.current,
                scope,
                state
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
                state
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
            Settings(navController, globalViewModel)
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
                state
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
            Settings(navController, globalViewModel)
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
                state
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