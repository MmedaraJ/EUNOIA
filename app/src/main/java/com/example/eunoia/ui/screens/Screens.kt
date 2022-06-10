package com.example.eunoia.ui.screens

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavBackStackEntry
import com.example.eunoia.R
import androidx.navigation.compose.*

/*fun NavBackStackEntry.getRoute(): String {
    return arguments?.getString(KEY_ROUTE) ?: ""
}*/

sealed class Screen(var title: String, var icon: Int, var screen_route: String) {
    object Routines: Screen("Routines", R.drawable.create_button,"routines")
    object Search: Screen("Search", R.drawable.search,"search")
    object Feedback: Screen("Feedback", R.drawable.feedback_icon,"feedback")
    object Account: Screen("Account", R.drawable.feedback_icon,"account")

    object Dashboard : Screen("Dashboard", R.drawable.cloud,"dashboard") {
        val routeWithArg: String = "$screen_route?arg={arg}"
        fun withArg(arg: String): String = routeWithArg.replace("{arg}", arg)
    }
    /*object Phrases : Screen("Phrases")
    object PhraseDetail : Screen("PhraseDetail?phrase={phrase}") {
        fun routeWithPhrase(phrase: String): String = screen_route.replace("{phrase}", phrase)
    }
    object DashboardDetail : Screen("DashboardDetail")*/

    object Sound: Screen("Sound", -1, "sound")
    object PouringRain: Screen("Pouring Rain", -1, "pouring_rain")
    object Settings: Screen("Settings", -1, "settings")
    object Article: Screen("Article", -1, "article")
    object Pricing: Screen("Pricing", -1, "pricing")

    //
    fun saveState(): Bundle {
        return bundleOf(KEY_SCREEN to screen_route)
    }

    companion object {
        fun restoreState(bundle: Bundle): Screen {
            val title = bundle.getString(KEY_SCREEN, Dashboard.screen_route)
            return when (title) {
                Dashboard.screen_route -> Dashboard
                Routines.screen_route -> Routines
                Search.screen_route -> Search
                Feedback.screen_route -> Feedback
                Account.screen_route -> Account
                Sound.screen_route -> Sound
                PouringRain.screen_route -> PouringRain
                Settings.screen_route -> Settings
                Article.screen_route -> Article
                Pricing.screen_route -> Pricing
                else -> Dashboard
            }
        }

        const val KEY_SCREEN = "screen_route"
    }
}