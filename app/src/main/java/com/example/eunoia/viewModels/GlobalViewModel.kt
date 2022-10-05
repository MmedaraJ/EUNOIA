package com.example.eunoia.viewModels

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.BedtimeStoryTimer
import com.example.eunoia.utils.GeneralPlaytimeTimer
import com.example.eunoia.utils.PrayerTimer
import com.example.eunoia.utils.SelfLoveTimer
import dagger.hilt.android.lifecycle.HiltViewModel

class GlobalViewModel: ViewModel(){
    //sound
    var currentSoundPlaying by mutableStateOf<SoundData?>(null)
    var isCurrentSoundPlaying by mutableStateOf(false)
    var currentSoundPlayingPreset by mutableStateOf<PresetData?>(null)
    var currentAllOriginalSoundPreset by mutableStateOf<MutableSet<PresetData>?>(null)
    var currentAllUserSoundPreset by mutableStateOf<MutableSet<PresetData>?>(null)
    var currentSoundPlayingContext by mutableStateOf<Context?>(null)
    var currentSoundPlayingSliderPositions = mutableListOf<MutableState<Float>?>()
    var currentSoundPlayingUris: MutableList<Uri?>? = null
    var currentUsersSounds by mutableStateOf<MutableList<UserSound?>?>(null)
    var currentUsersSoundRelationships by mutableStateOf<MutableList<UserSoundRelationship?>?>(null)
    var soundTimerTime by mutableStateOf(0L)
    var soundMeditationBellInterval by mutableStateOf(0)
    var soundCountDownTimer: CountDownTimer? = null
    var soundPlayTimeTimer: CountDownTimer? = null
    var soundSliderVolumes: MutableList<Int>? = null
    var soundMeditationBellMediaPlayer: MediaPlayer? = null
    var previouslyPlayedUserSoundRelationship: UserSoundRelationship? = null

    //adding sound
    var currentSoundToBeAdded by mutableStateOf<SoundData?>(null)
    var currentPresetToBeAdded by mutableStateOf<PresetData?>(null)
    var presetNameToBeCreated by mutableStateOf("")

    //routine
    var currentRoutinePlaying by mutableStateOf("")
    var routineNameToBeAdded by mutableStateOf("")
    var routineColorToBeAdded by mutableStateOf<Long?>(null)
    var routineIconToBeAdded by mutableStateOf<Int?>(null)
    var currentUsersRoutines by mutableStateOf<MutableList<UserRoutine?>?>(null)

    var isCurrentRoutinePlaying by mutableStateOf(false)

    var bottomSheetOpenFor by mutableStateOf("")

    //user
    var currentUser by mutableStateOf<UserData?>(null)

    //navController
    var navController by mutableStateOf<NavController?>(null)

    val soundScreenIcons = arrayOf(
        mutableStateOf(R.drawable.replay_sound_icon),
        mutableStateOf(R.drawable.reset_sliders_icon),
        mutableStateOf(R.drawable.sound_timer_icon),
        mutableStateOf(R.drawable.play_icon),
        mutableStateOf(R.drawable.increase_levels_icon),
        mutableStateOf(R.drawable.decrease_levels_icon),
        mutableStateOf(R.drawable.meditation_bell_icon),
    )
    val addIcon = mutableStateOf(R.drawable.add_sound_icon)

    var soundScreenBorderControlColors = arrayOf(
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
    )
    var soundScreenBackgroundControlColor1 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    var soundScreenBackgroundControlColor2 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(Solitude),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    val mixerColors = arrayOf(
        GoldSand,
        JungleMist,
        PeriwinkleGray,
        BeautyBush,
        WaikawaGray,
        Madang,
        SeaPink,
        Twine,
        DoveGray,
        Neptune,
    )

    var allowBottomSheetClose by mutableStateOf(true)

    /**
     * General playtime timer
     */
    var generalPlaytimeTimer = GeneralPlaytimeTimer(UserDashboardActivity.getInstanceActivity())

