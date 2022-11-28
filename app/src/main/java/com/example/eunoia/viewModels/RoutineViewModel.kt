package com.example.eunoia.viewModels

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amplifyframework.datastore.generated.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(): ViewModel() {
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

    var routineIndex = -1
}