package com.example.eunoia.viewModels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfo
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfoRelationship
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.BedtimeStoryTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BedtimeStoryViewModel @Inject constructor(): ViewModel() {
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
    var currentUserBedtimeStoryRelationship: UserBedtimeStoryInfoRelationship? = null
    var remainingPlayTime by mutableStateOf(0)
    var playTimeSoFar by mutableStateOf(0L)

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
}