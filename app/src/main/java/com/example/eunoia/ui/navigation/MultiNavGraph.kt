package com.example.eunoia.ui.navigation

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
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
import com.example.eunoia.dashboard.ArticleUI
import com.example.eunoia.dashboard.home.UserDashboardActivityUI
import com.example.eunoia.dashboard.sound.EunoiaSoundScreen
import com.example.eunoia.dashboard.sound.SoundActivityUI
import com.example.eunoia.feedback.FeedbackUI
import com.example.eunoia.pricing.PricingUI
import com.example.eunoia.settings.Settings
import com.example.eunoia.ui.theme.Grey
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.EUNOIATheme

@Composable
fun MultiBottomNavApp() {
    EunoiaApp {
        MultiNavTabContent(screen = it)
    }
}

@Composable
fun EunoiaApp(bodyContent: @Composable (Screen) -> Unit){
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
            Scaffold(
                content = {padding ->
                    Column(modifier = Modifier.padding(padding)) {
                        bodyContent(currentTab)
                    }
                },
                bottomBar = {
                    val items = listOf(
                        Screen.Dashboard,
                        Screen.Routines,
                        Screen.Search,
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

@Composable
fun MultiNavTabContent(screen: Screen) {
    val dashboardNavState = rememberSaveable(
        saver = navStateSaver()
    ) { mutableStateOf(Bundle()) }
    val routinesNavState = rememberSaveable(
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
        Screen.Dashboard -> DashboardTab(dashboardNavState)
        Screen.Routines -> RoutinesTab(routinesNavState)
        Screen.Search -> SearchTab(searchNavState)
        Screen.Feedback -> FeedbackTab(feedbackNavState)
        Screen.Account -> AccountTab(accountNavState)
        else -> DashboardTab(dashboardNavState)
    }
}

@Composable
fun DashboardTab(navState: MutableState<Bundle>) {
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
            UserDashboardActivityUI(navController)
        }
        composable(
            Screen.Sound.screen_route
        ) {
            Log.i("Sound", "You are now on the Sound tab")
            SoundActivityUI(navController, LocalContext.current)
        }
        composable(Screen.PouringRain.screen_route) {
            Log.i("Pouring Rain", "You are now on the Pouring Rain tab")
            EunoiaSoundScreen(navController, Screen.PouringRain.screen_route)
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(navController)
        }
        composable(Screen.Article.screen_route) {
            Log.i("Article", "You are now on the Article tab")
            ArticleUI(navController)
        }
    }
}

@Composable
fun RoutinesTab(navState: MutableState<Bundle>) {
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
        startDestination = Screen.Routines.screen_route
    ) {
        composable(Screen.Routines.screen_route) {
            Log.i("Routines", "You are now on the Routines tab")
            Text(text = "Routines")
        }
    }
}

@Composable
fun SearchTab(navState: MutableState<Bundle>) {
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
    }
}

@Composable
fun FeedbackTab(navState: MutableState<Bundle>) {
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
            FeedbackUI(navController = navController, context = LocalContext.current)
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(navController)
        }
    }
}

@Composable
fun AccountTab(navState: MutableState<Bundle>) {
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
            PricingUI(navController = navController)
        }
        composable(Screen.Settings.screen_route) {
            Log.i("Settings", "You are now on the Settings tab")
            Settings(navController)
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