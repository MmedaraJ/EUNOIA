package com.example.eunoia.viewModels

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel

class GlobalViewModel: ViewModel(){
    //sound
    var currentSoundPlaying by mutableStateOf<SoundData?>(null)
    var currentSoundPlayingPreset by mutableStateOf<PresetData?>(null)
    var currentSoundPlayingPresetNameAndVolumesMap by mutableStateOf<PresetNameAndVolumesMapData?>(null)
    var currentSoundPlayingContext by mutableStateOf<Context?>(null)
    var currentSoundPlayingSliderPositions = mutableListOf<MutableState<Float>?>()
    var currentSoundPlayingUris: MutableList<Uri?>? = null
    var currentUsersSounds by mutableStateOf<MutableList<UserSound?>?>(null)
    var soundTimerTime by mutableStateOf(0L)
    var soundMeditationBellInterval by mutableStateOf(0)
    //adding sound
    var currentSoundToBeAdded by mutableStateOf<SoundData?>(null)

    //bedtime story
    var currentUsersBedtimeStories by mutableStateOf<MutableList<UserBedtimeStoryInfo?>?>(null)
    var currentBedtimeStoryPlaying by mutableStateOf<BedtimeStoryInfoData?>(null)
    var isCurrentBedtimeStoryPlaying by mutableStateOf(false)

    //routine
    var currentRoutinePlaying by mutableStateOf("")
    var routineNameToBeAdded by mutableStateOf("")
    var routineColorToBeAdded by mutableStateOf<Long?>(null)
    var routineIconToBeAdded by mutableStateOf<Int?>(null)
    var currentUsersRoutines by mutableStateOf<MutableList<UserRoutine?>?>(null)

    var currentSelfLovePlaying by mutableStateOf("")
    var isCurrentSoundPlaying by mutableStateOf(false)
    var isCurrentRoutinePlaying by mutableStateOf(false)
    var isCurrentSelfLovePlaying by mutableStateOf(false)

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

    fun changeCurrentSoundPlaying(soundData: SoundData?){
        currentSoundPlaying = soundData
    }

    fun changeCurrentSoundPlayingPreset(presetData: PresetData?){
        currentSoundPlayingPreset = presetData
    }

    fun changeCurrentRoutinePlaying(text: String){
        currentRoutinePlaying = text
    }

    fun changeCurrentSelfLovePlaying(text: String){
        currentSelfLovePlaying = text
    }
}