    /**
     * bedtime story
     *
     */
    var currentUsersBedtimeStories by mutableStateOf<MutableList<UserBedtimeStoryInfo?>?>(null)
    var currentBedtimeStoryPlaying by mutableStateOf<BedtimeStoryInfoData?>(null)
    var isCurrentBedtimeStoryPlaying by mutableStateOf(false)
    var currentBedtimeStoryPlayingUri: Uri? = null
    var bedtimeStoryTimeDisplay by mutableStateOf("00.00")
    var bedtimeStoryCircularSliderAngle by mutableStateOf(0f)
    var bedtimeStoryCircularSliderClicked by mutableStateOf(false)
    var bedtimeStoryTimer = BedtimeStoryTimer(UserDashboardActivity.getInstanceActivity())

    var currentBedtimeStoryToBeAdded by mutableStateOf<BedtimeStoryInfoData?>(null)

    val bedtimeStoryScreenIcons = arrayOf(
        mutableStateOf(R.drawable.reset_sliders_icon),
        mutableStateOf(R.drawable.seek_back_15),
        mutableStateOf(R.drawable.play_icon),
        mutableStateOf(R.drawable.seek_forward_15),
    )

    var bedtimeStoryScreenBorderControlColors = arrayOf(
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
    )
    var bedtimeStoryScreenBackgroundControlColor1 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    var bedtimeStoryScreenBackgroundControlColor2 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(Solitude),
        mutableStateOf(White),
        mutableStateOf(White),
    )


    /**
     * self love
     */
    var currentUsersSelfLoves by mutableStateOf<MutableList<UserSelfLove?>?>(null)
    var currentSelfLovePlaying by mutableStateOf<SelfLoveData?>(null)
    var isCurrentSelfLovePlaying by mutableStateOf(false)
    var currentSelfLovePlayingUri: Uri? = null
    var selfLoveTimeDisplay by mutableStateOf("00.00")
    var selfLoveCircularSliderAngle by mutableStateOf(0f)
    var selfLoveCircularSliderClicked by mutableStateOf(false)
    var selfLoveTimer = SelfLoveTimer(UserDashboardActivity.getInstanceActivity())

    var currentSelfLoveToBeAdded by mutableStateOf<SelfLoveData?>(null)
    var currentSelfLoveLyricsToBeShown by mutableStateOf<SelfLoveData?>(null)

    val selfLoveScreenIcons = arrayOf(
        mutableStateOf(R.drawable.reset_sliders_icon),
        mutableStateOf(R.drawable.seek_back_15),
        mutableStateOf(R.drawable.play_icon),
        mutableStateOf(R.drawable.seek_forward_15),
    )

    var selfLoveScreenBorderControlColors = arrayOf(
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
    )
    var selfLoveScreenBackgroundControlColor1 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    var selfLoveScreenBackgroundControlColor2 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(Solitude),
        mutableStateOf(White),
        mutableStateOf(White),
    )


    /**
     * prayer
     */
    var currentUsersPrayers by mutableStateOf<MutableList<UserPrayer?>?>(null)
    var currentPrayerPlaying by mutableStateOf<PrayerData?>(null)
    var isCurrentPrayerPlaying by mutableStateOf(false)
    var currentPrayerPlayingUri: Uri? = null
    var prayerTimeDisplay by mutableStateOf("00.00")
    var prayerCircularSliderAngle by mutableStateOf(0f)
    var prayerCircularSliderClicked by mutableStateOf(false)
    var prayerTimer = PrayerTimer(UserDashboardActivity.getInstanceActivity())

    var currentPrayerToBeAdded by mutableStateOf<PrayerData?>(null)

    val prayerScreenIcons = arrayOf(
        mutableStateOf(R.drawable.reset_sliders_icon),
        mutableStateOf(R.drawable.seek_back_15),
        mutableStateOf(R.drawable.play_icon),
        mutableStateOf(R.drawable.seek_forward_15),
    )

    var prayerScreenBorderControlColors = arrayOf(
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
    )
    var prayerScreenBackgroundControlColor1 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    var prayerScreenBackgroundControlColor2 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(Solitude),
        mutableStateOf(White),
        mutableStateOf(White),
    )
}