package com.example.eunoia.viewModels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amplifyframework.datastore.generated.model.SelfLoveData
import com.amplifyframework.datastore.generated.model.UserSelfLove
import com.amplifyframework.datastore.generated.model.UserSelfLoveRelationship
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.SelfLoveTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelfLoveViewModel @Inject constructor(): ViewModel() {
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
}