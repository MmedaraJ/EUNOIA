package com.example.eunoia.ui.screens

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import com.example.eunoia.R

/*fun NavBackStackEntry.getRoute(): String {
    return arguments?.getString(KEY_ROUTE) ?: ""
}*/

sealed class Screen(var title: String, var icon: Int, var screen_route: String) {
    /**
     * bottom nav bar
     */
    object Search: Screen("Search", R.drawable.search,"search")
    object Feedback: Screen("Feedback", R.drawable.feedback_icon,"feedback")
    object Account: Screen("Account", R.drawable.feedback_icon,"account")
    object Dashboard : Screen("Dashboard", R.drawable.cloud,"dashboard")
    object Create: Screen("Create", R.drawable.create_button,"create")

    /**
     * Sound
     */
    object Sound: Screen("Sound", -1, "sound")
    object SoundScreen: Screen("Sound Screen", -1, "sound_screen")

    /**
     * Bedtime story
     */
    object BedtimeStory: Screen("Bedtime Story", -1, "bedtime_story")
    object BedtimeStoryScreen: Screen("Bedtime Story Screen", -1,"bedtime_story_screen")

    /**
     * Self love
     */
    object SelfLove: Screen("Self Love", -1, "self_love")
    object SelfLoveScreen: Screen("Self Love Screen", -1, "self_love_screen")

    /**
     * Prayer
     */
    object Prayer: Screen("Prayer", -1, "prayer")
    object PrayerScreen: Screen("Prayer Screen", -1, "prayer_screen")

    /**
     * Settings
     */
    object Settings: Screen("Settings", -1, "settings")
    object EightHourCountdown: Screen("Eight Hour Countdown", -1,"eight_hour_countdown")

    /**
     * Article
     */
    object Article: Screen("Article", -1, "article")

    /**
     * Pricing
     */
    object Pricing: Screen("Pricing", -1, "pricing")

    /**
     * Create Sound
     */
    object NameSound: Screen("Name Sound", -1,"name_sound")
    object UploadSounds: Screen("Upload Sounds", -1,"upload_sounds")
    object CreatePreset: Screen("Create Preset", -1,"create_preset")

    /**
     * Create bedtime story
     */
    object NameBedtimeStory: Screen("Name Bedtime Story", -1,"name_bedtime_story")
    object RecordBedtimeStory: Screen("Record Bedtime Story", -1,"record_bedtime_story")
    object UploadBedtimeStory: Screen("Upload Bedtime Story", -1,"upload_bedtime_story")
    object BedtimeStoryChapterScreen: Screen("Bedtime Story Chapter Screen", -1,"bedtime_story_chapter_screen")
    object IncompleteBedtimeStories: Screen("Incomplete Bedtime Story", -1,"incomplete_bedtime_story")
    object PageScreen: Screen("Page Screen", -1,"page_screen")

    /**
     * Create prayer
     */
    object NamePrayer: Screen("Name Prayer", -1,"name_prayer")
    object UploadPrayer: Screen("Upload Prayer", -1,"upload_prayer")
    object RecordPrayer: Screen("Record Prayer", -1,"record_prayer")

    /**
     * Create self love
     */
    object NameSelfLove: Screen("Name Self Love", -1,"name_self_love")
    object UploadSelfLove: Screen("Upload Self Love", -1,"upload_self_love")
    object RecordSelfLove: Screen("Record Self Love", -1,"record_self_love")

    /**
     * Routine
     */
    object RoutineScreen: Screen("Routine Screen", -1, "routine_screen")
    object RoutinePresetScreen: Screen("Routine Preset Screen", -1, "routine_preset_screen")

    fun saveState(): Bundle {
        return bundleOf(KEY_SCREEN to screen_route)
    }

