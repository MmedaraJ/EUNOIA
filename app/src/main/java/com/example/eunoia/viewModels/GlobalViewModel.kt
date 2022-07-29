package com.example.eunoia.viewModels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.ui.theme.*

class GlobalViewModel: ViewModel(){
    //sound
    var currentSoundPlaying by mutableStateOf<SoundData?>(null)
    var currentSoundPlayingPreset by mutableStateOf<PresetData?>(null)
    var currentSoundPlayingPresetNameAndVolumesMap by mutableStateOf<PresetNameAndVolumesMapData?>(null)
    var currentSoundPlayingContext by mutableStateOf<Context?>(null)
    var currentSoundPlayingSliderPositions = mutableListOf<MutableState<Float>?>()
    var currentSoundPlayingUris: MutableList<Uri>? = null
    var currentUsersSounds by mutableStateOf<MutableList<UserSound?>?>(null)
    //adding sound
    var currentSoundToBeAdded by mutableStateOf<SoundData?>(null)

    //routine
    var currentRoutinePlaying by mutableStateOf("")
    var routineNameToBeAdded by mutableStateOf("")
    var routineColorToBeAdded by mutableStateOf<Long?>(null)
    var routineIconToBeAdded by mutableStateOf<Int?>(null)
    var currentUsersRoutines by mutableStateOf<MutableList<UserRoutine?>?>(null)

    var currentSelfLovePlaying by mutableStateOf("")
    var currentBedtimeStoryPlaying by mutableStateOf("")
    var isCurrentSoundPlaying by mutableStateOf(false)
    var isCurrentRoutinePlaying by mutableStateOf(false)
    var isCurrentSelfLovePlaying by mutableStateOf(false)
    var isCurrentBedtimeStoryPlaying by mutableStateOf(false)

    var bottomSheetOpenFor by mutableStateOf("i")

    //user
    var currentUser by mutableStateOf<UserData?>(null)

    //navController
    var navController by mutableStateOf<NavController?>(null)

    val icons = arrayOf(
        mutableStateOf(R.drawable.replay_sound_icon),
        mutableStateOf(R.drawable.reset_sliders_icon),
        mutableStateOf(R.drawable.sound_timer_icon),
        mutableStateOf(R.drawable.play_icon),
        mutableStateOf(R.drawable.increase_levels_icon),
        mutableStateOf(R.drawable.decrease_levels_icon),
        mutableStateOf(R.drawable.meditation_bell_icon),
    )
    val addIcon = mutableStateOf(R.drawable.add_sound_icon)
    var borderControlColors = arrayOf(
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Black),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
        mutableStateOf(Bizarre),
    )
    var backgroundControlColor1 = arrayOf(
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(SoftPeach),
        mutableStateOf(White),
        mutableStateOf(White),
        mutableStateOf(White),
    )
    var backgroundControlColor2 = arrayOf(
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

    fun changeCurrentBedtimeStoryPlaying(text: String){
        currentBedtimeStoryPlaying = text
    }
}