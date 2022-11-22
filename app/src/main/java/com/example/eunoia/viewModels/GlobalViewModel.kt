package com.example.eunoia.viewModels

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(): ViewModel(){
    var CDT: CountDownTimer? = null
    var remainingPlayTime by mutableStateOf(0)
    var continuePlayingTime = 0

    private val TAG = "GlobalViewModel"

    var bottomSheetOpenFor by mutableStateOf("")

    //user
    var currentUser by mutableStateOf<UserData?>(null)

    //navController
    var navController by mutableStateOf<NavController?>(null)

    var allowBottomSheetClose by mutableStateOf(true)

    /**
     * General playtime timer
     */
    var generalPlaytimeTimer = GeneralPlaytimeTimer(UserDashboardActivity.getInstanceActivity())
    var soundPlaytimeTimer = SoundPlaytimeTimer(UserDashboardActivity.getInstanceActivity())
    var routinePlaytimeTimer = RoutinePlaytimeTimer(UserDashboardActivity.getInstanceActivity())

    fun startTheCDT(
        time: Long,
        generalMediaPlayerService: GeneralMediaPlayerService,
        completed: () -> Unit
    ) {
        if (CDT == null) {
            CDT = object : CountDownTimer(time, 1) {
                override fun onTick(millisUntilFinished: Long) {
                    if(generalMediaPlayerService.isMediaPlayerInitialized()){
                        if(
                            generalMediaPlayerService.getMediaPlayer()!!.currentPosition >=
                            generalMediaPlayerService.getMediaPlayer()!!.duration
                        ){
                            Log.i(TAG, "Done with CDT")
                            completed()
                        }
                    }

                    if(millisUntilFinished % 10000 == 0L) {
                        Log.i(TAG, "CDT: $millisUntilFinished")
                    }
                }

                override fun onFinish() {
                    Log.i(TAG, "CDT stopped")
                    completed()
                }
            }
        }
        CDT!!.start()
    }

    fun resetCDT(){
        if(CDT != null){
            CDT!!.cancel()
            CDT = null
        }
    }
}