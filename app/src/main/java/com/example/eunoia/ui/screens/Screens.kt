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
    object BedtimeStoryScreen: Screen("Bedtime Story Screen", -1,"bedtime_story_screen")
    object Settings: Screen("Settings", -1, "settings")
    object Article: Screen("Article", -1, "article")
    object Pricing: Screen("Pricing", -1, "pricing")

    //create element
    //sound
    object Create: Screen("Create", R.drawable.create_button,"create")
    object NameSound: Screen("Name Sound", -1,"name_sound")
    object UploadSounds: Screen("Upload Sounds", -1,"upload_sounds")
    object CreatePreset: Screen("Create Preset", -1,"create_preset")
    //bedtime story
    object NameBedtimeStory: Screen("Name Bedtime Story", -1,"name_bedtime_story")
    object RecordBedtimeStory: Screen("Record Bedtime Story", -1,"record_bedtime_story")
    object UploadBedtimeStory: Screen("Upload Bedtime Story", -1,"upload_bedtime_story")
    object BedtimeStoryChapterScreen: Screen("Bedtime Story Chapter Screen", -1,"bedtime_story_chapter_screen")
    object IncompleteBedtimeStories: Screen("Incomplete Bedtime Story", -1,"incomplete_bedtime_story")
    object ChapterPageScreen: Screen("Chapter Page Screen", -1,"chapter_page_screen")

    //routine
    object RoutineScreen: Screen("Routine Screen", -1, "routine_screen")

    //Settings
    object EightHourCountdown: Screen("Eight Hour Countdown", -1,"eight_hour_countdown")
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
                NameBedtimeStory.screen_route -> NameBedtimeStory
                RecordBedtimeStory.screen_route -> RecordBedtimeStory
                UploadBedtimeStory.screen_route -> UploadBedtimeStory
                BedtimeStoryChapterScreen.screen_route -> BedtimeStoryChapterScreen
                IncompleteBedtimeStories.screen_route -> IncompleteBedtimeStories
                ChapterPageScreen.screen_route -> ChapterPageScreen
                UploadSounds.screen_route -> UploadSounds
                CreatePreset.screen_route -> CreatePreset
                Search.screen_route -> Search
                Feedback.screen_route -> Feedback
                Account.screen_route -> Account
                Sound.screen_route -> Sound
                SoundScreen.screen_route -> SoundScreen
                RoutineScreen.screen_route -> RoutineScreen
                BedtimeStoryScreen.screen_route -> BedtimeStoryScreen
                Settings.screen_route -> Settings
                EightHourCountdown.screen_route -> EightHourCountdown
                Article.screen_route -> Article
                Pricing.screen_route -> Pricing
                else -> Dashboard
            }
        }

        const val KEY_SCREEN = "screen_route"
    }
}