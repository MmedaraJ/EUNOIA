package com.example.eunoia.ui.screens

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.eunoia.R

/*fun NavBackStackEntry.getRoute(): String {
    return arguments?.getString(KEY_ROUTE) ?: ""
}*/

sealed class Screen(var title: String, var icon: Int, var screen_route: String) {
    object Search: Screen("Search", R.drawable.search,"search")
    object Feedback: Screen("Feedback", R.drawable.feedback_icon,"feedback")
    object Account: Screen("Account", R.drawable.feedback_icon,"account")

    object Dashboard : Screen("Dashboard", R.drawable.cloud,"dashboard") {
        val routeWithArg: String = "$screen_route?arg={arg}"
        fun withArg(arg: String): String = routeWithArg.replace("{arg}", arg)
    }

    object Sound: Screen("User", -1, "sound")
    object SoundScreen: Screen("Sound Screen", -1, "sound_screen")
    object Settings: Screen("Settings", -1, "settings")
    object Article: Screen("Article", -1, "article")
    object Pricing: Screen("Pricing", -1, "pricing")

    //create element
    object Create: Screen("Create", R.drawable.create_button,"create")
    object NameSound: Screen("NameSound", -1,"name_sound")
    object UploadSounds: Screen("UploadSounds", -1,"upload_sounds")
    object CreatePreset: Screen("CreatePreset", -1,"create_preset")

    //
    fun saveState(): Bundle {
        return bundleOf(KEY_SCREEN to screen_route)
    }

    companion object {
        fun restoreState(bundle: Bundle): Screen {
            val title = bundle.getString(KEY_SCREEN, Dashboard.screen_route)
            return when (title) {
                Dashboard.screen_route -> Dashboard
                Create.screen_route -> Create
                NameSound.screen_route -> NameSound
                UploadSounds.screen_route -> UploadSounds
                CreatePreset.screen_route -> CreatePreset
                Search.screen_route -> Search
                Feedback.screen_route -> Feedback
                Account.screen_route -> Account
                Sound.screen_route -> Sound
                SoundScreen.screen_route -> SoundScreen
                Settings.screen_route -> Settings
                Article.screen_route -> Article
                Pricing.screen_route -> Pricing
                else -> Dashboard
            }
        }

        const val KEY_SCREEN = "screen_route"
    }
}