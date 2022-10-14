package com.example.eunoia.viewModels

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.*

class GlobalViewModel: ViewModel(){
    //general media player
    private val _generalMediaPlayerIsCompleted = MutableLiveData(false)
    var generalMediaPlayerIsCompleted: LiveData<Boolean> = _generalMediaPlayerIsCompleted

    fun setGeneralMediaPlayerIsCompleted(newValue : Boolean) {
        _generalMediaPlayerIsCompleted.postValue(newValue)
    }

    //sound
    var currentSoundPlaying by mutableStateOf<SoundData?>(null)
    //var currentSoundPlaying: MutableLiveData<SoundData?> = MutableLiveData(null)
    var isCurrentSoundPlaying by mutableStateOf(false)
    var currentSoundPlayingPreset by mutableStateOf<SoundPresetData?>(null)
    var currentAllOriginalSoundPreset by mutableStateOf<MutableSet<SoundPresetData>?>(null)
    var currentAllUserSoundPreset by mutableStateOf<MutableSet<SoundPresetData>?>(null)
    var currentSoundPlayingContext by mutableStateOf<Context?>(null)
    var currentSoundPlayingSliderPositions = mutableListOf<MutableState<Float>?>()
    var currentSoundPlayingUris: MutableList<Uri?>? = null
    var currentUsersSounds by mutableStateOf<MutableList<UserSound?>?>(null)
    var currentUsersSoundRelationships by mutableStateOf<MutableList<UserSoundRelationship?>?>(null)
    var soundTimerTime by mutableStateOf(0L)
    var soundMeditationBellInterval by mutableStateOf(0)
    var soundCountDownTimer: CountDownTimer? = null
    var soundSliderVolumes: MutableList<Int>? = null
    var soundMeditationBellMediaPlayer: MediaPlayer? = null
    var previouslyPlayedUserSoundRelationship: UserSoundRelationship? = null

    //adding sound
    var currentSoundToBeAdded by mutableStateOf<SoundData?>(null)
    var currentPresetToBeAdded by mutableStateOf<SoundPresetData?>(null)
    var presetNameToBeCreated by mutableStateOf("")

    /**
     * routine
     */
    var currentRoutinePlaying by mutableStateOf<RoutineData?>(null)
    var currentUserRoutineRelationshipPlaying by mutableStateOf<UserRoutineRelationship?>(null)
    var isCurrentRoutinePlaying by mutableStateOf(false)
    var routineNameToBeAdded by mutableStateOf("")
    var routineColorToBeAdded by mutableStateOf<Long?>(null)
    var routineIconToBeAdded by mutableStateOf<Int?>(null)
    var currentUsersRoutines by mutableStateOf<MutableList<UserRoutine?>?>(null)
    var currentUsersRoutineRelationships by mutableStateOf<MutableList<UserRoutineRelationship?>?>(null)
    var currentRoutinePlayingOrderIndex by mutableStateOf<Int?>(0)
    var currentRoutinePlayingOrder by mutableStateOf<MutableList<String?>?>(null)
    var previouslyPlayedUserRoutineRelationship: UserRoutineRelationship? = null

    var currentRoutinePlayingRoutinePresets by mutableStateOf<MutableList<RoutineSoundPreset?>?>(null)
    var currentRoutinePlayingUserRoutineRelationshipPresets by mutableStateOf<MutableList<UserRoutineRelationshipSoundPreset?>?>(null)
    var currentRoutinePlayingRoutinePresetsIndex by mutableStateOf<Int?>(0)
    var currentRoutinePlayingUserRoutineRelationshipPresetsIndex by mutableStateOf<Int?>(0)

    var currentRoutinePlayingRoutinePrayers by mutableStateOf<MutableList<RoutinePrayer?>?>(null)
    var currentRoutinePlayingUserRoutineRelationshipPrayers by mutableStateOf<MutableList<UserRoutineRelationshipPrayer?>?>(null)
    var currentRoutinePlayingRoutinePrayersIndex by mutableStateOf<Int?>(0)
    var currentRoutinePlayingUserRoutineRelationshipPrayersIndex by mutableStateOf<Int?>(0)

