package com.example.eunoia.viewModels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amplifyframework.datastore.generated.model.PrayerData
import com.amplifyframework.datastore.generated.model.UserPrayer
import com.amplifyframework.datastore.generated.model.UserPrayerRelationship
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.PrayerTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(): ViewModel() {
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