    companion object {
        fun restoreState(bundle: Bundle): Screen {
            val title = bundle.getString(KEY_SCREEN, Dashboard.screen_route)
            return when (title) {
                /**
                 * bottom nav bar
                 */
                Dashboard.screen_route -> Dashboard
                Create.screen_route -> Create
                Feedback.screen_route -> Feedback
                Account.screen_route -> Account
                Search.screen_route -> Search

                /**
                 * Create Sound
                 */
                NameSound.screen_route -> NameSound
                CreatePreset.screen_route -> CreatePreset
                UploadSounds.screen_route -> UploadSounds

                /**
                 * Create Bedtime Story
                 */
                NameBedtimeStory.screen_route -> NameBedtimeStory
                RecordBedtimeStory.screen_route -> RecordBedtimeStory
                UploadBedtimeStory.screen_route -> UploadBedtimeStory
                BedtimeStoryChapterScreen.screen_route -> BedtimeStoryChapterScreen
                IncompleteBedtimeStories.screen_route -> IncompleteBedtimeStories
                PageScreen.screen_route -> PageScreen

                /**
                 * Create Prayer
                 */
                NamePrayer.screen_route -> NamePrayer
                UploadPrayer.screen_route -> UploadPrayer
                RecordPrayer.screen_route -> RecordPrayer

                /**
                 * Create Self Love
                 */
                NameSelfLove.screen_route -> NameSelfLove
                UploadSelfLove.screen_route -> UploadSelfLove
                RecordSelfLove.screen_route -> RecordSelfLove

                /**
                 * Sound
                 */
                Sound.screen_route -> Sound
                SoundScreen.screen_route -> SoundScreen

                /**
                 * Self love
                 */
                SelfLove.screen_route -> SelfLove
                SelfLoveScreen.screen_route -> SelfLoveScreen

                /**
                 * Bedtime story
                 */
                BedtimeStory.screen_route -> BedtimeStory
                BedtimeStoryScreen.screen_route -> BedtimeStoryScreen

                /**
                 * Prayer
                 */
                Prayer.screen_route -> Prayer
                PrayerScreen.screen_route -> PrayerScreen

                /**
                 * Routine
                 */
                RoutineScreen.screen_route -> RoutineScreen
                RoutinePresetScreen.screen_route -> RoutinePresetScreen

                /**
                 * Settings
                 */
                Settings.screen_route -> Settings
                EightHourCountdown.screen_route -> EightHourCountdown

                /**
                 * Article
                 */
                Article.screen_route -> Article

                /**
                 * Pricing
                 */
                Pricing.screen_route -> Pricing

                else -> Dashboard
            }
        }

        fun getScreenFromRoute(route: String): Screen {
            return when (route) {
                /**
                 * bottom nav bar
                 */
                Dashboard.screen_route -> Dashboard
                Create.screen_route -> Create
                Feedback.screen_route -> Feedback
                Account.screen_route -> Account
                Search.screen_route -> Search

                /**
                 * Create Sound
                 */
                NameSound.screen_route -> NameSound
                CreatePreset.screen_route -> CreatePreset
                UploadSounds.screen_route -> UploadSounds

                /**
                 * Create Bedtime Story
                 */
                NameBedtimeStory.screen_route -> NameBedtimeStory
                RecordBedtimeStory.screen_route -> RecordBedtimeStory
                UploadBedtimeStory.screen_route -> UploadBedtimeStory
                BedtimeStoryChapterScreen.screen_route -> BedtimeStoryChapterScreen
                IncompleteBedtimeStories.screen_route -> IncompleteBedtimeStories
                PageScreen.screen_route -> PageScreen

                /**
                 * Create Prayer
                 */
                NamePrayer.screen_route -> NamePrayer
                UploadPrayer.screen_route -> UploadPrayer
                RecordPrayer.screen_route -> RecordPrayer

                /**
                 * Create Self Love
                 */
                NameSelfLove.screen_route -> NameSelfLove
                UploadSelfLove.screen_route -> UploadSelfLove
                RecordSelfLove.screen_route -> RecordSelfLove

                /**
                 * Sound
                 */
                Sound.screen_route -> Sound
                SoundScreen.screen_route -> SoundScreen

                /**
                 * Self love
                 */
                SelfLove.screen_route -> SelfLove
                SelfLoveScreen.screen_route -> SelfLoveScreen

                /**
                 * Bedtime story
                 */
                BedtimeStory.screen_route -> BedtimeStory
                BedtimeStoryScreen.screen_route -> BedtimeStoryScreen

                /**
                 * Prayer
                 */
                Prayer.screen_route -> Prayer
                PrayerScreen.screen_route -> PrayerScreen

                /**
                 * Routine
                 */
                RoutineScreen.screen_route -> RoutineScreen
                RoutinePresetScreen.screen_route -> RoutinePresetScreen

                /**
                 * Settings
                 */
                Settings.screen_route -> Settings
                EightHourCountdown.screen_route -> EightHourCountdown

                /**
                 * Article
                 */
                Article.screen_route -> Article

                /**
                 * Pricing
                 */
                Pricing.screen_route -> Pricing

                else -> Dashboard
            }
        }

        const val KEY_SCREEN = "screen_route"
    }
}