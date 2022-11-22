package com.example.eunoia.viewModels

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.SoundPresetData
import com.amplifyframework.datastore.generated.model.UserSound
import com.amplifyframework.datastore.generated.model.UserSoundRelationship
import com.example.eunoia.R
import com.example.eunoia.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SoundViewModel @Inject constructor(): ViewModel() {
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
}