    var currentRoutinePlayingSelfLoves by mutableStateOf<MutableList<RoutineSelfLove?>?>(null)
    var currentRoutinePlayingUserRoutineRelationshipSelfLoves by mutableStateOf<MutableList<UserRoutineRelationshipSelfLove?>?>(null)
    var currentRoutinePlayingSelfLovesIndex by mutableStateOf<Int?>(0)
    var currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex by mutableStateOf<Int?>(0)

    var currentRoutinePlayingBedtimeStories by mutableStateOf<MutableList<RoutineBedtimeStoryInfo?>?>(null)
    var currentRoutinePlayingUserRoutineRelationshipBedtimeStories by mutableStateOf<MutableList<UserRoutineRelationshipBedtimeStoryInfo?>?>(null)
    var currentRoutinePlayingBedtimeStoriesIndex by mutableStateOf<Int?>(0)
    var currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex by mutableStateOf<Int?>(0)

    var currentRoutinePlayingSoundCountDownTimer: CountDownTimer? = null
    var currentRoutinePlayingPrayerCountDownTimer: CountDownTimer? = null
    var currentRoutinePlayingNextPrayerCountDownTimer: CountDownTimer? = null
    var currentRoutinePlayingSelfLoveCountDownTimer: CountDownTimer? = null
    var currentRoutinePlayingNextSelfLoveCountDownTimer: CountDownTimer? = null
    var currentRoutinePlayingBedtimeStoryCountDownTimer: CountDownTimer? = null
    var currentRoutinePlayingNextBedtimeStoryCountDownTimer: CountDownTimer? = null


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
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
    )
    var soundScreenBackgroundControlColor1 = arrayOf(
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    var soundScreenBackgroundControlColor2 = arrayOf(
        mutableStateOf(Solitude),
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
    var soundPlaytimeTimer = SoundPlaytimeTimer(UserDashboardActivity.getInstanceActivity())
    var routinePlaytimeTimer = RoutinePlaytimeTimer(UserDashboardActivity.getInstanceActivity())

    /**
     * bedtime story
     *
     */
    var currentUsersBedtimeStories by mutableStateOf<MutableList<UserBedtimeStoryInfo?>?>(null)
    var currentUsersBedtimeStoryRelationships by mutableStateOf<MutableList<UserBedtimeStoryInfoRelationship?>?>(null)
    var currentBedtimeStoryPlaying by mutableStateOf<BedtimeStoryInfoData?>(null)
    var isCurrentBedtimeStoryPlaying by mutableStateOf(false)
    var currentBedtimeStoryPlayingUri: Uri? = null
    var bedtimeStoryTimeDisplay by mutableStateOf("00.00")
    var bedtimeStoryCircularSliderAngle by mutableStateOf(0f)
    var bedtimeStoryCircularSliderClicked by mutableStateOf(false)
    var bedtimeStoryTimer = BedtimeStoryTimer(UserDashboardActivity.getInstanceActivity())
    var previouslyPlayedUserBedtimeStoryRelationship: UserBedtimeStoryInfoRelationship? = null

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
    var currentUsersSelfLoveRelationships by mutableStateOf<MutableList<UserSelfLoveRelationship?>?>(null)
    var currentSelfLovePlaying by mutableStateOf<SelfLoveData?>(null)
    var isCurrentSelfLovePlaying by mutableStateOf(false)
    var currentSelfLovePlayingUri: Uri? = null
    var selfLoveTimeDisplay by mutableStateOf("00.00")
    var selfLoveCircularSliderAngle by mutableStateOf(0f)
    var selfLoveCircularSliderClicked by mutableStateOf(false)
    var selfLoveTimer = SelfLoveTimer(UserDashboardActivity.getInstanceActivity())
    var previouslyPlayedUserSelfLoveRelationship: UserSelfLoveRelationship? = null

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
    var currentUsersPrayerRelationships by mutableStateOf<MutableList<UserPrayerRelationship?>?>(null)
    var currentPrayerPlaying by mutableStateOf<PrayerData?>(null)
    var isCurrentPrayerPlaying by mutableStateOf(false)
    var currentPrayerPlayingUri: Uri? = null
    var prayerTimeDisplay by mutableStateOf("00.00")
    var prayerCircularSliderAngle by mutableStateOf(0f)
    var prayerCircularSliderClicked by mutableStateOf(false)
    var prayerTimer = PrayerTimer(UserDashboardActivity.getInstanceActivity())
    var previouslyPlayedUserPrayerRelationship: UserPrayerRelationship? = null

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